package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.controller.EquipmentInfo;

import java.util.List;

public interface ThingService {

    public List<EquipmentInfo> getEquipmentInfoByThingcode(List<String> thingCodeList);

}
