package com.vmware.nimbus.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.vmware.nimbus.api.VolleyController;
import com.vmware.nimbus.data.LoginRepository;
import com.vmware.nimbus.R;
import com.vmware.nimbus.data.Result;
import com.vmware.nimbus.data.model.LoggedInUser;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login( String api_key) {
        // can be launched in a separate asynchronous job

        Log.d("apikey", api_key);

        
        Result<LoggedInUser> result = loginRepository.login(api_key);
        loginResult.setValue(new LoginResult(new LoggedInUserView("test")));
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged( String password) {
        if (!isApiKeyValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder password validation check
    private boolean isApiKeyValid(String api_key) {
        return api_key != null && api_key.trim().length() > 5;
    }
}
