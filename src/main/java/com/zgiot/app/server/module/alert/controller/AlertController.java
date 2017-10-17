package com.zgiot.app.server.module.alert.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.*;
import com.zgiot.app.server.service.FileService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.FileModel;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by xiayun on 2017/9/26.
 */
@RestController
public class AlertController {
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private FileService fileService;

    private static final String MODULE_NAME = "alert";

    @ApiOperation("获取参数类报警规则")
    @GetMapping(value = "alert/rule/param")
    public void getParamRule(@RequestParam int alertType, @RequestParam(required = false) Integer assetType,
            @RequestParam(required = false) String category, @RequestParam(required = false) String system,
            @RequestParam(required = false) String metricType, @RequestParam(required = false) String thingCode,
            @RequestParam(required = false) Boolean enable, @RequestParam(required = false) Integer level,
            @RequestParam(required = false) String metricCode, @RequestParam(required = false) Integer buildingId) {
        alertManager.getAlertRuleList(alertType, assetType, category, system, metricType, thingCode, enable, level,
                metricCode, buildingId);
    }

    @ApiOperation("更新参数类报警规则")
    @PostMapping(value = "alert/rule/param")
    public void updateParamRule() {
        alertManager.updateParamRuleMap();
    }

    @ApiOperation("更新保护类报警规则")
    @PostMapping(value = "alert/rule/protect")
    public void updateProtectRule() {
        alertManager.updateProtectRuleMap();
    }

    @ApiOperation("下发报警指令")
    @PostMapping(value = "alert/cmd")
    public void sendAlertCmd(@RequestParam String thingCode, @RequestParam String metricCode,
            @RequestBody AlertMessage alertMessage, HttpServletRequest request) throws Exception {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        alertManager.sendAlertCmd(thingCode, metricCode, alertMessage, requestId);
    }

    @ApiOperation("获取报警记录,按设备分组")
    @GetMapping(value = "alert/record/group")
    public List<AlertRecord> getAlertRecordGroupByThing(@RequestParam(required = false) String stage,
            @RequestParam(required = false) List<Integer> levels, @RequestParam(required = false) List<Short> types,
            @RequestParam(required = false) List<Integer> buildingIds,
            @RequestParam(required = false) List<Integer> floors, @RequestParam(required = false) List<Integer> systems,
            @RequestParam(required = false) String assetType, @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer sortType, @RequestParam(required = false) Long duration,
            @RequestParam(required = false) String thingCode, @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer count) {
        return alertManager.getAlertDataListGroupByThing(stage, levels, types, buildingIds, floors, systems, assetType,
                category, sortType, duration, thingCode, page, count);
    }

    @ApiOperation("获取报警记录")
    @GetMapping(value = "alert/record")
    public List<AlertData> getAlertRecord(@RequestParam(required = false) String stage,
            @RequestParam(required = false) Integer level, @RequestParam(required = false) Short type,
            @RequestParam(required = false) Integer system, @RequestParam(required = false) String assetType,
            @RequestParam(required = false) String category, @RequestParam(required = false) Integer sortType,
            @RequestParam(required = false) Long duration, @RequestParam(required = false) String thingCode,
            @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer count) {
        return alertManager.getAlertDataList(stage, level, type, system, assetType, category, sortType, duration,
                thingCode, page, count);
    }

    @ApiOperation("人为生成报警")
    @PostMapping(value = "alert/generate")
    public void generateManuAlert(@RequestParam String thingCode, @RequestParam String info,
            @RequestParam String userId, @RequestParam String permission) {
        alertManager.generateManuAlert(thingCode, info, userId, permission);
    }

    @ApiOperation("获取报警消息列表")
    @GetMapping(value = "alert/message")
    public List<AlertMessage> getAlertMessages(@RequestParam int alertId) {
        return alertManager.getAlertMessage(alertId);
    }

    @ApiOperation("反馈图片和视频信息")
    @PostMapping(value = "alert/feedback")
    public void feedbackImageAndVideo(String thingCode, String metricCode, List<MultipartFile> files, String userId,
            int type) {
        StringBuilder stringBuilder = new StringBuilder();
        for (MultipartFile file : files) {
            try {
                FileModel attach = fileService.uploadFile(file, file.getOriginalFilename(), MODULE_NAME, type, userId);
                String url = attach.getAbsolutePath();
                stringBuilder.append(url);
                stringBuilder.append(";");
            } catch (Exception e) {
                throw new SysException("file upload fail", SysException.EC_UNKNOWN);
            }

        }
        alertManager.feedback(thingCode, metricCode, stringBuilder.toString(), type);
    }

    @ApiOperation("获取统计数量信息")
    @GetMapping(value = "alert/statistics")
    public AlertStatisticsRsp getStatisticsInfo(@RequestParam int type,
            @RequestParam(required = false) String alertStage, @RequestParam Date startTime,
            @RequestParam Date endTime) {
        return alertManager.getStatisticsInfo(type, alertStage, startTime, endTime);
    }

    @ApiOperation("按类型获取统计数量信息")
    @GetMapping(value = "alert/statistics/type")
    public AlertStatisticsRsp getTypeStatisticsInfo(Date startTime, Date endTime) {
        return alertManager.getTypeStatisticsInfo(startTime, endTime);
    }

    @ApiOperation("获取统计维修信息")
    @GetMapping(value = "alert/statistics/repair")
    public List<AlertRepairStatistics> getRepairStatisticsInfo(Date startTime, Date endTime, @RequestParam(required = false) Short alertLevel) {
        return alertManager.getRepairStatisticsInfo(startTime, endTime, alertLevel);
    }

}
