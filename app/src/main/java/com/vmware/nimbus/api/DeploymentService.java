package com.vmware.nimbus.api;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObservable;
import android.database.Observable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.vmware.nimbus.R;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.data.model.DeploymentItemModel.DeploymentItemPage;
import com.vmware.nimbus.data.model.DeploymentStore;
import com.vmware.nimbus.data.model.LoginModel;

import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class DeploymentService extends IntentService {

    private DeploymentItemModel.DeploymentItemPage deploymentItemPage;
    private ContentObservable contentObservable;
    private List<DeploymentItemModel.DeploymentItem> deploymentItems;
    static CompletableFuture<DeploymentItemPage> f;
    private Context ctx;

    public static final String ACTION_GET_DEPLOYMENTS = "get_deployments";

    public static final String URL = "url";
    public static final String METHOD = "method";
    public static final String BODY = "body";

    public DeploymentService() {
        super("DeploymentService");
    }

    public static void startActionGetDeployments(Context context, String url, String method, String body) {
        Intent intent = new Intent(context, DeploymentService.class);
        intent.setAction(ACTION_GET_DEPLOYMENTS);
        intent.putExtra(URL, url);
        intent.putExtra(METHOD, method);
        intent.putExtra(BODY, body);
        context.startService(intent);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_DEPLOYMENTS.equals(action)) {
                final String url = intent.getStringExtra(URL);
                final String method = intent.getStringExtra(METHOD);
                final String body = intent.getStringExtra(BODY);
                handleActionGetDeployments(url, method, body);
            }
        }
    }

    private void handleActionGetDeployments(String url, String method, String body) {
        apiCallAsync(url, method, body);
    }

    private Gson gson = new Gson();

    public void apiCallAsync(String url, String method, String body) {
        //String deploymentsUrl = baseUrl + "deployment/api/deployments?size=100";
        if (method == "GET") {
            StringRequest jsonObjRequest = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Deployments Response", response);
                            deploymentItemPage = gson.fromJson(response, DeploymentItemPage.class);
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

    private DeploymentItemPage parseResponse(String response) {
        Gson gson = new Gson();
        return gson.fromJson(response, DeploymentItemPage.class);
    }

    private void setDeploymentItemPage(DeploymentItemModel.DeploymentItemPage deploymentItemPage) {
        this.deploymentItemPage = deploymentItemPage;
    }

    public DeploymentItemModel.DeploymentItemPage getDeploymentItemPage() {
        return deploymentItemPage;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
