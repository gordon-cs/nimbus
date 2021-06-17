package com.vmware.nimbus.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.vmware.nimbus.R;
import com.vmware.nimbus.api.APIService;
import com.vmware.nimbus.api.DeploymentCallback;
import com.vmware.nimbus.api.LogInCallback;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.ui.main.MainActivity;
import com.vmware.nimbus.ui.main.OptionsActivity;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/**
 * The login activity.
 */
public class TokenLoginActivity extends AppCompatActivity {

    public static final String DEPLOYMENT_GET_FAILURE = "Failed to authenticate. Try again or use a different API token.";
    public static final String AUTH_FAILURE = "Failed to authenticate. Verify that your API token is for an " +
            "organization where you have access to Cloud Assembly and/or Service Broker. Try again or use a different API token.";

    String LOG_TAG = "LoginActivity";

    /**
     * Called after the login activity is created.
     *
     * @param savedInstanceState - the savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        switchToCSPLoginIfNotSet();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tokenlogin);
        Intent mainIntent = new Intent(this, MainActivity.class);
        Intent errorIntent = new Intent(this, LoginErrorActivity.class);
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
                                //attempt deployment loading
                                APIService.loadDeployments(new DeploymentCallback() {
                                    @Override
                                    public void onSuccess(List<DeploymentItemModel.DeploymentItem> result) {
                                        startActivity(mainIntent);
                                        //Complete and destroy login activity once successful
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(VolleyError error) {
                                        String message;
                                        if (error.toString().contains("AuthFailureError")) {
                                            message = AUTH_FAILURE;
                                        } else {
                                            message = DEPLOYMENT_GET_FAILURE;
                                        }
                                        Bundle bundle = new Bundle();
                                        bundle.putString("message", message);
                                        errorIntent.putExtras(bundle);
                                        startActivity(errorIntent);
                                        finish();
                                    }
                                }, getBaseContext());
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
    protected void onResume() {
        switchToCSPLoginIfNotSet();
        super.onResume();
    }

    private void switchToCSPLoginIfNotSet() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent cspIntent = new Intent(this, CSPLoginActivity.class);
        if(!settings.getBoolean(getApplicationContext().getResources().getString(R.string.login_source_property_name),
                false)) {
            startActivity(cspIntent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
