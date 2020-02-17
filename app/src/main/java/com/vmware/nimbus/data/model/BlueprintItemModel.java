package com.vmware.nimbus.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BlueprintItemModel {

    public class BlueprintItem implements Serializable {
        public String id;
        public String createdAt;
        public String createdBy;
        public String updatedAt;
        public String updatedBy;
        public String orgId;
        public String projectId;
        public String projectName;
        public String selfLink;
        public String name;
        public String description;
        public String status;
        public String valid;
        public ArrayList<String> validationMessages;
        public int totalVersions;
        public int totalReleasedVersions;
        public boolean requestScopeOrg;
        public ArrayList<String> contentSourceSyncMessages;
    }

    public class BlueprintItemPage implements Serializable {
        public List<BlueprintItem> content;
        public Boolean last;
        public int totalElements;
        public int totalPages;
        public Boolean first;
        public int numberOfElements;
        public int size;
        public int number;
        public Boolean empty;
    }

}
