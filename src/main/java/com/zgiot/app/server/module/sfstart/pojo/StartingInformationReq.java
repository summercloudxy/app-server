package com.zgiot.app.server.module.sfstart.pojo;

import java.util.Date;
import java.util.List;


public class StartingInformationReq {

    // 当前时间
    private Date nowTime;
    // 开始启车时间
    private Date startTime;
    // 是否暂停中
    private Boolean pauseState;
    // 设备状态
    private List<StartDeviceStateRecord> startDeviceStateRecords;

    public Date getNowTime() {
        return nowTime;
    }

    public void setNowTime(Date nowTime) {
        this.nowTime = nowTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Boolean getPauseState() {
        return pauseState;
    }

    public void setPauseState(Boolean pauseState) {
        this.pauseState = pauseState;
    }

    public List<StartDeviceStateRecord> getStartDeviceStateRecords() {
        return startDeviceStateRecords;
    }

    public void setStartDeviceStateRecords(List<StartDeviceStateRecord> startDeviceStateRecords) {
        this.startDeviceStateRecords = startDeviceStateRecords;
    }
}
