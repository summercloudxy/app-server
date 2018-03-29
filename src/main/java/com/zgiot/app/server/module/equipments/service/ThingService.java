package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.controller.ChuteInfo;
import com.zgiot.app.server.module.equipments.controller.EquipmentInfo;
import com.zgiot.app.server.module.equipments.pojo.Thing;

import java.util.List;

public interface ThingService {

    public List<EquipmentInfo> getEquipmentInfoByThingcode(List<String> thingCodeList);

    public List<Thing> getThingByCode(String thingCode, String thingType1Code);

    public void addChute(ChuteInfo chuteInfo);

}
