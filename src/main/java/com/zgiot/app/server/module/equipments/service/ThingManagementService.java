package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.controller.*;
import com.zgiot.app.server.module.equipments.pojo.Thing;

import java.util.List;

public interface ThingManagementService {

    /**
     * 设备-列表-查询条件：设备编号，设备类型
     *
     * @param thingCode
     * @param thingType1Code
     * @return
     */
    public List<Thing> getThingByCode(String thingCode, String thingType1Code);

    /**
     * 判断是否有此设备编号
     * true有此编号,false没有
     *
     * @param thingCode
     * @return
     */
    public boolean getThingByThingCode(String thingCode);

    /**
     * 设备-溜槽-添加
     *
     * @param chuteInfo
     */
    public void addChute(ChuteInfo chuteInfo);

    /**
     * 设备-溜槽-编辑
     *
     * @param chuteInfo
     */
    public void editChute(ChuteInfo chuteInfo);

    /**
     * 设备-设备-添加
     *
     * @param deviceInfo
     */
    public void addDevice(DeviceInfo deviceInfo);

    /**
     * 设备-设备-删除
     *
     * @param id
     */
    public void deleteDevice(Long id);

    /**
     * 设备-设备-编辑
     *
     * @param deviceInfo
     */
    public void editDevice(DeviceInfo deviceInfo);

    /**
     * 分页-获取所有设备列表
     *
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getAllDeviceInfo(Long id, int pageNum, int pageSize);

    /**
     * 分页-获取某一类型设备列表
     *
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getDeviceInfoByThingTagId(Long id, int pageNum, int pageSize);

    /**
     * 分页-获取部件列表
     *
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getPartsInfoByThingTagId(Long id, int pageNum, int pageSize);

    /**
     * 设备-溜槽，管道-删除
     *
     * @param id
     */
    public void delChuteOrPipe(Long id);

    /**
     * 设备-管道-添加
     *
     * @param pipeInfo
     */
    public void addPipe(PipeInfo pipeInfo);

    /**
     * 设备-管道-编辑
     *
     * @param pipeInfo
     */
    public void editPipe(PipeInfo pipeInfo);

    /**
     * 设备-部件-添加
     *
     * @param partsInfo
     */
    public void addParts(PartsInfo partsInfo);

    /**
     * 设备-部件-删除
     *
     * @param id
     */
    public void deleteParts(Long id);

    /**
     * 设备-部件-编辑
     *
     * @param partsInfo
     */
    public void editParts(PartsInfo partsInfo);

    /**
     * 分页-获取管道列表
     *
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getPipeInfoByThingTagId(Long id, int pageNum, int pageSize);

    /**
     * 设备-闸板-添加
     *
     * @param flashboardInfo
     */
    public void addFlashboard(FlashboardInfo flashboardInfo);

    /**
     * 设备-闸板，阀门-删除
     *
     * @param id
     */
    public void delFlashboardOrValve(Long id);

    /**
     * 设备-闸板-编辑
     *
     * @param flashboardInfo
     */
    public void editFlashboard(FlashboardInfo flashboardInfo);

    /**
     * 设备-阀门-添加
     *
     * @param valveInfo
     */
    public void addValve(ValveInfo valveInfo);

    /**
     * 设备-阀门-编辑
     *
     * @param valveInfo
     */
    public void editValve(ValveInfo valveInfo);

    /**
     * 分页-获取溜槽列表
     *
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getChuteInfoByThingTagId(Long id, int pageNum, int pageSize);

    /**
     * 分页-获取阀门列表
     *
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getValveInfoByThingTagId(Long id, int pageNum, int pageSize);

    /**
     * 分页-获取闸板列表
     *
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getFlashboardInfoByThingTagId(Long id, int pageNum, int pageSize);

    /**
     * 电气设备-仪表-添加
     *
     * @param meterInfo
     */
    public void addMeter(MeterInfo meterInfo);

    /**
     * 电气设备-仪表-删除
     *
     * @param id
     */
    public void deleteMeter(Long id);

    /**
     * 电气设备-仪表-编辑
     *
     * @param meterInfo
     */
    public void editMeter(MeterInfo meterInfo);

    /**
     * 分页-获取所有类型仪表列表
     *
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getAllMeterInfo(Long id, int pageNum, int pageSize);

    /**
     * 分页-获取某一类型仪表列表
     *
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getMeterInfoByThingTagId(Long id, int pageNum, int pageSize);

    /**
     * 根据设备编号获取部件信息
     *
     * @param thingCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getPartsInfoByThingCode(String thingCode, int pageNum, int pageSize);

    /**
     * 根据设备编号或名称查询设备
     *
     * @param codeOrName
     * @param type
     * @return
     */
    public List<Thing> getThingByCodeOrName(String codeOrName, String type);

    /**
     * 根据设备编号和类型获取设备信息列表(列表搜索用)
     *
     * @param thingCode
     * @param type
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getThingInfoByThingCode(String thingCode, String type, int pageNum, int pageSize);

    /**
     * 根据设备编号集合获取设备信息列表
     *
     * @param thingCodeList
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelpInfo getDeviceInfoByThingCode(List<String> thingCodeList, int pageNum, int pageSize);

}
