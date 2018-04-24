package com.zgiot.app.server.module.sfmonitor.controller;

public class EquipmentMonFindReq {
    private String configProgress;
    private String thingCode;
    private int pageNum;
    private int pageSize;

    public String getConfigProgress() {
        return configProgress;
    }

    public String getThingCode() {
        return thingCode;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setConfigProgress(String configProgress) {
        this.configProgress = configProgress;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
