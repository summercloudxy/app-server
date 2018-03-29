package com.zgiot.app.server.module.equipments.controller;

public class PipeInfo {

    private String thingCode;
    private String thingName;
    private String startThingCode;
    private String terminalThingName;
    private String systemName;
    private Long systemId;
    private String updateTime;

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

    public String getStartThingCode() {
        return startThingCode;
    }

    public void setStartThingCode(String startThingCode) {
        this.startThingCode = startThingCode;
    }

    public String getTerminalThingName() {
        return terminalThingName;
    }

    public void setTerminalThingName(String terminalThingName) {
        this.terminalThingName = terminalThingName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
