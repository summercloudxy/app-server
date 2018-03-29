package com.zgiot.app.server.module.equipments.service.impl;

import com.zgiot.app.server.module.equipments.controller.ChuteInfo;
import com.zgiot.app.server.module.equipments.controller.EquipmentInfo;
import com.zgiot.app.server.module.equipments.mapper.ThingMapper;
import com.zgiot.app.server.module.equipments.mapper.ThingPropertiesMapper;
import com.zgiot.app.server.module.equipments.pojo.Thing;
import com.zgiot.app.server.module.equipments.pojo.ThingProperties;
import com.zgiot.app.server.module.equipments.service.ThingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

import static com.zgiot.app.server.module.equipments.constants.EquipmentConstant.*;


@Service
public class ThingServiceImpl implements ThingService {
    @Autowired
    private ThingMapper thingMapper;

    @Autowired
    private ThingPropertiesMapper thingPropertiesMapper;

    @Override
    public List<EquipmentInfo> getEquipmentInfoByThingcode(List<String> thingCodeList) {
        return thingMapper.getEquipmentInfoByThingcode(thingCodeList);
    }

    @Override
    public List<Thing> getThingByCode(String thingCode, String thingType1Code) {
        thingCode = "%" + thingCode + "%";
        return thingMapper.getThingByCode(thingCode,thingType1Code);
    }


    @Transactional
    public void addChute(ChuteInfo chuteInfo) {

        String thingCode = "调用wang磊的方法";
        Thing thing = new Thing();
        thing.setThingCode(thingCode);
        thing.setThingName(chuteInfo.getChuteName());
        thing.setThingType1Code(THING_TYPE1_CODE_CHUTE);
        thingMapper.addThing(thing);

        ThingProperties tp1 = new ThingProperties();
        tp1.setThingCode(thingCode);
        tp1.setPropKey(CHUTE_START_THING_CODE);
        tp1.setPropValue(chuteInfo.getStartThingCode());
        tp1.setPropType(PROP_TYPE_PROP);
        thingPropertiesMapper.addThingProperties(tp1);

        ThingProperties tp2 = new ThingProperties();
        tp1.setThingCode(thingCode);
        tp1.setPropKey(CHUTE_START_THING_NAME);
        tp1.setPropValue(chuteInfo.getStartThingName());
        tp1.setPropType(PROP_TYPE_PROP);
        thingPropertiesMapper.addThingProperties(tp2);

        ThingProperties tp3 = new ThingProperties();
        tp1.setThingCode(thingCode);
        tp1.setPropKey(CHUTE_TERMINAL_THING_CODE);
        tp1.setPropValue(chuteInfo.getTerminalThingCode());
        tp1.setPropType(PROP_TYPE_PROP);
        thingPropertiesMapper.addThingProperties(tp3);

        ThingProperties tp4 = new ThingProperties();
        tp1.setThingCode(thingCode);
        tp1.setPropKey(CHUTE_TERMINAL_THING_NAME);
        tp1.setPropValue(chuteInfo.getTerminalThingName());
        tp1.setPropType(PROP_TYPE_PROP);
        thingPropertiesMapper.addThingProperties(tp4);

        ThingProperties tp5 = new ThingProperties();
        tp1.setThingCode(thingCode);
        tp1.setPropKey(CHUTE_ENABLE_DATE);
        tp1.setPropValue(chuteInfo.getEnableDate());
        tp1.setPropType(PROP_TYPE_PROP);
        thingPropertiesMapper.addThingProperties(tp5);

        ThingProperties tp6 = new ThingProperties();
        tp1.setThingCode(thingCode);
        tp1.setPropKey(CHUTE_DISABLE_DATE);
        tp1.setPropValue(chuteInfo.getDisableDate());
        tp1.setPropType(PROP_TYPE_PROP);
        thingPropertiesMapper.addThingProperties(tp6);



    }
}
