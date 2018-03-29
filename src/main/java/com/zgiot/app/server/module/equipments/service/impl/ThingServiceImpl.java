package com.zgiot.app.server.module.equipments.service.impl;

import com.zgiot.app.server.module.equipments.controller.EquipmentInfo;
import com.zgiot.app.server.module.equipments.mapper.ThingMapper;
import com.zgiot.app.server.module.equipments.pojo.Thing;
import com.zgiot.app.server.module.equipments.service.ThingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThingServiceImpl implements ThingService {
    @Autowired
    private ThingMapper thingMapper;

    @Override
    public List<EquipmentInfo> getEquipmentInfoByThingcode(List<String> thingCodeList) {
        return thingMapper.getEquipmentInfoByThingcode(thingCodeList);
    }

    @Override
    public List<Thing> getThingByCode(String thingCode, String thingType1Code) {
        thingCode = "%" + thingCode + "%";
        return thingMapper.getThingByCode(thingCode,thingType1Code);
    }
}
