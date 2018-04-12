package com.zgiot.app.server.module.equipments.controller;

import java.util.Date;

public class DeviceInfo {

    private Long id;
    private String thingCode;// 设备编号
    private String thingName;// 设备名称
    private String thingShortname;// 设备简称
    private String specification;// 规格型号
    private String thingTagCode;// 设备类型-3级类型code
    private String thingTagName;// 设备类型-3级类型名称
    private String manufacturer;// 生产厂家
    private String systemId;// 所属系统id
    private String systemName;// 所属系统名称
    private String enableDate;// 投用日期
    private String disableDate;// 停用日期
    private String powerProperties;// 动力属性:1动力,0非动力
    private Integer locationArea;// 所处位置id
    private String locationX;// 位置x坐标
    private String locationY;// 位置y坐标
    private Integer buildingId;// 车间id
    private String buildingName;// 车间名称
    private Integer floor;// 楼层
    private Integer maxFloor;// 最大楼层
    private String angle;// 角度
    private String granularity;// 给料粒度
    private String imageName;// 图片名
    private Date updateDate;// 更新时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public String getThingShortname() {
        return thingShortname;
    }

    public void setThingShortname(String thingShortname) {
        this.thingShortname = thingShortname;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getThingTagCode() {
        return thingTagCode;
    }

    public void setThingTagCode(String thingTagCode) {
        this.thingTagCode = thingTagCode;
    }

    public String getThingTagName() {
        return thingTagName;
    }

    public void setThingTagName(String thingTagName) {
        this.thingTagName = thingTagName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(String enableDate) {
        this.enableDate = enableDate;
    }

    public String getDisableDate() {
        return disableDate;
    }

    public void setDisableDate(String disableDate) {
        this.disableDate = disableDate;
    }

    public String getPowerProperties() {
        return powerProperties;
    }

    public void setPowerProperties(String powerProperties) {
        this.powerProperties = powerProperties;
    }

    public Integer getLocationArea() {
        return locationArea;
    }

    public void setLocationArea(Integer locationArea) {
        this.locationArea = locationArea;
    }

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getMaxFloor() {
        return maxFloor;
    }

    public void setMaxFloor(Integer maxFloor) {
        this.maxFloor = maxFloor;
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public String getGranularity() {
        return granularity;
    }

    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
