package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;
import com.zgiot.app.server.module.sfmonitor.monitorservice.SFSysMonitorService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping(value = GlobalConstants.API  + GlobalConstants.API_VERSION + "/sfsysmonitor")
public class SFSysMonitorController {

    @Autowired
    private SFSysMonitorService sfSysMonitorService;
    /**
     * 获取系统监控首页数据
     */
    @GetMapping("/getSystemMonitor")
    public ResponseEntity<String> getSystemMonitor() {
        List<ThingTag> thingTagList =sfSysMonitorService.getSysMonitorIndex();
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingTagList), HttpStatus.OK);
    }

    /**
     * 获取系统监控系统详情页数据
     */
    @GetMapping("/getSystemMonitorDetail")
    public ResponseEntity<String> getSystemMonitorDetail(@RequestParam String thingTagId1,@RequestParam(required = false)  String thingTagId2,@RequestParam(required = false) String term) {
        SystemMonitorDetailInfo systemMonitorDetailInfo = sfSysMonitorService.getSystemMonitorDetail(thingTagId1,thingTagId2,term);
        return new ResponseEntity<>(ServerResponse.buildOkJson(systemMonitorDetailInfo), HttpStatus.OK);
    }

}
