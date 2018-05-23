package com.zgiot.app.server.module.auth.pojo;

public class RelRoleAuthorityGroup {
    private int roleId;
    private int authorityGroupId;
    private boolean enable;

    public int getRoleId() {
        return roleId;
    }

    public int getAuthorityGroupId() {
        return authorityGroupId;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setAuthorityGroupId(int authorityGroupId) {
        this.authorityGroupId = authorityGroupId;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
