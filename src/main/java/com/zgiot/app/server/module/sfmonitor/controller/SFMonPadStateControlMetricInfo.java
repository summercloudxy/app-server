package com.zgiot.app.server.module.sfmonitor.controller;

public class SFMonPadStateControlMetricInfo {
    private String metricName;
    private String metricCode;
    private String value;
    private int model;//(0:查看，1：操作，2：不显示查看操作)
    private int sort;//信号在信号包中的顺序

    public String getMetricName() {
        return metricName;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public String getValue() {
        return value;
    }

    public int getModel() {
        return model;
    }

    public int getSort() {
        return sort;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
