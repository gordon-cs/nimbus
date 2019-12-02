package com.vmware.nimbus.api;

import com.vmware.nimbus.data.model.BlueprintItemModel;

import java.util.List;

public interface DataCallback {
    void onSuccess(List<BlueprintItemModel.BlueprintItem> result);
}
