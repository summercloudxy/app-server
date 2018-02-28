package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.metrictag.pojo.MetricTag;

public class SignalWrapperInfo {
    private MetricTag metricTag;
    private String zone;

    public MetricTag getMetricTag() {
        return metricTag;
    }

    public String getZone() {
        return zone;
    }

    public void setMetricTag(MetricTag metricTag) {
        this.metricTag = metricTag;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
