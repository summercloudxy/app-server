package com.zgiot.app.server.module.alert.pojo;

import com.zgiot.common.pojo.ThingModel;

public class AlertMaskStatistics {
    private String thingCode;
    private ThingModel thingModel;
    private Integer systemId;
    private String metricCode;
    private int alertType;
    private Integer count;
//    private List<AlertMask> alertMasks;

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

//    public List<AlertMask> getAlertMasks() {
//        return alertMasks;
//    }
//
//    public void setAlertMasks(List<AlertMask> alertMasks) {
//        this.alertMasks = alertMasks;
//    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
