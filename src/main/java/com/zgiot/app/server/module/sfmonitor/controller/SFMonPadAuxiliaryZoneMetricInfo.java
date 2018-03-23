package com.zgiot.app.server.module.sfmonitor.controller;

public class SFMonPadAuxiliaryZoneMetricInfo {
    private String thingName;
    private String thingCode;
    private String metricName;
    private String value;
    private int model;
    //方向，只针对设备保护包，direction:1放在前面，设备保护第一个包，// direction:2放在后面，设备保护第二个包
    private int direction;
    private int sort;//信号点在信号包中的顺序
    private String metricCode;
    private String unit;

    public String getThingName() {
        return thingName;
    }

    public String getThingCode() {
        return thingCode;
    }

    public String getMetricName() {
        return metricName;
    }

    public String getValue() {
        return value;
    }

    public int getModel() {
        return model;
    }

    public int getDirection() {
        return direction;
    }

    public int getSort() {
        return sort;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
