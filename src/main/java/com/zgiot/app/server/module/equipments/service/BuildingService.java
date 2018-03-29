package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.pojo.Building;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BuildingService {

    public void addBuilding(Building building);

    public Building getBuildingById(Long id);

    public void editBuilding(Building building);

    public void deleteBuilding(Long id);

    public List<Building> getBuildingAll(int pageNum,int pageSize);
}
