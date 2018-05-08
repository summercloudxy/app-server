package com.zgiot.app.server.module.sfstart.pojo;


public class StartDeviceRelation {

    // 启车被限制设备
    private String deviceId;

    // 启车限制控制设备
    private String controlDeviceId;

    // 启车限制控制设备code
    private String controlDeviceCode;

    // 启车限制控制设备name
    private String controlDeviceName;

    // 启车限制控制设备状态(0:未启动，1:异常，2:已启动)
    private Double controlDeviceState;

    // 启车限制控制设备异常信息
    private String abnormal;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getControlDeviceId() {
        return controlDeviceId;
    }

    public void setControlDeviceId(String controlDeviceId) {
        this.controlDeviceId = controlDeviceId;
    }

    public String getControlDeviceCode() {
        return controlDeviceCode;
    }

    public void setControlDeviceCode(String controlDeviceCode) {
        this.controlDeviceCode = controlDeviceCode;
    }

    public String getControlDeviceName() {
        return controlDeviceName;
    }

    public void setControlDeviceName(String controlDeviceName) {
        this.controlDeviceName = controlDeviceName;
    }

    public Double getControlDeviceState() {
        return controlDeviceState;
    }

    public void setControlDeviceState(Double controlDeviceState) {
        this.controlDeviceState = controlDeviceState;
    }

    public String getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(String abnormal) {
        this.abnormal = abnormal;
    }
}
