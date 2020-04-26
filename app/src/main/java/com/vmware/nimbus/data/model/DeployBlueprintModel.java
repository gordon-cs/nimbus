package com.vmware.nimbus.data.model;

public class DeployBlueprintModel {

    public String deploymentName;
    public String projectId;
    public String reason;
    public String blueprintId;
    public String description;

    public DeployBlueprintModel(String name, String pId, String rsn, String bpId, String desc) {
        deploymentName = name;
        projectId = pId;
        reason = rsn;
        blueprintId = bpId;
        description = desc;
    }
}
