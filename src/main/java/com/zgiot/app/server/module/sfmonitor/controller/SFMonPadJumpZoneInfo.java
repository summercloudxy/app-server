package com.zgiot.app.server.module.sfmonitor.controller;

import java.util.List;

public class SFMonPadJumpZoneInfo {
    private List<SFMonPadJumpZoneEquipmentInfo> sfMonPadJumpZoneEquipmentInfos;
    //对应设备跳转区自、至、同等区域
    private String zoneCode;

    public List<SFMonPadJumpZoneEquipmentInfo> getSfMonPadJumpZoneEquipmentInfos() {
        return sfMonPadJumpZoneEquipmentInfos;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setSfMonPadJumpZoneEquipmentInfos(List<SFMonPadJumpZoneEquipmentInfo> sfMonPadJumpZoneEquipmentInfos) {
        this.sfMonPadJumpZoneEquipmentInfos = sfMonPadJumpZoneEquipmentInfos;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }
}
