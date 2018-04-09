package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.equipments.pojo.Building;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BuildingService {

    /**
     * 生产车间-添加
     * @param building
     */
    public void addBuilding(Building building);

    /**
     * 生产车间-查找指定
     * @param id
     * @return
     */
    public Building getBuildingById(Long id);

    /**
     * 生产车间-编辑
     * @param building
     */
    public void editBuilding(Building building);

    /**
     * 生产车间-删除
     * @param id
     */
    public void deleteBuilding(Long id);

    /**
     * 生产车间-列表-全部
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getBuildingAll(int pageNum, int pageSize);

    /**
     * 生产车间-列表-按名称查询
     * @param pageNum
     * @param pageSize
     * @param buildingName
     * @return
     */
    public PageHelpInfo getBuildingByBuildingName(int pageNum,int pageSize,String buildingName);

    /**
     * getAllBuilding
     * @return
     */
    public List<Building> getAllBuilding();
}
