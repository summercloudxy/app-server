package com.zgiot.app.server.module.sfmonitor.pojo;

import java.util.Date;

public class RelSFSysMonitorThingMetric {
    private int id;
    private String thingCode;
    private String thingTagCode;
    private String thingName;
    private String metricCode;
    private String metricName;
    private Short isShowMetric;
    private Short isJoinSystem;
    private String metricValue;
    private Date createDate;
    private Date updateDate;

    public String getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(String metricValue) {
        this.metricValue = metricValue;
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

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public Short getIsShowMetric() {
        return isShowMetric;
    }

    public void setIsShowMetric(Short isShowMetric) {
        this.isShowMetric = isShowMetric;
    }

    public Short getIsJoinSystem() {
        return isJoinSystem;
    }

    public void setIsJoinSystem(Short isJoinSystem) {
        this.isJoinSystem = isJoinSystem;
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
