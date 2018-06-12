package com.zgiot.app.server.module.auth.pojo;

public class UserDepartmentStation {
    private Integer id;
    private int departmentId;
    private int stationId;
    private int workshopPostId;
    private long userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getWorkshopPostId() {
        return workshopPostId;
    }

    public void setWorkshopPostId(int workshopPostId) {
        this.workshopPostId = workshopPostId;
    }
}
