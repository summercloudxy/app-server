package com.zgiot.app.server.module.sfmonitor.controller;

import java.util.List;

public class AddOrEditMonitorResponse {
    private boolean isExsit;
    private long monitorId;
    private List<Long> items;

    public boolean isExsit() {
        return isExsit;
    }

    public long getMonitorId() {
        return monitorId;
    }

    public List<Long> getItems() {
        return items;
    }

    public void setExsit(boolean exsit) {
        isExsit = exsit;
    }

    public void setMonitorId(long monitorId) {
        this.monitorId = monitorId;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }
}
