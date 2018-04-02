package com.zgiot.app.server.module.equipments.controller;

public class FlashboardInfo {

    /**
     * 主键
     */
    private Long id;

    /**
     *编号
     */
    private String thingCode;

    /**
     *更新时间
     */
    private String updateDate;

    /**
     *投用日期
     */
    private String enableDate;

    /**
     *停用日期
     */
    private String disableDate;

    /**
     *闸板名称
     */
    private String  thingName;


    /**
     *生产厂家
     */
    private String manufacturer;

    /**
     *归属主体类型
     */
    private String subjectType;

    /**
     *所属设备编号
     */
    private String parentThingCode;

    /**
     *所属设备名称
     */
    private String parentThingName;

    /**
     *闸板类型
     */
    private String flashboardType;

    /**
     *所属车间
     */
    private Long buildingId;

    /**
     *楼层
     */
    private Integer floor;

    /**
     *执行方式
     */
    private String flashboardExecutionMode;

    /**
     *控制方式
     */
    private String flashboardControlMode;

    /**
     *类型简称
     */
    private String thingType;

    /**
     *设备类型
     */
    private String thingTagCode;

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

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
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

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getParentThingCode() {
        return parentThingCode;
    }

    public void setParentThingCode(String parentThingCode) {
        this.parentThingCode = parentThingCode;
    }

    public String getParentThingName() {
        return parentThingName;
    }

    public void setParentThingName(String parentThingName) {
        this.parentThingName = parentThingName;
    }

    public String getFlashboardType() {
        return flashboardType;
    }

    public void setFlashboardType(String flashboardType) {
        this.flashboardType = flashboardType;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getFlashboardExecutionMode() {
        return flashboardExecutionMode;
    }

    public void setFlashboardExecutionMode(String flashboardExecutionMode) {
        this.flashboardExecutionMode = flashboardExecutionMode;
    }

    public String getFlashboardControlMode() {
        return flashboardControlMode;
    }

    public void setFlashboardControlMode(String flashboardControlMode) {
        this.flashboardControlMode = flashboardControlMode;
    }

    public String getThingType() {
        return thingType;
    }

    public void setThingType(String thingType) {
        this.thingType = thingType;
    }

    public String getThingTagCode() {
        return thingTagCode;
    }

    public void setThingTagCode(String thingTagCode) {
        this.thingTagCode = thingTagCode;
    }
}
