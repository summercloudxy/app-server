package com.zgiot.app.server.module.sfstart.pojo;


public class StartAreaRule {

    // 规则id
    private Integer ruleId;

    // 起始区域id
    private Integer areaFirstId;

    // 等待区域id
    private Integer areaSecondId;

    //等待时间
    private Integer delayTime;

    // 规则类型
    private Integer type;

    // 涉及设备id
    private String deviceId;

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getAreaFirstId() {
        return areaFirstId;
    }

    public void setAreaFirstId(Integer areaFirstId) {
        this.areaFirstId = areaFirstId;
    }

    public Integer getAreaSecondId() {
        return areaSecondId;
    }

    public void setAreaSecondId(Integer areaSecondId) {
        this.areaSecondId = areaSecondId;
    }

    public Integer getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
