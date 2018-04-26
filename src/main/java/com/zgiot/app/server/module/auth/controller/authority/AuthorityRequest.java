package com.zgiot.app.server.module.auth.controller.authority;

import java.util.List;

public class AuthorityRequest {
    private int roleId;
    private String roleName;
    private List<Integer> authorityList;

    public int getRoleId() {
        return roleId;
    }

    public List<Integer> getAuthorityList() {
        return authorityList;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setAuthorityList(List<Integer> authorityList) {
        this.authorityList = authorityList;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
