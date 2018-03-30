package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.controller.ChuteInfo;
import com.zgiot.app.server.module.equipments.controller.EquipmentInfo;
import com.zgiot.app.server.module.equipments.controller.PipeInfo;
import com.zgiot.app.server.module.equipments.pojo.Thing;

import java.util.List;

public interface ThingService {

    public List<EquipmentInfo> getEquipmentInfoByThingcode(List<String> thingCodeList);

    public List<Thing> getThingByCode(String thingCode, String thingType1Code);

    public boolean getThingByThingCode(String thingCode);

    public void addChute(ChuteInfo chuteInfo);

    public void addDevice(DeviceInfo deviceInfo);

    public void deleteDevice(Long id);

    public void editChute(ChuteInfo chuteInfo);

    public void delChuteOrPipe(Long id);

    public void addPipe(PipeInfo pipeInfo);

    public void editPipe(PipeInfo pipeInfo);

}
