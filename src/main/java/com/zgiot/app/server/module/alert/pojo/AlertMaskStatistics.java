package com.zgiot.app.server.module.alert.pojo;

import com.zgiot.common.pojo.ThingModel;

public class AlertMaskStatistics {
    private String thingCode;
    private ThingModel thingModel;
    private Integer systemId;
    private String systemName;
    private String metricCode;
    private String alertInfo;
    private int alertType;
    private Integer count;

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public ThingModel getThingModel() {
        return thingModel;
    }

    public void setThingModel(ThingModel thingModel) {
        this.thingModel = thingModel;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public int getAlertType() {
        return alertType;
    }

    public void setAlertType(int alertType) {
        this.alertType = alertType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getAlertInfo() {
        return alertInfo;
    }

    public void setAlertInfo(String alertInfo) {
        this.alertInfo = alertInfo;
    }
}
