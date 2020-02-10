package  com.vmware.nimbus.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
// import android.text.Editable;
// import android.text.TextWatcher;
// import android.view.KeyEvent;
import android.util.Log;
import android.view.View;
// import android.view.inputmethod.EditorInfo;
import android.widget.Button;
// import android.widget.EditText;
import android.widget.EditText;
import android.widget.ProgressBar;
// import android.widget.TextView;
import android.widget.Toast;

import com.vmware.nimbus.api.APIService;
import com.vmware.nimbus.api.LogInCallback;
import com.vmware.nimbus.ui.main.MainActivity;
import com.vmware.nimbus.R;

public class LoginActivity extends AppCompatActivity {

    String LOG_TAG = "LoginActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent mainIntent = new Intent(this, MainActivity.class);
        final EditText apiKeyEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        //todo - verify input
        loginButton.setEnabled(true);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                APIService.LogIn(getBaseContext(), getResources().getString(R.string.csp_URL),
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
                                toastMsg("Login Failed");
                            }
                        });
            }
        });
    }

    // Displays a toast so we can verify that the buttons work when clicked
    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    boolean isDataValid(String api_key) {
        boolean isDataValid = false;
        if(api_key != "" && api_key.length() > 5){
            isDataValid = true;
        }
        Log.d("apikey", api_key);
        return isDataValid;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
