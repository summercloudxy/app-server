package com.zgiot.app.server.service.pojo;

public class RelSendTraceInfo {
    private Integer id;
    private String sendThingCode;
    private String sendMetricCode;
    private String sendValue;
    private Integer sendType;
    private String influenceThingCode;
    private String influenceMetricCode;
    private String expectedValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getSendValue() {
        return sendValue;
    }

    public void setSendValue(String sendValue) {
        this.sendValue = sendValue;
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

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }
}
