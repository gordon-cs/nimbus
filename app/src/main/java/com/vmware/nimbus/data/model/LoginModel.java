package com.vmware.nimbus.data.model;

import android.content.Context;

public class LoginModel {

    private String api_token;
    private boolean isAuthenticated;
    private static LoginModel instance;
    private static Context ctx;


    private LoginModel(Context context) {
        this.ctx = context;
        this.api_token = "";
        this.isAuthenticated = false;
    }

    public static synchronized LoginModel getInstance(Context context) {
        if (instance == null) {
            instance = new LoginModel(context);
        }
        return instance;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }


}
