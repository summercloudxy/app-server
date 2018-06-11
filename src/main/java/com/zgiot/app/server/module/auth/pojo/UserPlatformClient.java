package com.zgiot.app.server.module.auth.pojo;

public class UserPlatformClient {
    private Integer id;
    private long userId;
    private int platformClientId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
