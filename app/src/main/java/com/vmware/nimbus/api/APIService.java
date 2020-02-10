package com.vmware.nimbus.api;

import android.content.Context;

import com.vmware.nimbus.data.model.LoginModel;

public class APIService {

    public void LogOut(Context c) {
        LoginModel.getInstance(c).setAuthenticated(false);
        LoginModel.getInstance(c).setApi_token("");
    }
}
