package com.zgiot.app.server.service.pojo;

public class HistdataWhitelistModel {
    private String thingCode;
    private String metricCode;
    private int toStore;

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

    public int getToStore() {
        return toStore;
    }

    public void setToStore(int toStore) {
        this.toStore = toStore;
    }
}
