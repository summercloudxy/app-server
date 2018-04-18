package com.zgiot.app.server.module.auth.controller.department;

public class DeleteDepartmentResponse {
    private int userCount;
    private int subDepCount;

    public int getUserCount() {
        return userCount;
    }

    public int getSubDepCount() {
        return subDepCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public void setSubDepCount(int subDepCount) {
        this.subDepCount = subDepCount;
    }
}
