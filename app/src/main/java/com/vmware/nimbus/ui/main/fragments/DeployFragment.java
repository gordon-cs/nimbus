package com.vmware.nimbus.ui.main.fragments;

import android.app.Dialog;
import android.content.Context;
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
import com.vmware.nimbus.api.APIService;
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

    private Context c;
    private String bpRequestUrl;

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
        View rootView = inflater.inflate(R.layout.deploy_dialog, container, false);
        c = getContext();
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
                EditText dp_desc = rootView.findViewById(R.id.dp_desc);
                String descText = dp_desc.getText().toString();
                String bpIdTxt = getArguments().getString("bp_id");

                try {
                    APIService.deployBlueprint(dpNameTxt, projectIdTxt, dpRsnTxt, bpIdTxt, descText, c);
                    dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
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
     * Displays a toast!
     *
     * @param msg - the toast message
     */
    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG);
        toast.show();
    }
}
