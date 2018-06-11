package com.zgiot.app.server.service.pojo;

public class HistdataWhitelistModel {
    private Integer id;
    private String thingCode;
    private String metricCode;
    private int toStore;
    private int minuteAvg;
    private int minuteAccu;
    private int hourAvg;
    private int hourAccu;
    private int dayAvg;
    private int dayAccu;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getToStore() {
        return toStore;
    }

    public void setToStore(int toStore) {
        this.toStore = toStore;
    }

    public int getMinuteAvg() {
        return minuteAvg;
    }

    public void setMinuteAvg(int minuteAvg) {
        this.minuteAvg = minuteAvg;
    }

    public int getMinuteAccu() {
        return minuteAccu;
    }

    public void setMinuteAccu(int minuteAccu) {
        this.minuteAccu = minuteAccu;
    }

    public int getHourAvg() {
        return hourAvg;
    }

    public void setHourAvg(int hourAvg) {
        this.hourAvg = hourAvg;
    }

    public int getHourAccu() {
        return hourAccu;
    }

    public void setHourAccu(int hourAccu) {
        this.hourAccu = hourAccu;
    }

    public int getDayAvg() {
        return dayAvg;
    }

    public void setDayAvg(int dayAvg) {
        this.dayAvg = dayAvg;
    }

    public int getDayAccu() {
        return dayAccu;
    }

    public void setDayAccu(int dayAccu) {
        this.dayAccu = dayAccu;
    }
}
