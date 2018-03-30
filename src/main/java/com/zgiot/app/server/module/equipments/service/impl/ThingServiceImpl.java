package com.zgiot.app.server.module.equipments.service.impl;

import com.zgiot.app.server.module.equipments.constants.EquipmentConstant;
import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.controller.EquipmentInfo;
import com.zgiot.app.server.module.equipments.controller.PartsInfo;
import com.zgiot.app.server.module.equipments.mapper.RelThingtagThingMapper;
import com.zgiot.app.server.module.equipments.mapper.ThingMapper;
import com.zgiot.app.server.module.equipments.mapper.ThingPositionMapper;
import com.zgiot.app.server.module.equipments.pojo.RelThingtagThing;
import com.zgiot.app.server.module.equipments.pojo.Thing;
import com.zgiot.app.server.module.equipments.pojo.ThingPosition;
import com.zgiot.app.server.module.equipments.mapper.RelThingSystemMapper;
import com.zgiot.app.server.module.equipments.pojo.*;
import com.zgiot.app.server.module.equipments.controller.ChuteInfo;
import com.zgiot.app.server.module.equipments.mapper.ThingPropertiesMapper;
import com.zgiot.app.server.module.equipments.service.ThingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.zgiot.app.server.module.equipments.constants.EquipmentConstant.*;


@Service
public class ThingServiceImpl implements ThingService {

    @Autowired
    private ThingMapper thingMapper;
    @Autowired
    private ThingPositionMapper thingPositionMapper;
    @Autowired
    private ThingPropertiesMapper thingPropertiesMapper;
    @Autowired
    private RelThingtagThingMapper relThingtagThingMapper;
    @Autowired
    private RelThingSystemMapper relThingSystemMapper;

    @Override
    public List<DeviceInfo> getDeviceInfoByThingcode(List<String> thingCodeList) {
        return thingMapper.getDeviceInfoByThingcode(thingCodeList);
    }

    /**
     * 设备信息列表
     * @param id
     * @return
     */
    @Override
    public List<DeviceInfo> getDeviceInfoByThingTagId(Long id) {
        List<RelThingtagThing> relThingtagThingList = relThingtagThingMapper.getRelThingtagThingByThreeLevelId(id);
        List<String> thingCodeList = new ArrayList<>();
        if(relThingtagThingList != null && relThingtagThingList.size() > 0){
            for(int i=0;i<relThingtagThingList.size();i++){
                thingCodeList.add(relThingtagThingList.get(i).getThingCode());
            }
        }
        List<DeviceInfo> deviceInfoList = getDeviceInfoByThingcode(thingCodeList);
        return  deviceInfoList;
    }

    /**
     * 部件信息列表
     * @param id
     * @return
     */
    @Override
    public List<PartsInfo> getPartsInfoByThingTagId(Long id) {
        List<RelThingtagThing> relThingtagThingList = relThingtagThingMapper.getRelThingtagThingByThingTagCode(id);
        List<String> thingCodeList = new ArrayList<>();
        if(relThingtagThingList != null && relThingtagThingList.size() > 0){
            for(int i=0;i<relThingtagThingList.size();i++){
                thingCodeList.add(relThingtagThingList.get(i).getThingCode());
            }
        }
//        List<PartsInfo> partsInfoList = getPartsInfoByThingcode(thingCodeList);
//        return partsInfoList;
        return null;
    }

    @Override
    public List<Thing> getThingByCode(String thingCode, String thingType1Code) {
        thingCode = "%" + thingCode + "%";
        return thingMapper.getThingByCode(thingCode,thingType1Code);
    }

    @Override
    public boolean getThingByThingCode(String thingCode) {
        List<Thing> thingList = thingMapper.getThingByThingCode(thingCode);
        if(thingList != null && thingList.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void addDevice(DeviceInfo deviceInfo) {
        // thing
        Thing thing = new Thing();
        thing.setThingCode(deviceInfo.getThingCode());
        thing.setThingName(deviceInfo.getThingName());
        thing.setThingType1Code(EquipmentConstant.THING_TYPE1_CODE_DEVICE);
        thing.setThingShortName(deviceInfo.getThingShortname());
        thingMapper.addThing(thing);

        // thingPosition
        ThingPosition thingPosition = new ThingPosition();
        thingPosition.setThingCode(deviceInfo.getThingCode());
        thingPosition.setBuildingId(Long.parseLong(deviceInfo.getBuildingId()));
        thingPosition.setFloor(Integer.parseInt(deviceInfo.getFloor()));
        thingPosition.setLocationArea(deviceInfo.getLocationArea());
        thingPosition.setLocationX(Double.parseDouble(deviceInfo.getLocationX()));
        thingPosition.setLocationY(Double.parseDouble(deviceInfo.getLocationY()));
        thingPositionMapper.addThingPosition(thingPosition);

        // relThingtagThing
        RelThingtagThing relThingtagThing = new RelThingtagThing();
        relThingtagThing.setThingCode(deviceInfo.getThingCode());
        relThingtagThing.setThingTagCode(deviceInfo.getThingTagCode());
        relThingtagThing.setCreateDate(new Date());
        relThingtagThingMapper.addRelThingtagThing(relThingtagThing);

        // relThingSystem
        RelThingSystem relThingSystem = new RelThingSystem();
        relThingSystem.setSystemId(Long.parseLong(deviceInfo.getSystemId()));
        relThingSystem.setThingCode(deviceInfo.getThingCode());
        relThingSystem.setUpdateTime(new Date());
        relThingSystemMapper.addRelThingSystem(relThingSystem);

        // thingProperties
        ThingProperties tp = new ThingProperties();
        tp.setThingCode(deviceInfo.getThingCode());
        tp.setPropKey(EquipmentConstant.SPECIFICATION);
        tp.setPropValue(deviceInfo.getSpecification());
        tp.setPropType(EquipmentConstant.PROP_TYPE_PROP);
        thingPropertiesMapper.addThingProperties(tp);

        tp.setPropKey(EquipmentConstant.POWER_PROPERTIES);
        tp.setPropValue(deviceInfo.getPowerProperties());
        thingPropertiesMapper.addThingProperties(tp);

        tp.setPropKey(EquipmentConstant.START_DATE);
        tp.setPropValue(deviceInfo.getEnableDate());
        thingPropertiesMapper.addThingProperties(tp);

        tp.setPropKey(EquipmentConstant.STOP_DATE);
        tp.setPropValue(deviceInfo.getDisableDate());
        thingPropertiesMapper.addThingProperties(tp);

        tp.setPropKey(EquipmentConstant.ANGLE);
        tp.setPropValue(deviceInfo.getAngle());
        thingPropertiesMapper.addThingProperties(tp);

        tp.setPropKey(EquipmentConstant.GRANULARITY);
        tp.setPropValue(deviceInfo.getGranularity());
        thingPropertiesMapper.addThingProperties(tp);

        tp.setPropKey(EquipmentConstant.IMAGE_NAME);
        tp.setPropValue(deviceInfo.getImageName());
        tp.setPropType(EquipmentConstant.PROP_TYPE_DISP_PROP);
        thingPropertiesMapper.addThingProperties(tp);

    }

    @Override
    @Transactional
    public void deleteDevice(Long id) {
        Thing thing = thingMapper.getThingById(id);
        String thingCode = thing.getThingCode();
        // thing
        thingMapper.deleteThingById(id);

        // thingPosition
        thingPositionMapper.deleteThingPosition(thingCode);

        // relThingtagThing
        relThingtagThingMapper.deleteRelThingtagThingByThingCode(thingCode);

        // relThingSystem
        relThingSystemMapper.deleteRelThingSystemByThingCode(thingCode);

        // thingProperties
        thingPropertiesMapper.deleteThingPropertiesByThingCode(thingCode);

    }

    @Override
    public void editDevice(DeviceInfo deviceInfo) {
        deleteDevice(deviceInfo.getId());
        addDevice(deviceInfo);
    }

    @Transactional
    public void addChute(ChuteInfo chuteInfo) {

        String thingCode = "调用wang磊的方法";
        //插入tb_thing表
        Thing thing = new Thing();
        thing.setThingCode(thingCode);
        thing.setThingName(chuteInfo.getChuteName());
        thing.setThingType1Code(THING_TYPE1_CODE_CHUTE);
        thingMapper.addThing(thing);

        //插入tb_thing_properties表
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

        //插入rel_thing_system表
        RelThingSystem rts = new RelThingSystem();
        rts.setThingCode(thingCode);
        rts.setSystemId(chuteInfo.getThingSystemId());
        relThingSystemMapper.addRelThingSystem(rts);

    }

    @Transactional
    public void delChute(Long id){
        Thing t = thingMapper.getThingById(id);
        String thingCode = t.getThingCode();
        thingMapper.deleteThingById(id);
        thingPropertiesMapper.deleteThingPropertiesByThingCode(thingCode);
        relThingSystemMapper.deleteRelThingSystemByThingCode(thingCode);
    }

    @Transactional
    public void editChute(ChuteInfo chuteInfo){
        Long id = chuteInfo.getId();
        delChute(id);
        addChute(chuteInfo);
    }

    @Override
    @Transactional
    public void addParts(PartsInfo partsInfo) {
        // thing
        Thing thing = new Thing();
        String thingCode = getThingCodeByInfo(partsInfo.getParentThingCode(), partsInfo.getThingType(),
                EquipmentConstant.THING_TYPE1_CODE_PARTS);
        thing.setThingCode(thingCode);
        thing.setThingName(partsInfo.getThingName());
        thing.setThingType1Code(EquipmentConstant.THING_TYPE1_CODE_PARTS);
        thingMapper.addThing(thing);

        //

    }

    /**
     * 获取该设备的thingCode
     * @param parentThingCode 701
     * @param thingType .BJ.BS-
     * @param thingType1CodeParts PARTS
     * @return
     */
    private String getThingCodeByInfo(String parentThingCode, String thingType, String thingType1CodeParts) {
        String thingCode = "";
        String likeThingCode = parentThingCode + thingType + "%";
        List<Thing> thingList = thingMapper.getThingByType(likeThingCode, thingType1CodeParts);
        if(thingList != null && thingList.size() > 0){
            Collections.sort(thingList, new Comparator<Thing>() {
                @Override
                public int compare(Thing o1, Thing o2) {
                    Integer code1 = Integer.parseInt(o1.getThingCode().substring(o1.getThingCode().lastIndexOf("-") + 1));
                    Integer code2 = Integer.parseInt(o2.getThingCode().substring(o2.getThingCode().lastIndexOf("-") + 1));
                    return code2 - code1;
                }
            });

            Thing thing = thingList.get(0);
            thingCode = new Integer(Integer.parseInt(thing.getThingCode()
                    .substring(thing.getThingCode().lastIndexOf("-") + 1)) + 1).toString();
        }else{
            thingCode = parentThingCode + thingType + 1;
        }
        return thingCode;
    }

}
