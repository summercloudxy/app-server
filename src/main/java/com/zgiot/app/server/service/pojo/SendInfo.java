package com.zgiot.app.server.service.pojo;

import com.zgiot.common.pojo.DataModel;

import java.util.Date;

public class SendInfo {
    private String userName;
    private String userId;
    private Integer platform;
    private Date sendTime;
    private String thingCode;
    private String metricCode;
    private String sendValue;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
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

    public String getSendValue() {
        return sendValue;
    }

    public void setSendValue(String sendValue) {
        this.sendValue = sendValue;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public SendInfo(DataModel dataModel) {
        this.thingCode = dataModel.getThingCode();
        this.metricCode = dataModel.getMetricCode();
        this.sendValue = dataModel.getValue();
    }

    public SendInfo() {

    }
}
