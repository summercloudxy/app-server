package com.zgiot.app.server.module.historydata;

import java.util.Date;
import java.util.List;

/**
 * @author wangwei
 */
public class HistoryDataDto {

    private Date startTime;

    private Date endTime;

    private List<String> thingCodes;

    private Integer segment;

    private boolean isTimeCorrection;

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

    public List<String> getThingCodes() {
        return thingCodes;
    }

    public void setThingCodes(List<String> thingCodes) {
        this.thingCodes = thingCodes;
    }

    public Integer getSegment() {
        return segment;
    }

    public void setSegment(Integer segment) {
        this.segment = segment;
    }

    public boolean isTimeCorrection() {
        return isTimeCorrection;
    }

    public void setTimeCorrection(boolean timeCorrection) {
        isTimeCorrection = timeCorrection;
    }
}
