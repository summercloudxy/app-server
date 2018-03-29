package com.zgiot.app.server.module.equipments.controller;

public class ChuteInfo {

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
     *溜槽名称
     */
    private String  chuteName;

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

    public String getChuteName() {
        return chuteName;
    }

    public void setChuteName(String chuteName) {
        this.chuteName = chuteName;
    }
}
