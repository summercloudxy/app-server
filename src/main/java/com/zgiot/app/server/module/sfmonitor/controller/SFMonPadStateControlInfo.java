package com.zgiot.app.server.module.sfmonitor.controller;

import java.util.List;

public class SFMonPadStateControlInfo {
    private List<SFMonPadStateControlMetricInfo> sfMonPadStateControlMetricInfos;
    private boolean powerEquipment;

    public List<SFMonPadStateControlMetricInfo> getSfMonPadStateControlMetricInfos() {
        return sfMonPadStateControlMetricInfos;
    }

    public boolean isPowerEquipment() {
        return powerEquipment;
    }

    public void setSfMonPadStateControlMetricInfos(List<SFMonPadStateControlMetricInfo> sfMonPadStateControlMetricInfos) {
        this.sfMonPadStateControlMetricInfos = sfMonPadStateControlMetricInfos;
    }

    public void setPowerEquipment(boolean powerEquipment) {
        this.powerEquipment = powerEquipment;
    }
}
