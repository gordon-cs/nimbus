package com.vmware.nimbus.ui.main.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vmware.nimbus.R;
import com.vmware.nimbus.api.DeploymentActionResultCallback;
import com.vmware.nimbus.api.SingletonRequest;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.data.model.LoginModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class DeploymentActionsFragment extends DialogFragment {
    final String LOG_TAG = "DeploymentActionsFragment";

    private String deploymentName;
    private String deploymentId;
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

        final Button cancelButton = rootView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        deploymentName = (String) getArguments().getSerializable("name");
        deploymentId = (String) getArguments().getSerializable("id");
        TextView title = rootView.findViewById(R.id.deployment_title);
        title.setText(deploymentName);

        final Button powerOnButton = rootView.findViewById(R.id.power_on_button);
        powerOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionId = "Deployment.PowerOn";
                Log.d(LOG_TAG, "Clicked power on");
                Map<String, String> inputs = new HashMap<String, String>();
                String reason = "CAStoff Test";
                DeploymentActionRequest request = new DeploymentActionRequest(actionId, inputs, reason);
                try {
                    performDeploymentAction( request);
                    Toast.makeText(getContext(), "Powering On " + deploymentName, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button powerOffButton = rootView.findViewById(R.id.power_off_button);
        powerOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionId = "Deployment.PowerOff";
                Log.d(LOG_TAG, "Clicked power off");
                Map<String, String> inputs = new HashMap<String, String>();
                String reason = "CAStoff Test";
                DeploymentActionRequest request = new DeploymentActionRequest(actionId, inputs, reason);
                try {
                    performDeploymentAction(request);
                    Toast.makeText(getContext(), "Powering Off " + deploymentName, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button deleteButton = rootView.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionId = "Deployment.Delete";
                Log.d(LOG_TAG, "Clicked delete");
                Map<String, String> inputs = new HashMap<String, String>();
                String reason = "CAStoff Test";
                DeploymentActionRequest request = new DeploymentActionRequest(actionId, inputs, reason);
                try {
                    performDeploymentAction(request);
                    Toast.makeText(getContext(), "Deleting " + deploymentName, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                try {
//                    performDeploymentAction(new DeploymentActionResultCallback() {
//                        @Override
//                        public void onSuccess(DeploymentActionResult result) {
//                            Log.d(LOG_TAG, "Delete successful");
//                            mDeploymentActionResult = result;
//                            Log.d(LOG_TAG, "Status: " + mDeploymentActionResult.status);
//                        }
//                    }, request);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

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

    private String baseUrl = "https://api.mgmt.cloud.vmware.com/deployment/api/deployments/";

    public void performDeploymentAction(DeploymentActionRequest request) throws JSONException {
        String requestUrl = baseUrl + deploymentId + "/requests";
        Log.d(LOG_TAG, "Request URL: " + requestUrl);
        Gson gson = new Gson();
        String body = gson.toJson(request, DeploymentActionRequest.class);
        Log.d(LOG_TAG, "Body of JSON request: " + body);
        JSONObject jsonObject = new JSONObject(body);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                requestUrl,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(LOG_TAG, "Response: " + response.toString());
                        dismiss();
                        //Gson gson = new Gson();
                        //DeploymentActionResult deploymentActionResult = gson.fromJson(request.toString(), DeploymentActionResult.class);
                        //callback.onSuccess(deploymentActionResult);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "Error: " + error.getMessage());
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Log.e(LOG_TAG, "Timeout or NoConnectionError");
//                            Toast.makeText(getContext(),
//                                    "Error: Network Timeout",
//                                    Toast.LENGTH_LONG).show();
//                        } else if (error instanceof AuthFailureError) {
//                            Log.e(LOG_TAG, "AuthFailureError");
//                        } else if (error instanceof ServerError) {
//                            Log.e(LOG_TAG, "ServerError");
//                        } else if (error instanceof NetworkError) {
//                            Log.e(LOG_TAG, "NetworkError");
//                        } else if (error instanceof ParseError) {
//                            Log.e(LOG_TAG, "ParseError");
//                        }

                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String bearerToken = "Bearer " + LoginModel.getInstance(getActivity().getApplicationContext()).getBearer_token();
                headers.put("Authorization", bearerToken);
                return headers;
            }
        };
        SingletonRequest.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
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
