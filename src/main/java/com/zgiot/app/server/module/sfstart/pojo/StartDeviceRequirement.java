package com.zgiot.app.server.module.sfstart.pojo;

import java.util.List;


public class StartDeviceRequirement {

    // 规则设备id
    private String deviceId;

    // 对应规则
    private List<StartRequirement> startRequirements;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<StartRequirement> getStartRequirements() {
        return startRequirements;
    }

    public void setStartRequirements(List<StartRequirement> startRequirements) {
        this.startRequirements = startRequirements;
    }
}
