package com.zgiot.app.server.service.pojo;

import java.util.Date;

public class SendTraceLog {
    private Integer id;
    private String userName;
    private String userId;
    private Integer platform;
    private Date sendTime;
    private Integer sendType;
    private String sendThingCode;
    private String sendThingName;
    private String sendMetricCode;
    private String sendMetricName;
    private String preValue;
    private String preValueShow;
    private String currentValue;
    private String currentValueShow;
    private String influenceThingCode;
    private String influenceMetricCode;

    public SendTraceLog() {
    }

    public SendTraceLog(SendInfo sendInfo) {
        this.userName = sendInfo.getUserName();
        this.userId = sendInfo.getUserId();
        this.platform = sendInfo.getPlatform();
        this.sendTime = sendInfo.getSendTime();
        this.sendThingCode = sendInfo.getThingCode();
        this.sendMetricCode = sendInfo.getMetricCode();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public String getSendThingCode() {
        return sendThingCode;
    }

    public void setSendThingCode(String sendThingCode) {
        this.sendThingCode = sendThingCode;
    }

    public String getSendMetricCode() {
        return sendMetricCode;
    }

    public void setSendMetricCode(String sendMetricCode) {
        this.sendMetricCode = sendMetricCode;
    }

    public String getInfluenceThingCode() {
        return influenceThingCode;
    }

    public void setInfluenceThingCode(String influenceThingCode) {
        this.influenceThingCode = influenceThingCode;
    }

    public String getInfluenceMetricCode() {
        return influenceMetricCode;
    }

    public void setInfluenceMetricCode(String influenceMetricCode) {
        this.influenceMetricCode = influenceMetricCode;
    }

    public String getPreValue() {
        return preValue;
    }

    public void setPreValue(String preValue) {
        this.preValue = preValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getPreValueShow() {
        return preValueShow;
    }

    public void setPreValueShow(String preValueShow) {
        this.preValueShow = preValueShow;
    }

    public String getCurrentValueShow() {
        return currentValueShow;
    }

    public void setCurrentValueShow(String currentValueShow) {
        this.currentValueShow = currentValueShow;
    }

    public String getSendThingName() {
        return sendThingName;
    }

    public void setSendThingName(String sendThingName) {
        this.sendThingName = sendThingName;
    }

    public String getSendMetricName() {
        return sendMetricName;
    }

    public void setSendMetricName(String sendMetricName) {
        this.sendMetricName = sendMetricName;
    }
}
