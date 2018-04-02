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

    public List<DeviceInfo> getDeviceInfoByThingTagId(Long id, int pageNum, int pageSize);

    public List<PartsInfo> getPartsInfoByThingTagId(Long id, int pageNum, int pageSize);

    public void delChuteOrPipe(Long id);

    public void addPipe(PipeInfo pipeInfo);

    public void editPipe(PipeInfo pipeInfo);

    public void addParts(PartsInfo partsInfo);

    public void deleteParts(Long id);

    public void editParts(PartsInfo partsInfo);

    public List<PipeInfo> getPipeInfoByThingTagId(Long id, int pageNum, int pageSize);

    public void addFlashboard(FlashboardInfo flashboardInfo);

    public void delFlashboardOrValve(Long id);

    public void editFlashboard(FlashboardInfo flashboardInfo);

    public void addValve(ValveInfo valveInfo);

    public void editValve(ValveInfo valveInfo);

    public List<ChuteInfo> getChuteInfoByThingTagId(Long id, int pageNum, int pageSize);

    public List<ValveInfo> getValveInfoByThingTagId(Long id, int pageNum, int pageSize);

    public List<FlashboardInfo> getFlashboardInfoByThingTagId(Long id, int pageNum, int pageSize);

}
