package com.zgiot.app.server.module.qualityandquantity.pojo;


import java.util.Date;

public class DutyInfoInOneDay {
    private String dayValue;
    private String nightValue;
    private Date dayTime;


    public String getDayValue() {
        return dayValue;
    }

    public void setDayValue(String dayValue) {
        this.dayValue = dayValue;
    }

    public String getNightValue() {
        return nightValue;
    }

    public void setNightValue(String nightValue) {
        this.nightValue = nightValue;
    }

    public Date getDayTime() {
        return dayTime;
    }

    public void setDayTime(Date dayTime) {
        this.dayTime = dayTime;
    }
}
