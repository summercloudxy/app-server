package com.zgiot.app.server.module.equipments.controller;

import com.zgiot.app.server.module.equipments.pojo.Building;

import java.util.List;

public class BuildingInfo {

    private List<Building> buildingList;

    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Building> getBuildingList() {

        return buildingList;
    }

    public void setBuildingList(List<Building> buildingList) {
        this.buildingList = buildingList;
    }
}
