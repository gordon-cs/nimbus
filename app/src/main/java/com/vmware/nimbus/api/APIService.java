package com.vmware.nimbus.api;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.vmware.nimbus.R;
import com.vmware.nimbus.data.model.BlueprintItemModel;
import com.vmware.nimbus.data.model.CspResult;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.data.model.LoginModel;

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
        StringRequest jsonObjRequest = new StringRequest(

                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("volley response", response);
                        Gson gson = new Gson();
                        CspResult cspResult = gson.fromJson(response, CspResult.class);
                        LoginModel.getInstance(c).setAuthenticated(true);
                        LoginModel.getInstance(c).setApi_token(APIKey);
                        LoginModel.getInstance(c).setBearer_token(cspResult.getAccess_token());

                        // todo - Ask class
                        callback.onSuccess(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                        callback.onFailure(false);
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("refresh_token", APIKey);
                return params;
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
        blueprintsUrl = c.getApplicationContext().getResources().getString(R.string.blueprints_url);
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
        deploymentsUrl = c.getApplicationContext().getResources().getString(R.string.deployments_url);
        StringRequest jsonObjRequest = new StringRequest(
                Request.Method.GET,
                deploymentsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("deployments response", response);
                        Gson gson = new Gson();
                        deploymentItemPage = gson.fromJson(response, DeploymentItemModel.DeploymentItemPage.class);
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
     * Gets the power state of a given deployment.
     *
     * @param deploymentItem - the deploymentItem associated with the desired deployment
     * @return - the PowerState as a String
     */
    @TargetApi(26)
    public static String getPowerState(DeploymentItemModel.DeploymentItem deploymentItem) {
        //Unknown status if null or empty resources
        if (deploymentItem.resources == null || deploymentItem.resources.size() == 0) {
            return "Unknown";
        }
        String result = "Unknown";
        for(int i = 0; i < deploymentItem.resources.size(); i++){
            if(deploymentItem.resources.get(i).properties == null || deploymentItem.resources.get(i).properties.powerState == null){
                result = "Unknown";
            }
            else if (deploymentItem.resources.get(i).properties.powerState.contains("OFF")){
                result = "Off";
            }
            else if (deploymentItem.resources.get(i).properties.powerState.contains("ON")){
                result = "On";
            }
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
}
