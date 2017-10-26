package com.zgiot.app.server.module.historydata;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 * @author wangwei
 */
public class HistoryDataDto {

    private Date startTime;

    private Date endTime;

    private String thingCodes;

    private Integer segment;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getThingCodes() {
        return thingCodes;
    }

    public void setThingCodes(String thingCodes) {
        this.thingCodes = thingCodes;
    }

    public Integer getSegment() {
        return segment;
    }

    public void setSegment(Integer segment) {
        this.segment = segment;
    }
}
