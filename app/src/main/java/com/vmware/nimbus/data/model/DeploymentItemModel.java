package com.vmware.nimbus.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeploymentItemModel {

    public class DeploymentItem {
        public String blueprintId;
        private String catalogItemId;
        private String createdAt;
        public String createdBy;
        public String description;
        public String id;
        private String lastUpdatedAt;
        private String lastUpdatedBy;
        private String leaseExpireAt;
        private String projectId;
        private Boolean simulated;
        private ArrayList<DeploymentResource> resources;
        private ArrayList<DeploymentAction> actions;
    }
//    public DeploymentItemModel(String id, String blueprintId) {
//        this.id = id;
//        this.blueprintId = blueprintId;
//    }

    public class DeploymentItemPage {
        public List<DeploymentItem> content;
        private Boolean last;
        private int totalElements;
        private int totalPages;
        private Boolean first;
        private int numberOfElements;
        public int size;
        private int number;
        private Boolean empty;
    }

    public class DeploymentResourcePage {
        private ArrayList<DeploymentResource> content;
        private Boolean last;
        private int totalElements;
        private int totalPages;
        private Boolean first;
        private int numberOfElements;
        private int size;
        private int number;
        private Boolean empty;
    }

    public class DeploymentResource {
        private String createdAt;
        private String description;
        private String id;
        private String name;
        private String state;
        private String syncStatus;
        private String type;
        private DeploymentProperties properties;
    }

    public class DeploymentProperties {
        private String powerState;
        private ArrayList<NetworkCard> networks;
    }

    public class NetworkCard {
        private String address;
    }

    public class DeploymentAction {
        private String id;
        private String displayName;
        private Boolean valid;
    }


}
