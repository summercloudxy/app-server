package com.zgiot.app.server.module.auth.controller.role;

import com.zgiot.app.server.module.auth.pojo.Role;

public class RoleReturn {
    private Role role;
    private int userCount;

    public Role getRole() {
        return role;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}
