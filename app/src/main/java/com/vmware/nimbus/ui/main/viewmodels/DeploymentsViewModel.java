package com.vmware.nimbus.ui.main.viewmodels;

import android.annotation.TargetApi;
import android.app.Application;
import android.graphics.Color;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.vmware.nimbus.api.DeploymentCallback;
import com.vmware.nimbus.api.SingletonRequest;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.data.model.LoginModel;

import java.util.HashMap;
import java.util.Map;

/**
 * An [AndroidViewModel] that defines the deployments view.
 */
public class DeploymentsViewModel extends AndroidViewModel {

    public DeploymentsViewModel(Application application) {
        super(application);
    }

    // TODO - Refactor URL
    private String deploymentsUrl = "https://api.mgmt.cloud.vmware.com/deployment/api/deployments?size=100";

    private DeploymentItemModel.DeploymentItemPage deploymentItemPage;

    /**
     * Loads the deployments asynchronously
     * @param callback - callback that watches for successful deployments data from the response
     */
    public void loadDeployments(final DeploymentCallback callback) {
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

    //TODO: fully implement this
    @TargetApi(26)
    public Color getPowerState(final DeploymentCallback callback, int index) {
        Color result = Color.valueOf(Color.GREEN);
        if (deploymentItemPage.content.get(index).resources == null) {
            return Color.valueOf(Color.GRAY);
        }
        else {
            
        }
        return null;
    }
}
