package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.metrictag.pojo.MetricTag;

public class SignalWrapperReq {
    private MetricTag metricTag;
    private int zoneId;
    private String userName;

    public MetricTag getMetricTag() {
        return metricTag;
    }

    public int getZoneId() {
        return zoneId;
    }

    public String getUserName() {
        return userName;
    }

    public void setMetricTag(MetricTag metricTag) {
        this.metricTag = metricTag;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
