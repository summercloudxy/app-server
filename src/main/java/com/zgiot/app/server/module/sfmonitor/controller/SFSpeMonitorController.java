package com.zgiot.app.server.module.sfmonitor.controller;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.sfmonitor.monitorservice.SFSpeMonitorService;
import com.zgiot.app.server.module.sfmonitor.monitorservice.SFSysMonitorService;
import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;
import com.zgiot.app.server.module.util.validate.ControllerUtil;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = GlobalConstants.API  + GlobalConstants.API_VERSION + "/sfspemonitor")
public class SFSpeMonitorController {

    @Autowired
    private SFSpeMonitorService sfSpeMonitorService;
    /**
     * 获取专题监控首页数据
     */
    @GetMapping("/getSpecialMonitor")
    public ResponseEntity<String> getSpecialMonitor() {
        List<ThingTag> thingTagList =sfSpeMonitorService.getSpeMonitorIndex();
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingTagList), HttpStatus.OK);
    }

    /**
     * 获取专题监控二级系统详情页面(有关联系统的)
     */
    @PostMapping("/getSpecialMonitorDetailHaveSub")
    public ResponseEntity<String> getSpecialMonitorDetailHaveSub(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        SFSpeMonitorReq sFSpeMonitorReq = JSON.parseObject(bodyStr, SFSpeMonitorReq.class);
        SystemMonitorDetailInfo systemMonitorDetailInfo =sfSpeMonitorService.getSpecialMonitorDetailHaveSub(sFSpeMonitorReq);
        return new ResponseEntity<>(ServerResponse.buildOkJson(systemMonitorDetailInfo), HttpStatus.OK);
    }

    /**
     * 获取专题监控二级系统详情页面(通用)
     */
    @PostMapping("/getSpecialMonitorDetailCommon")
    public ResponseEntity<String> getSpecialMonitorDetailCommon(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        SFSpeMonitorReq sFSpeMonitorReq = JSON.parseObject(bodyStr, SFSpeMonitorReq.class);
        SystemMonitorDetailInfo systemMonitorDetailInfo =sfSpeMonitorService.getSpecialMonitorDetailCommon(sFSpeMonitorReq);
        return new ResponseEntity<>(ServerResponse.buildOkJson(systemMonitorDetailInfo), HttpStatus.OK);
    }

    /**
     * 获取专题监控设备保护详情页面
     */
    @PostMapping("/getSpecialMonitorDetailProtect")
    public ResponseEntity<String> getSpecialMonitorDetailProtect(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        SFSpeMonitorReq sFSpeMonitorReq = JSON.parseObject(bodyStr, SFSpeMonitorReq.class);
        SystemMonitorDetailInfo systemMonitorDetailInfo =sfSpeMonitorService.getSpecialMonitorDetailProtect(sFSpeMonitorReq);
        return new ResponseEntity<>(ServerResponse.buildOkJson(systemMonitorDetailInfo), HttpStatus.OK);
    }

    /**
     * 获取专题监控三级目录页
     */
    @GetMapping("/getThingTagListThree")
    public ResponseEntity<String> getThingTagListThree(@RequestParam String thingTagId){
        List<ThingTag> thingTagList = sfSpeMonitorService.getThingTagListThree(thingTagId);
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingTagList), HttpStatus.OK);
    }

    /**
     * 获取专题监控页面查询的设备编号
     */
    @PostMapping("/getSpecialMonitorThingCode")
    public ResponseEntity<String> getSpecialMonitorThingCode(@RequestBody String bodyStr){
        ControllerUtil.validateBodyRequired(bodyStr);
        SFSpeMonitorReq sFSpeMonitorReq = JSON.parseObject(bodyStr, SFSpeMonitorReq.class);
        List<String> thingCodeList = sfSpeMonitorService.getThingCodeList(sFSpeMonitorReq);
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingCodeList), HttpStatus.OK);
    }

    /**
     * 获取专题监控页面查询的信号
     */
    @PostMapping("/getSpecialMonitorMetric")
    public ResponseEntity<String> getSpecialMonitorMetric(@RequestBody String bodyStr){
        ControllerUtil.validateBodyRequired(bodyStr);
        SFSpeMonitorReq sFSpeMonitorReq = JSON.parseObject(bodyStr, SFSpeMonitorReq.class);
        List<Map<String,String>> metricList = sfSpeMonitorService.getMetricList(sFSpeMonitorReq);
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricList), HttpStatus.OK);
    }
}
