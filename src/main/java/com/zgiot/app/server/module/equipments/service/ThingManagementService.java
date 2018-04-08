package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.controller.*;
import com.zgiot.app.server.module.equipments.pojo.Thing;

import java.util.List;

public interface ThingManagementService {

    /**
     * 设备-列表-查询条件：设备编号，设备类型
     * @param thingCode
     * @param thingType1Code
     * @return
     */
    public List<Thing> getThingByCode(String thingCode, String thingType1Code);


    public boolean getThingByThingCode(String thingCode);

    /**
     * 设备-溜槽-添加
     * @param chuteInfo
     */
    public void addChute(ChuteInfo chuteInfo);

    /**
     * 设备-溜槽-编辑
     * @param chuteInfo
     */
    public void editChute(ChuteInfo chuteInfo);

    /**
     *设备-设备-添加
     * @param deviceInfo
     */
    public void addDevice(DeviceInfo deviceInfo);

    /**
     * 设备-设备-删除
     * @param id
     */
    public void deleteDevice(Long id);

    /**
     *设备-设备-编辑
     * @param deviceInfo
     */
    public void editDevice(DeviceInfo deviceInfo);

    public PageHelpInfo getAllDeviceInfo(Long id, int pageNum, int pageSize);

    public PageHelpInfo getDeviceInfoByThingTagId(Long id, int pageNum, int pageSize);

    public PageHelpInfo getPartsInfoByThingTagId(Long id, int pageNum, int pageSize);

    /**
     * 设备-溜槽，管道-删除
     * @param id
     */
    public void delChuteOrPipe(Long id);

    /**
     * 设备-管道-添加
     * @param pipeInfo
     */
    public void addPipe(PipeInfo pipeInfo);

    /**
     * 设备-管道-编辑
     * @param pipeInfo
     */
    public void editPipe(PipeInfo pipeInfo);

    /**
     * 设备-部件-添加
     * @param partsInfo
     */
    public void addParts(PartsInfo partsInfo);

    /**
     * 设备-部件-删除
     * @param id
     */
    public void deleteParts(Long id);

    /**
     * 设备-部件-编辑
     * @param partsInfo
     */
    public void editParts(PartsInfo partsInfo);

    public PageHelpInfo getPipeInfoByThingTagId(Long id, int pageNum, int pageSize);

    /**
     *设备-闸板-添加
     * @param flashboardInfo
     */
    public void addFlashboard(FlashboardInfo flashboardInfo);

    /**
     * 设备-闸板，阀门-删除
     * @param id
     */
    public void delFlashboardOrValve(Long id);

    /**
     * 设备-闸板-编辑
     * @param flashboardInfo
     */
    public void editFlashboard(FlashboardInfo flashboardInfo);

    /**
     *设备-阀门-添加
     * @param valveInfo
     */
    public void addValve(ValveInfo valveInfo);

    /**
     * 设备-阀门-编辑
     * @param valveInfo
     */
    public void editValve(ValveInfo valveInfo);

    public PageHelpInfo getChuteInfoByThingTagId(Long id, int pageNum, int pageSize);

    public PageHelpInfo getValveInfoByThingTagId(Long id, int pageNum, int pageSize);

    public PageHelpInfo getFlashboardInfoByThingTagId(Long id, int pageNum, int pageSize);

    public void addMeter(MeterInfo meterInfo);

    public void deleteMeter(Long id);

    public void editMeter(MeterInfo meterInfo);

    public PageHelpInfo getAllMeterInfo(Long id, int pageNum, int pageSize);

    public PageHelpInfo getMeterInfoByThingTagId(Long id, int pageNum, int pageSize);

    public List<PartsInfo> getPartsInfoByThingId(String thingCode);

    public List<Thing> getThingByCodeOrName(String codeOrName, String type);

    public PageHelpInfo getThingInfoByThingCode(String thingCode, String type, int pageNum, int pageSize);
}
