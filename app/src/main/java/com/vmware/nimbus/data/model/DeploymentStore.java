package com.vmware.nimbus.data.model;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;


import com.vmware.nimbus.api.DeploymentService;

import java.util.List;
import android.os.Handler;

public class DeploymentStore {
    private List<DeploymentItemModel.DeploymentItem> deployments;
    private static Context context;

    private String url = "https://api.mgmt.cloud.vmware.com/deployment/api/deployments?size=100";

    public DeploymentStore() {
        this.deployments = null;
        this.context = context;
    }

    public List<DeploymentItemModel.DeploymentItem> getDeployments() {
        //DeploymentService.startActionGetDeployments(this.context, url, "GET", "");

        return deployments;
    }

    public void replaceDeployment(DeploymentItemModel.DeploymentItem deployment) {

    }
}
