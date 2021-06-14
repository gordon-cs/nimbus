package com.vmware.nimbus.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vmware.nimbus.R;
import com.vmware.nimbus.data.model.CspResult;
import com.vmware.nimbus.data.model.LoginModel;
import com.vmware.nimbus.ui.main.MainActivity;
import com.vmware.nimbus.ui.main.OptionsActivity;

public class CSPLoginActivity extends AppCompatActivity {

    Integer targetUrlHitCount;
    Boolean loaded;
    Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        switchToTokenLoginIfSet();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csplogin);

        mainIntent = new Intent(this, MainActivity.class);
        targetUrlHitCount = 0;
        loaded = false;

        // create WebView, clear local storage and cookies
        WebView cspLoginPage = (WebView) findViewById(R.id.webview);
        WebStorage.getInstance().deleteAllData();
        CookieManager.getInstance().removeAllCookies(null);

        // allow storage and javascript, set Webview Client
        WebSettings websettings = cspLoginPage.getSettings();
        websettings.setDomStorageEnabled(true);
        websettings.setJavaScriptEnabled(true);
        cspLoginPage.setWebViewClient(new MyBrowser());
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

    @Override
    protected void onResume() {
        switchToTokenLoginIfSet();
        super.onResume();
    }

    private void switchToTokenLoginIfSet() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent tokenIntent = new Intent(this, TokenLoginActivity.class);
        if(settings.getBoolean(getApplicationContext().getResources().getString(R.string.login_source_property_name),
                false)) {
            startActivity(tokenIntent);
            finish();
        }
    }

    /**
     * Wrapper for Toast class.
     *
     * @param msg - the message to Toast
     */
    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
        toast.show();
    }

    private class MyBrowser extends WebViewClient {
        // allow redirect
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String uri = request.getUrl().toString();
            view.loadUrl(uri);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // allow 2 hits to library before redirecting
            if ("https://www.mgmt.cloud.vmware.com/catalog/#/library".equals(url)) {
                targetUrlHitCount = targetUrlHitCount + 1;
                Log.d("CspLoginUrlHitCount", String.format("targetUrlHitCount: %s",targetUrlHitCount));
                if (targetUrlHitCount == 2) {
                    view.setVisibility(View.GONE);
                    view.loadUrl("https://api.mgmt.cloud.vmware.com/provisioning/access-token");
                }
            }

            if ("https://api.mgmt.cloud.vmware.com/provisioning/access-token".equals(url) && loaded) {
                view.evaluateJavascript("document.body.innerText", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        processResponse(s);
                    }
                });
            }

            // wait for login page to load before attempting login redirect
            if (url.contains("csp/gateway/discovery")) {
                loaded = true;
            }
        }

        private void processResponse (String s) {
            try {
                //alter string to remove double quotes and backslashes
                s = s.replaceAll("^\"|\"$", "");
                s = s.replaceAll("\\\\", "");

                // derive access_token and token_type to make CspResult from response
                JsonObject obj = new Gson().fromJson(s, JsonObject.class);
                String access_token = obj.get("access_token").getAsString();
                String token_type = obj.get("token_type").getAsString();
                CspResult result = new CspResult(token_type, access_token);

                if (access_token.isEmpty()) {
                    throw new IllegalStateException("Access token for login could not be found.");
                }

                // add token to context
                Context c = getBaseContext();
                LoginModel.getInstance(c).setAuthenticated(true);
                LoginModel.getInstance(c).setBearer_token(result.getToken());
                startActivity(mainIntent);
            } catch (Exception e){
                toastMsg("Failed to retrieve login information. See Options to log in with an API token instead");
            }

        }
    }

}