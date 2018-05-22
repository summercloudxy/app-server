package com.zgiot.app.server.module.sfstart.pojo;


public class StartAreaInformation {

    // 区域名称
    private String areaName;

    // 区域等级
    private Integer level;

    // 所属大区
    private Integer regionId;

    // 区域类型
    private String areaType;

    // 大区编号
    private Integer number;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
