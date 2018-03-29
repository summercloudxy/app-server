package com.zgiot.app.server.module.equipments.controller;

public class ValveInfo {

    private String thingCode;
    private String thingName;
    private String parentThingCode;
    private String parentThingName;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
