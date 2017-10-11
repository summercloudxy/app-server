package com.zgiot.app.server.module.filterpress.pojo;

/**
 * Created by xiayun on 2017/9/13.
 */
public class FilterPressElectricity {
    private String deviceCode;

    private Double maxFeedCurrent;

    private Double minFeedCurrent;

    private Double averageFeedCurrent;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public Double getMaxFeedCurrent() {
        return maxFeedCurrent;
    }

    public void setMaxFeedCurrent(Double maxFeedCurrent) {
        this.maxFeedCurrent = maxFeedCurrent;
    }

    public Double getMinFeedCurrent() {
        return minFeedCurrent;
    }

    public void setMinFeedCurrent(Double minFeedCurrent) {
        this.minFeedCurrent = minFeedCurrent;
    }

    public Double getAverageFeedCurrent() {
        return averageFeedCurrent;
    }

    public void setAverageFeedCurrent(Double averageFeedCurrent) {
        this.averageFeedCurrent = averageFeedCurrent;
    }
}
