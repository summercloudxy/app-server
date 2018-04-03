package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.controller.*;
import com.zgiot.app.server.module.equipments.pojo.Thing;

import java.util.List;

public interface ThingManagementService {

    public List<Thing> getThingByCode(String thingCode, String thingType1Code);

    public boolean getThingByThingCode(String thingCode);

    public void addChute(ChuteInfo chuteInfo);

    public void editChute(ChuteInfo chuteInfo);

    public void addDevice(DeviceInfo deviceInfo);

    public void deleteDevice(Long id);

    public void editDevice(DeviceInfo deviceInfo);

    public PageHelpInfo getAllDeviceInfo(Long id, int pageNum, int pageSize);

    public PageHelpInfo getDeviceInfoByThingTagId(Long id, int pageNum, int pageSize);

    public PageHelpInfo getPartsInfoByThingTagId(Long id, int pageNum, int pageSize);

    public void delChuteOrPipe(Long id);

    public void addPipe(PipeInfo pipeInfo);

    public void editPipe(PipeInfo pipeInfo);

    public void addParts(PartsInfo partsInfo);

    public void deleteParts(Long id);

    public void editParts(PartsInfo partsInfo);

    public PageHelpInfo getPipeInfoByThingTagId(Long id, int pageNum, int pageSize);

    public void addFlashboard(FlashboardInfo flashboardInfo);

    public void delFlashboardOrValve(Long id);

    public void editFlashboard(FlashboardInfo flashboardInfo);

    public void addValve(ValveInfo valveInfo);

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
}
