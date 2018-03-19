package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.sfmonitor.pojo.SFMonEquipMonitorInfo;

import java.util.List;

public class EquipmentMonitorPageRes {
    List<SFMonEquipMonitorInfo> sfMonEquipMonitorInfos;
    int count;

    public List<SFMonEquipMonitorInfo> getSfMonEquipMonitorInfos() {
        return sfMonEquipMonitorInfos;
    }

    public int getCount() {
        return count;
    }

    public void setSfMonEquipMonitorInfos(List<SFMonEquipMonitorInfo> sfMonEquipMonitorInfos) {
        this.sfMonEquipMonitorInfos = sfMonEquipMonitorInfos;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
