package com.zgiot.app.server.module.alert.controller;

import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.AlertMessage;
import com.zgiot.app.server.module.alert.pojo.AlertRecord;
import com.zgiot.common.constants.GlobalConstants;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by xiayun on 2017/9/26.
 */
@RestController
public class AlertController {
    @Autowired
    private AlertManager alertManager;


    @PostMapping(value = "api/alert/rule/param")
    public void updateParamRule() {
        alertManager.updateParamRuleMap();
    }

    @PostMapping(value = "api/alert/rule/protect")
    public void updateProtectRule(){
        alertManager.updateProtectRuleMap();
    }


    @ApiOperation("下发报警指令")
    @PostMapping(value = "api/alert/cmd")
    public void sendAlertCmd(@RequestParam String thingCode, @RequestParam String metricCode, @RequestBody AlertMessage alertMessage,
                             HttpServletRequest request) throws Exception{
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        alertManager.sendAlertCmd(thingCode, metricCode, alertMessage, requestId);
    }

    @ApiOperation("获取报警记录")
    @GetMapping(value = "api/alert/record")
    public List<AlertRecord> getAlertRecord(String stage, List<Integer> levels, List<Short> types,
                               List<Integer> buildingIds, List<Integer> floors, List<Integer> systems, Integer sortType, Long duration){
        return alertManager.getAlertDataList(stage, levels, types, buildingIds, floors, systems, sortType, duration);
    }

    @ApiOperation("人为生成报警")
    @PostMapping(value = "api/alert/generate")
    public void generateManuAlert(String thingCode, String info, String userId, String permission){
        alertManager.generateManuAlert(thingCode, info, userId, permission);
    }

    public void uploadImage(String thingCode, String metricCode, List<MultipartFile> files){

    }




}
