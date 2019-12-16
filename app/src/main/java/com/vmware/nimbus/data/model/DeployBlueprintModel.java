package com.vmware.nimbus.data.model;

public class DeployBlueprintModel {

    public DeployBlueprintModel(String name, String pId, String rsn, String bpId){
        deploymentName = name;
        projectId = pId;
        reason = rsn;
        blueprintId = bpId;
    }

    public String deploymentName;
    public String projectId;
    public String reason;
    public String blueprintId;
}
