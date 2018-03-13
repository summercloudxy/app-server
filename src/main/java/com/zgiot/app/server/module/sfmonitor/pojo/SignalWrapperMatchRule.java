package com.zgiot.app.server.module.sfmonitor.pojo;

public class SignalWrapperMatchRule {
    private int id;
    private String signalWrapperName;
    private String zoneCode;
    private boolean isAllMatch;

    public int getId() {
        return id;
    }

    public String getSignalWrapperName() {
        return signalWrapperName;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public boolean isAllMatch() {
        return isAllMatch;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSignalWrapperName(String signalWrapperName) {
        this.signalWrapperName = signalWrapperName;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public void setAllMatch(boolean allMatch) {
        isAllMatch = allMatch;
    }
}
