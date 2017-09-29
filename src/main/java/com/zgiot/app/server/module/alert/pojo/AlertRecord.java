package com.zgiot.app.server.module.alert.pojo;

import java.util.List;

/**
 * Created by xiayun on 2017/9/29.
 */
public class AlertRecord {
    private String thingCode;
    private List<AlertData> alertDataList;

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public List<AlertData> getAlertDataList() {
        return alertDataList;
    }

    public void setAlertDataList(List<AlertData> alertDataList) {
        this.alertDataList = alertDataList;
    }
}
