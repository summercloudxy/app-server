package com.zgiot.app.server.module.sfmonitor.pojo;


public class SFMonEquipMonitorConfig {
    private int id;
    private String thingCode;
    private String metricTagName;
    private String key;
    private String value;
    private Boolean selected;
    private int model;

    public int getId() {
        return id;
    }

    public String getThingCode() {
        return thingCode;
    }

    public String getMetricTagName() {
        return metricTagName;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Boolean isSelected() {
        return selected;
    }

    public int getModel() {
        return model;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public void setMetricTagName(String metricTagName) {
        this.metricTagName = metricTagName;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public void setModel(int model) {
        this.model = model;
    }
}
