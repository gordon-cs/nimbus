package com.vmware.nimbus.ui.main.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.vmware.nimbus.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DeployFragment extends DialogFragment {

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

        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }
}
