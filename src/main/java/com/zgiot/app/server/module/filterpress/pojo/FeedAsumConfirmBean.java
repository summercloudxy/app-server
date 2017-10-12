package com.zgiot.app.server.module.filterpress.pojo;

public class FeedAsumConfirmBean {
    private String deviceCode;
    private Long feedOverDuration;
    private Float feedOverCurrent;

    public String getDeviceCode() {
        return deviceCode;
    }

    public Long getFeedOverDuration() {
        return feedOverDuration;
    }

    public Float getFeedOverCurrent() {
        return feedOverCurrent;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public void setFeedOverDuration(Long feedOverDuration) {
        this.feedOverDuration = feedOverDuration;
    }

    public void setFeedOverCurrent(Float feedOverCurrent) {
        this.feedOverCurrent = feedOverCurrent;
    }
}
