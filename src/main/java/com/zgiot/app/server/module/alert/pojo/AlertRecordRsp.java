package com.zgiot.app.server.module.alert.pojo;

import java.util.Date;
import java.util.List;

public class AlertRecordRsp {
    private List<AlertRecord> alertRecords;
    private Date timeStamp;

    public List<AlertRecord> getAlertRecords() {
        return alertRecords;
    }

    public void setAlertRecords(List<AlertRecord> alertRecords) {
        this.alertRecords = alertRecords;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public AlertRecordRsp(List<AlertRecord> alertRecords, Date timeStamp) {
        this.alertRecords = alertRecords;
        this.timeStamp = timeStamp;
    }

    public AlertRecordRsp() {

    }


}
