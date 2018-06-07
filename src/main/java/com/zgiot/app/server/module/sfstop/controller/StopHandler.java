package com.zgiot.app.server.module.sfstop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.sfstart.pojo.StartMessage;
import com.zgiot.app.server.module.sfstop.constants.StopConstants;
import com.zgiot.app.server.module.sfstop.entity.pojo.*;
import com.zgiot.app.server.module.sfstop.entity.vo.StopExamineThing;
import com.zgiot.app.server.module.sfstop.entity.vo.StopIndexVO;
import com.zgiot.app.server.module.sfstop.entity.vo.StopMessage;
import com.zgiot.app.server.module.sfstop.exception.StopException;
import com.zgiot.app.server.module.sfstop.service.*;
import com.zgiot.app.server.module.sfstop.util.DateTimeUtils;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.SessionContext;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 停车辅助
 */
@Component
public class StopHandler {
    private static final Logger logger = LoggerFactory.getLogger(StopHandler.class);
    @Autowired
    private StopService stopService;
    @Autowired
    private StopDeviceAreaService stopDeviceAreaService;
    @Autowired
    private StopLineService stopLineService;
    @Autowired
    private StopDeviceBagService stopDeviceBagService;
    @Autowired
    private StopInformationService stopInformationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private DataService dataService;

    @Autowired
    private CmdControlService cmdControlService;
    // 一期系统暂停状态
    private static Boolean system1PauseState;
    // 二期系统暂停状态
    private static Boolean system2PauseState;

    private static BlockingQueue blockingQueue;


    public static Boolean startExamineListenerFlag;
    public static Boolean startListenerFlag;

    public static Logger getLogger() {
        return logger;
    }

    public StopService getStopService() {
        return stopService;
    }

    public void setStopService(StopService stopService) {
        this.stopService = stopService;
    }

    public StopDeviceAreaService getStopDeviceAreaService() {
        return stopDeviceAreaService;
    }

    public void setStopDeviceAreaService(StopDeviceAreaService stopDeviceAreaService) {
        this.stopDeviceAreaService = stopDeviceAreaService;
    }

    public StopLineService getStopLineService() {
        return stopLineService;
    }

    public void setStopLineService(StopLineService stopLineService) {
        this.stopLineService = stopLineService;
    }

    public StopDeviceBagService getStopDeviceBagService() {
        return stopDeviceBagService;
    }

    public void setStopDeviceBagService(StopDeviceBagService stopDeviceBagService) {
        this.stopDeviceBagService = stopDeviceBagService;
    }

    public StopInformationService getStopInformationService() {
        return stopInformationService;
    }

    public void setStopInformationService(StopInformationService stopInformationService) {
        this.stopInformationService = stopInformationService;
    }


    public static Boolean getSystem2PauseState() {
        return system2PauseState;
    }

    public static void setSystem2PauseState(Boolean system2PauseState) {
        StopHandler.system2PauseState = system2PauseState;
    }

    public static Boolean getSystem1PauseState() {
        return system1PauseState;
    }

    public static void setSystem1PauseState(Boolean system1PauseState) {
        StopHandler.system1PauseState = system1PauseState;
    }

    public static Boolean getStartExamineListenerFlag() {
        return startExamineListenerFlag;
    }

    public static void setStartExamineListenerFlag(Boolean startExamineListenerFlag) {
        StopHandler.startExamineListenerFlag = startExamineListenerFlag;
    }

    public static Boolean getStartListenerFlag() {
        return startListenerFlag;
    }

    public static void setStartListenerFlag(Boolean startListenerFlag) {
        StopHandler.startListenerFlag = startListenerFlag;
    }

    /**
     * 判断当前启车状态
     *
     * @return
     */
    public StopOperationRecord getStopState(int systemId) {
        List<StopOperationRecord> querystartOperationRecord = stopService.findUnfinishStopOperate(systemId, null, StopConstants.STOP_FINISH_STATE);
        if (CollectionUtils.isEmpty(querystartOperationRecord)) {
            StopOperationRecord startOperationRecord = new StopOperationRecord();
            startOperationRecord.setOperateState(StopConstants.STOP_NO_STATE);
            return startOperationRecord;
        } else if (querystartOperationRecord.size() > 1) {
            throw new StopException("当前起车数据异常，存在多条停车有效数据，请检查。");
        }
        return querystartOperationRecord.get(0);
    }

    /**
     * 判断当前停车状态
     *
     * @param startState
     * @param finishState
     * @return
     */
    public boolean judgeStopingState(Integer systemId, Integer startState, Integer finishState) {
        List<StopOperationRecord> stopOperationRecords = stopService.findUnfinishStopOperate(systemId, startState, finishState);
        if (CollectionUtils.isNotEmpty(stopOperationRecords)) {
            // 已经存在启车任务
            return true;
        }
        return false;
    }

    /**
     * 查询二期所有停车线
     *
     * @param system
     * @return
     */
    public List<String> getStopLinesBySystem(String system) {
        List<String> stopLineList = new ArrayList<>();
        List<StopDeviceArea> stopDeviceAreas = stopDeviceAreaService.getStopDeviceArea(StopConstants.REGION_1, Integer.valueOf(system));
        for (StopDeviceArea stopDeviceArea : stopDeviceAreas) {
            List<StopLine> stopLines = stopLineService.getStopLineByAreaId(stopDeviceArea.getId());
            for (StopLine stopLine : stopLines) {
                stopLineList.add(String.valueOf(stopLine.getId()));
            }
        }
        return stopLineList;
    }


    /**
     * 获取所有停车设备id
     *
     * @param lineIds
     * @return
     */
    public Set<String> getStopDeviceIds(List<String> lineIds) {
        Set<String> startDeviceIds = new HashSet<>();
        for (String line : lineIds) {
            List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(Long.valueOf(line));
            for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
                List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
                for (StopInformation stopInformation : stopInformations) {
                    startDeviceIds.add(stopInformation.getThingCode());
                }
            }
        }
        return startDeviceIds;
    }

    /**
     * 发送json格式信息
     *
     * @param urlTopic
     * @param message
     */
    public void sendMessageTemplateByJson(String urlTopic, String message) {
        String jsonPause = "{\"message\":\"" + message + "\"}";
        JSONObject jsonObject = JSON.parseObject(jsonPause);
        sendMessagingTemplate(urlTopic, jsonObject);
    }

    /**
     * 存储订阅消息
     *
     * @param label
     * @param object
     */
    public void sendMessagingTemplate(String label, Object object) {
        if (blockingQueue == null) {
            // 已经首次建立消息发送任务
            blockingQueue = new LinkedBlockingQueue();
            // 首次建立消息发送任务
            createMessagingTemplateTimer();
        }
        StartMessage startMessage = new StartMessage();
        startMessage.setLabel(label);
        startMessage.setObject(object);
        try {
            blockingQueue.put(startMessage);
        } catch (Exception e) {
            logger.error("下发信号存储错误");
        }
    }

    protected void createMessagingTemplateTimer() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!blockingQueue.isEmpty()) {
                    try {
                        StopMessage stopMessage = (StopMessage) blockingQueue.take();
                        messagingTemplate.convertAndSend(stopMessage.getLabel(), stopMessage.getObject());
                    } catch (Exception e) {
                        logger.error("停车发送任务失败");
                    }
                }
            }
        };
        timer.schedule(timerTask, StopConstants.SEND_MESSAGE_CIRCLE_TIME, StopConstants.SEND_MESSAGE_WAIT_TIME);
    }

    /**
     * 停车自检
     *
     * @param stopExamineRules
     */
    public void autoExamineStarting(List<StopExamineRule> stopExamineRules) {
        List<StopExamineThing> examinethings = new ArrayList<>();
        for (StopExamineRule rule : stopExamineRules) {
            try {

                logger.info("启车自检,检查设备:{},检查内容:{}", rule.getExamineThingCode(), rule.getExamineMetricCode());
                String metricValue = getMetricValue(rule.getExamineThingCode(), rule.getExamineMetricCode());


                if (StringUtils.isNotEmpty(metricValue)) {

                    // updateExamineRecord(rule, metricValue);

                }
            } catch (Exception e) {
                logger.error("检查建立失败,错误内容{}", e);
                throw new StopException("自动检查任务失败，请稍后重新提交");
            }
            StopExamineThing stopExamineThing = new StopExamineThing();
            stopExamineThing.setMetricCode(rule.getExamineMetricCode());
        }
        //    startExamineListener.setStartExamineLabels(examineLabel);
        startExamineListenerFlag = true;

    }


    /**
     * 查询指标数据
     *
     * @param thingCode
     * @param metricCode
     * @return
     */
    public String getMetricValue(String thingCode, String metricCode) {
        DataModelWrapper dataModelWrapper = dataService.getData(thingCode, metricCode).orElse(null);
        if (dataModelWrapper != null) {
            return dataModelWrapper.getValue();
        }
        return "";
    }

    /**
     * 查询设备的告警信息
     *
     * @param thingCode
     * @return
     */
    public String getThingAlertInfo(String thingCode) {

        AlertData maxLevelAlertData = stopService.getMaxLevelAlertData(thingCode);
        if (maxLevelAlertData != null) {
            return maxLevelAlertData.getAlertInfo();
        }
        return "";
    }

    /**
     * 解除人工干预
     *
     * @param system
     * @param thingCode
     * @return
     */
    public String relieveManualIntervention(String system, String thingCode) {
        String information;
        if (stopService.judgeStopingState(system, null, StopConstants.STOP_FINISH_STATE)) {
            Integer operteId = stopService.getStopOperateId(Integer.valueOf(system));
            stopService.updateStopManualInterventionRecord(thingCode, operteId, StopConstants.MANUAL_INTERVENTION_REMOVE, null);
            // 通知前端人工干预变化
            sendMessageTemplateByJson(StopConstants.URI_MANUAL_INTERVENTION, StopConstants.URI_MANUAL_INTERVENTION_MESSAGE_MANUAL_INTERVENTION_CHANGE);
            information = "success";
        } else {
            information = "noStarting";
        }
        return information;
    }

    /**
     * 完成停车操作
     */
    public void finishStopState(String system) {

        // 关闭停车
        stopService.updateStopOperate(system, StopConstants.STOP_FINISH_STATE);
    }

    /**
     * 更新停车自检记录
     *
     * @param thingCode
     * @param metricCode
     * @param value
     */
    public void updateExamineRecordByRule(String thingCode, String metricCode, String value) {

    }

    /**
     * 停车中操作(包括启车结束检查)
     */
    public void operateStoping(String system, String operate) {
        switch (operate) {
            case "pause":
                // 暂停停车
                if (StopConstants.SYSTEM_1.equals(system)) {
                    system1PauseState = true;
                } else if (StopConstants.SYSTEM_2.equals(system)) {
                    system2PauseState = true;
                }
                stopService.updateStopOperate(system, StopConstants.STOP_PAUSE_STATE);
                sendMessageTemplateByJson(StopConstants.URI_STOP_STATE, StopConstants.URI_STOP_STATE_MESSAGE_PAUSE);
                break;
            case "continueStop":
                // 恢复停车
                if (StopConstants.SYSTEM_1.equals(system)) {
                    system1PauseState = false;
                } else if (StopConstants.SYSTEM_2.equals(system)) {
                    system2PauseState = false;
                }
                stopService.updateStopOperate(system, StopConstants.STOP_STOPING_STATE);
                sendMessageTemplateByJson(StopConstants.URI_STOP_STATE, StopConstants.URI_STOP_STATE_MESSAGE_CONTINUE_STOP);
                break;
            case "closeStop":
                // 结束停车
                stopService.closeStopOperate(system);
                if (StopConstants.SYSTEM_1.equals(system)) {
                    system1PauseState = false;
                } else if (StopConstants.SYSTEM_2.equals(system)) {
                    system2PauseState = false;
                }
                sendMessageTemplateByJson(StopConstants.URI_STOP_STATE, StopConstants.URI_STOP_STATE_MESSAGE_CLOSE_STOP);
                break;
            default:
                break;
        }

    }

    /**
     * 停车设备线总览
     *
     * @param system
     * @return
     */
    public StopIndexVO getStopIndex(String system) {
        StopIndexVO stopIndexVO = new StopIndexVO();
        Long runTime = null;
        //最后的启车的记录
        StartOperationRecord startOperationRecord = stopService.getStartOperationRecord(StopConstants.START_FINISH_STATE);
        if (startOperationRecord != null) {
            Date updateTime = startOperationRecord.getUpdateTime();
            runTime = DateTimeUtils.getDifferenceTime(updateTime, new Date(), StopConstants.TIMEFORMAT);
        }
        stopIndexVO.setThingRunTime(runTime);
        List<StopIndexVO.StopThingArea> stopThingAreas = new ArrayList<>();
        //查询大区下区域
        int lineRunCount = 0;
        List<StopDeviceArea> stopDeviceAreas = stopDeviceAreaService.getStopDeviceArea(StopConstants.REGION_1, Integer.valueOf(system));
        for (StopDeviceArea stopDeviceArea : stopDeviceAreas) {
            StopIndexVO.StopThingArea stopThingArea = stopIndexVO.new StopThingArea();
            stopThingArea.setAreaId(String.valueOf(stopDeviceArea.getId()));
            stopThingArea.setAreaName(stopDeviceArea.getAreaName());
            //区域下的停车线
            List<StopLine> stopLines = stopLineService.getStopLineByAreaId(stopDeviceArea.getId());
            stopThingArea.setLineCnt(String.valueOf(stopLines.size()));
            List<StopIndexVO.StopThingLine> stopThingLines = new ArrayList<>();
            for (StopLine stopLine : stopLines) {
                //待机
                Boolean lineRunState_1 = true;
                //运行
                Boolean lineRunState_2 = true;
                //故障
                Boolean lineRunState_4 = true;
                StopIndexVO.StopThingLine stopThingLine = stopIndexVO.new StopThingLine();
                stopThingLine.setLineId(String.valueOf(stopLine.getId()));
                stopThingLine.setLineName(stopLine.getLineName());
                //停车线下的停车包
                List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(stopLine.getId());
                for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
                    //查询停车包下的设备
                    List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
                    for (StopInformation stopInformation : stopInformations) {
                        String metricValue = getMetricValue(stopInformation.getThingCode(), MetricCodes.STATE);

                        if (!StopConstants.RUNSTATE_2.equals(metricValue)) {
                            lineRunState_2 = false;
                        }
                        if (!StopConstants.RUNSTATE_1.equals(metricValue)) {
                            lineRunState_1 = false;
                        }
                        if (!StopConstants.RUNSTATE_4.equals(metricValue)) {
                            lineRunState_4 = false;
                        }
                    }
                }
                stopThingLine.setLineRunState("");
                if (lineRunState_1) {
                    stopThingLine.setLineRunState(StopConstants.RUNSTATE_1);
                } else if (lineRunState_2) {
                    lineRunCount++;
                    stopThingLine.setLineRunState(StopConstants.RUNSTATE_2);
                } else if (lineRunState_4) {
                    stopThingLine.setLineRunState(StopConstants.RUNSTATE_4);
                }
                stopThingLines.add(stopThingLine);
            }
            stopThingArea.setStopThingLines(stopThingLines);
            stopThingAreas.add(stopThingArea);

        }
        stopIndexVO.setStopThingAreas(stopThingAreas);
        stopIndexVO.setThingRunCount(String.valueOf(lineRunCount));
        return stopIndexVO;
    }

    /**
     * 停车自检修复
     *
     * @param ruleIds
     * @param tapOperate
     */
    public void handleExamine(List<Integer> ruleIds, String tapOperate) {
        for (Integer ruleId : ruleIds) {
            StopExamineRule stopExamineRule = stopService.getStopExamineRuleByRuleId(ruleId);

            if (stopExamineRule != null) {


                switch (stopExamineRule.getExamineType()) {
                    // 控制类型
                    case StopConstants.REMOTE_ERROR_TYPE:
                        cmdControl(stopExamineRule.getExamineThingCode(), stopExamineRule.getExamineMetricCode(), stopExamineRule.getCompareValue());
                        break;
                    // 液位检查
                    case StopConstants.LEVEL_ERROR_TYPE:
                        // 不做处理
                        //TODO 更新自检记录
                        break;
                    default:
                        break;

                }
            }
        }

    }

    /**
     * 发送停车自检修复
     *
     * @param thingCode
     * @param metricCode
     * @param value
     */
    public void cmdControl(String thingCode, String metricCode, String value) {

        DataModel dataModel = new DataModel();
        dataModel.setThingCode(thingCode);
        dataModel.setMetricCode(metricCode);
        dataModel.setValue(value);
        logger.info("ThingCode{},MetricCode{},Value{}", thingCode, metricCode, value);
        CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(dataModel, SessionContext.getCurrentUser().getRequestId());
        if (cmdSendResponseData.getOkCount() == 0) {
            logger.error("下发信号失败，失败原因：" + cmdSendResponseData.getErrorMessage());
        }

    }
}
