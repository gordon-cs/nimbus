package com.vmware.nimbus.api;

import java.util.List;

public interface ServiceRolesCallback {
    void onSuccess(List<String> result);
    void onFailure();
}
