package com.zgiot.app.server.module.sfstart.pojo;


public class StartDeviceInformation {

    // 设备id
    private String deviceId;

    // 设备启动分包
    private Integer productionLine;

    // 设备启动包内顺序
    private Integer startSequence;

    // 设备功率
    private Float rateWork;

    // 设备所属变压器id
    private Integer transformerId;

    // 设备等待时间
    private Integer startWaitTime;

    // 启车设备所属大区区域包信息
    private String startHierarchy;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(Integer productionLine) {
        this.productionLine = productionLine;
    }

    public Integer getStartSequence() {
        return startSequence;
    }

    public void setStartSequence(Integer startSequence) {
        this.startSequence = startSequence;
    }

    public Float getRateWork() {
        return rateWork;
    }

    public void setRateWork(Float rateWork) {
        this.rateWork = rateWork;
    }

    public Integer getTransformerId() {
        return transformerId;
    }

    public void setTransformerId(Integer transformerId) {
        this.transformerId = transformerId;
    }

    public Integer getStartWaitTime() {
        return startWaitTime;
    }

    public void setStartWaitTime(Integer startWaitTime) {
        this.startWaitTime = startWaitTime;
    }

    public String getStartHierarchy() {
        return startHierarchy;
    }

    public void setStartHierarchy(String startHierarchy) {
        this.startHierarchy = startHierarchy;
    }
}
