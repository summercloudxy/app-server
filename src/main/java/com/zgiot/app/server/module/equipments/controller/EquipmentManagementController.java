package com.zgiot.app.server.module.equipments.controller;

import com.zgiot.app.server.module.equipments.pojo.*;
import com.zgiot.app.server.module.equipments.service.*;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static com.zgiot.app.server.module.equipments.constants.EquipmentConstant.THING_TYPE1_CODE_DEVICE;

@Controller
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/equipments")
public class EquipmentManagementController {

    @Autowired
    private BuildingService buildingService;
    @Autowired
    private ThingTagService thingTagService;
    @Autowired
    private ThingBaseDictService thingBaseDictService;
    @Autowired
    private RelThingtagThingService relThingtagThingService;
    @Autowired
    private ThingService thingService;

    @Autowired
    private TBSystemService tbSystemService;

    @RequestMapping(value = "/building/list/pageNum/{pageNum}/pageSize/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity<String> getBuildingList(@PathVariable int pageNum, @PathVariable int pageSize) {
        List<Building> buildingList = buildingService.getBuildingAll(pageNum,pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(buildingList), HttpStatus.OK);
    }

    @RequestMapping(value = "/building/add", method = RequestMethod.POST)
    public ResponseEntity<String> addBuilding(@RequestBody Building building) {
        buildingService.addBuilding(building);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/building/edit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> editBuilding(@RequestBody Building building, @PathVariable("id") Long id) {
        building.setId(id);
        buildingService.editBuilding(building);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/building/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delBuilding(@PathVariable("id") Long id) {
        buildingService.deleteBuilding(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/building/find/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> findBuilding(@PathVariable("id") Long id) {
        Building building = buildingService.getBuildingById(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(building), HttpStatus.OK);
    }

    /**
     * 获取所有装备类型
     * @return
     */
    @RequestMapping(value = "/thingTag/getAllEquipmentType", method = RequestMethod.GET)
    public ResponseEntity<String> getAllEquipmentType() {
        List<ThingTag> thingTagList = thingTagService.getAllEquipmentType();
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingTagList), HttpStatus.OK);
    }

    /**
     * 根据key查找基础数据字典
     * @param key
     * @return
     */
    @RequestMapping(value = "/thingBaseDict/getThingBaseDictListByKey/{key}", method = RequestMethod.GET)
    public ResponseEntity<String> getThingBaseDictListByKey(@PathVariable("key") String key){
        List<ThingBaseDict> thingBaseDictList = thingBaseDictService.getThingBaseDictListByKey(key);
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingBaseDictList), HttpStatus.OK);
    }

    /**
     * 根据当前节点id获取对应设备thing_code
     * @param id
     * @return
     */
    @RequestMapping(value = "/relThingtagThing/relThingtagThingService/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getRelThingtagThingByThreeLevelId(@PathVariable("id") Long id){
        List<RelThingtagThing> relThingtagThingList = relThingtagThingService.getRelThingtagThingByThreeLevelId(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(relThingtagThingList), HttpStatus.OK);
    }

    /*3.29 begin*/
    /**
     * 下拉框根据thingCode模糊搜索设备
     * @param thingCode
     * @return 集合
     */
    @RequestMapping(value = "/thing/thingCode/{thingCode}", method = RequestMethod.GET)
    public ResponseEntity<String> getThingByCode(@PathVariable("thingCode") String thingCode) {
        List<Thing> thingList = thingService.getThingByCode(thingCode, THING_TYPE1_CODE_DEVICE);
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingList), HttpStatus.OK);
    }

    /**
     * 下拉框-所属系统
     * @param sysLevel,系统层级，分为1,2,3,4
     * @return 集合
     */
    @RequestMapping(value = "/thing/sysLevel/{sysLevel}", method = RequestMethod.GET)
    public ResponseEntity<String> getSysByLevel(@PathVariable("sysLevel") int sysLevel) {
        List<TBSystem> tbSystemList = tbSystemService.getTBSystemALL(sysLevel);
        return new ResponseEntity<>(ServerResponse.buildOkJson(tbSystemList), HttpStatus.OK);
    }
    /*3.29 end*/

    @RequestMapping(value = "/device/add", method = RequestMethod.POST)
    public ResponseEntity<String> addDevice(@RequestBody DeviceInfo deviceInfo) {
        thingService.addDevice(deviceInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 是否有此thingCode
     * @param thingCode
     * @return
     */
    @RequestMapping(value = "/thing/isHasThingCode/{thingCode}", method = RequestMethod.GET)
    public ResponseEntity<String> isHasThingCode(@PathVariable("thingCode") String thingCode) {
        boolean flag = thingService.getThingByThingCode(thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(flag), HttpStatus.OK);
    }

}
