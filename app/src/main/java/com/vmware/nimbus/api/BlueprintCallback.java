package com.vmware.nimbus.api;

import com.vmware.nimbus.data.model.BlueprintItemModel;

import java.util.List;

public interface BlueprintCallback {
    void onSuccess(List<BlueprintItemModel.BlueprintItem> result);
}
