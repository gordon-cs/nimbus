package com.vmware.nimbus.data.model;

import com.vmware.nimbus.api.APIService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeploymentItemModel implements Serializable {

    public class DeploymentItem implements Serializable {
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
        public APIService.PowerState powerState;
        public Boolean simulated;
        public ArrayList<DeploymentResource> resources;
        public ArrayList<DeploymentAction> actions;
        public DeploymentRequestStatus lastRequest;
    }

    public class DeploymentItemPage implements Serializable {
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

    public class DeploymentResourcePage implements Serializable {
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

    public class DeploymentResource implements Serializable {
        public String createdAt;
        public String description;
        public String id;
        public String name;
        public String state;
        public String syncStatus;
        public String type;
        public DeploymentProperties properties;
    }

    public class DeploymentProperties implements Serializable {
        public String address;
        public String powerState;
        public ArrayList<NetworkCard> networks;
    }

    public class NetworkCard implements Serializable {
        public String address;
    }

    public class DeploymentAction implements Serializable {
        public String id;
        public String displayName;
        public Boolean valid;
    }

    public class DeploymentRequestStatus implements Serializable {
        public String id;
        public String name;
        public String requestedBy;
        public String actionId;
        public String deploymentId;
        public String status;
        public String details;
        public Date createdAt;
        public Date updatedAt;
        public Integer totalTasks;
        public Integer completedTasks;
    }
}
