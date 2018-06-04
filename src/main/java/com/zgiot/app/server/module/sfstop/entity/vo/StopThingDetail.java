package com.zgiot.app.server.module.sfstop.entity.vo;

import java.util.List;

/**
 * 停车设备详情
 */
public class StopThingDetail {

    private String thingCode;

    private String thingName;

    private List<ThingCondition> thingConditionList;

    private List<PararmeterCondition> pararmeterConditionList;
    /**
     * 是否人工干预
     */
    private String isManualIntervention;
    /**
     * 人工干预是否接触
     */
    private String isRelieve;


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

    public List<ThingCondition> getThingConditionList() {
        return thingConditionList;
    }

    public void setThingConditionList(List<ThingCondition> thingConditionList) {
        this.thingConditionList = thingConditionList;
    }

    public List<PararmeterCondition> getPararmeterConditionList() {
        return pararmeterConditionList;
    }

    public void setPararmeterConditionList(List<PararmeterCondition> pararmeterConditionList) {
        this.pararmeterConditionList = pararmeterConditionList;
    }

    public String getIsManualIntervention() {
        return isManualIntervention;
    }

    public void setIsManualIntervention(String isManualIntervention) {
        this.isManualIntervention = isManualIntervention;
    }

    public String getIsRelieve() {
        return isRelieve;
    }

    public void setIsRelieve(String isRelieve) {
        this.isRelieve = isRelieve;
    }

    public class ThingCondition {

        private String thingCode;

        private String thingName;

        private String runState;

        private String alertInfo;

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

        public String getRunState() {
            return runState;
        }

        public void setRunState(String runState) {
            this.runState = runState;
        }

        public String getAlertInfo() {
            return alertInfo;
        }

        public void setAlertInfo(String alertInfo) {
            this.alertInfo = alertInfo;
        }
    }

    public class PararmeterCondition {

        private String thingCode;

        private String thingName;

        private String pararmeter;

        private String isUnusual;

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

        public String getPararmeter() {
            return pararmeter;
        }

        public void setPararmeter(String pararmeter) {
            this.pararmeter = pararmeter;
        }

        public String getIsUnusual() {
            return isUnusual;
        }

        public void setIsUnusual(String isUnusual) {
            this.isUnusual = isUnusual;
        }
    }
}
