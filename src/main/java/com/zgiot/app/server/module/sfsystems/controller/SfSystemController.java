package com.zgiot.app.server.module.sfsystems.controller;

import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.sfsystems.service.SfSystemService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = GlobalConstants.API  + GlobalConstants.API_VERSION + "/sfsystems")
public class SfSystemController {

    @Autowired
    SfSystemService sfSystemService;

    @RequestMapping(value = "/system/getDeviceInfoBySystemId/{systemId}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getDeviceInfoBySystemId(@PathVariable("systemId") int systemId, @PathVariable int pageNum,
                                                          @PathVariable int pageSize){
        List<DeviceInfo> deviceInfoList = sfSystemService.getDeviceInfoBySystemId(systemId, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(deviceInfoList), HttpStatus.OK);
    }

}
