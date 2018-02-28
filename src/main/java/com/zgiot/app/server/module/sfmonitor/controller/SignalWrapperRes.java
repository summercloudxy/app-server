package com.zgiot.app.server.module.sfmonitor.controller;

import java.util.List;

public class SignalWrapperRes {
    private List<SignalWrapperInfo> signalWrapperInfoList;
    private int count;

    public List<SignalWrapperInfo> getSignalWrapperInfoList() {
        return signalWrapperInfoList;
    }

    public int getCount() {
        return count;
    }

    public void setSignalWrapperInfoList(List<SignalWrapperInfo> signalWrapperInfoList) {
        this.signalWrapperInfoList = signalWrapperInfoList;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
