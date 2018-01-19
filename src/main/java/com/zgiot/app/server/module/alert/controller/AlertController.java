package com.zgiot.app.server.module.alert.controller;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.*;
import com.zgiot.app.server.service.FileService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.FileModel;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xiayun on 2017/9/26.
 */
@RestController
@RequestMapping(GlobalConstants.API_VERSION + "/alert")
public class AlertController {
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private FileService fileService;

    private static final String MODULE_NAME = "alert";

    @ApiOperation("获取参数类报警规则")
    @PostMapping(value = "rule/param/show")
    public ResponseEntity<String> getParamRule(@RequestBody FilterCondition filterCondition) {
        AlertRuleRsp alertRules = alertManager.getParamAlertRuleList(filterCondition);
        return new ResponseEntity<>(ServerResponse.buildOkJson(alertRules), HttpStatus.OK);
    }

    @ApiOperation("获取保护类报警规则")
    @PostMapping(value = "rule/prot/show")
    public ResponseEntity<String> getProtectRule(@RequestBody FilterCondition filterCondition) {
        AlertRuleRsp alertRules = alertManager.getProtAlertRuleList(filterCondition);
        return new ResponseEntity<>(ServerResponse.buildOkJson(alertRules), HttpStatus.OK);
    }

    // @ApiOperation("更新参数类报警规则")
    // @PostMapping(value = "rule/param")
    // public ResponseEntity<String> updateParamRule(@RequestBody List<AlertRule>
    // alertRules) {
    //
    // alertManager.initParamRuleMap();
    // return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    // }

    @ApiOperation("更新报警规则")
    @PostMapping(value = "rule")
    public ResponseEntity<String> updateRule(@RequestBody List<AlertRule> alertRules, @RequestParam int type) {

        List<AlertRule> alertRulesRsp = alertManager.updateRule(alertRules, type);
        return new ResponseEntity<>(ServerResponse.buildOkJson(alertRulesRsp), HttpStatus.OK);
    }

    // @ApiOperation("更新保护类报警规则")
    // @PostMapping(value = "rule/protect")
    // public ResponseEntity<String> updateProtectRule() {
    // alertManager.initProtectRuleMap();
    // return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    // }

    @ApiOperation("删除报警规则")
    @DeleteMapping(value = "rule")
    public ResponseEntity<String> deleteRule(@RequestBody List<Long> list, @RequestParam int type) {
        alertManager.deleteRule(list, type);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @ApiOperation("下发报警指令")
    @PostMapping(value = "cmd")
    public ResponseEntity<String> sendAlertCmd(@RequestParam String thingCode, @RequestParam String metricCode,
            @RequestBody AlertMessage alertMessage, HttpServletRequest request)  {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        Integer messageId = alertManager.sendAlertCmd(thingCode, metricCode, alertMessage, requestId);
        return new ResponseEntity<>(ServerResponse.buildOkJson(messageId), HttpStatus.OK);
    }

    @ApiOperation("获取报警记录,按设备分组")
    @GetMapping(value = "record/group")
    public ResponseEntity<String> getAlertRecordGroupByThing(@RequestParam(required = false) String stage,
            @RequestParam(required = false) List<Integer> levels, @RequestParam(required = false) List<Short> types,
            @RequestParam(required = false) List<Integer> buildingIds,
            @RequestParam(required = false) List<Integer> floors, @RequestParam(required = false) List<Integer> systems,
            @RequestParam(required = false) String assetType, @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer sortType, @RequestParam(required = false) Long duration,
            @RequestParam(required = false) String thingCode, @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer count, @RequestParam(required = false) Date timeStamp) {
        if (timeStamp == null) {
            timeStamp = new Date();
        }
        List<AlertRecord> alertDataListGroupByThing =
                alertManager.getAlertDataListGroupByThing(stage, levels, types, buildingIds, floors, systems, assetType,
                        category, sortType, duration, thingCode, page, count, timeStamp);
        AlertRecordRsp alertRecordRsp = new AlertRecordRsp(alertDataListGroupByThing, timeStamp);
        return new ResponseEntity<>(ServerResponse.buildOkJson(alertRecordRsp), HttpStatus.OK);
    }

//    @ApiOperation("获取报警记录")
//    @GetMapping(value = "record")
//    public ResponseEntity<String> getAlertRecord(@RequestParam(required = false) String stage,
//            @RequestParam(required = false) Integer level, @RequestParam(required = false) Short type,
//            @RequestParam(required = false) Integer system, @RequestParam(required = false) String assetType,
//            @RequestParam(required = false) String category, @RequestParam(required = false) Integer sortType,
//            @RequestParam(required = false) Long duration, @RequestParam(required = false) String thingCode,
//            @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer count) {
//        List<AlertData> alertDataList = alertManager.getAlertDataList(stage, level, type, system, assetType, category,
//                sortType, duration, thingCode, page, count);
//        return new ResponseEntity<>(ServerResponse.buildOkJson(alertDataList), HttpStatus.OK);
//    }

    @ApiOperation("获取报警记录")
    @PostMapping(value = "record")
    public ResponseEntity<String> getAlertRecord(@RequestBody FilterCondition filterCondition){
        AlertRecord alertDataList = alertManager.getAlertDataList(filterCondition);
        return new ResponseEntity<>(ServerResponse.buildOkJson(alertDataList), HttpStatus.OK);
    }

    @ApiOperation("人为生成报警")
    @PostMapping(value = "generate")
    public ResponseEntity<String> generateManuAlert(@RequestParam String thingCode, @RequestParam String info,
            @RequestParam String userId, @RequestParam String permission) {
        alertManager.generateManuAlert(thingCode, info, userId, permission);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @ApiOperation("获取报警消息列表")
    @GetMapping(value = "message")
    public ResponseEntity<String> getAlertMessages(@RequestParam int alertId) {
        List<AlertMessage> alertMessage = alertManager.getAlertMessage(alertId);
        return new ResponseEntity<>(ServerResponse.buildOkJson(alertMessage), HttpStatus.OK);
    }

    @ApiOperation("设置消息已读")
    @PostMapping(value = "message/read")
    public ResponseEntity<String> setMessageRead(@RequestParam List<Integer> messageIds) {
        alertManager.setRead(messageIds);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @ApiOperation("反馈图片和视频信息")
    @PostMapping(value = "feedback")
    public ResponseEntity<String> feedbackImageAndVideo(@RequestParam String thingCode, @RequestParam String metricCode,
            @RequestBody MultipartFile files, @RequestParam String userId, @RequestParam int type) {
        String url;
        try {
            FileModel attach = fileService.uploadFile(files, files.getOriginalFilename(), MODULE_NAME, type, userId);
            url = attach.getAbsolutePath();
        } catch (IOException e) {
            throw new SysException("file upload fail,"+e.getMessage(), SysException.EC_UNKNOWN);
        }

        alertManager.feedback(thingCode, metricCode, url, type);
        return new ResponseEntity<>(ServerResponse.buildOkJson(url), HttpStatus.OK);
    }

    @ApiOperation("获取统计数量信息")
    @GetMapping(value = "statistics")
    public ResponseEntity<String> getStatisticsInfo(@RequestParam int type,
            @RequestParam(required = false) String alertStage, @RequestParam Date startTime,
            @RequestParam Date endTime) {
        AlertStatisticsRsp statisticsInfo = alertManager.getStatisticsInfo(type, alertStage, startTime, endTime);
        return new ResponseEntity<>(ServerResponse.buildOkJson(statisticsInfo), HttpStatus.OK);
    }

    @ApiOperation("按类型获取统计数量信息")
    @GetMapping(value = "statistics/type")
    public ResponseEntity<String> getTypeStatisticsInfo(@RequestParam Date startTime, @RequestParam Date endTime) {
        AlertStatisticsRsp typeStatisticsInfo = alertManager.getTypeStatisticsInfo(startTime, endTime);
        return new ResponseEntity<>(ServerResponse.buildOkJson(typeStatisticsInfo), HttpStatus.OK);
    }

    @ApiOperation("获取统计维修信息")
    @GetMapping(value = "statistics/repair")
    public ResponseEntity<String> getRepairStatisticsInfo(@RequestParam Date startTime, @RequestParam Date endTime,
            @RequestParam(required = false) Short alertLevel) {
        List<AlertRepairStatistics> repairStatisticsInfo =
                alertManager.getRepairStatisticsInfo(startTime, endTime, alertLevel);
        return new ResponseEntity<>(ServerResponse.buildOkJson(repairStatisticsInfo), HttpStatus.OK);
    }

    @ApiOperation("反馈图片信息")
    @PostMapping(value = "feedback/image")
    public ResponseEntity<String> feedBackImage(@RequestParam String thingCode, @RequestParam String metricCode,
            @RequestParam(required = false) List<String> existedUri, @RequestParam(required = false) MultipartFile file,
            @RequestParam String userId) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> result = new ArrayList<>();
        if (existedUri != null) {
            for (String uri : existedUri) {
                stringBuilder.append(uri);
                stringBuilder.append(';');
            }
            result.addAll(existedUri);
        }
        if (file != null) {
            try {
                FileModel attach = fileService.uploadFile(file, file.getOriginalFilename(), MODULE_NAME,
                        FileService.IMAGE, userId);
                String url = attach.getAbsolutePath();
                stringBuilder.append(url);
                result.add(url);
            } catch (IOException e) {
                throw new SysException("file upload fail,"+e.getMessage(), SysException.EC_UNKNOWN);
            }
        }
        alertManager.feedback(thingCode, metricCode, stringBuilder.toString(), FileService.IMAGE);
        return new ResponseEntity<>(ServerResponse.buildOkJson(result), HttpStatus.OK);
    }

    @ApiOperation("删除视频信息")
    @PostMapping(value = "feedback/video/del")
    public ResponseEntity<String> delVideo(@RequestParam String thingCode, @RequestParam String metricCode) {
        alertManager.delVideo(thingCode, metricCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @ApiOperation("申请复位")
    @PostMapping(value = "reset/req")
    public ResponseEntity<String> requestReset(@RequestParam String thingCode, @RequestParam String userId,
            @RequestParam String permission) {
        alertManager.requestReset(thingCode, userId, permission);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @ApiOperation("复位")
    @PostMapping(value = "reset")
    public ResponseEntity<String> requestReset(@RequestParam String thingCode, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        alertManager.reset(thingCode, requestId);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @ApiOperation("最严重报警等级")
    @GetMapping(value = "seriousLevel")
    public ResponseEntity<String> getAlertSeriousLevel(@RequestParam List<String> thingCodeList) {
        Map<String, Short> seriousAlertLevel = alertManager.getSeriousAlertLevel(thingCodeList);
        return new ResponseEntity<>(ServerResponse.buildOkJson(seriousAlertLevel), HttpStatus.OK);
    }

    @ApiOperation("设置参数类可以配置报警规则的信号列表")
    @PostMapping(value = "param/configureList")
    public ResponseEntity<String> setParamConfigurationList(@RequestBody List<AlertRule> list) {
        List<AlertRule> duplicateRules = alertManager.setParamConfigurationList(list);
        return new ResponseEntity<>(ServerResponse.buildOkJson(duplicateRules), HttpStatus.OK);
    }

    @ApiOperation("获取参数类配置阈值")
    @GetMapping(value = "param/threshold")
    public ResponseEntity<String> getParamThreshold(@RequestParam String thingCode, @RequestParam String metricCode) {
        AlertRule paramThreshold = alertManager.getParamThreshold(thingCode, metricCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(paramThreshold), HttpStatus.OK);
    }

    @ApiOperation("设置参数类配置阈值")
    @PostMapping(value = "param/threshold")
    public ResponseEntity<String> setParamThreshold(@RequestBody AlertRule alertRule) {
        alertManager.setParamThreshlold(alertRule);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }


    @PostMapping(value = "mask/statistics")
    public ResponseEntity<String> getMaskStatisticInfo(@RequestBody FilterCondition filterCondition){
        AlertMaskRsp maskStatisticInfo = alertManager.getMaskStatisticInfo(filterCondition);
        return new ResponseEntity<>(ServerResponse.buildOkJson(maskStatisticInfo), HttpStatus.OK);
    }

    @PostMapping(value = "mask/detail")
    public  ResponseEntity<String> getDetailMaskInfo(@RequestBody FilterCondition filterCondition){
        List<AlertMaskInfo> detailMaskInfo = alertManager.getDetailMaskInfo(filterCondition);
        return new ResponseEntity<>(ServerResponse.buildOkJson(detailMaskInfo), HttpStatus.OK);
    }

}
