package com.zgiot.app.server.module.sfstart.pojo;


public class StartDevice {

    // 设备id
    private String deviceId;

    // 设备机器号
    private String deviceCode;

    // 设备名称
    private String deviceName;

    // 设备状态
    private Integer deviceState;

    // 设备带煤量
    private Double coalCapacity;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(Integer deviceState) {
        this.deviceState = deviceState;
    }

    public Double getCoalCapacity() {
        return coalCapacity;
    }

    public void setCoalCapacity(Double coalCapacity) {
        this.coalCapacity = coalCapacity;
    }
}
