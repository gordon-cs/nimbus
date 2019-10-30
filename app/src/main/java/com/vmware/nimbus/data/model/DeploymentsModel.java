package com.vmware.nimbus.data.model;

import org.json.JSONObject;

public class DeploymentsModel {
    public String helloData;
    public String worldData;
    public String dataIndex;

    public JSONObject deploymentsJSON;

    public DeploymentsModel(String helloData, String worldData, String dataIndex) {
        this.helloData = helloData;
        this.worldData = worldData;
        this.dataIndex = dataIndex;
    }

    public DeploymentsModel(JSONObject jsonObject) {
        this.deploymentsJSON = jsonObject;
    }
}
