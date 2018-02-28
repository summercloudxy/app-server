package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.metrictag.pojo.MetricTag;

public class SignalWrapperReq {
    private MetricTag metricTag;
    private int zoneId;
    private int userId;

    public MetricTag getMetricTag() {
        return metricTag;
    }

    public int getZoneId() {
        return zoneId;
    }

    public int getUserId() {
        return userId;
    }

    public void setMetricTag(MetricTag metricTag) {
        this.metricTag = metricTag;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
