package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.sfmonitor.pojo.SignalWrapperMetric;

import java.util.List;

public class EquipmentRelateToSignalWrapper {
    private String warpperName;
    private List<SignalWrapperMetric> signalWrapperMetrics;
    private boolean selected;
    private String zone;
    private String thingCode;

    public String getWarpperName() {
        return warpperName;
    }

    public List<SignalWrapperMetric> getSignalWrapperMetrics() {
        return signalWrapperMetrics;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getZone() {
        return zone;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setWarpperName(String warpperName) {
        this.warpperName = warpperName;
    }

    public void setSignalWrapperMetrics(List<SignalWrapperMetric> signalWrapperMetrics) {
        this.signalWrapperMetrics = signalWrapperMetrics;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }
}
