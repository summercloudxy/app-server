package com.zgiot.app.server.module.alert.pojo;

import java.util.List;

public class AlertMaskInfo {
    private List<AlertMask> alertMasks;
    private Integer maskId;
    private String info;

    public List<AlertMask> getAlertMasks() {
        return alertMasks;
    }

    public void setAlertMasks(List<AlertMask> alertMasks) {
        this.alertMasks = alertMasks;
    }

    public Integer getMaskId() {
        return maskId;
    }

    public void setMaskId(Integer maskId) {
        this.maskId = maskId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
