package com.zgiot.app.server.module.sfmonitor.pojo;

import org.springframework.boot.actuate.metrics.Metric;

import java.util.Date;
import java.util.List;

public class RelSFSysMonitorTermThing {
    private int id;
    private String thingCode;
    private String thingTagCode;
    private Short sort;
    private Short termId;
    private Date createDate;
    private Date updateDate;
    private Short showType;
    private List<RelSFSysMonitorThingMetric> thingMetricList;//主设备信号数据
    private List<RelSfSpeMonThingSubthing> speMonThingSubthing;//附属设备数据
    private Integer selectCount;//查询条数
    private String metricCode;//信号code（查询条件）

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public Integer getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(Integer selectCount) {
        this.selectCount = selectCount;
    }

    public List<RelSfSpeMonThingSubthing> getSpeMonThingSubthing() {
        return speMonThingSubthing;
    }

    public void setSpeMonThingSubthing(List<RelSfSpeMonThingSubthing> speMonThingSubthing) {
        this.speMonThingSubthing = speMonThingSubthing;
    }

    public Short getShowType() {
        return showType;
    }

    public void setShowType(Short showType) {
        this.showType = showType;
    }

    public List<RelSFSysMonitorThingMetric> getThingMetricList() {
        return thingMetricList;
    }

    public void setThingMetricList(List<RelSFSysMonitorThingMetric> thingMetricList) {
        this.thingMetricList = thingMetricList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getThingTagCode() {
        return thingTagCode;
    }

    public void setThingTagCode(String thingTagCode) {
        this.thingTagCode = thingTagCode;
    }

    public Short getSort() {
        return sort;
    }

    public void setSort(Short sort) {
        this.sort = sort;
    }

    public Short getTermId() {
        return termId;
    }

    public void setTermId(Short termId) {
        this.termId = termId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
