package com.vmware.nimbus.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.vmware.nimbus.R;
import com.vmware.nimbus.api.DeploymentActionResultCallback;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.data.model.LoginModel;
import com.vmware.nimbus.ui.main.fragments.DeploymentActionsFragment;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class DeploymentActivity extends AppCompatActivity implements Serializable {

    DeploymentItemModel.DeploymentItem deploymentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deployment);
        deploymentItem = (DeploymentItemModel.DeploymentItem) getIntent().getSerializableExtra("DeploymentItemModel.DeploymentItem");
        Log.d("DeploymentActivity", deploymentItem.name);

        TextView deploymentName = findViewById(R.id.deployment_name);
        TextView deploymentCreatedAt = findViewById(R.id.deployment_created_at);

        deploymentName.setText(deploymentItem.name);
        deploymentCreatedAt.setText("Created At: " + deploymentItem.createdAt);

        getSupportActionBar().setTitle(deploymentItem.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Button actionsButton = findViewById(R.id.actions_button);
        actionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new DeploymentActionsFragment();
                Bundle args = new Bundle();
                args.putSerializable("deploymentItem", deploymentItem);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "Actions");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }




}
