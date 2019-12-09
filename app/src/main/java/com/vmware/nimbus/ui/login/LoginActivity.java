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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vmware.nimbus.api.SingletonRequest;
import com.vmware.nimbus.data.model.CspResult;
import com.vmware.nimbus.data.model.LoginModel;
import com.vmware.nimbus.ui.main.MainActivity;
import com.vmware.nimbus.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    String LOG_TAG = "LoginActivity";
    private String cspUrl = "https://console.cloud.vmware.com/csp/gateway/am/api/auth/api-tokens/authorize";
    //RequestQueue queue = SingletonRequest.getInstance(this.getApplicationContext()).getRequestQueue();

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

//        RequestQueue queue = Volley.newRequestQueue(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                StringRequest jsonObjRequest = new StringRequest(

                        Request.Method.POST,
                        cspUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("volley response", response);
                                Gson gson = new Gson();
                                CspResult cspResult = gson.fromJson(response, CspResult.class);
                                //Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                                LoginModel.getInstance(getBaseContext()).setAuthenticated(true);
                                LoginModel.getInstance(getBaseContext()).setApi_token(apiKeyEditText.getText().toString());
                                LoginModel.getInstance(getBaseContext()).setBearer_token(cspResult.getAccess_token());
                                startActivity(mainIntent);
                                //Complete and destroy login activity once successful
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("volley", "Error: " + error.getMessage());
                                error.printStackTrace();
                                loadingProgressBar.setVisibility(View.INVISIBLE);
                                toastMsg("Login Failed");
                            }
                        }) {

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("refresh_token", apiKeyEditText.getText().toString());
                        return params;
                    }
                };

                SingletonRequest.getInstance(getBaseContext()).addToRequestQueue(jsonObjRequest);
                Log.d(LOG_TAG, "Added request to queue.");

//                startActivity(mainIntent);
//                //Complete and destroy login activity once successful
                  //toastMsg("toast" + CspResult.getInstance().getAccess_token());
//                finish();
//
//                if(LoginModel.getInstance().isAuthenticated()) {
//
//                }
//                else{
//                    toastMsg("Login Failed");
//                }
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
