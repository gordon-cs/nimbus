package com.vmware.nimbus.data.model;

public class CspResult {

    private String tokenType;
    private String token;

    public CspResult(String tokenType, String token) {
        this.token = token;
        this.tokenType = tokenType;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getToken() {
        return token;
    }
}
