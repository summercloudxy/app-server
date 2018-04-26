package com.zgiot.app.server.module.equipments.controller;

import java.util.Date;

public class PipeInfo {
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
    private Date updateDate;

    /**
     *起始设备CODE
     */
    private String startThingCode;

    /**
     *起始设备名称
     */
    private String startThingName;

    /**
     *终止设备CODE
     */
    private String terminalThingCode;

    /**
     *终止设备名称
     */
    private String terminalThingName;

    /**
     *投用日期
     */
    private String enableDate;

    /**
     *停用日期
     */
    private String disableDate;

    /**
     *所属系统
     */
    private Long thingSystemId;

    /**
     * 所属系统名称
     */
    private String thingSystemName;

    /**
     *管道名称
     */
    private String  thingName;

    /**
     *管道通径
     */
    private String nominalDiameter;

    /**
     *生产厂家
     */
    private String manufacturer;

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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getStartThingCode() {
        return startThingCode;
    }

    public void setStartThingCode(String startThingCode) {
        this.startThingCode = startThingCode;
    }

    public String getStartThingName() {
        return startThingName;
    }

    public void setStartThingName(String startThingName) {
        this.startThingName = startThingName;
    }

    public String getTerminalThingCode() {
        return terminalThingCode;
    }

    public void setTerminalThingCode(String terminalThingCode) {
        this.terminalThingCode = terminalThingCode;
    }

    public String getTerminalThingName() {
        return terminalThingName;
    }

    public void setTerminalThingName(String terminalThingName) {
        this.terminalThingName = terminalThingName;
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

    public Long getThingSystemId() {
        return thingSystemId;
    }

    public void setThingSystemId(Long thingSystemId) {
        this.thingSystemId = thingSystemId;
    }

    public String getThingSystemName() {
        return thingSystemName;
    }

    public void setThingSystemName(String thingSystemName) {
        this.thingSystemName = thingSystemName;
    }

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public String getNominalDiameter() {
        return nominalDiameter;
    }

    public void setNominalDiameter(String nominalDiameter) {
        this.nominalDiameter = nominalDiameter;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getThingTagCode() {
        return thingTagCode;
    }

    public void setThingTagCode(String thingTagCode) {
        this.thingTagCode = thingTagCode;
    }
}
