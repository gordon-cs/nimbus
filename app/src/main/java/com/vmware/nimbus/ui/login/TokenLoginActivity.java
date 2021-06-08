package com.vmware.nimbus.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vmware.nimbus.R;
import com.vmware.nimbus.api.APIService;
import com.vmware.nimbus.api.LogInCallback;
import com.vmware.nimbus.ui.main.MainActivity;
import com.vmware.nimbus.ui.main.OptionsActivity;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The login activity.
 */
public class TokenLoginActivity extends AppCompatActivity {

    String LOG_TAG = "LoginActivity";

    /**
     * Called after the login activity is created.
     *
     * @param savedInstanceState - the savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tokenlogin);
        Intent mainIntent = new Intent(this, MainActivity.class);
        Intent optionsIntent = new Intent(this, OptionsActivity.class);
        final EditText apiKeyEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button optionsButton = findViewById(R.id.options);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(optionsIntent);
            }
        });

        //todo - verify input
        loginButton.setEnabled(true);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                APIService.LogIn(getBaseContext(), APIService.getBaseEndpointURL(getBaseContext()) + getResources().getString(R.string.login_uri),
                        apiKeyEditText.getText().toString(), new LogInCallback() {
                            @Override
                            public void onSuccess(boolean result) {
                                startActivity(mainIntent);
                                //Complete and destroy login activity once successful
                                finish();
                            }

                            @Override
                            public void onFailure(boolean result) {
                                loadingProgressBar.setVisibility(View.INVISIBLE);
                                toastMsg("Login failed, is root URI set?");
                            }
                        });
            }
        });
    }

    /**
     * Wrapper for Toast class.
     *
     * @param msg - the message to Toast
     */
    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
