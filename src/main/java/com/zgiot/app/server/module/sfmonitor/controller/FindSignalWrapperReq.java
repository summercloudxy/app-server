package com.zgiot.app.server.module.sfmonitor.controller;

public class FindSignalWrapperReq {
    private String wrapperName;
    private String metricName;
    private String combineName;
    private int pageNum;
    private int pageSize;

    public String getWrapperName() {
        return wrapperName;
    }

    public String getMetricName() {
        return metricName;
    }

    public String getCombineName() {
        return combineName;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setWrapperName(String wrapperName) {
        this.wrapperName = wrapperName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public void setCombineName(String combineName) {
        this.combineName = combineName;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
