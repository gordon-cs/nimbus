package com.vmware.nimbus.api;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.vmware.nimbus.R;
import com.vmware.nimbus.data.model.BlueprintItemModel;
import com.vmware.nimbus.data.model.CspResult;
import com.vmware.nimbus.data.model.DeployBlueprintModel;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.data.model.LoginModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A service to interact with the Cloud Assembly API.
 *
 */
public class APIService {

    private static String blueprintsUrl;
    private static BlueprintItemModel.BlueprintItemPage blueprintItemPage;

    private static String deploymentsUrl;
    private static DeploymentItemModel.DeploymentItemPage deploymentItemPage;
    private static String bpRequestUrl;
    public enum PowerState {UNKNOWN, ON, OFF, MIXED};

    /**
     * Changes the state of the LoginModel to logged out.
     *
     * @param c - the Context
     */
    public static void LogOut(Context c) {
        LoginModel.getInstance(c).setAuthenticated(false);
        LoginModel.getInstance(c).setApi_token("");
    }

    /**
     * Makes a login request to the API
     *
     * @param c - the Context
     * @param URL - The url to make the request to
     * @param APIKey - the API key to use
     * @param callback - the callback object to return the data to
     */
    public static void LogIn(Context c, String URL, String APIKey, final LogInCallback callback) {
        final String LOG_TAG = "API_SERVICE.LOG_IN";

        JSONObject jsonBody;
        try {
             jsonBody = new JSONObject("{\"refreshToken\":\"" + APIKey + "\"}");
        } catch (JSONException jsonException) {
            callback.onFailure(true);
            return;
        }
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(

                URL,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("volley response", response.toString());
                        Gson gson = new Gson();
                        CspResult cspResult = gson.fromJson(response.toString(), CspResult.class);
                        LoginModel.getInstance(c).setAuthenticated(true);
                        LoginModel.getInstance(c).setApi_token(APIKey);
                        LoginModel.getInstance(c).setBearer_token(cspResult.getToken());

                        // todo - Ask class
                        callback.onSuccess(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "Error: " + error.getMessage());
                        Log.d("volley", new String(error.networkResponse.data));
                        error.printStackTrace();
                        callback.onFailure(false);
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        SingletonRequest.getInstance(c).addToRequestQueue(jsonObjRequest);
        Log.d(LOG_TAG, "Added request to queue.");
    }

    /**
     * Loads the blueprint data asynchronously.
     *
     * @param callback - callback object to return the data to on success
     * @param c - the Context
     */
    public static void loadBlueprints(final BlueprintCallback callback, Context c) {
        blueprintsUrl = getBaseEndpointURL(c) + c.getApplicationContext().getResources().getString(R.string.blueprints_uri);
        StringRequest jsonObjRequest = new StringRequest(
                Request.Method.GET,
                blueprintsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("blueprints response", response);
                        Gson gson = new Gson();
                        blueprintItemPage = gson.fromJson(response, BlueprintItemModel.BlueprintItemPage.class);
                        callback.onSuccess(blueprintItemPage.content);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                        toastMsg("The blueprints were unable to load properly", c);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String bearerToken = "Bearer " + LoginModel.getInstance(c).getBearer_token();
                params.put("Authorization", bearerToken);
                return params;
            }
        };
        SingletonRequest.getInstance(c).addToRequestQueue(jsonObjRequest);
    }

    /**
     * Loads the deployments asynchronously
     *
     * @param callback - callback that watches for successful deployments data from the response
     */
    public static void loadDeployments(final DeploymentCallback callback, Context c) {
        deploymentsUrl = getBaseEndpointURL(c) + c.getApplicationContext().getResources().getString(R.string.deployments_uri);
        StringRequest jsonObjRequest = new StringRequest(
                Request.Method.GET,
                deploymentsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("deployments response", response);
                        Gson gson = new Gson();
                        deploymentItemPage = gson.fromJson(response, DeploymentItemModel.DeploymentItemPage.class);
                        for(int i  = 0; i < deploymentItemPage.content.size(); i++){
                            deploymentItemPage.content.get(i).powerState = getPowerState(deploymentItemPage.content.get(i));
                        }
                        callback.onSuccess(deploymentItemPage.content);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                        toastMsg("The deployments were unable to load properly.", c);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String bearerToken = "Bearer " + LoginModel.getInstance(c).getBearer_token();
                params.put("Authorization", bearerToken);
                return params;
            }
        };
        SingletonRequest.getInstance(c).addToRequestQueue(jsonObjRequest);
    }

    /**
     * Deploys a blueprint.
     *
     * @param name - name of the deployment
     * @param pId  - project ID
     * @param rsn  - reason for deploying this blueprint
     * @param bpId - blueprint ID
     * @throws JSONException
     */
    public static void deployBlueprint(String name, String pId, String rsn, String bpId, String desc, Context c) throws JSONException {
        DeployBlueprintModel requestBody = new DeployBlueprintModel(name, pId, rsn, bpId, desc);
        Gson gson = new Gson();
        String json = gson.toJson(requestBody);
        JSONObject jsonObject = new JSONObject(json);
        bpRequestUrl = getBaseEndpointURL(c) + c.getApplicationContext().getResources().getString(R.string.bp_request_uri);
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, bpRequestUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("dp request", response.toString());
                        toastMsg("Blueprint deployed successfully.", c);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String responseBody = null;
                        try {
                            responseBody = new String(error.networkResponse.data, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (responseBody.contains("Value is mandatory")) {
                            toastMsg("Blueprint requires input and is unsupported by this application.", c);
                        }
                        else {
                            toastMsg("Blueprint failed to deploy.", c);
                        }
                        Log.d("dp request", "error in deploying: " + error.getMessage());

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String bearerToken = "Bearer " + LoginModel.getInstance(c).getBearer_token();
                params.put("Authorization", bearerToken);
                return params;
            }
        };

        SingletonRequest.getInstance(c).addToRequestQueue(jsonObjRequest);

    }

    /**
     * Gets the power state of a given deployment.
     *
     * @param deploymentItem - the deploymentItem associated with the desired deployment
     * @return - the PowerState as a String
     */
    @TargetApi(26)
    public static PowerState getPowerState(DeploymentItemModel.DeploymentItem deploymentItem) {

//        if all null: unknown or N/A
//        if none off and >=1 on: mark on
//        if none on and >=1 off: mark off
//        if some on some off: mixed?
        boolean anyOff = false;
        boolean anyOn = false;
        PowerState result;

        if (deploymentItem.resources == null || deploymentItem.resources.size() == 0) {
            result = PowerState.UNKNOWN;
        }
        result = PowerState.UNKNOWN;
        if (deploymentItem.resources != null) {
            for(int i = 0; i < deploymentItem.resources.size(); i++){
                if(deploymentItem.resources.get(i).properties == null || deploymentItem.resources.get(i).properties.powerState == null){
                }
                else if (deploymentItem.resources.get(i).properties.powerState.contains("OFF")){
                    anyOff = true;
                }
                else if (deploymentItem.resources.get(i).properties.powerState.contains("ON")){
                    anyOn = true;
                }
            }
        }

        if(anyOff == false && anyOn == false) {
            result = PowerState.UNKNOWN;
        }
        else if(anyOff == false && anyOn == true) {
            result = PowerState.ON;
        }
        else if(anyOff == true && anyOn == false) {
            result = PowerState.OFF;
        }
        else if(anyOff == true && anyOn == true) {
            result = PowerState.MIXED;
        }

        return result;
    }

    /**
     * Toast message wrapper.
     *
     * @param msg - the message to Toast
     * @param c - the Context
     */
    public static void toastMsg(String msg, Context c) {
        Toast toast = Toast.makeText(c, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Get the base endpoint URL from the application's settings or get the default if not present
     * @param context Application Context
     * @return The base URL without the trailing slash
     */
    public static String getBaseEndpointURL(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return settings.getString(
                context.getApplicationContext().getResources().getString(R.string.base_url_shared_property_name),
                context.getApplicationContext().getResources().getString(R.string.base_url));
    }
}
