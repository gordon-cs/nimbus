package com.vmware.nimbus.ui.main;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vmware.nimbus.R;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        final Button saveButton = findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastMsg("Save clicked", getApplicationContext());
            }
        });
    }

    public static void toastMsg(String msg, Context c) {
        Toast toast = Toast.makeText(c, msg, Toast.LENGTH_LONG);
        toast.show();
    }
}