package com.zgiot.app.server.module.equipments.service.impl;

import com.zgiot.app.server.module.equipments.mapper.BuildingMapper;
import com.zgiot.app.server.module.equipments.pojo.Building;
import com.zgiot.app.server.module.equipments.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingMapper buildingMapper;

    @Override
    public void addBuilding(Building building) {

        buildingMapper.addBuilding(building);

    }

    @Override
    public Building getBuildingById(Long id) {
        return buildingMapper.getBuildingById(id);
    }

    @Override
    public void editBuilding(Building building) {
        buildingMapper.editBuilding(building);
    }

    @Override
    public void deleteBuilding(Long id) {
        buildingMapper.deleteBuilding(id);
    }

    @Override
    public List<Building> getBuildingAll() {
        return buildingMapper.getBuildingAll();
    }
}
