package com.vmware.nimbus.data.model;

public class LoginModel {

    private String api_token;
    private boolean isAuthenticated;
    private static LoginModel instance;


    public LoginModel() {
        this.api_token = "";
        this.isAuthenticated = false;
    }

    public static LoginModel getInstance() {
        if (instance == null) {
            instance = new LoginModel();
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
