package com.zgiot.app.server.module.auth.pojo;

public class UserPlatformClient {
    private long userId;
    private int platformClientId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getPlatformClientId() {
        return platformClientId;
    }

    public void setPlatformClientId(int platformClientId) {
        this.platformClientId = platformClientId;
    }
}
