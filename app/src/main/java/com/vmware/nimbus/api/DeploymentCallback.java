package com.vmware.nimbus.api;

import com.android.volley.VolleyError;
import com.vmware.nimbus.data.model.DeploymentItemModel;

import java.util.List;

public interface DeploymentCallback {
    void onSuccess(List<DeploymentItemModel.DeploymentItem> result);
    void onFailure(VolleyError error);
}
