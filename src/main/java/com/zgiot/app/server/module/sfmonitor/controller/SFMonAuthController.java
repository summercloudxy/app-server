package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.sfmonitor.monitorservice.MonitorService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfmonAuthz")
public class SFMonAuthController {

    @Autowired
    private MonitorService monitorService;

    @GetMapping("/byUser/{loginName}")
    public ResponseEntity<String> byUser(@PathVariable String loginName) {
        Map<String, Object> map = monitorService.byUser(loginName);
        return new ResponseEntity(ServerResponse.buildOkJson(map), HttpStatus.OK);
    }

}
