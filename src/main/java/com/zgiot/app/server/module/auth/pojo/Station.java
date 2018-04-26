package com.zgiot.app.server.module.auth.pojo;


import java.util.Date;

public class Station {
    private int id;
    private String name;
    private String code;
    private String remark;
    private int stationSort;
    private String stationType;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }

    public int getStationSort() {
        return stationSort;
    }

    public String getStationType() {
        return stationType;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setStationSort(int stationSort) {
        this.stationSort = stationSort;
    }

    public void setStationType(String stationType) {
        this.stationType = stationType;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
