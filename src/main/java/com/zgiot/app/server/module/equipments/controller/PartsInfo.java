package com.zgiot.app.server.module.equipments.controller;

public class PartsInfo {

    private Long id;
    private String parentThingCode;// 所属设备编号
    private String parentThingName;// 所属设备名称
    private String thingType;// 部件类型简称
    private String thingTypeName;// 部件类型名称
    private String thingName;// 部件名称
    private String specification;// 规格型号
    private String manufacturer;// 生产厂家
    private String enableDate;// 投用日期
    private String disableDate;// 停用日期
    private String startType;// 启动方式
    private String ratedPower;// 额定功率
    private String voltageLevel;// 电压等级
    private String explosionProof;// 是否防爆
    private String grade;// 级数
    private String insulationGrade;// 绝缘等级
    private String protectionGrade;// 防护等级
    private String ratedCurrent;// 额定电流
    private String updateTime;// 更新时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getThingType() {
        return thingType;
    }

    public void setThingType(String thingType) {
        this.thingType = thingType;
    }

    public String getThingTypeName() {
        return thingTypeName;
    }

    public void setThingTypeName(String thingTypeName) {
        this.thingTypeName = thingTypeName;
    }

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
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

    public String getStartType() {
        return startType;
    }

    public void setStartType(String startType) {
        this.startType = startType;
    }

    public String getRatedPower() {
        return ratedPower;
    }

    public void setRatedPower(String ratedPower) {
        this.ratedPower = ratedPower;
    }

    public String getVoltageLevel() {
        return voltageLevel;
    }

    public void setVoltageLevel(String voltageLevel) {
        this.voltageLevel = voltageLevel;
    }

    public String getExplosionProof() {
        return explosionProof;
    }

    public void setExplosionProof(String explosionProof) {
        this.explosionProof = explosionProof;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getInsulationGrade() {
        return insulationGrade;
    }

    public void setInsulationGrade(String insulationGrade) {
        this.insulationGrade = insulationGrade;
    }

    public String getProtectionGrade() {
        return protectionGrade;
    }

    public void setProtectionGrade(String protectionGrade) {
        this.protectionGrade = protectionGrade;
    }

    public String getRatedCurrent() {
        return ratedCurrent;
    }

    public void setRatedCurrent(String ratedCurrent) {
        this.ratedCurrent = ratedCurrent;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
