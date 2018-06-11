package com.zgiot.app.server.module.sfstop.entity.vo;

import java.util.List;

public class StopLineDetailVO {

    private String bagId;

    private String bagName;

    private String isFault;

    private List<ThingAlertInfo> thingAlertInfoList;


    public String getBagId() {
        return bagId;
    }

    public void setBagId(String bagId) {
        this.bagId = bagId;
    }

    public String getBagName() {
        return bagName;
    }

    public void setBagName(String bagName) {
        this.bagName = bagName;
    }

    public String getIsFault() {
        return isFault;
    }

    public void setIsFault(String isFault) {
        this.isFault = isFault;
    }

    public List<ThingAlertInfo> getThingAlertInfoList() {
        return thingAlertInfoList;
    }

    public void setThingAlertInfoList(List<ThingAlertInfo> thingAlertInfoList) {
        this.thingAlertInfoList = thingAlertInfoList;
    }

    public class ThingAlertInfo {

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
}
