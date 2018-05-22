package com.zgiot.app.server.module.sfstart.pojo;


public class StartBrowseCoalDevice {

    // 设备id
    private String deviceId;

    // 设备机器号
    private String deviceCode;

    // 设备名称
    private String deviceName;

    // 仓库高度
    private Double deportHigh;

    // 仓库煤高1-1235
    private Double coalDeportOne;

    // 仓库煤高2-1236
    private Double coalDeportTwo;

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

    public Double getDeportHigh() {
        return deportHigh;
    }

    public void setDeportHigh(Double deportHigh) {
        this.deportHigh = deportHigh;
    }

    public Double getCoalDeportOne() {
        return coalDeportOne;
    }

    public void setCoalDeportOne(Double coalDeportOne) {
        this.coalDeportOne = coalDeportOne;
    }

    public Double getCoalDeportTwo() {
        return coalDeportTwo;
    }

    public void setCoalDeportTwo(Double coalDeportTwo) {
        this.coalDeportTwo = coalDeportTwo;
    }
}
