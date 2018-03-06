package com.zgiot.app.server.module.sfmonitor.pojo;

public class SFMonEquipMonitorConfig {
    private int id;
    private String thingCode;
    private String metricTagCode;
    private String key;
    private String value;
    private boolean model;

    public int getId() {
        return id;
    }

    public String getThingCode() {
        return thingCode;
    }

    public String getMetricTagCode() {
        return metricTagCode;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean isModel() {
        return model;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public void setMetricTagCode(String metricTagCode) {
        this.metricTagCode = metricTagCode;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setModel(boolean model) {
        this.model = model;
    }
}
