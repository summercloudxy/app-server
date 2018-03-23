package com.zgiot.app.server.module.sfmonitor.controller;

public class EquipmentMonFindReq {
    private String configProgress;
    private String thingCode;

    public String getConfigProgress() {
        return configProgress;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setConfigProgress(String configProgress) {
        this.configProgress = configProgress;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }
}
