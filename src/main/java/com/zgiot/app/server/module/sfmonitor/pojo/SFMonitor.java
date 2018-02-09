package com.zgiot.app.server.module.sfmonitor.pojo;

public class SFMonitor {
    private Long id;
    private String sfMonName;
    private Long userId;
    private Float sort;
    private String userName;

    public Long getId() {
        return id;
    }

    public String getSfMonName() {
        return sfMonName;
    }

    public Long getUserId() {
        return userId;
    }

    public Float getSort() {
        return sort;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSfMonName(String sfMonName) {
        this.sfMonName = sfMonName;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setSort(Float sort) {
        this.sort = sort;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
