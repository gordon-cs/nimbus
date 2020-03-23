package com.vmware.nimbus.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.api.APIService;
import com.vmware.nimbus.api.DeploymentCallback;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.ui.main.fragments.DeploymentActionsFragment;

import java.io.Serializable;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

/**
 * A [Serializable] [AppCompatActivity] for the deployments page.
 */
public class DeploymentActivity extends AppCompatActivity implements Serializable {

    DeploymentItemModel.DeploymentItem deploymentItem;

    /**
     * Called after the activity is created.
     *
     * @param savedInstanceState - the savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deployment);
        deploymentItem = (DeploymentItemModel.DeploymentItem) getIntent()
                .getSerializableExtra("DeploymentItemModel.DeploymentItem");

        TextView deploymentName = findViewById(R.id.deployment_name);
        TextView deploymentCreatedAt = findViewById(R.id.deployment_created_at);
        TextView deploymentDescription = findViewById(R.id.deployment_description);
        TextView deploymentCreatedBy = findViewById(R.id.deployment_created_by);
        TextView deploymentId = findViewById(R.id.deployment_id);
        TextView deploymentUpdatedAt = findViewById(R.id.deployment_updated_at);
        TextView deploymentUpdatedBy = findViewById(R.id.deployment_updated_by);
        TextView deploymentProjectId = findViewById(R.id.deployment_project_id);
        TextView deploymentStatus = findViewById(R.id.deployment_status);
        TextView deploymentExpiration = findViewById(R.id.deployment_expires_at);

        String status = APIService.getPowerState(deploymentItem);
        CardView deploymentCard = findViewById(R.id.deployment_card);

        if (status.equals("Unknown")){
            deploymentCard.setCardBackgroundColor(Color.parseColor("#a4a9ac"));
        }
        else if (status.equals("Off")) {
            deploymentCard.setCardBackgroundColor(Color.parseColor("#ffcccb"));
        }
        else if (status.equals("On")) {
            deploymentCard.setCardBackgroundColor(Color.parseColor("#90ee90"));
        }

        deploymentName.setText(deploymentItem.name);
        deploymentDescription.setText(deploymentItem.description);
        deploymentStatus.setText(status);
        deploymentCreatedAt.setText(deploymentItem.createdAt);
        deploymentCreatedBy.setText(deploymentItem.createdBy);
        deploymentId.setText(deploymentItem.id);
        deploymentUpdatedAt.setText(deploymentItem.lastUpdatedAt);
        deploymentUpdatedBy.setText(deploymentItem.lastUpdatedBy);
        deploymentProjectId.setText(deploymentItem.projectId);
        deploymentExpiration.setText(deploymentItem.leaseExpireAt);

        getSupportActionBar().setTitle(deploymentItem.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Button actionsButton = findViewById(R.id.actions_button);
        actionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new DeploymentActionsFragment();
                Bundle args = new Bundle();
                args.putSerializable("name", deploymentItem.name);
                args.putSerializable("id", deploymentItem.id);
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
