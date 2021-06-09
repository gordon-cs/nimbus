package com.vmware.nimbus.ui.main;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.vmware.nimbus.R;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        final Switch catalogSource = findViewById(R.id.catalogSwitch);
        final Switch loginSource = findViewById(R.id.loginSwitch);
        final Button saveButton = findViewById(R.id.save);
        final EditText rootUri = findViewById(R.id.rootUri);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String currentBaseUrl = settings.getString(getApplicationContext().getResources().getString(R.string.base_url_shared_property_name), null);
        if (currentBaseUrl != null && !currentBaseUrl.equals("")) {
            rootUri.setText(currentBaseUrl);
        }

        catalogSource.setChecked(settings.getBoolean(
                getApplicationContext().getResources().getString(R.string.catalog_source_property_name),
                false
        ));

        loginSource.setChecked(settings.getBoolean(
                getApplicationContext().getResources().getString(R.string.login_source_property_name),
                false
        ));

        catalogSource.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                settings.edit().putBoolean(
                        getApplicationContext().getResources().getString(R.string.catalog_source_property_name),
                        isChecked
                ).apply();
            }
        });

        loginSource.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                settings.edit().putBoolean(
                        getApplicationContext().getResources().getString(R.string.login_source_property_name),
                        isChecked
                ).apply();

                if(isChecked) {
                    saveButton.setEnabled(true);
                    rootUri.setEnabled(true);
                } else {
                    saveButton.setEnabled(false);
                    rootUri.setEnabled(false);
                }
            }
        });

        if(!settings.getBoolean(getApplicationContext().getResources().getString(R.string.login_source_property_name),
                false)) {
            saveButton.setEnabled(false);
            rootUri.setEnabled(false);
        } else {
            saveButton.setEnabled(true);
            rootUri.setEnabled(true);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                settings.edit().putString(
                        getApplicationContext().getResources().getString(R.string.base_url_shared_property_name),
                        rootUri.getText().toString()
                ).apply();
                toastMsg("Saved Settings", getApplicationContext());
            }
        });

        final Button restoreDefaultsButton = findViewById(R.id.restore);

        restoreDefaultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                settings.edit().remove(
                        getApplicationContext().getResources().getString(R.string.base_url_shared_property_name)
                ).apply();
                rootUri.setText(getApplicationContext().getResources().getString(R.string.base_url));
                toastMsg("Restored Settings", getApplicationContext());
            }
        });

        getSupportActionBar().setTitle("Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public static void toastMsg(String msg, Context c) {
        Toast toast = Toast.makeText(c, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}