package com.vmware.nimbus.ui.main.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.vmware.nimbus.R;
import com.vmware.nimbus.api.APIService;
import com.vmware.nimbus.api.SingletonRequest;
import com.vmware.nimbus.data.model.LoginModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * A [DialogFragment] for the deployment actions.
 */
public class DeploymentActionsFragment extends BottomSheetDialogFragment {
    final String LOG_TAG = "DeploymentActionsFragment";

    private String deploymentName;
    private String deploymentId;
    private DeploymentActionResult mDeploymentActionResult;
    private String baseUrl;
    private Context c;

    /**
     * Called when the View is created.
     *
     * @param inflater           - the LayoutInflater
     * @param container          - the ViewGroup
     * @param savedInstanceState - the savedInstanceState
     * @return - the root view of this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.deployment_actions_dialog, container, false);
        c = getContext();

        deploymentName = (String) getArguments().getSerializable("name");
        deploymentId = (String) getArguments().getSerializable("id");

        final Button powerOnButton = rootView.findViewById(R.id.power_on_button);
        powerOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionId = "Deployment.PowerOn";
                Log.d(LOG_TAG, "Clicked power on");
                Map<String, String> inputs = new HashMap<String, String>();
                String reason = "vRAC Companion";
                DeploymentActionRequest request = new DeploymentActionRequest(actionId, inputs, reason);
                try {
                    performDeploymentAction(request);
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
                String reason = "vRAC Companion";
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
                String reason = "vRAC Companion";
                DeploymentActionRequest request = new DeploymentActionRequest(actionId, inputs, reason);
                try {
                    performDeploymentAction(request);
                    Toast.makeText(getContext(), "Deleting " + deploymentName, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button leaseButton = rootView.findViewById(R.id.change_lease_button);
        leaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                showTimePicker(year, monthOfYear, dayOfMonth);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(cldr.getTimeInMillis());
                cldr.add(Calendar.DATE, getResources().getInteger(R.integer.lease_limit));
                picker.getDatePicker().setMaxDate(cldr.getTimeInMillis());
                picker.show();
            }
        });

        return rootView;
    }

    /**
     * Shows the time picker when changing the lease of a deployment.
     *
     * @param year        - the year for the lease to expire
     * @param monthOfYear - the month of that year for the lease to expire
     * @param dayOfMonth  - the day of that month for the lease to expire
     */
    protected void showTimePicker(int year, int monthOfYear, int dayOfMonth) {
        final Calendar cldr = Calendar.getInstance();
        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);
        String actionId = "Deployment.ChangeLease";
        // time picker dialog
        TimePickerDialog picker = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        final Calendar expireDate = Calendar.getInstance();
                        expireDate.set(year, monthOfYear, dayOfMonth, sHour, sMinute, 0);
                        Log.d("unformatted time", expireDate.toString());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        sdf.setTimeZone(expireDate.getTimeZone());
                        String expireTime = sdf.format(expireDate.getTime());
                        Log.d("formatted time", expireTime);

                        Map<String, String> inputs = new HashMap<String, String>();
                        inputs.put("Lease Expiration Date", expireTime);
                        String reason = "vRAC Companion";
                        DeploymentActionRequest request = new DeploymentActionRequest(actionId, inputs, reason);


                        try {
                            performDeploymentAction(request);
                            Toast.makeText(getContext(), "Changing lease of " + deploymentName, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, hour, minutes, false);
        picker.show();
    }

    /**
     * Called when the Dialog box is created.
     *
     * @param savedInstanceState - the savedInstanceState
     * @return - the dialog object
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    /**
     * Performs a deployment action.
     *
     * @param request - a DeploymentActionRequest defining the deployment action
     * @throws JSONException
     */
    public void performDeploymentAction(DeploymentActionRequest request) throws JSONException {
        baseUrl = APIService.getBaseEndpointURL(c) + c.getApplicationContext().getResources().getString(R.string.deployments_base_uri);
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

    /**
     * Model for deployment actions
     */
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

    /**
     * Model for deployment action results
     */
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
