package com.vmware.nimbus.ui.main;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vmware.nimbus.R;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String currentBaseUrl = settings.getString(getApplicationContext().getResources().getString(R.string.base_url_shared_property_name), null);
        if (currentBaseUrl != null && !currentBaseUrl.equals("")) {
            ((EditText)findViewById(R.id.rootUri)).setText(currentBaseUrl);
        }

        final Button saveButton = findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                settings.edit().putString(
                        getApplicationContext().getResources().getString(R.string.base_url_shared_property_name),
                        ((EditText)findViewById(R.id.rootUri)).getText().toString()
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
                ((EditText)findViewById(R.id.rootUri)).setText(getApplicationContext().getResources().getString(R.string.base_url));
                toastMsg("Restored Settings", getApplicationContext());
            }
        });
    }

    public static void toastMsg(String msg, Context c) {
        Toast toast = Toast.makeText(c, msg, Toast.LENGTH_LONG);
        toast.show();
    }
}