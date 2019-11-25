package com.vmware.nimbus.data.model;

import java.util.ArrayList;
import java.util.Optional;

public class DeploymentItemModel {

    private Optional<String> blueprintId;
    private Optional<String> catalogItemId;
    private String createdAt;
    private String createdBy;
    private Optional<String> description;
    private String id;
    private String lastUpdatedAt;
    private String lastUpdatedBy;
    private String leaseExpireAt;
    private String projectId;
    private Optional<Boolean> simulated;
    private Optional<ArrayList<DeploymentResource>> resources;
    private Optional<ArrayList<DeploymentAction>> actions;

    public DeploymentItemModel(String id, Optional<String> blueprintId) {
        this.id = id;
        this.blueprintId = blueprintId;
    }

    class DeploymentItemPage {
        private ArrayList<DeploymentItemModel> content;
        private Boolean last;
        private int totalElements;
        private int totalPages;
        private Boolean first;
        private int numberOfElements;
        private int size;
        private int number;
        private Boolean empty;
    }

    class DeploymentResourcePage {
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

    class DeploymentResource {
        private Optional<String> createdAt;
        private Optional<String> description;
        private String id;
        private String name;
        private Optional<String> state;
        private Optional<String> syncStatus;
        private Optional<String> type;
        private DeploymentProperties properties;
    }

    class DeploymentProperties {
        private Optional<String> powerState;
        private Optional<ArrayList<NetworkCard>> networks;
    }

    class NetworkCard {
        private Optional<String> address;
    }

    class DeploymentAction {
        private String id;
        private String displayName;
        private Boolean valid;
    }


}
