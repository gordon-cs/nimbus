package com.vmware.nimbus.data.model;

public class CspResult {

    private String id_token;
    private String token_type;
    private String scope;
    private String access_token;
    private String refresh_token;

    private int expires_in;
    //private boolean isAuthenticated = false;
    //private static CspResult loginModelInstance;

//    public boolean isAuthenticated() {
//        return isAuthenticated;
//    }
//
//    public void setAuthenticated(boolean authenticated) {
//        isAuthenticated = authenticated;
//    }
//
//    public static CspResult getInstance(){
//        return loginModelInstance;
//    }

    public String getId_token() {
        return id_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getScope() {
        return scope;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public CspResult(String id_token, String token_type, String scope, String access_token, String refresh_token, int expires_in) {
        this.id_token = id_token;
        this.token_type = token_type;
        this.scope = scope;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
    }
}
