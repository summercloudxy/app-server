package com.zgiot.app.server.module.equipments.controller;

import java.util.Date;

public class DeviceInfo {

    private String thingCode;// 设备编号
    private String thingName;// 设备名称
    private String thingShortname;// 设备简称
    private String specification;// 规格型号
    private String tagName;// 设备类型-3级类型名称
    private String manufacturer;// 生产厂家
    private String thingSystemId;// 所属系统id
    private String enableDate;// 投用日期
    private String disableDate;// 停用日期
    private String powerProperties;// 动力属性
    private String locationArea;// 所处位置
    private String locationX;// 位置x坐标
    private String locationY;// 位置y坐标
    private String buildingId;// 车间id
    private String floor;// 楼层
    private String angle;// 角度
    private String granularity;// 给料粒度
    private String imageName;// 图片名

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

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getThingSystemId() {
        return thingSystemId;
    }

    public void setThingSystemId(String thingSystemId) {
        this.thingSystemId = thingSystemId;
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

    public String getLocationArea() {
        return locationArea;
    }

    public void setLocationArea(String locationArea) {
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

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
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
}
