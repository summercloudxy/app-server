package com.zgiot.app.server.module.sfstart.pojo;


public class StartManualInterventionRecord {

    // 检查id
    private Integer manualInterventionId;

    // 检查对应启车操作记录
    private Integer operateId;

    // 干预设备id
    private String deviceId;

    // 干预设备code
    private String deviceCode;

    // 干预设备名称
    private String deviceName;

    // 干预设备所属区域名称
    private String areaName;

    // 干预设备所属总系统名称
    private String systemName;

    // 干预设备楼层
    private String floor;

    // 干预人
    private String manualInterventionPerson;

    // 人工干预状态
    private Integer interventionState;

    public Integer getManualInterventionId() {
        return manualInterventionId;
    }

    public void setManualInterventionId(Integer manualInterventionId) {
        this.manualInterventionId = manualInterventionId;
    }

    public Integer getOperateId() {
        return operateId;
    }

    public void setOperateId(Integer operateId) {
        this.operateId = operateId;
    }

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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getManualInterventionPerson() {
        return manualInterventionPerson;
    }

    public void setManualInterventionPerson(String manualInterventionPerson) {
        this.manualInterventionPerson = manualInterventionPerson;
    }

    public Integer getInterventionState() {
        return interventionState;
    }

    public void setInterventionState(Integer interventionState) {
        this.interventionState = interventionState;
    }
}
