package com.zgiot.app.server.module.sfstart.controller;

import com.zgiot.app.server.dataprocessor.DataProcessor;
import com.zgiot.app.server.module.sfstart.StartExamineListener;
import com.zgiot.app.server.module.sfstart.StartListener;
import com.zgiot.app.server.module.sfstart.constants.StartConstants;
import com.zgiot.app.server.module.sfstart.pojo.*;
import com.zgiot.app.server.module.sfstart.service.StartMultithreading;
import com.zgiot.app.server.module.sfstart.service.StartService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.pojo.SessionContext;
import com.zgiot.common.restcontroller.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfstart")
public class StartController {

    private static final Logger logger = LoggerFactory.getLogger(StartController.class);

    @Autowired
    private StartService startService;
    @Autowired
    private StartHandler startHandler;


    private static List<String> staticSystemIds;


    @Autowired
    @Qualifier("wsProcessor")
    private DataProcessor processor;
    @Autowired
    private StartExamineListener startExamineListener;

    @Autowired
    private StartListener startListener;

    // 当前启车操作号
    private static Integer operateId;

    public static Integer getOperateId() {
        return operateId;
    }

    public static void setOperateId(Integer operateId) {
        StartController.operateId = operateId;
    }

    public static final String SUCCESS = "success";

    public static List<StartDeviceRequirement> startDeviceRequirements;

    public List<StartDeviceRequirement> getStartDeviceRequirements() {
        return startDeviceRequirements;
    }

    public void setStartDeviceRequirements(List<StartDeviceRequirement> startDeviceRequirements) {
        StartController.startDeviceRequirements = startDeviceRequirements;
    }


    /**
     * 查询启车状态,根据启车状态判断跳转页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/findStartState", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findStartState() throws Exception {
        StartOperationRecord startOperationRecord = startHandler.getStartState();
        logger.info("{}查询到的启车状态为{}", SessionContext.getCurrentUser().getUserUuid(), startOperationRecord.getOperateState());
        return new ResponseEntity<>(ServerResponse.buildOkJson(startOperationRecord), HttpStatus.OK);
    }

    /**
     * 查询系统列表信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getSystem", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSystem() throws Exception {
        List<StartSystem> startSystems = startHandler.getStartSystem();
        return new ResponseEntity<>(ServerResponse.buildOkJson(startSystems), HttpStatus.OK);
    }


    /**
     * 获取启车中系统和设备关系
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStartingSystem", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStartingSystem() throws Exception {
        List<StartSystem> startSystems = startHandler.getStartingSystem();
        return new ResponseEntity<>(ServerResponse.buildOkJson(startSystems), HttpStatus.OK);
    }

    /**
     * 获取启车中设备状态
     * a
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getStartDeviceState", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStartDeviceState() {
        StartingInformationReq startingInformationReq = new StartingInformationReq();
        startingInformationReq.setStartDeviceStateRecords(startService.getStartDeviceState());
        startingInformationReq.setNowTime(new Date());
        startingInformationReq.setStartTime(StartHandler.getStartTime());
        startingInformationReq.setPauseState(StartHandler.getPauseState());
        return new ResponseEntity<>(ServerResponse.buildOkJson(startingInformationReq), HttpStatus.OK);
    }

    /**
     * 启车中操作(包括启车结束检查)
     *
     * @param operate 启车中操作
     * @return
     */
    @RequestMapping(value = "/operateStarting", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> operateStarting(@RequestParam("operate") String operate) throws Exception {
        // 使用者id
        logger.info("{}执行了{}操作", SessionContext.getCurrentUser().getUserUuid(), operate);
        startHandler.operateStarting(operate);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 正式启车
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setUpStartTask", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public synchronized ResponseEntity<String> setUpStartTask() throws Exception {
        if (startService.judgeStartingState(StartConstants.START_SEND_FINISH_STATE, StartConstants.START_FINISH_STATE)) {
            // 通知前端已经存在启车任务
            return new ResponseEntity<>(ServerResponse.buildOkJson("starting"), HttpStatus.OK);
        }
        if (startService.judgeStartingState(null, StartConstants.START_SEND_FINISH_STATE)) {
            // 通知前端信号未发送完毕
            return new ResponseEntity<>(ServerResponse.buildOkJson("sendMessageIsNotFinish"), HttpStatus.OK);
        }
        // 判断启车检查是否合格
        if (startService.isfullyExamine()) {
            Set<String> startDeviceIds = startHandler.getStartDeviceIds(staticSystemIds);
            // 准备设备观察点
            startHandler.observeDeviceState(startDeviceIds);
            // 发送正式启车命令
            sendStarting(startService.findOperateIdWhenNull(), startDeviceIds);
            // 信号发送完毕，修改状态
            return new ResponseEntity<>(ServerResponse.buildOkJson(SUCCESS), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(ServerResponse.buildOkJson("examineError"), HttpStatus.OK);
        }

    }

    /**
     * 发送正式启车命令
     *
     * @param operateId
     */
    public void sendStarting(Integer operateId, Set<String> startDeviceIds) throws Exception {
        processor.removeListener(startExamineListener);
        StartHandler.setStartTime(new Date());
        startService.updateStartOperate(StartConstants.START_STARTING_STATE);
        startHandler.sendMessageTemplateByJson(StartConstants.URI_START_STATE, StartConstants.URI_START_STATE_MESSAGE_SET_UP_START_TASK);
        startMultithreadingStart(operateId, startDeviceIds, StartMultithreading.SEND_STARTING);
    }

    /**
     * 获取总览页面系统和设备关系
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStartBrowseSystem", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStartBrowseSystem() throws Exception {
        List<StartSystem> startSystems = startService.getStartBrowseSystem();
        return new ResponseEntity<>(ServerResponse.buildOkJson(startSystems), HttpStatus.OK);
    }

    /**
     * 获取总览页面设备信息
     *
     * @return
     */
    @RequestMapping(value = "/getStartBrowseDevice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStartBrowseDevice() throws Exception {
        List<StartDevice> startDevices = startHandler.getStartBrowseDevice();
        return new ResponseEntity<>(ServerResponse.buildOkJson(startDevices), HttpStatus.OK);
    }

    /**
     * 获取总览页面的仓库信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStartBrowseCoalDepot", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStartBrowseCoalDepot() throws Exception {
        List<StartSystem> startSystems = startService.getStartBrowseDeprotSystem();
        return new ResponseEntity<>(ServerResponse.buildOkJson(startSystems), HttpStatus.OK);
    }

    /**
     * 配合测试反馈设备信息
     *
     * @return
     */
    @RequestMapping(value = "/testDevice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> testGetDevice() {
        List<StartDevice> startDevices = startService.testGetDevice();
        return new ResponseEntity<>(ServerResponse.buildOkJson(startDevices), HttpStatus.OK);
    }

    /**
     * 启车检查
     *
     * @param systemIds
     * @return
     */
    @RequestMapping(value = "/setUpAutoTest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public synchronized ResponseEntity<String> setUpAutoTest(@RequestBody List<String> systemIds) throws Exception {
        if (startService.judgeStartingState(null, StartConstants.START_FINISH_STATE)) {
            // 通知前端已经存在启车任务
            return new ResponseEntity<>(ServerResponse.buildOkJson("starting"), HttpStatus.OK);
        }
        // 保存启车检查操作
        StartOperationRecord saveStartOperationRecord = startService.saveStartOperationRecord(systemIds, SessionContext.getCurrentUser().getUserUuid());
        Set<String> startDeviceIds = startHandler.getStartDeviceIds(systemIds);
        startService.setUpAutoExamine(startDeviceIds, saveStartOperationRecord.getOperateId());
        // 建立启车自检开始
        autoExamineStarting(startDeviceIds, saveStartOperationRecord.getOperateId());
        staticSystemIds = systemIds;
        return new ResponseEntity<>(ServerResponse.buildOkJson(SUCCESS), HttpStatus.OK);
    }

    /**
     * 获得本次启车检查信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getAutoExamineRecord", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAutoExamineRecord() throws Exception {
        List<StartExamineRecord> startExamineRecords = startService.getAutoExamineRecord();
        return new ResponseEntity<>(ServerResponse.buildOkJson(startExamineRecords), HttpStatus.OK);
    }


    /**
     * 修复检查
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/handleExamine", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleExamine(@RequestBody List<Integer> ruleIds, @RequestParam("tapOperate") String tapOperate) throws Exception {

        startHandler.handleExamine(ruleIds, tapOperate, SessionContext.getCurrentUser().getUserUuid());
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 获取自检中系统和设备关系
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getAutoExamineSystem", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAutoExamineSystem() throws Exception {
        List<StartSystem> startSystems = startService.getAutoExamineSystem();
        return new ResponseEntity<>(ServerResponse.buildOkJson(startSystems), HttpStatus.OK);
    }


    /**
     * 获取启车可设置人工干预范围
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getManualInterventionScopeStart", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getManualInterventionScopeStart(@RequestParam("deviceCode") String deviceCode) throws Exception {
        List<StartManualInterventionRecord> startManualInterventionDevices = startService.getManualInterventionScopeStart(deviceCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(startManualInterventionDevices), HttpStatus.OK);
    }

    /**
     * 设置启车临时人工干预
     *
     * @param deviceId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setManualInterventionStart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> setManualIntervention(@RequestParam("deviceId") String deviceId) throws Exception {
        String information = startHandler.setManualIntervention(deviceId, SessionContext.getCurrentUser().getUserUuid());
        return new ResponseEntity<>(ServerResponse.buildOkJson(information), HttpStatus.OK);
    }

    /**
     * 反馈人工干预设备信息
     *
     * @param deviceId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getManualInterventionDeviceInformation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getManualInterventionDeviceInformation(@RequestParam("deviceId") String deviceId) throws Exception {
        StartManualInterventionRecord startManualInterventionRecord = startService.getManualInterventionDeviceInformation(deviceId);
        return new ResponseEntity<>(ServerResponse.buildOkJson(startManualInterventionRecord), HttpStatus.OK);
    }


    /**
     * 反馈当前设置的干预项
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getManualInterventionRecord", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getManualInterventionRecord() throws Exception {
        List<StartManualInterventionRecord> startManualInterventionDevices = startService.getManualInterventionRecord();
        return new ResponseEntity<>(ServerResponse.buildOkJson(startManualInterventionDevices), HttpStatus.OK);
    }

    /**
     * 解除人工干预
     *
     * @param deviceId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/relieveManualIntervention", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> relieveManualIntervention(@RequestParam String deviceId) throws Exception {
        // 使用者id
        String information = startHandler.relieveManualIntervention(deviceId, SessionContext.getCurrentUser().getUserUuid());
        return new ResponseEntity<>(ServerResponse.buildOkJson(information), HttpStatus.OK);
    }

    /**
     * 获取启车中包和设备关系
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStartingPackage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStartingPackage() throws Exception {
        List<StartPackage> startPackages = startService.getStartingPackage();
        return new ResponseEntity<>(ServerResponse.buildOkJson(startPackages), HttpStatus.OK);
    }

    /**
     * 获取启车包内逻辑关系
     *
     * @param packageId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStartingDeviceRelation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStartingDeviceRelation(@RequestParam("packageId") String packageId) throws Exception {
        List<StartDeviceRelation> startPackages = startHandler.getStartDeviceRelationByPackageId(packageId, staticSystemIds);
        return new ResponseEntity<>(ServerResponse.buildOkJson(startPackages), HttpStatus.OK);
    }

    /**
     * 获取启车设备启动控制条件信息
     *
     * @param deviceId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStartingDeviceControlInformation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStartingDeviceControlInformation(@RequestParam("deviceId") String deviceId) throws Exception {
        StartDeviceControlInformation startDeviceControlInformation = startHandler.getStartingDeviceControlInformation(deviceId);
        return new ResponseEntity<>(ServerResponse.buildOkJson(startDeviceControlInformation), HttpStatus.OK);
    }

    /**
     * 发送信号
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendInformation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public synchronized ResponseEntity<String> sendInformation() throws Exception {
        if (!startService.isfullyExamine()) {
            // 通知前端检查不合格
            return new ResponseEntity<>(ServerResponse.buildOkJson("examineError"), HttpStatus.OK);
        }
        if (startService.judgeStartingState(StartConstants.START_SEND_INFORMATION_STATE, StartConstants.START_FINISH_STATE)) {
            // 通知前端已经发送成功了
            return new ResponseEntity<>(ServerResponse.buildOkJson("sendIsFinish"), HttpStatus.OK);
        }
        if (startService.judgeStartingState(StartConstants.START_SEND_CLEAN_STATE, StartConstants.START_SEND_FINISH_STATE)) {
            // 通知前端信号发送中
            return new ResponseEntity<>(ServerResponse.buildOkJson("sendNotFinish"), HttpStatus.OK);
        }
        if (startService.judgeStartingState(StartConstants.START_NO_STATE, StartConstants.START_SEND_CLEAN_STATE)) {
            // 发送启车清除信息
            startHandler.sendCleanInformation();
        }
        Set<String> startDeviceIds = startService.getAllDeviceIdBySystenIds(staticSystemIds);
        // 发送启车信息到plc
        startMultithreadingStart(startService.findOperateIdWhenNull(), startDeviceIds, StartMultithreading.SEND_INFORMATION);
        startService.updateStartOperate(StartConstants.START_SEND_INFORMATION_STATE);
        return new ResponseEntity<>(ServerResponse.buildOkJson(SUCCESS), HttpStatus.OK);
    }

    /**
     * 建立多线程任务
     *
     * @param operateId      启车操作id
     * @param startDeviceIds 涉及设备
     * @param operate        任务类型
     */
    public void startMultithreadingStart(Integer operateId, Set<String> startDeviceIds, Integer operate) {
        StartMultithreading startMultithreading = new StartMultithreading();
        startMultithreading.setOperate(operate);
        startMultithreading.setStartService(startService);
        startMultithreading.setStartHandler(startHandler);
        startMultithreading.setStartDeviceIds(startDeviceIds);
        startMultithreading.setOperateId(operateId);
        new Thread(startMultithreading).start();
    }

    /**
     * 自检开始
     *
     * @throws Exception
     */
    public void autoExamineStarting(Set<String> startDeviceIds, Integer operateId) throws Exception {
        startMultithreadingStart(operateId, startDeviceIds, StartMultithreading.SET_UP_AUTO_TEST);
    }


}
