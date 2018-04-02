package com.zgiot.app.server.module.equipments.controller;

public class ValveInfo {

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
     *阀门名称
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
     *阀门类型
     */
    private String valveType;

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
    private String valveExecutionMode;

    /**
     *控制方式
     */
    private String valveControlMode;

    /**
     *类型简称
     */
    private String thingType;

    /**
     *阀门用途
     */
    private  String valvePurpose;

    /**
     *通径
     */
    private String nominalDiameter;

    /**
     * 规格型号
     */
    private String specification;

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

    public String getValveType() {
        return valveType;
    }

    public void setValveType(String valveType) {
        this.valveType = valveType;
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

    public String getValveExecutionMode() {
        return valveExecutionMode;
    }

    public void setValveExecutionMode(String valveExecutionMode) {
        this.valveExecutionMode = valveExecutionMode;
    }

    public String getValveControlMode() {
        return valveControlMode;
    }

    public void setValveControlMode(String valveControlMode) {
        this.valveControlMode = valveControlMode;
    }

    public String getThingType() {
        return thingType;
    }

    public void setThingType(String thingType) {
        this.thingType = thingType;
    }

    public String getValvePurpose() {
        return valvePurpose;
    }

    public void setValvePurpose(String valvePurpose) {
        this.valvePurpose = valvePurpose;
    }

    public String getNominalDiameter() {
        return nominalDiameter;
    }

    public void setNominalDiameter(String nominalDiameter) {
        this.nominalDiameter = nominalDiameter;
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
}
