package com.zgiot.app.server.module.sfmonitor.controller;

public class FindSignalWrapperReq {
    private String wrapperName;
    private String metricName;
    private String combineName;

    public String getWrapperName() {
        return wrapperName;
    }

    public String getMetricName() {
        return metricName;
    }

    public String getCombineName() {
        return combineName;
    }

    public void setWrapperName(String wrapperName) {
        this.wrapperName = wrapperName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public void setCombineName(String combineName) {
        this.combineName = combineName;
    }
}
