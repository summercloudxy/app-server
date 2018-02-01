package com.zgiot.app.server.module.alert.pojo;

import java.util.ArrayList;
import java.util.List;

public class AlertStatisticsNum {
    private String thingCode;
    private int sumNum;
    private List<AlertLevelNum> alertLevelNumList = new ArrayList<>();
//    private Map<Integer, Integer> alertLevelNums = new TreeMap<>();
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

//    public Map<Integer, Integer> getAlertLevelNums() {
//        return alertLevelNums;
//    }
//
//    public void setAlertLevelNums(Map<Integer, Integer> alertLevelNums) {
//        this.alertLevelNums = alertLevelNums;
//    }

    public String getDayStr() {
        return dayStr;
    }

    public void setDayStr(String dayStr) {
        this.dayStr = dayStr;
    }

    public List<AlertLevelNum> getAlertLevelNumList() {
        return alertLevelNumList;
    }

    public void setAlertLevelNumList(List<AlertLevelNum> alertLevelNumList) {
        this.alertLevelNumList = alertLevelNumList;
    }
}
