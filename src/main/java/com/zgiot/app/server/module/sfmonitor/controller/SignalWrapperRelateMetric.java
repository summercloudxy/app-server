package com.zgiot.app.server.module.sfmonitor.controller;

import java.util.List;

public class SignalWrapperRelateMetric {
    private int id;
    private String wrapperName;
    private List<String> metricNames;
    private String comment;
    private String userName;

    public int getId() {
        return id;
    }

    public String getWrapperName() {
        return wrapperName;
    }

    public List<String> getMetricNames() {
        return metricNames;
    }

    public String getComment() {
        return comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWrapperName(String wrapperName) {
        this.wrapperName = wrapperName;
    }

    public void setMetricNames(List<String> metricNames) {
        this.metricNames = metricNames;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
