package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.equipments.pojo.CoalStorageDepot;

import java.util.List;

public interface CoalStorageDepotService {

    /**
     * 储煤仓-添加
     *
     * @param coalStorageDepot
     */
    public void addCoalStorageDepot(CoalStorageDepot coalStorageDepot);

    /**
     * 储煤仓-获取指定
     *
     * @param id
     * @return
     */
    public CoalStorageDepot getCoalStorageDepotById(Long id);

    /**
     * 储煤仓-编辑
     *
     * @param coalStorageDepot
     */
    public void editCoalStorageDepot(CoalStorageDepot coalStorageDepot);

    /**
     * 储煤仓-删除
     *
     * @param id
     */
    public void deleteCoalStorageDepot(Long id);

    /**
     * 储煤仓-列表-全部
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getCoalStorageDepotAll(int pageNum, int pageSize);

    /**
     * 储煤仓-列表-根据名称查询
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    public PageHelpInfo getCoalStorageDepotByName(int pageNum, int pageSize, String name);

}
