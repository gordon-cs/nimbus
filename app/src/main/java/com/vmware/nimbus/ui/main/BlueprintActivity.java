package com.vmware.nimbus.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.data.model.BlueprintItemModel;

import java.io.Serializable;

public class BlueprintActivity extends AppCompatActivity implements Serializable {

    BlueprintItemModel.BlueprintItem blueprintItem;

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


        bpDescription.setText("Description: " + blueprintItem.description);
        bpId.setText("ID: " + blueprintItem.id);
        bpUpdated.setText("Updated At: " + blueprintItem.updatedAt);
        bpUpdatedBy.setText("Updated by: " + blueprintItem.updatedBy);
        bpOrgId.setText("Org ID: " + blueprintItem.orgId);
        bpProjectName.setText("Project: " + blueprintItem.projectName);
        bpStatus.setText("Status: " + blueprintItem.status);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
