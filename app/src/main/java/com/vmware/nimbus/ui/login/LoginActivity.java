package  com.vmware.nimbus.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import com.vmware.nimbus.R;
import com.vmware.nimbus.api.APIService;
import com.vmware.nimbus.api.LogInCallback;
import com.vmware.nimbus.ui.main.MainActivity;

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

    @Override
    protected void onPause() {
        super.onPause();
    }
}
