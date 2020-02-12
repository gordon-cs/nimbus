package com.vmware.nimbus.ui.main.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.vmware.nimbus.api.BlueprintCallback;
import com.vmware.nimbus.api.SingletonRequest;
import com.vmware.nimbus.data.model.BlueprintItemModel;
import com.vmware.nimbus.data.model.LoginModel;

import java.util.HashMap;
import java.util.Map;

public class BlueprintsViewModel extends AndroidViewModel {

    public BlueprintsViewModel(Application application) {
        super(application);
    }

    // TODO - Refactor URL
    private String blueprintsUrl = "https://api.mgmt.cloud.vmware.com/blueprint/api/blueprints";

    private BlueprintItemModel.BlueprintItemPage blueprintItemPage;

    public void loadBlueprints(final BlueprintCallback callback) {
        StringRequest jsonObjRequest = new StringRequest(
                Request.Method.GET,
                blueprintsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("deployments response", response);
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
                String bearerToken = "Bearer " + LoginModel.getInstance(getApplication().getApplicationContext()).getBearer_token();
                params.put("Authorization", bearerToken);
                return params;
            }
        };
        SingletonRequest.getInstance(getApplication().getApplicationContext()).addToRequestQueue(jsonObjRequest);
    }
}

