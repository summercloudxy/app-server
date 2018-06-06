package com.zgiot.app.server.module.densitycontrol.controller;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.densitycontrol.DensityControlManager;
import com.zgiot.app.server.module.densitycontrol.pojo.DensityControlConfig;
import com.zgiot.app.server.module.densitycontrol.pojo.MonitoringParam;
import com.zgiot.app.server.module.densitycontrol.service.DensityControlService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(GlobalConstants.API_VERSION + "/densityControl")
public class DensityController {
    @Autowired
    private DensityControlManager densityControlManager;
    @Autowired
    private DensityControlService densityControlService;

    @GetMapping("notifyInfo")
    public ResponseEntity<String> getNotifyInfo(@RequestParam String thingCode) {
        List<MonitoringParam> notifyInfo = densityControlManager.getNotifyInfo(thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(notifyInfo), HttpStatus.OK);
    }

    /**
     * 获取所有设定参数
     *
     * @return
     */
    @GetMapping("/getAllDensityControlConfig")
    public ResponseEntity<String> getAllDensityControlConfig() {
        List<DensityControlConfig> densityControlConfigList = densityControlService.getAllDensityControlConfig();
        return new ResponseEntity<>(ServerResponse.buildOkJson(densityControlConfigList), HttpStatus.OK);
    }

    /**
     * 修改设定参数
     *
     * @param bodyStr
     * @return
     */
    @PostMapping("/updateDensityControl")
    public ResponseEntity<String> updateDensityControlConfig(@RequestBody String bodyStr) {
        List<DensityControlConfig> densityControlConfigList = JSON.parseArray(bodyStr, DensityControlConfig.class);
        densityControlService.updateDensityControlConfig(densityControlConfigList);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 智能开关
     *
     * @param reqBody
     * @return
     */
    @PostMapping("/switchDensityControl")
    public ResponseEntity<String> switchDensityControl(@RequestBody String reqBody) {
        DataModel dataModel = JSON.parseObject(reqBody.trim(), DataModel.class);
        densityControlService.switchDensityControl(dataModel);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 目标密度设定
     *
     * @param reqBody
     * @return
     */
    @PostMapping("/setTargetDensity")
    public ResponseEntity<String> setTargetDensity(@RequestBody String reqBody) {
        DataModel dataModel = JSON.parseObject(reqBody.trim(), DataModel.class);
        densityControlService.setTargetDensity(dataModel);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

}
