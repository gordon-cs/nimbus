package com.vmware.nimbus.ui.main.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.vmware.nimbus.R;
import com.vmware.nimbus.api.DeploymentActionResultCallback;
import com.vmware.nimbus.api.SingletonRequest;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.data.model.LoginModel;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class DeploymentActionsFragment extends DialogFragment {

    private DeploymentItemModel.DeploymentItem deploymentItem;
    private DeploymentActionResult mDeploymentActionResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.deployment_actions_dialog, container, false);

        (rootView.findViewById(R.id.button_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        (rootView.findViewById(R.id.cancel_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        deploymentItem = (DeploymentItemModel.DeploymentItem) getArguments().getSerializable("deploymentItem");
        TextView title = rootView.findViewById(R.id.deployment_title);
        title.setText(deploymentItem.name);

        (rootView.findViewById(R.id.power_on_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionId = "Deployment.PowerOn";
                Map<String, String> inputs = new HashMap<String, String>();
                String reason = "CAStoff Test";
                DeploymentActionRequest request = new DeploymentActionRequest(actionId, inputs, reason);
                performDeploymentAction(new DeploymentActionResultCallback() {
                    @Override
                    public void onSuccess(DeploymentActionsFragment.DeploymentActionResult result) {
                        mDeploymentActionResult = result;
                    }
                }, request);
            }
        });

        (rootView.findViewById(R.id.power_off_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionId = "Deployment.PowerOff";
                Map<String, String> inputs = new HashMap<String, String>();
                String reason = "CAStoff Test";
                DeploymentActionRequest request = new DeploymentActionRequest(actionId, inputs, reason);
                performDeploymentAction(new DeploymentActionResultCallback() {
                    @Override
                    public void onSuccess(DeploymentActionsFragment.DeploymentActionResult result) {
                        mDeploymentActionResult = result;
                    }
                }, request);
            }
        });

        (rootView.findViewById(R.id.delete_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionId = "Deployment.Delete";
                Map<String, String> inputs = new HashMap<String, String>();
                String reason = "CAStoff Test";
                DeploymentActionRequest request = new DeploymentActionRequest(actionId, inputs, reason);
                performDeploymentAction(new DeploymentActionResultCallback() {
                    @Override
                    public void onSuccess(DeploymentActionsFragment.DeploymentActionResult result) {
                        mDeploymentActionResult = result;
                        Toast.makeText(getContext(), "Status: " + mDeploymentActionResult.status, Toast.LENGTH_LONG).show();
                    }
                }, request);
            }
        });

        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private String baseUrl = "https://api.mgmt.cloud.vmware.com/";

    public void performDeploymentAction(final DeploymentActionResultCallback callback, DeploymentActionRequest request) {
        String requestUrl = "deployment/api/deployments/" + deploymentItem.id + "requests";
        String urlString = baseUrl + requestUrl;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("deployment action response", response);
                        Gson gson = new Gson();
                        DeploymentActionResult deploymentActionResult = gson.fromJson(response, DeploymentActionResult.class);
                        callback.onSuccess(deploymentActionResult);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "Error: " + error.getMessage());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Log.d("Volley Error", "Timeout or NoConnectionError");
                            Toast.makeText(getContext(),
                                    "Error: Network Timeout",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Log.d("Volley Error", "AuthFailureError");
                        } else if (error instanceof ServerError) {
                            Log.d("Volley Error", "ServerError");
                        } else if (error instanceof NetworkError) {
                            Log.d("Volley Error", "NetworkError");
                        } else if (error instanceof ParseError) {
                            Log.d("Volley Error", "ParseError");
                        }

                        error.printStackTrace();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String bearerToken = "Bearer " + LoginModel.getInstance(getActivity().getApplicationContext()).getBearer_token();
                headers.put("Authorization", bearerToken);
                //headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                String body = gson.toJson(request, DeploymentActionRequest.class);
                Log.d("Deployment body", body);
                return body.getBytes();
            }
        };

        SingletonRequest.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    public class DeploymentActionRequest {
        private String actionId;
        private Map<String, String> inputs;
        private String reason;

        public DeploymentActionRequest(String actionId, Map<String, String> inputs, String reason) {
            this.actionId = actionId;
            this.inputs = inputs;
            this.reason = reason;
        }
    }

    public class DeploymentActionResult {
        public String id;
        public String name;
        public String deploymentId;
        public String createdAt;
        public String updatedAt;
        public String requestedBy;
        public String requester;
        public String blueprintId;
        public int completedTasks;
        public int totalTasks;
        public String status;
        public Map<String, String> inputs;
    }

}
