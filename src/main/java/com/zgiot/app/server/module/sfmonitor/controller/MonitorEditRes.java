package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.sfmonitor.pojo.RelSFMonItem;

import java.util.List;

public class MonitorEditRes {
    List<RelSFMonItem> items;
    private boolean exist;

    public List<RelSFMonItem> getItems() {
        return items;
    }

    public boolean isExist() {
        return exist;
    }

    public void setItems(List<RelSFMonItem> items) {
        this.items = items;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }
}
