package com.vmware.nimbus.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vmware.nimbus.R;
import com.vmware.nimbus.ui.main.MainActivity;
import com.vmware.nimbus.ui.main.OptionsActivity;

public class CSPLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csplogin);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent tokenIntent = new Intent(this, TokenLoginActivity.class);
        if(settings.getBoolean(getApplicationContext().getResources().getString(R.string.catalog_source_property_name),
                false)) {
            startActivity(tokenIntent);
            finish();
        }

        WebView cspLoginPage = (WebView) findViewById(R.id.webview);
        cspLoginPage.loadUrl("https://www.mgmt.cloud.vmware.com/catalog/#/library");

        Intent optionsIntent = new Intent(this, OptionsActivity.class);
        final Button optionsButton = findViewById(R.id.options);

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(optionsIntent);
            }
        });
    }

    public static void toastMsg(String msg, Context c) {
        Toast toast = Toast.makeText(c, msg, Toast.LENGTH_LONG);
        toast.show();
    }
}