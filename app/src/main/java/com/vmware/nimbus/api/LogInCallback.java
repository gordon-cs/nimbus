package com.vmware.nimbus.api;

public interface LogInCallback {
    void onSuccess(boolean result);

    void onFailure(boolean result);
}
