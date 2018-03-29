package com.zgiot.app.server.module.equipments.service.impl;

import com.zgiot.app.server.module.equipments.constants.EquipmentConstant;
import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.controller.EquipmentInfo;
import com.zgiot.app.server.module.equipments.mapper.ThingMapper;
import com.zgiot.app.server.module.equipments.mapper.ThingPositionMapper;
import com.zgiot.app.server.module.equipments.pojo.Thing;
import com.zgiot.app.server.module.equipments.pojo.ThingPosition;
import com.zgiot.app.server.module.equipments.controller.ChuteInfo;
import com.zgiot.app.server.module.equipments.mapper.ThingPropertiesMapper;
import com.zgiot.app.server.module.equipments.pojo.ThingProperties;
import com.zgiot.app.server.module.equipments.service.ThingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zgiot.app.server.module.equipments.constants.EquipmentConstant.*;


@Service
public class ThingServiceImpl implements ThingService {
    @Autowired
    private ThingMapper thingMapper;
    @Autowired
    private ThingPositionMapper thingPositionMapper;

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

    @Override
    @Transactional
    public void addDevice(DeviceInfo deviceInfo) {
        Thing thing = new Thing();
        thing.setThingCode(deviceInfo.getThingCode());
        thing.setThingName(deviceInfo.getThingName());
        thing.setThingType1Code(EquipmentConstant.THING_TYPE1_CODE_DEVICE);
        thing.setThingShortName(deviceInfo.getThingShortname());
        thingMapper.addThing(thing);

        ThingPosition thingPosition = new ThingPosition();
        thingPosition.setThingCode(deviceInfo.getThingCode());
        thingPosition.setBuildingId(Long.parseLong(deviceInfo.getBuildingId()));
        thingPosition.setFloor(Integer.parseInt(deviceInfo.getFloor()));
        thingPosition.setLocationArea(deviceInfo.getLocationArea());
        thingPosition.setLocationX(Double.parseDouble(deviceInfo.getLocationX()));
        thingPosition.setLocationY(Double.parseDouble(deviceInfo.getLocationY()));
        thingPositionMapper.addThingPosition(thingPosition);
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
