package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.sfmonitor.pojo.RelSFSysMonitorTermThing;
import com.zgiot.app.server.module.sfmonitor.pojo.RelSfSpeMonThingMetric;
import com.zgiot.app.server.module.sfmonitor.pojo.RelSfSpeMonThingSubthing;
import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;

import java.util.List;

public class SFSpeMonitorReq {
    private Short termCount;
    private String thingtagId1;//一级系统id
    private String thingtagId2;//二级系统id
    private String thingCode;//设备名称
    private String metricCode;//设备名称
    private Integer pageNum;

    public Short getTermCount() {
        return termCount;
    }

    public void setTermCount(Short termCount) {
        this.termCount = termCount;
    }

    public String getThingtagId1() {
        return thingtagId1;
    }

    public void setThingtagId1(String thingtagId1) {
        this.thingtagId1 = thingtagId1;
    }

    public String getThingtagId2() {
        return thingtagId2;
    }

    public void setThingtagId2(String thingtagId2) {
        this.thingtagId2 = thingtagId2;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public Integer getPageNum() {
        return pageNum == null ?1:pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

}