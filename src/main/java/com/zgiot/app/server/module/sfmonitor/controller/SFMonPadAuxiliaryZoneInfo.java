package com.zgiot.app.server.module.sfmonitor.controller;

import java.util.List;

public class SFMonPadAuxiliaryZoneInfo {
    private List<SFMonPadAuxiliaryZoneMetricInfo> sfMonPadAuxiliaryZoneMetricInfoList;
    private String wrapperName;
    private String styleName;
    private int rule;

    public List<SFMonPadAuxiliaryZoneMetricInfo> getSfMonPadAuxiliaryZoneMetricInfoList() {
        return sfMonPadAuxiliaryZoneMetricInfoList;
    }

    public String getWrapperName() {
        return wrapperName;
    }

    public String getStyleName() {
        return styleName;
    }

    public int getRule() {
        return rule;
    }

    public void setSfMonPadAuxiliaryZoneMetricInfoList(List<SFMonPadAuxiliaryZoneMetricInfo> sfMonPadAuxiliaryZoneMetricInfoList) {
        this.sfMonPadAuxiliaryZoneMetricInfoList = sfMonPadAuxiliaryZoneMetricInfoList;
    }

    public void setWrapperName(String wrapperName) {
        this.wrapperName = wrapperName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }
}
