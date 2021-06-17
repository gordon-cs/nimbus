package com.vmware.nimbus.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vmware.nimbus.R;

public class LoginErrorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginerror);
        final TextView details = (TextView) findViewById(R.id.details);

        Bundle bundle = getIntent().getExtras();
        String cause = bundle.getString("message");
        details.setText(cause);

        Intent tokenLoginIntent = new Intent(this, TokenLoginActivity.class);
        final Button tokenLoginButton = findViewById(R.id.apilogin);

        Intent cspLoginIntent = new Intent(this, CSPLoginActivity.class);
        final Button cspLoginButton = findViewById(R.id.csplogin);

        tokenLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                settings.edit().putBoolean(
                        getApplicationContext().getResources().getString(R.string.login_source_property_name),
                        true
                ).apply();
                startActivity(tokenLoginIntent);
                finish();
            }
        });

        cspLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(cspLoginIntent);
                finish();
            }
        });

    }
}
