package com.zgiot.app.server.module.sfmonitor.controller;

public class SFMonPadJumpZoneEquipmentInfo {
    private String thingName;
    private String thingCode;
    //设备对应状态值
    private String state;

    public String getThingName() {
        return thingName;
    }

    public String getThingCode() {
        return thingCode;
    }

    public String getState() {
        return state;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public void setState(String state) {
        this.state = state;
    }
}
