package com.zgiot.app.server.module.equipments.controller;

import com.zgiot.app.server.module.equipments.pojo.CoalStorageDepot;

import java.util.List;

public class CoalStorageDepotInfo {

    private List<CoalStorageDepot> coalStorageDepotList;

    private Integer count;

    public List<CoalStorageDepot> getCoalStorageDepotList() {
        return coalStorageDepotList;
    }

    public void setCoalStorageDepotList(List<CoalStorageDepot> coalStorageDepotList) {
        this.coalStorageDepotList = coalStorageDepotList;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
