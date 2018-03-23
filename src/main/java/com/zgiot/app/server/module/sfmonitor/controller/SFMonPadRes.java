package com.zgiot.app.server.module.sfmonitor.controller;

import java.util.List;

public class SFMonPadRes {
    private List<SFMonPadJumpZoneInfo> sfMonPadJumpZoneInfoList;
    private List<SFMonPadParameterZoneInfo> sfMonPadParameterZoneInfos;
    private SFMonPadStateControlInfo sfMonPadStateControlInfo;
    private List<SFMonPadAuxiliaryZoneInfo> sfMonPadAuxiliaryZoneInfos;

    public List<SFMonPadJumpZoneInfo> getSfMonPadJumpZoneInfoList() {
        return sfMonPadJumpZoneInfoList;
    }

    public List<SFMonPadParameterZoneInfo> getSfMonPadParameterZoneInfos() {
        return sfMonPadParameterZoneInfos;
    }

    public SFMonPadStateControlInfo getSfMonPadStateControlInfo() {
        return sfMonPadStateControlInfo;
    }

    public List<SFMonPadAuxiliaryZoneInfo> getSfMonPadAuxiliaryZoneInfos() {
        return sfMonPadAuxiliaryZoneInfos;
    }

    public void setSfMonPadJumpZoneInfoList(List<SFMonPadJumpZoneInfo> sfMonPadJumpZoneInfoList) {
        this.sfMonPadJumpZoneInfoList = sfMonPadJumpZoneInfoList;
    }

    public void setSfMonPadParameterZoneInfos(List<SFMonPadParameterZoneInfo> sfMonPadParameterZoneInfos) {
        this.sfMonPadParameterZoneInfos = sfMonPadParameterZoneInfos;
    }

    public void setSfMonPadStateControlInfo(SFMonPadStateControlInfo sfMonPadStateControlInfo) {
        this.sfMonPadStateControlInfo = sfMonPadStateControlInfo;
    }

    public void setSfMonPadAuxiliaryZoneInfos(List<SFMonPadAuxiliaryZoneInfo> sfMonPadAuxiliaryZoneInfos) {
        this.sfMonPadAuxiliaryZoneInfos = sfMonPadAuxiliaryZoneInfos;
    }
}
