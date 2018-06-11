package com.zgiot.app.server.module.sfstop.entity.vo;

/**
 * 人工干预设备
 */

public class StopManualInterventionVO {

    private String thingCode;

    private String thingName;

    private String isManualIntervention;

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

    public String getIsManualIntervention() {
        return isManualIntervention;
    }

    public void setIsManualIntervention(String isManualIntervention) {
        this.isManualIntervention = isManualIntervention;
    }
}

