package com.zgiot.app.server.module.sfsystems.service;

import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.equipments.pojo.TBSystem;

import java.util.List;

public interface SfSystemService {

    /**
     * 根据系统id获取设备列表
     *
     * @param systemId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getDeviceInfoBySystemId(Long systemId, int pageNum, int pageSize);

    /**
     * 根据区域id获取设备列表
     *
     * @param areaId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getDeviceInfoByAreaId(Long areaId, int pageNum, int pageSize);

    /**
     * 根据设备编号获取设备信息
     *
     * @param thingCode
     * @return
     */
    public List<DeviceInfo> getDeviceInfoByThingCode(String thingCode);

    /**
     * 根据设备编号和区域id模糊查询无所属系统的设备信息
     *
     * @param thingCode
     * @param areaId
     * @return
     */
    public List<DeviceInfo> getFreeDeviceInfo(String thingCode, Long areaId);

    /**
     * 获取菜单
     *
     * @param id
     * @param level
     * @return
     */
    public TBSystem getMenu(Long id, int level);

}
