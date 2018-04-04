package com.zgiot.app.server.module.sfsystems.controller;

import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
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
@RequestMapping(value = GlobalConstants.API  + GlobalConstants.API_VERSION + "/sfsystems")
public class SfSystemController {

    @Autowired
    SfSystemService sfSystemService;
    @Autowired
    private RelThingSystemService relThingSystemService;

    @RequestMapping(value = "/getDeviceInfoBySystemId/{systemId}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getDeviceInfoBySystemId(@PathVariable("systemId") int systemId, @PathVariable int pageNum,
                                                          @PathVariable int pageSize) {
        PageHelpInfo pageHelpInfo = sfSystemService.getDeviceInfoBySystemId(systemId, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 添加系统设备
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
     * @param thingCode
     * @return
     */
    @RequestMapping(value = "/relThingSystem/delete/{thingCode}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delBuilding(@PathVariable("thingCode") String thingCode) {
        relThingSystemService.deleteRelThingSystemByThingCode(thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

}
