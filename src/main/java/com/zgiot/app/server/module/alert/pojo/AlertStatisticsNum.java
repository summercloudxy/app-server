package com.zgiot.app.server.module.alert.pojo;

public class AlertStatisticsNum {
    private String thingCode;
    private int sumNum;
    private int level30Num;
    private int level20Num;
    private int level10Num;
    private String dayStr;

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public int getSumNum() {
        return sumNum;
    }

    public void setSumNum(int sumNum) {
        this.sumNum = sumNum;
    }

    public int getLevel30Num() {
        return level30Num;
    }

    public void setLevel30Num(int level30Num) {
        this.level30Num = level30Num;
    }

    public int getLevel20Num() {
        return level20Num;
    }

    public void setLevel20Num(int level20Num) {
        this.level20Num = level20Num;
    }

    public int getLevel10Num() {
        return level10Num;
    }

    public void setLevel10Num(int level10Num) {
        this.level10Num = level10Num;
    }

    public String getDayStr() {
        return dayStr;
    }

    public void setDayStr(String dayStr) {
        this.dayStr = dayStr;
    }
}
