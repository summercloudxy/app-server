package com.zgiot.app.server.module.filterpress;

import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class FilterPressLogBean {
    private String thingCode;
    private Date feedStartTime;
    private long feedDuration;
    private float feedCurrent;
    private Date unloadTime;
    private long unloadDuration;
    private String feedState;
    private String unloadState;
    private int plateCount;
    private int totalPlateCount;
    private long waitDuration;
    private long proceedingDuration;
    private int team;
    private Date saveTime;
    private Date plateStartTime;
    private boolean isDayShift;
    private int period;

    public String getThingCode() {
        return thingCode;
    }

    public Date getFeedStartTime() {
        return feedStartTime;
    }

    public long getFeedDuration() {
        return feedDuration;
    }

    public float getFeedCurrent() {
        return feedCurrent;
    }

    public Date getUnloadTime() {
        return unloadTime;
    }

    public long getUnloadDuration() {
        return unloadDuration;
    }

    public String getFeedState() {
        return feedState;
    }

    public String getUnloadState() {
        return unloadState;
    }

    public int getPlateCount() {
        return plateCount;
    }

    public int getTotalPlateCount() {
        return totalPlateCount;
    }

    public long getWaitDuration() {
        return waitDuration;
    }

    public long getProceedingDuration() {
        return proceedingDuration;
    }

    public int getTeam() {
        return team;
    }

    public Date getSaveTime() {
        return saveTime;
    }

    public Date getPlateStartTime() {
        return plateStartTime;
    }

    public boolean isDayShift() {
        return isDayShift;
    }

    public int getPeriod() {
        return period;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public void setFeedStartTime(Date feedStartTime) {
        this.feedStartTime = feedStartTime;
    }

    public void setFeedDuration(long feedDuration) {
        this.feedDuration = feedDuration;
    }

    public void setFeedCurrent(float feedCurrent) {
        this.feedCurrent = feedCurrent;
    }

    public void setUnloadTime(Date unloadTime) {
        this.unloadTime = unloadTime;
    }

    public void setUnloadDuration(long unloadDuration) {
        this.unloadDuration = unloadDuration;
    }

    public void setFeedState(String feedState) {
        this.feedState = feedState;
    }

    public void setUnloadState(String unloadState) {
        this.unloadState = unloadState;
    }

    public void setPlateCount(int plateCount) {
        this.plateCount = plateCount;
    }

    public void setTotalPlateCount(int totalPlateCount) {
        this.totalPlateCount = totalPlateCount;
    }

    public void setWaitDuration(long waitDuration) {
        this.waitDuration = waitDuration;
    }

    public void setProceedingDuration(long proceedingDuration) {
        this.proceedingDuration = proceedingDuration;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    public void setPlateStartTime(Date plateStartTime) {
        this.plateStartTime = plateStartTime;
    }

    public void setDayShift(boolean dayShift) {
        isDayShift = dayShift;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public boolean isEmpty(){
        if(!StringUtils.isBlank(thingCode)
                && feedDuration > 0 && (feedStartTime != null)
                && unloadDuration > 0 && (unloadTime != null)
                && waitDuration > 0 && (saveTime != null)
                &&  feedCurrent > 0 && (!StringUtils.isBlank(feedState))
                && (!StringUtils.isBlank(feedState)))
        {
           return false;
        }
        return true;
    }
}
