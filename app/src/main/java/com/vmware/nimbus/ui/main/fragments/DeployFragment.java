package com.vmware.nimbus.ui.main.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.vmware.nimbus.R;
import com.vmware.nimbus.api.SingletonRequest;
import com.vmware.nimbus.data.model.DeployBlueprintModel;
import com.vmware.nimbus.data.model.LoginModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * A [DialogFragment] for the deploy dialog box.
 */
public class DeployFragment extends DialogFragment {

    // TODO: Change this string to a resource or something less hard-coded
    String bpRequestUrl = "https://api.mgmt.cloud.vmware.com/blueprint/api/blueprint-requests";

    /**
     * Called when the View is created.
     * @param inflater - the LayoutInflater
     * @param container - the ViewGroup
     * @param savedInstanceState - the savedInstanceState
     * @return - the root view of this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.deploy_dialog, container, false);
        (rootView.findViewById(R.id.button_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView title = rootView.findViewById(R.id.bp_title);
        title.setText(getArguments().getString("bp_name"));

        final Button deployButton = rootView.findViewById(R.id.confirm_deploy);
        deployButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText dpName = rootView.findViewById(R.id.dp_name);
                String dpNameTxt = dpName.getText().toString();
                String projectIdTxt = getArguments().getString("project_id");
                EditText dp_rsn = rootView.findViewById(R.id.dp_rsn);
                String dpRsnTxt = dp_rsn.getText().toString();
                String bpIdTxt = getArguments().getString("bp_id");

                try {
                    deployBlueprint(dpNameTxt, projectIdTxt, dpRsnTxt, bpIdTxt);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    /**
     * Called when the Dialog box is created.
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
     * Deploys a blueprint.
     * @param name - name of the deployment
     * @param pId - project ID
     * @param rsn - reason for deploying this blueprint
     * @param bpId - blueprint ID
     * @throws JSONException
     */
    public void deployBlueprint(String name, String pId, String rsn, String bpId) throws JSONException {
        DeployBlueprintModel requestBody = new DeployBlueprintModel(name, pId, rsn, bpId);
        Gson gson = new Gson();
        String json = gson.toJson(requestBody);
        JSONObject jsonObject = new JSONObject(json);

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, bpRequestUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("dp request", response.toString());
                        dismiss();
                        toastMsg("Blueprint deployed");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("dp request", "error in deploying: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String bearerToken = "Bearer " + LoginModel.getInstance(getActivity().getApplicationContext()).getBearer_token();
                params.put("Authorization", bearerToken);
                return params;
            }
        };

        SingletonRequest.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjRequest);

    }

    /**
     * Displays a toast!
     * @param msg - the toast message
     */
    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG);
        toast.show();
    }
}
