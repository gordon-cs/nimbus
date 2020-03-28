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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        int status = APIService.getPowerState(deploymentItem);

        CardView deploymentCard = findViewById(R.id.deployment_card);
        deploymentCard.setCardBackgroundColor(status);

        deploymentName.setText(deploymentItem.name);

        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat sdfOut = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        Date date;
        String goodCreatedAt = null;
        try {
            date = sdfIn.parse(deploymentItem.createdAt);
            goodCreatedAt = sdfOut.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        deploymentCreatedAt.setText("Created At: " + goodCreatedAt);

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
