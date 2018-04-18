package com.zgiot.app.server.module.auth.controller.role;

import com.zgiot.app.server.module.auth.pojo.RelRoleAuthorityGroup;
import com.zgiot.app.server.module.auth.pojo.Role;

import java.util.List;

public class RoleAndAuthorityGroup {
    private Role role;
    private List<RelRoleAuthorityGroup> relRoleAuthorityGroups;

    public Role getRole() {
        return role;
    }

    public List<RelRoleAuthorityGroup> getRelRoleAuthorityGroups() {
        return relRoleAuthorityGroups;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRelRoleAuthorityGroups(List<RelRoleAuthorityGroup> relRoleAuthorityGroups) {
        this.relRoleAuthorityGroups = relRoleAuthorityGroups;
    }
}
