package com.zgiot.app.server.module.densitycontrol.controller;

import com.zgiot.app.server.module.densitycontrol.DensityControlManager;
import com.zgiot.app.server.module.densitycontrol.pojo.MonitoringParam;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(GlobalConstants.API_VERSION + "/densityControl")
public class DensityController {
    @Autowired
    private DensityControlManager densityControlManager;

    @GetMapping("notifyInfo")
    public ResponseEntity<String> getNotifyInfo(@RequestParam String thingCode) {
        List<MonitoringParam> notifyInfo = densityControlManager.getNotifyInfo(thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(notifyInfo), HttpStatus.OK);
    }
}
