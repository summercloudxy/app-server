package com.zgiot.app.server.module.qualityandquantity.pojo;

import java.util.Date;

public class DutyInfo {
    //该班开始时间
    private Date dutyStartTime;
    //该班结束时间
    private Date dutyEndTime;
    //白班/夜班  "day" "night"
    private String dutyType;
    //班最大值
    private String maxValue;

    public Date getDutyStartTime() {
        return dutyStartTime;
    }

    public void setDutyStartTime(Date dutyStartTime) {
        this.dutyStartTime = dutyStartTime;
    }

    public Date getDutyEndTime() {
        return dutyEndTime;
    }

    public void setDutyEndTime(Date dutyEndTime) {
        this.dutyEndTime = dutyEndTime;
    }

    public String getDutyType() {
        return dutyType;
    }

    public void setDutyType(String dutyType) {
        this.dutyType = dutyType;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public DutyInfo(Date dutyStartTime, Date dutyEndTime, String dutyType) {
        this.dutyStartTime = dutyStartTime;
        this.dutyEndTime = dutyEndTime;
        this.dutyType = dutyType;
    }

    public DutyInfo() {

    }
}
