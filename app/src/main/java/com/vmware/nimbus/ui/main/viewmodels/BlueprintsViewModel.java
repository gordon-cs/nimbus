package com.vmware.nimbus.ui.main.viewmodels;


import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.AsyncListUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.vmware.nimbus.api.DataCallback;
import com.vmware.nimbus.api.SingletonRequest;
import com.vmware.nimbus.data.model.BlueprintItemModel;
import com.vmware.nimbus.data.model.BlueprintsModel;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.data.model.LoginModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlueprintsViewModel extends AndroidViewModel {

    public BlueprintsViewModel(Application application) {
        super(application);
    }

    private List<BlueprintsModel> blueprintsTests;
    private List<BlueprintItemModel> blueprintData;

    private String blueprintsUrl = "https://api.mgmt.cloud.vmware.com/blueprint/api/blueprints";

    private BlueprintItemModel blueprintItemModel;
    private BlueprintItemModel.BlueprintItemPage blueprintItemPage;
    private List<BlueprintItemModel.BlueprintItem> blueprintItems;

    public void loadBlueprints(final DataCallback callback) {
        Toast.makeText(getApplication().getApplicationContext(), "loading blueprints", Toast.LENGTH_LONG);
        StringRequest jsonObjRequest = new StringRequest(
                Request.Method.GET,
                blueprintsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("deployments response", response);
                        //Toast.makeText(getApplication().getApplicationContext(), "blueprint-content:" + response, Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        blueprintItemPage = gson.fromJson(response, BlueprintItemModel.BlueprintItemPage.class);
                        Log.d("blueprint id", blueprintItemPage.content.get(0).id);
                        Log.d("blueprint name", blueprintItemPage.content.get(0).name);


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

    public List<BlueprintItemModel.BlueprintItem> initializeBlueprintsData() {
        return blueprintItemPage.content;
    }
}

