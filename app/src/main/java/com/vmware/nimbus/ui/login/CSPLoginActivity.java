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
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vmware.nimbus.R;
import com.vmware.nimbus.api.APIService;
import com.vmware.nimbus.api.DefaultOrgCallback;
import com.vmware.nimbus.api.ServiceRolesCallback;
import com.vmware.nimbus.data.model.CspResult;
import com.vmware.nimbus.data.model.LoginModel;
import com.vmware.nimbus.ui.main.MainActivity;
import com.vmware.nimbus.ui.main.OptionsActivity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSPLoginActivity extends AppCompatActivity {

    Integer targetUrlHitCount;
    Boolean loaded;
    Intent mainIntent;
    Intent errorIntent;
    String defaultOrgRefLink;
    String defaultOrgId;
    List<String> serviceRoleNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        switchToTokenLoginIfSet();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csplogin);

        mainIntent = new Intent(this, MainActivity.class);
        errorIntent = new Intent(this, LoginErrorActivity.class);
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
        cspLoginPage.loadUrl(getApplicationContext().getResources().getString(R.string.mgmt_url));

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

    private class MyBrowser extends WebViewClient {
        // allow redirect
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String uri = request.getUrl().toString();
            view.loadUrl(uri);
            return false;
        }

        @Override
        public void onReceivedError (WebView view, WebResourceRequest request, WebResourceError error) {
            startActivityWithMessage(errorIntent, getApplicationContext().getResources().getString(R.string.loading_failure_error));
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
                        completeLogin(s);
                    }
                });
            }

            // wait for login page to load before attempting login redirect
            if (url.contains("csp/gateway/discovery")) {
                loaded = true;
            }
        }

        private void completeLogin (String s) {
            try {
                //alter string to remove double quotes and backslashes
                s = s.replaceAll("^\"|\"$", "");
                s = s.replaceAll("\\\\", "");

                // derive access_token and token_type to make CspResult from response
                JsonObject obj = new Gson().fromJson(s, JsonObject.class);
                String access_token = obj.get("access_token").getAsString();
                String token_type = obj.get("token_type").getAsString();
                CspResult result = new CspResult(token_type, access_token);

                if (result.getToken().isEmpty()) {
                    startActivityWithMessage(errorIntent, getApplicationContext().getResources().getString(R.string.missing_token_error));
                }


                // add token to context
                Context c = getBaseContext();
                LoginModel.getInstance(c).setAuthenticated(true);
                LoginModel.getInstance(c).setBearer_token(result.getToken());


                APIService.getDefaultOrg(new DefaultOrgCallback() {
                    @Override
                    public void onSuccess(String result) {
                        defaultOrgRefLink = result;
                        Pattern pattern = Pattern.compile("\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}");
                        Matcher matcher = pattern.matcher(defaultOrgRefLink);
                        if (matcher.find()){
                            defaultOrgId = matcher.group(0);
                        } else {
                            startActivityWithMessage(errorIntent, getApplicationContext().getResources().getString(R.string.org_unconfirmed_error));
                        }

                        APIService.getUserServiceRoles(new ServiceRolesCallback() {
                            @Override
                            public void onSuccess(List<String> result) {
                                serviceRoleNames = result;
                                if (serviceRoleNames.contains("automationservice:user") || serviceRoleNames.contains("catalog:user")) {
                                        startActivity(mainIntent);
                                        finish();
                                } else {
                                    startActivityWithMessage(errorIntent, getApplicationContext().getResources().getString(R.string.org_access_error));
                                }
                            }

                            @Override
                            public void onFailure() {
                                startActivityWithMessage(errorIntent, getApplicationContext().getResources().getString(R.string.org_unconfirmed_error));
                            }
                        }, defaultOrgId, c, true);
                    }

                    @Override
                    public void onFailure() {
                        startActivityWithMessage(errorIntent, getApplicationContext().getResources().getString(R.string.org_unconfirmed_error));
                    }
                }, c, true);

            } catch (Exception e){
                startActivityWithMessage(errorIntent, getApplicationContext().getResources().getString(R.string.general_login_error));
            }

        }

        private void startActivityWithMessage (Intent intent, String message) {
            Bundle bundle = new Bundle();
            bundle.putString("message", message);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

}