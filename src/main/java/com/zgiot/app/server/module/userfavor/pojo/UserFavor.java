package com.zgiot.app.server.module.userfavor.pojo;

public class UserFavor {
    private String userUuid;
    private long padModuleId;
    private float sort;

    public String getUserUuid() {
        return userUuid;
    }

    public long getPadModuleId() {
        return padModuleId;
    }

    public float getSort() {
        return sort;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public void setPadModuleId(long padModuleId) {
        this.padModuleId = padModuleId;
    }

    public void setSort(float sort) {
        this.sort = sort;
    }
}
