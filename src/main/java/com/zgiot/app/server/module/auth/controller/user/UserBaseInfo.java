package com.zgiot.app.server.module.auth.controller.user;

public class UserBaseInfo {
    private long userId;
    private String uuid;
    private String userName;

    public long getUserId() {
        return userId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
