package com.zgiot.app.server.module.sfmonitor.pojo;

import java.util.Date;

public class RelSFSysMonitorThingMetric {
    private int id;
    private String thingCode;
    private String thingTagCode;
    private String thingName;
    private String metricCode;
    private String metricName;
    private short isShowMetric;
    private short isJoinSystem;

    public String getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(String metricValue) {
        this.metricValue = metricValue;
    }

    private String metricValue;

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

    public short getIsShowMetric() {
        return isShowMetric;
    }

    public void setIsShowMetric(short isShowMetric) {
        this.isShowMetric = isShowMetric;
    }

    public short getIsJoinSystem() {
        return isJoinSystem;
    }

    public void setIsJoinSystem(short isJoinSystem) {
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

    private Date createDate;
    private Date updateDate;

}
