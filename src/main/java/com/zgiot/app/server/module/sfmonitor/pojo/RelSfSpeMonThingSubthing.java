package com.zgiot.app.server.module.sfmonitor.pojo;

import java.util.Date;

public class RelSfSpeMonThingSubthing {
    /**
     *主键id
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
     *附属设备code
     */
    private String subThingCode;

    /**
     *
     */
    private String subMetricCode;

    /**
     *
     */
    private String subMetricName;

    /**
     *
     */
    private Date createDate;

    /**
     *
     */
    private Date updateDate;

    private String subMetricValue;

    public String getSubMetricValue() {
        return subMetricValue;
    }

    public void setSubMetricValue(String subMetricValue) {
        this.subMetricValue = subMetricValue;
    }

    /**
     *主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     *主键id
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
     *附属设备code
     */
    public String getSubThingCode() {
        return subThingCode;
    }

    /**
     *附属设备code
     */
    public void setSubThingCode(String subThingCode) {
        this.subThingCode = subThingCode == null ? null : subThingCode.trim();
    }

    /**
     *
     */
    public String getSubMetricCode() {
        return subMetricCode;
    }

    /**
     *
     */
    public void setSubMetricCode(String subMetricCode) {
        this.subMetricCode = subMetricCode == null ? null : subMetricCode.trim();
    }

    /**
     *
     */
    public String getSubMetricName() {
        return subMetricName;
    }

    /**
     *
     */
    public void setSubMetricName(String subMetricName) {
        this.subMetricName = subMetricName == null ? null : subMetricName.trim();
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