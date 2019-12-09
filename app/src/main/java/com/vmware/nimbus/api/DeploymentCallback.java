package com.vmware.nimbus.api;

import com.vmware.nimbus.data.model.DeploymentItemModel;

import java.util.List;

public interface DeploymentCallback {
    void onSuccess(List<DeploymentItemModel.DeploymentItem> result);
}
