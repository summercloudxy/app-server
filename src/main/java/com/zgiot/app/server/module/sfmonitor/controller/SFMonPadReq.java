package com.zgiot.app.server.module.sfmonitor.controller;

import java.util.Date;

public class SFMonPadReq {
    private String thingCode;
    private long interval;//时间间隔，单位：s
    private Integer segment;

    public String getThingCode() {
        return thingCode;
    }

    public long getInterval() {
        return interval;
    }

    public Integer getSegment() {
        return segment;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setSegment(Integer segment) {
        this.segment = segment;
    }
}
