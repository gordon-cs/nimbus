package com.vmware.nimbus.ui.main;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.data.model.BlueprintItemModel;
import com.vmware.nimbus.ui.main.fragments.DeployFragment;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

/**
 * A [Serializable] [AppCompatActivity] for the blueprints page.
 */
public class BlueprintActivity extends AppCompatActivity implements Serializable {

    BlueprintItemModel.BlueprintItem blueprintItem;

    /**
     * Called after the activity is created.
     *
     * @param savedInstanceState - the savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blueprint);
        blueprintItem = (BlueprintItemModel.BlueprintItem) getIntent().getSerializableExtra("BlueprintItemModel.BlueprintItem");
        getSupportActionBar().setTitle(blueprintItem.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView bpDescription = findViewById(R.id.bp_description);
        TextView bpId = findViewById(R.id.bp_id);
        TextView bpUpdated = findViewById(R.id.bp_updated);
        TextView bpUpdatedBy = findViewById(R.id.bp_updated_by);
        TextView bpOrgId = findViewById(R.id.bp_org_id);
        TextView bpProjectName = findViewById(R.id.bp_project_name);
        TextView bpStatus = findViewById(R.id.bp_status);
        TextView bpName = findViewById(R.id.blueprint_name);

        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat sdfOut = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        Date date;
        String goodUpdatedAt = null;
        try {
            date = sdfIn.parse(blueprintItem.updatedAt);
            goodUpdatedAt = sdfOut.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        bpName.setText(blueprintItem.name);
        bpDescription.setText(blueprintItem.description);
        bpId.setText(blueprintItem.id);
        bpUpdated.setText(goodUpdatedAt);
        bpUpdatedBy.setText(blueprintItem.updatedBy);
        bpOrgId.setText(blueprintItem.orgId);
        bpProjectName.setText(blueprintItem.projectName);
        bpStatus.setText(blueprintItem.status);

        final Button deployButton = findViewById(R.id.deployButton);
        deployButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new DeployFragment();
                Bundle args = new Bundle();
                args.putSerializable("bp_name", blueprintItem.name);
                args.putSerializable("project_id", blueprintItem.projectId);
                args.putSerializable("bp_id", blueprintItem.id);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "deploying");
            }
        });
    }

    /**
     * Gets a single blueprint item.
     *
     * @return - a blueprint item.
     */
    public BlueprintItemModel.BlueprintItem getBlueprintItem() {
        return blueprintItem;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
