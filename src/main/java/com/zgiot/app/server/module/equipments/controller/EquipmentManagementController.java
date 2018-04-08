package com.zgiot.app.server.module.equipments.controller;

import com.zgiot.app.server.module.equipments.pojo.*;
import com.zgiot.app.server.module.equipments.service.*;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.zgiot.app.server.module.equipments.constants.EquipmentConstant.THING_TYPE1_CODE_DEVICE;

@Controller
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/equipments")
public class EquipmentManagementController {

    @Autowired
    private BuildingService buildingService;
    @Autowired
    private ThingTagManagementService thingTagService;
    @Autowired
    private ThingBaseDictService thingBaseDictService;
    @Autowired
    private ThingManagementService thingService;
    @Autowired
    private TBSystemService tbSystemService;
    @Autowired
    private CoalStorageDepotService coalStorageDepotService;

    /**
     * 生产车间列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/building/list/pageNum/{pageNum}/pageSize/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity<String> getBuildingList(@PathVariable int pageNum, @PathVariable int pageSize) {

        PageHelpInfo pageHelpInfo = buildingService.getBuildingAll(pageNum,pageSize);

        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 生产车间添加
     * @param building
     * @return
     */
    @RequestMapping(value = "/building/add", method = RequestMethod.POST)
    public ResponseEntity<String> addBuilding(@RequestBody Building building) {
        buildingService.addBuilding(building);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 生产车间编辑
     * @param building
     * @return
     */
    @RequestMapping(value = "/building/edit", method = RequestMethod.PUT)
    public ResponseEntity<String> editBuilding(@RequestBody Building building) {
        buildingService.editBuilding(building);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 生产车间删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/building/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delBuilding(@PathVariable("id") Long id) {
        buildingService.deleteBuilding(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 生产车间-获取指定生产车间
     * @param id
     * @return
     */
    @RequestMapping(value = "/building/find/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> findBuilding(@PathVariable("id") Long id) {
        Building building = buildingService.getBuildingById(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(building), HttpStatus.OK);
    }

    /**
     * 生产车间-查询
     * @param buildingName
     * @param pageNum
     * @param pageSize
     * @return
     */

    @RequestMapping(value = "/building/query/{buildingName}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> queryBuildingByBuildingName(@PathVariable String buildingName,
                                                              @PathVariable int pageNum, @PathVariable int pageSize) {
        PageHelpInfo pageHelpInfo = buildingService.getBuildingByBuildingName(pageNum,pageSize,buildingName);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 获取所有设备类型
     * @return
     */
    @RequestMapping(value = "/thingTag/getAllEquipmentType", method = RequestMethod.GET)
    public ResponseEntity<String> getAllEquipmentType() {
        List<ThingTag> thingTagList = thingTagService.getAllEquipmentType();
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingTagList), HttpStatus.OK);
    }

    /**
     * 根据父节点获取其所有子节点
     * @param id
     * @return
     */
    @RequestMapping(value = "/thingTag/getThingTagByParentId/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getThingTagByParentId(@PathVariable("id") Long id){
        List<ThingTag> thingTagList = thingTagService.getThingTagByParentId(id);
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
     * 所有设备信息列表
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/thing/getAllDeviceInfo/{id}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getAllDeviceInfo(@PathVariable("id") Long id, @PathVariable int pageNum,
                                                            @PathVariable int pageSize){
        PageHelpInfo pageHelpInfo = thingService.getAllDeviceInfo(id, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 根据当前节点id获取设备信息列表
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/thing/getDeviceInfoByThingTagId/{id}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getDeviceInfoByThingTagId(@PathVariable("id") Long id, @PathVariable int pageNum,
                                                            @PathVariable int pageSize){
        PageHelpInfo pageHelpInfo = thingService.getDeviceInfoByThingTagId(id, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 根据当前节点id获取部件信息列表
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/thing/getPartsInfoByThingTagId/{id}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getPartsInfoByThingTagId(@PathVariable("id") Long id, @PathVariable int pageNum,
                                                           @PathVariable int pageSize){
        PageHelpInfo pageHelpInfo = thingService.getPartsInfoByThingTagId(id, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
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

    /**
     * 添加设备
     * @param deviceInfo
     * @return
     */
    @RequestMapping(value = "/device/add", method = RequestMethod.POST)
    public ResponseEntity<String> addDevice(@RequestBody DeviceInfo deviceInfo) {
        thingService.addDevice(deviceInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 删除设备
     * @param id
     * @return
     */
    @RequestMapping(value = "/device/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delDevice(@PathVariable("id") Long id) {
        thingService.deleteDevice(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 编辑设备
     * @param deviceInfo
     * @return
     */
    @RequestMapping(value = "/device/edit", method = RequestMethod.PUT)
    public ResponseEntity<String> editDevice(@RequestBody DeviceInfo deviceInfo) {
        thingService.editDevice(deviceInfo);
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

    /**
     * 添加溜槽
     * @param chuteInfo
     * @return
     */
    @RequestMapping(value = "/chute/add", method = RequestMethod.POST)
    public ResponseEntity<String> addChute(@RequestBody ChuteInfo chuteInfo) {
        thingService.addChute(chuteInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 编辑溜槽
     * @param chuteInfo
     * @return
     */
    @RequestMapping(value = "/chute/edit", method = RequestMethod.PUT)
    public ResponseEntity<String> editChute(@RequestBody ChuteInfo chuteInfo) {
        thingService.editChute(chuteInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 删除溜槽
     * @param id
     * @return
     */
    @RequestMapping(value = "/chute/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delChute(@PathVariable("id") Long id) {
        thingService.delChuteOrPipe(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /*3.30 begin*/

    /**
     * 添加管道
     * @param pipeInfo
     * @return
     */
    @RequestMapping(value = "/pipe/add", method = RequestMethod.POST)
    public ResponseEntity<String> addChute(@RequestBody PipeInfo pipeInfo) {
        thingService.addPipe(pipeInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 编辑管道
     * @param pipeInfo
     * @return
     */
    @RequestMapping(value = "/pipe/edit", method = RequestMethod.PUT)
    public ResponseEntity<String> editChute(@RequestBody PipeInfo pipeInfo) {
        thingService.editPipe(pipeInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 删除管道
     * @param id
     * @return
     */
    @RequestMapping(value = "/pipe/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delPipe(@PathVariable("id") Long id) {
        thingService.delChuteOrPipe(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }


    /**
     * 储煤仓列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/coalStorageDepot/list/pageNum/{pageNum}/pageSize/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity<String> getCoalStorageDepotList(@PathVariable int pageNum, @PathVariable int pageSize) {

        PageHelpInfo pageHelpInfo = coalStorageDepotService.getCoalStorageDepotAll(pageNum,pageSize);

        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 储煤仓添加
     * @param coalStorageDepot
     * @return
     */
    @RequestMapping(value = "/coalStorageDepot/add", method = RequestMethod.POST)
    public ResponseEntity<String> addCoalStorageDepot(@RequestBody CoalStorageDepot coalStorageDepot) {
        coalStorageDepotService.addCoalStorageDepot(coalStorageDepot);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 储煤仓编辑
     * @param coalStorageDepot
     * @return
     */
    @RequestMapping(value = "/coalStorageDepot/edit", method = RequestMethod.PUT)
    public ResponseEntity<String> editCoalStorageDepot(@RequestBody CoalStorageDepot coalStorageDepot) {
        coalStorageDepotService.editCoalStorageDepot(coalStorageDepot);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 储煤仓删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/coalStorageDepot/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delCoalStorageDepot(@PathVariable("id") Long id) {
        coalStorageDepotService.deleteCoalStorageDepot(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 储煤仓-获取指定储煤仓
     * @param id
     * @return
     */
    @RequestMapping(value = "/coalStorageDepot/find/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> findCoalStorageDepot(@PathVariable("id") Long id) {
        CoalStorageDepot coalStorageDepot = coalStorageDepotService.getCoalStorageDepotById(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(coalStorageDepot), HttpStatus.OK);
    }
    /*3.30 end*/
    /*4.2 begin*/
    /**
     * 储煤仓-查询
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */

    @RequestMapping(value = "/coalStorageDepot/query/{name}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> querycoalStorageDepotByName(@PathVariable String name,
                                                              @PathVariable int pageNum, @PathVariable int pageSize) {
        PageHelpInfo pageHelpInfo = coalStorageDepotService.getCoalStorageDepotByName(pageNum,pageSize,name);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }
    /*4.2 end*/
    /**
     * 添加部件
     * @param partsInfo
     * @return
     */
    @RequestMapping(value = "/parts/add", method = RequestMethod.POST)
    public ResponseEntity<String> addParts(@RequestBody PartsInfo partsInfo) {
        thingService.addParts(partsInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 删除部件
     * @param id
     * @return
     */
    @RequestMapping(value = "/parts/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteParts(@PathVariable("id") Long id) {
        thingService.deleteParts(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 编辑部件
     * @param partsInfo
     * @return
     */
    @RequestMapping(value = "/parts/edit", method = RequestMethod.PUT)
    public ResponseEntity<String> editParts(@RequestBody PartsInfo partsInfo) {
        thingService.editParts(partsInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 根据当前节点id获取管道信息列表
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/thing/getPipeInfoByThingTagId/{id}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getPipeInfoByThingTagId(@PathVariable("id") Long id, @PathVariable int pageNum,
                                                          @PathVariable int pageSize){
        PageHelpInfo pageHelpInfo = thingService.getPipeInfoByThingTagId(id, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 添加阀门
     * @param valveInfo
     * @return
     */
    @RequestMapping(value = "/valve/add", method = RequestMethod.POST)
    public ResponseEntity<String> addValve(@RequestBody ValveInfo valveInfo) {
        thingService.addValve(valveInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 删除阀门
     * @param id
     * @return
     */
    @RequestMapping(value = "/valve/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteValve(@PathVariable("id") Long id) {
        thingService.delFlashboardOrValve(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 编辑阀门
     * @param valveInfo
     * @return
     */
    @RequestMapping(value = "/valve/edit", method = RequestMethod.PUT)
    public ResponseEntity<String> editValve(@RequestBody ValveInfo valveInfo) {
        thingService.editValve(valveInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 添加闸板
     * @param flashboardInfo
     * @return
     */
    @RequestMapping(value = "/flashboard/add", method = RequestMethod.POST)
    public ResponseEntity<String> addflashboard(@RequestBody FlashboardInfo flashboardInfo) {
        thingService.addFlashboard(flashboardInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 删除闸板
     * @param id
     * @return
     */
    @RequestMapping(value = "/flashboard/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteflashboard(@PathVariable("id") Long id) {
        thingService.delFlashboardOrValve(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 编辑闸板
     * @param flashboardInfo
     * @return
     */
    @RequestMapping(value = "/flashboard/edit", method = RequestMethod.PUT)
    public ResponseEntity<String> editflashboard(@RequestBody FlashboardInfo flashboardInfo) {
        thingService.editFlashboard(flashboardInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 根据当前节点id获取溜槽信息列表
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/thing/getChuteInfoByThingTagId/{id}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getChuteInfoByThingTagId(@PathVariable("id") Long id, @PathVariable int pageNum,
                                                          @PathVariable int pageSize){
        PageHelpInfo pageHelpInfo = thingService.getChuteInfoByThingTagId(id, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 根据当前节点id获取阀门信息列表
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/thing/getValveInfoByThingTagId/{id}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getValveInfoByThingTagId(@PathVariable("id") Long id, @PathVariable int pageNum,
                                                           @PathVariable int pageSize){
        PageHelpInfo pageHelpInfo = thingService.getValveInfoByThingTagId(id, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 根据当前节点id获取闸板信息列表
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/thing/getFlashboardInfoByThingTagId/{id}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getFlashboardInfoByThingTagId(@PathVariable("id") Long id, @PathVariable int pageNum,
                                                           @PathVariable int pageSize){
        PageHelpInfo pageHelpInfo = thingService.getFlashboardInfoByThingTagId(id, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 添加仪表
     * @param meterInfo
     * @return
     */
    @RequestMapping(value = "/meter/add", method = RequestMethod.POST)
    public ResponseEntity<String> addMeter(@RequestBody MeterInfo meterInfo) {
        thingService.addMeter(meterInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 删除仪表
     * @param id
     * @return
     */
    @RequestMapping(value = "/meter/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMeter(@PathVariable("id") Long id) {
        thingService.deleteMeter(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 编辑仪表
     * @param meterInfo
     * @return
     */
    @RequestMapping(value = "/meter/edit", method = RequestMethod.PUT)
    public ResponseEntity<String> editMeter(@RequestBody MeterInfo meterInfo) {
        thingService.editMeter(meterInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 电气设备列表
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/thing/getAllMeterInfo/{id}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getAllMeterInfo(@PathVariable("id") Long id, @PathVariable int pageNum,
                                                                @PathVariable int pageSize){
        PageHelpInfo pageHelpInfo = thingService.getAllMeterInfo(id, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 根据当前节点id获取电气设备列表
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/thing/getMeterInfoByThingTagId/{id}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getMeterInfoByThingTagId(@PathVariable("id") Long id, @PathVariable int pageNum,
                                                            @PathVariable int pageSize){
        PageHelpInfo pageHelpInfo = thingService.getMeterInfoByThingTagId(id, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 根据父节点获取系统表信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/system/getSystemByParentId/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getSystemByParentId(@PathVariable("id") Long id){
        List<TBSystem> tbSystemList = tbSystemService.getSystemByParentId(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(tbSystemList), HttpStatus.OK);
    }

    /**
     * 根据地区id获取车间信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/building/getBuildingByAreaId/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getBuildingByAreaId(@PathVariable("id") Long id){
        List<Building> buildingList = buildingService.getBuildingByAreaId(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(buildingList), HttpStatus.OK);
    }

    /**
     * 根据设备code获取部件信息
     * @param thingCode
     * @return
     */
    @RequestMapping(value = "/thing/getPartsInfoByThingCode", method = RequestMethod.GET)
    public ResponseEntity<String> getPartsInfoByThingId(@PathVariable("thingCode") String thingCode){
        List<PartsInfo> partsInfoList = thingService.getPartsInfoByThingId(thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(partsInfoList), HttpStatus.OK);
    }

    /**
     * 菜单
     * @param id
     * @return
     */
    @RequestMapping(value = "/thingTag/getMenu/thingTagId/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getMenu(@PathVariable("id") Long id){
        ThingTag thingTag = thingTagService.getMenu(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingTag), HttpStatus.OK);
    }

    /**
     * 根据设备编号或设备名称查询设备
     * @param codeOrName
     * @param type
     * @return
     */
    @RequestMapping(value = "/thing/codeOrName/{codeOrName}/type/{type}", method = RequestMethod.GET)
    public ResponseEntity<String> getThingByCodeOrName(@PathVariable("codeOrName") String codeOrName,
                                                       @PathVariable("type") String type) {
        List<Thing> thingList = thingService.getThingByCodeOrName(codeOrName, type);
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingList), HttpStatus.OK);
    }

    @RequestMapping(value = "/thing/thingCode/{thingCode}/type/{type}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getThingInfoByThingCode(
            @PathVariable("thingCode") String thingCode, @PathVariable("type") String type,
            @PathVariable int pageNum, @PathVariable int pageSize) {
        PageHelpInfo pageHelpInfo = thingService.getThingInfoByThingCode(thingCode, type, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

}
