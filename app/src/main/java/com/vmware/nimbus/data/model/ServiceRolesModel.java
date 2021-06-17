package com.vmware.nimbus.data.model;

import java.util.List;

public class ServiceRolesModel {
    public List<ServiceRoles> serviceRoles;

    public class ServiceRoles {
        private List<String> serviceRoleNames;
        private List<ServiceRole> serviceRoles;
        private String serviceDefinitionLink;

        public List<String> getServiceRoleNames () {
            return serviceRoleNames;
        }
    }

    private class ServiceRole {
        String membershipType;
        String resource;
        String name;
    }

    public List<ServiceRoles> getServiceRoles () {
        return serviceRoles;
    }
}
