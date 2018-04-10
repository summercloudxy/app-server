package com.zgiot.app.server.module.equipments.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeterInfo {

    private Long id;

    /**
     *生产厂家
     */
    private String manufacturer;

    /**
     *归属主体类型
     */
    private String subjectType;

    /**
     *仪表名称
     */
    private String thingName;

    /**
     *设备编号
     */
    private String thingCode;

    /**
     *所属设备编号
     */
    private String parentThingCode;

    /**
     *所属设备名称
     */
    private String parentThingName;

    /**
     *投用日期
     */
    private String enableDate;

    /**
     *停用日期
     */
    private String disableDate;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 仪表类型
     */
    private String meterType;

    /**
     * 车间id
     */
    private Integer buildingId;

    /**
     * 车间名称
     */
    private String buildingName;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 最大楼层
     */
    private Integer maxFloor;

    /**
     * 仪表类型code
     */
    private String thingTagCode;

    /**
     * 仪表类型名称
     */
    private String thingTagName;

    /**
     * 仪表类型简称
     */
    private String thingType;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 参数集合
     */
    private List<ConfigInfo> configList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
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

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
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

    public String getThingType() {
        return thingType;
    }

    public void setThingType(String thingType) {
        this.thingType = thingType;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public List<ConfigInfo> getConfigList() {
        return configList;
    }

    public void setConfigList(List<ConfigInfo> configList) {
        this.configList = configList;
    }
}
