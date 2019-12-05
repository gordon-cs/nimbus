package com.vmware.nimbus.data.model;

import java.util.ArrayList;
import java.util.List;

public class BlueprintItemModel {

    public class BlueprintItem {
        public String id;
        private String createdAt;
        public String createdBy;
        private String updatedAt;
        private String updatedBy;
        private String orgId;
        private String projectId;
        private String projectName;
        private String selfLink;
        public String name;
        private String description;
        private String status;
        private String valid;
        private ArrayList<String> validationMessages;
        private int totalVersions;
        private int totalReleasedVersions;
        private boolean requestScopeOrg;
        private ArrayList<String> contentSourceSyncMessages;
    }

    public class BlueprintItemPage {
        public List<BlueprintItem> content;
        private Boolean last;
        private int totalElements;
        private int totalPages;
        private Boolean first;
        private int numberOfElements;
        public int size;
        private int number;
        private Boolean empty;
    }

}
