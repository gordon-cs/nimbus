package com.vmware.nimbus.api;

import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.vmware.nimbus.data.model.BlueprintItemModel;
import com.vmware.nimbus.data.model.CspResult;
import com.vmware.nimbus.data.model.LoginModel;

import java.util.HashMap;
import java.util.Map;

public class APIService {

    public static void LogOut(Context c) {
        LoginModel.getInstance(c).setAuthenticated(false);
        LoginModel.getInstance(c).setApi_token("");
    }

//    public void RequestAPI(Request.Method method, String URL, )

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

    //TODO: don't hardcode this string
    private static String blueprintsUrl = "https://api.mgmt.cloud.vmware.com/blueprint/api/blueprints";

    private static BlueprintItemModel.BlueprintItemPage blueprintItemPage;

    public static void loadBlueprints(final BlueprintCallback callback, Context c) {
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
}
