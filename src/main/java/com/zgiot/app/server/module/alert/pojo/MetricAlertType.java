package com.zgiot.app.server.module.alert.pojo;

/**
 * Created by xiayun on 2017/9/27.
 */
public class MetricAlertType {
    private String metricCode;
    private Short alertType;

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public Short getAlertType() {
        return alertType;
    }

    public void setAlertType(Short alertType) {
        this.alertType = alertType;
    }
}
