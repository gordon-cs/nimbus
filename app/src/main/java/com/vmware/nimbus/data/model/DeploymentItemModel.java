package com.vmware.nimbus.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeploymentItemModel {

    public class DeploymentItem {
        public String blueprintId;
        public String catalogItemId;
        public String createdAt;
        public String createdBy;
        public String description;
        public String id;
        public String lastUpdatedAt;
        public String lastUpdatedBy;
        public String leaseExpireAt;
        public String name;
        public String projectId;
        public Boolean simulated;
        public ArrayList<DeploymentResource> resources;
        public ArrayList<DeploymentAction> actions;
    }
//    public DeploymentItemModel(String id, String blueprintId) {
//        this.id = id;
//        this.blueprintId = blueprintId;
//    }

    public class DeploymentItemPage {
        public List<DeploymentItem> content;
        public Boolean last;
        public int totalElements;
        public int totalPages;
        public Boolean first;
        public int numberOfElements;
        public int size;
        public int number;
        public Boolean empty;
    }

    public class DeploymentResourcePage {
        public ArrayList<DeploymentResource> content;
        public Boolean last;
        public int totalElements;
        public int totalPages;
        public Boolean first;
        public int numberOfElements;
        public int size;
        public int number;
        public Boolean empty;
    }

    public class DeploymentResource {
        public String createdAt;
        public String description;
        public String id;
        public String name;
        public String state;
        public String syncStatus;
        public String type;
        public DeploymentProperties properties;
    }

    public class DeploymentProperties {
        public String powerState;
        public ArrayList<NetworkCard> networks;
    }

    public class NetworkCard {
        public String address;
    }

    public class DeploymentAction {
        public String id;
        public String displayName;
        public Boolean valid;
    }


}
