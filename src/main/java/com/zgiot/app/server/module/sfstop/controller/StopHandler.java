package com.zgiot.app.server.module.sfstop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.sfstart.pojo.StartMessage;
import com.zgiot.app.server.module.sfstop.constants.StopConstants;
import com.zgiot.app.server.module.sfstop.entity.pojo.*;
import com.zgiot.app.server.module.sfstop.entity.vo.StopExamineThing;
import com.zgiot.app.server.module.sfstop.entity.vo.StopMessage;
import com.zgiot.app.server.module.sfstop.exception.StopException;
import com.zgiot.app.server.module.sfstop.service.*;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModelWrapper;
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

    // 当前系统暂停状态
    private static Boolean pauseState;

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

    public static Boolean getPauseState() {
        return pauseState;
    }

    public static void setPauseState(Boolean pauseState) {
        StopHandler.pauseState = pauseState;
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
                // 暂停启车
                //  writeSignalByLabel(StopConstants.PAUSE_STARTING_LABEL, StopConstants.VALUE_TRUE, StopConstants.LABEL_TYPE_BOOLEAN);
                sendMessageTemplateByJson(StopConstants.URI_STOP_STATE, StopConstants.URI_STOP_STATE_MESSAGE_PAUSE);
                break;
            case "continueStart":
                // 恢复启车
                //writeSignalByLabel(StopConstants.PAUSE_STARTING_LABEL, StopConstants.VALUE_FALSE, StopConstants.LABEL_TYPE_BOOLEAN);
                sendMessageTemplateByJson(StopConstants.URI_STOP_STATE, StopConstants.URI_STOP_STATE_MESSAGE_CONTINUE_START);
                break;
            case "closeStart":
                // 结束启车
                //writeSignalByLabel(StopConstants.START_DEVICE_LABEL, StopConstants.VALUE_FALSE, StopConstants.LABEL_TYPE_BOOLEAN);
                //writeSignalByLabel(StopConstants.PAUSE_STARTING_LABEL, StopConstants.VALUE_FALSE, StopConstants.LABEL_TYPE_BOOLEAN);
                stopService.closeStopOperate(system);
                sendMessageTemplateByJson(StopConstants.URI_STOP_STATE, StopConstants.URI_STOP_STATE_MESSAGE_CLOSE_START);
                break;
            default:
                break;
        }

    }
}
