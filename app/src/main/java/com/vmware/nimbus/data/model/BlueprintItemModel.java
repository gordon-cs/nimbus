package com.vmware.nimbus.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BlueprintItemModel {

    public class BlueprintItem implements Serializable {
        // common elements
        public String id;
        public String createdAt;
        public String createdBy;
        public String updatedAt;

        //catalog properties
        public String lastUpdatedAt;
        public String lastUpdatedBy;
        public Type type;
        public List<String> projectIds;

        //blueprint properties
        public String updatedBy;
        public String orgId;
        public String projectId;
        public String projectName;
        public String selfLink;
        public String name;
        public String description;
        public String status;
        public String valid;
        public int totalVersions;
        public int totalReleasedVersions;
        public boolean requestScopeOrg;
        public ArrayList<String> contentSourceSyncMessages;
    }

    public class Type implements Serializable {
        public String id;
        public String link;
        public String name;
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
