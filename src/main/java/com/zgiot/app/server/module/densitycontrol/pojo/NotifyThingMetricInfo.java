package com.zgiot.app.server.module.densitycontrol.pojo;

public class NotifyThingMetricInfo {
    private String notifyThingCode;
    private String densityThingCode;
    private String valveOpeningThingCode;
    private String levelThingCode;

    public String getNotifyThingCode() {
        return notifyThingCode;
    }

    public void setNotifyThingCode(String notifyThingCode) {
        this.notifyThingCode = notifyThingCode;
    }

    public String getDensityThingCode() {
        return densityThingCode;
    }

    public void setDensityThingCode(String densityThingCode) {
        this.densityThingCode = densityThingCode;
    }

    public String getValveOpeningThingCode() {
        return valveOpeningThingCode;
    }

    public void setValveOpeningThingCode(String valveOpeningThingCode) {
        this.valveOpeningThingCode = valveOpeningThingCode;
    }

    public String getLevelThingCode() {
        return levelThingCode;
    }

    public void setLevelThingCode(String levelThingCode) {
        this.levelThingCode = levelThingCode;
    }
}
