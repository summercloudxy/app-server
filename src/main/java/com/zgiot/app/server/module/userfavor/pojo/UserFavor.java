package com.zgiot.app.server.module.userfavor.pojo;

public class UserFavor {
    private Integer id;
    private String userUuid;
    private Long moduleId;
    private float sort;
    private Long clientId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public long getModuleId() {
        return moduleId;
    }

    public float getSort() {
        return sort;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public void setSort(float sort) {
        this.sort = sort;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
