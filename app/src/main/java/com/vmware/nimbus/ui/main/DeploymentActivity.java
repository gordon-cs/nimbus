package com.vmware.nimbus.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.data.model.DeploymentItemModel;

import java.io.Serializable;

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
    }

}
