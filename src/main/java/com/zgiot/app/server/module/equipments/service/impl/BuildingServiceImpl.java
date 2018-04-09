package com.zgiot.app.server.module.equipments.service.impl;

import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.equipments.mapper.BuildingMapper;
import com.zgiot.app.server.module.equipments.pojo.Building;
import com.zgiot.app.server.module.equipments.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.Page;

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
    public PageHelpInfo getBuildingAll(int pageNum,int pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Building> buildingList = buildingMapper.getBuildingAll();
        PageHelpInfo pageHelpInfo = new PageHelpInfo();
        pageHelpInfo.setList(buildingList);
        pageHelpInfo.setSum(page.getTotal());
        return pageHelpInfo;
    }

    @Override
    public PageHelpInfo getBuildingByBuildingName(int pageNum,int pageSize,String buildingName) {
        buildingName = "%" + buildingName + "%";
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Building> buildingList = buildingMapper.getBuildingByBuildingName(buildingName);
        PageHelpInfo pageHelpInfo = new PageHelpInfo();
        pageHelpInfo.setList(buildingList);
        pageHelpInfo.setSum(page.getTotal());
        return pageHelpInfo;
    }

    @Override
    public List<Building> getAllBuilding() {
        return buildingMapper.getAllBuilding();
    }
}
