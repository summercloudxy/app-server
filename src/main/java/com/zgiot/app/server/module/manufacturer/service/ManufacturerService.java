package com.zgiot.app.server.module.manufacturer.service;

import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.manufacturer.pojo.Manufacturer;

import java.util.List;

public interface ManufacturerService {

    /**
     *厂家管理-添加
     * @param manufacturer
     * @return
     */
    public void addManufacturer(Manufacturer manufacturer);

    /**
     *厂家管理-编辑
     * @param manufacturer
     * @return
     */
    public void editManufacturer(Manufacturer manufacturer);

    /**
     *厂家管理-删除
     * @param id
     * @return
     */
    public void deleteManufacturer(Long id);

    /**
     *厂家管理-查询-条件：厂家类型
     * @param thingType1Code
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getManufacturerByType(String thingType1Code, int pageNum, int pageSize);

    /**
     *厂家管理-查询-条件：厂家类型&&（编码或名称）
     * @param thingType1Code
     * @param codeOrName
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getManufacturerByCodeOrName(String thingType1Code,String codeOrName, int pageNum, int pageSize);
}
