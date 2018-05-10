package com.zgiot.app.server.module.sfsystems.controller;

import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.equipments.pojo.TBSystem;
import com.zgiot.app.server.module.sfsystems.service.SfSystemService;
import com.zgiot.app.server.module.equipments.pojo.RelThingSystem;
import com.zgiot.app.server.module.equipments.service.RelThingSystemService;
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

@Controller
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfsystems")
public class SfSystemController {

    @Autowired
    SfSystemService sfSystemService;
    @Autowired
    private RelThingSystemService relThingSystemService;

    /**
     * 根据系统id查找对应设备
     *
     * @param systemId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getDeviceInfoBySystemId/{systemId}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getDeviceInfoBySystemId(@PathVariable("systemId") Long systemId, @PathVariable int pageNum,
                                                          @PathVariable int pageSize) {
        PageHelpInfo pageHelpInfo = sfSystemService.getDeviceInfoBySystemId(systemId, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 根据区域id查找对应设备
     *
     * @param areaId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getDeviceInfoByAreaId/{areaId}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getDeviceInfoByAreaId(@PathVariable("areaId") Long areaId, @PathVariable int pageNum,
                                                        @PathVariable int pageSize) {
        PageHelpInfo pageHelpInfo = sfSystemService.getDeviceInfoByAreaId(areaId, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 根据thingCode查询该设备信息
     *
     * @param thingCode
     * @return
     */
    @RequestMapping(value = "/getDeviceInfoByThingCode/{thingCode}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getDeviceInfoByThingCode(@PathVariable("thingCode") String thingCode) {
        List<DeviceInfo> deviceInfoList = sfSystemService.getDeviceInfoByThingCode(thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(deviceInfoList), HttpStatus.OK);
    }

    /**
     * 添加系统设备
     *
     * @param relThingSystem
     * @return
     */
    @RequestMapping(value = "/relThingSystem/add", method = RequestMethod.POST)
    public ResponseEntity<String> addBuilding(@RequestBody RelThingSystem relThingSystem) {
        relThingSystemService.addRelThingSystem(relThingSystem);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 系统设备删除
     *
     * @param thingCode
     * @return
     */
    @RequestMapping(value = "/relThingSystem/delete/{thingCode}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delBuilding(@PathVariable("thingCode") String thingCode) {
        relThingSystemService.deleteRelThingSystemByThingCode(thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 根据设备编号和所属区域查询没有所属系统的设备
     *
     * @param thingCode
     * @return
     */
    @RequestMapping(value = "/getFreeDeviceInfo/thingCode/{thingCode}/areaId/{areaId}", method = RequestMethod.GET)
    public ResponseEntity<String> getFreeDeviceInfo(@PathVariable("thingCode") String thingCode, @PathVariable("areaId") Long areaId) {
        List<DeviceInfo> deviceInfoList = sfSystemService.getFreeDeviceInfo(thingCode, areaId);
        return new ResponseEntity<>(ServerResponse.buildOkJson(deviceInfoList), HttpStatus.OK);
    }

    /**
     * 菜单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getMenu/id/{id}/level/{level}", method = RequestMethod.GET)
    public ResponseEntity<String> getMenu(@PathVariable("id") Long id, @PathVariable("level") int level) {
        TBSystem tbSystem = sfSystemService.getMenu(id, level);
        return new ResponseEntity<>(ServerResponse.buildOkJson(tbSystem), HttpStatus.OK);
    }

}
