package com.zgiot.app.server.module.sfstart.pojo;


public class StartFaultInformation {

    // 故障点涉及设备
    private String deviceId;

    // 故障点name
    private Integer name;

    // 故障名称
    private String faultName;

    private String deviceCode;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public String getFaultName() {
        return faultName;
    }

    public void setFaultName(String faultName) {
        this.faultName = faultName;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
}
