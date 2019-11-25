package com.vmware.nimbus.ui.main.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.vmware.nimbus.api.SingletonRequest;
import com.vmware.nimbus.data.model.CspResult;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.data.model.DeploymentStore;
import com.vmware.nimbus.data.model.DeploymentsModel;
import com.vmware.nimbus.data.model.LoginModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeploymentsViewModel extends AndroidViewModel {

    String LOG_TAG = "DeploymentsViewModel";

    public DeploymentsViewModel(Application application) {
        super(application);
    }
    //private int count;

    //public DeploymentsViewModel(int count) {
    //    this.count = count;
    //}

    private List<DeploymentsModel> deploymentsTests;
    private List<DeploymentItemModel> deploymentsData;

    private String deploymentsUrl = "https://api.mgmt.cloud.vmware.com/deployment/api/deployments?size=100";

    //protected int getCount() {
    //    return this.count;
    //}

    // Use LiveData object here
    private DeploymentItemModel deploymentItemModel;

    private DeploymentItemModel.DeploymentItemPage deploymentItemPage;
    private List<DeploymentItemModel.DeploymentItem> deploymentItems;

    public DeploymentItemModel.DeploymentItemPage getDeploymentItemPage() {
        return deploymentItemPage;
    }

    public List<DeploymentItemModel.DeploymentItem> getDeploymentItems() {
//        deploymentItems = deploymentItemPage.content;
        return deploymentItemPage.content;
    }

    public void loadDeployments() {
        Toast.makeText(getApplication().getApplicationContext(), "loading deployments", Toast.LENGTH_LONG);
        StringRequest jsonObjRequest = new StringRequest(
                Request.Method.GET,
                deploymentsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("deployments response", response);
                        Toast.makeText(getApplication().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        deploymentItemPage = gson.fromJson(response, DeploymentItemModel.DeploymentItemPage.class);
                        Log.d("deserialized object", deploymentItemPage.content.get(0).id);
                        Log.d("Deserialized pt 2", deploymentItemPage.content.get(0).createdBy);
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

    public void loadDeploymentStore() {
    }

    public List<DeploymentsModel> initializeDeploymentsData() {
        String hello = "Hello ";
        String deployments = "deployments ";
        deploymentsTests = new ArrayList<>(8);
        for (int i = 0; i < 8; i++)
            deploymentsTests.add(new DeploymentsModel(hello, deployments, Integer.toString(i + 1)));

        return deploymentsTests;
    }
}
