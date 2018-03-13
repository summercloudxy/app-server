package com.zgiot.app.server.module.sfmonitor.controller;

import java.util.List;

public class SignalWrapperPage {
    List<FindSignalWrapperRes> findSignalWrapperRes;
    int count;

    public List<FindSignalWrapperRes> getFindSignalWrapperRes() {
        return findSignalWrapperRes;
    }

    public int getCount() {
        return count;
    }

    public void setFindSignalWrapperRes(List<FindSignalWrapperRes> findSignalWrapperRes) {
        this.findSignalWrapperRes = findSignalWrapperRes;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
