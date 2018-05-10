package com.zgiot.app.server.module.sfmonitor.pojo;

import java.util.Date;

public class RelSfSpeMonThingMetric {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private String thingTagCode;

    /**
     *
     */
    private String thingCode;

    /**
     *
     */
    private String metricCode;

    /**
     *
     */
    private String metricName;

    /**
     *
     */
    private String metricCode2;

    /**
     *
     */
    private String metricName2;

    /**
     *
     */
    private Date createDate;

    /**
     *
     */
    private Date updateDate;

    private String metricValue;

    private String metricValue2;

    public String getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(String metricValue) {
        this.metricValue = metricValue;
    }

    public String getMetricValue2() {
        return metricValue2;
    }

    public void setMetricValue2(String metricValue2) {
        this.metricValue2 = metricValue2;
    }

    /**
     *
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     */
    public String getThingTagCode() {
        return thingTagCode;
    }

    /**
     *
     */
    public void setThingTagCode(String thingTagCode) {
        this.thingTagCode = thingTagCode == null ? null : thingTagCode.trim();
    }

    /**
     *
     */
    public String getThingCode() {
        return thingCode;
    }

    /**
     *
     */
    public void setThingCode(String thingCode) {
        this.thingCode = thingCode == null ? null : thingCode.trim();
    }

    /**
     *
     */
    public String getMetricCode() {
        return metricCode;
    }

    /**
     *
     */
    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode == null ? null : metricCode.trim();
    }

    /**
     *
     */
    public String getMetricName() {
        return metricName;
    }

    /**
     *
     */
    public void setMetricName(String metricName) {
        this.metricName = metricName == null ? null : metricName.trim();
    }

    /**
     *
     */
    public String getMetricCode2() {
        return metricCode2;
    }

    /**
     *
     */
    public void setMetricCode2(String metricCode2) {
        this.metricCode2 = metricCode2 == null ? null : metricCode2.trim();
    }

    /**
     *
     */
    public String getMetricName2() {
        return metricName2;
    }

    /**
     *
     */
    public void setMetricName2(String metricName2) {
        this.metricName2 = metricName2 == null ? null : metricName2.trim();
    }

    /**
     *
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     *
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     *
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     *
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}