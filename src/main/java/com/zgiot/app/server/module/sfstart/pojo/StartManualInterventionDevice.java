package com.zgiot.app.server.module.sfstart.pojo;


public class StartManualInterventionDevice {

    // 人工干预设备id
    private String deviceId;

    // 干预设置
    private Integer state;

    // 干预人id
    private String personId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
