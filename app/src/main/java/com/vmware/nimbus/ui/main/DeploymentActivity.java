package com.vmware.nimbus.ui.main;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.api.APIService;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.ui.main.fragments.DeploymentActionsFragment;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

/**
 * A [Serializable] [AppCompatActivity] for the deployments page.
 */
public class DeploymentActivity extends AppCompatActivity implements Serializable {

    DeploymentItemModel.DeploymentItem deploymentItem;
    TableLayout resourcesLayout;

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

        resourcesLayout = findViewById(R.id.resources_layout);
        if (deploymentItem.resources == null) {
            TableRow defaultRow = new TableRow(this);
            TextView defaultHeading = new TextView(this);
            defaultHeading.setText("No Resources Found");
            defaultRow.addView(defaultHeading);
            resourcesLayout.addView(defaultRow);
        } else {
            for (int i = 0; i < deploymentItem.resources.size(); i++) {
                TableRow typeRow = new TableRow(this);
                TextView resourceTypeHeading = new TextView(this);
                resourceTypeHeading.setText("Type: ");
                resourceTypeHeading.setTextSize(12);
                resourceTypeHeading.setTypeface(resourceTypeHeading.getTypeface(), Typeface.BOLD);
                TextView resourceType = new TextView(this);
                resourceType.setText(deploymentItem.resources.get(i).name);
                resourceType.setTextSize(12);
                typeRow.addView(resourceTypeHeading);
                typeRow.addView(resourceType);
                resourcesLayout.addView(typeRow);

                TableRow nameRow = new TableRow(this);
                TextView resourceNameHeading = new TextView(this);
                resourceNameHeading.setText("Name: ");
                resourceNameHeading.setTextSize(12);
                resourceNameHeading.setTypeface(resourceNameHeading.getTypeface(), Typeface.BOLD);
                TextView resourceName = new TextView(this);
                resourceName.setText(deploymentItem.resources.get(i).name);
                resourceName.setTextSize(12);
                nameRow.addView(resourceNameHeading);
                nameRow.addView(resourceName);
                resourcesLayout.addView(nameRow);

                TableRow statusRow = new TableRow(this);
                TextView resourceStateHeading = new TextView(this);
                resourceStateHeading.setText("Status: ");
                resourceStateHeading.setTextSize(12);
                resourceStateHeading.setTypeface(resourceStateHeading.getTypeface(), Typeface.BOLD);
                TextView resourceState = new TextView(this);
                resourceState.setText(deploymentItem.resources.get(i).properties.powerState);
                resourceState.setTextSize(12);
                statusRow.addView(resourceStateHeading);
                statusRow.addView(resourceState);
                resourcesLayout.addView(statusRow);

                TableRow ipRow = new TableRow(this);
                TextView resourceIpHeading = new TextView(this);
                resourceIpHeading.setText("IP: ");
                resourceIpHeading.setTextSize(12);
                resourceIpHeading.setTypeface(resourceIpHeading.getTypeface(), Typeface.BOLD);
                TextView resourceIP = new TextView(this);
                resourceIP.setText(deploymentItem.resources.get(i).properties.address);
                resourceIP.setTextSize(12);
                ipRow.addView(resourceIpHeading);
                ipRow.addView(resourceIP);
                ipRow.setPadding(0, 0, 0, 20);
                resourcesLayout.addView(ipRow);
            }
        }

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

        APIService.PowerState status = APIService.getPowerState(deploymentItem);
        CardView deploymentCard = findViewById(R.id.deployment_card);

        if (status == APIService.PowerState.UNKNOWN){
            deploymentCard.setCardBackgroundColor(Color.parseColor("#a4a9ac"));
        }
        else if (status == APIService.PowerState.OFF) {
            deploymentCard.setCardBackgroundColor(Color.parseColor("#ffcccb"));
        }
        else if (status == APIService.PowerState.ON) {
            deploymentCard.setCardBackgroundColor(Color.parseColor("#90ee90"));
        }

        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat sdfInExpiration = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat sdfOut = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        Date createDate;
        String goodCreatedAt = null;
        Date updatedDate;
        String goodUpdatedAt = null;
        Date expireDate;
        String goodExpiresAt = null;
        try {
            createDate = sdfIn.parse(deploymentItem.createdAt);
            goodCreatedAt = sdfOut.format(createDate);

            updatedDate = sdfIn.parse(deploymentItem.lastUpdatedAt);
            goodUpdatedAt = sdfOut.format(updatedDate);

            if (deploymentItem.leaseExpireAt != null) {
                expireDate = sdfInExpiration.parse(deploymentItem.leaseExpireAt);
                goodExpiresAt = sdfOut.format(expireDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        deploymentName.setText(deploymentItem.name);
        deploymentDescription.setText(deploymentItem.description);
        deploymentStatus.setText(status.toString());
        deploymentCreatedAt.setText(goodCreatedAt);
        deploymentCreatedBy.setText(deploymentItem.createdBy);
        deploymentId.setText(deploymentItem.id);
        deploymentUpdatedAt.setText(goodUpdatedAt);
        deploymentUpdatedBy.setText(deploymentItem.lastUpdatedBy);
        deploymentProjectId.setText(deploymentItem.projectId);
        deploymentExpiration.setText(goodExpiresAt);

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
