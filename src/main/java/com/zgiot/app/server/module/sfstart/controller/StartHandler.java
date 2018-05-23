package com.zgiot.app.server.module.sfstart.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zgiot.app.server.dataprocessor.DataProcessor;
import com.zgiot.app.server.module.sfstart.StartExamineListener;
import com.zgiot.app.server.module.sfstart.StartListener;
import com.zgiot.app.server.module.sfstart.constants.StartStopConstants;
import com.zgiot.app.server.module.sfstart.enums.EnumDeviceCode;
import com.zgiot.app.server.module.sfstart.exception.StartException;
import com.zgiot.app.server.module.sfstart.pojo.*;
import com.zgiot.app.server.module.sfstart.service.StartService;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.impl.mapper.TMLMapper;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.MetricModel;
import com.zgiot.common.pojo.SessionContext;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 启车辅助类 （信号）
 */
@Component
public class StartHandler {
    private static final Logger logger = LoggerFactory.getLogger(StartHandler.class);

    @Autowired
    private StartService startService;

    @Autowired
    private DataService dataService;


    // 开始启车时间
    private static Date startTime;

    public static Date getStartTime() {
        return startTime;
    }

    public static void setStartTime(Date startTime) {
        StartHandler.startTime = startTime;
    }

    // 当前系统暂停状态
    private static Boolean pauseState;

    public static Boolean getPauseState() {
        return pauseState == null ? false : pauseState;
    }

    public static void setPauseState(Boolean pauseState) {
        StartHandler.pauseState = pauseState;
    }

    private static BlockingQueue blockingQueue;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static List<StartDeviceRequirement> startDeviceRequirements;

    public List<StartDeviceRequirement> getStartDeviceRequirements() {
        return startDeviceRequirements;
    }

    public static void setStartDeviceRequirements(List<StartDeviceRequirement> startDeviceRequirements) {
        StartHandler.startDeviceRequirements = startDeviceRequirements;
    }

    @Autowired
    private TMLMapper tmlMapper;

    @Autowired
    private CmdControlService cmdControlService;

    @Autowired
    @Qualifier("wsProcessor")
    private DataProcessor processor;
    @Autowired
    private StartExamineListener startExamineListener;

    @Autowired
    private StartListener startListener;

    private static String CMDLOG = "ThingCode:{},MetricCode :{},信号下发数据值:{}";

    private static String CMD_FAILED_LOG = "下发信号失败，失败原因：";

    /**
     * 判断当前启车状态
     *
     * @return
     */
    public StartOperationRecord getStartState() {
        List<StartOperationRecord> querystartOperationRecord = startService.findUnfinishStartOperate(null, StartStopConstants.START_FINISH_STATE);
        if (CollectionUtils.isEmpty(querystartOperationRecord)) {
            StartOperationRecord startOperationRecord = new StartOperationRecord();
            startOperationRecord.setOperateState(StartStopConstants.START_NO_STATE);
            return startOperationRecord;
        } else if (querystartOperationRecord.size() > 1) {
            throw new StartException("当前起车数据异常，存在多条启动中有效数据，请检查。");
        }
        return querystartOperationRecord.get(0);
    }

    /**
     * 获得启车系统信息根据类型
     *
     * @return
     * @throws Exception
     */
    public List<StartSystem> getStartSystem() {
        // 查询系统分类
        List<StartSystem> startSystems = new LinkedList<>();
        // 查询二期系统
        startSystems.addAll(startService.getStartSystem(StartStopConstants.PRODUCTION_LINE_TWO, StartStopConstants.SYSTEM_TYPE_SYSTEM, StartStopConstants.SYSTEM_CATEGORY_SELECT_PAGE));
        // 查询二期公用设备
        startSystems.addAll(startService.getStartSystem(StartStopConstants.PRODUCTION_LINE_TWO, StartStopConstants.SYSTEM_TYPE_DEVICE, StartStopConstants.SYSTEM_CATEGORY_SELECT_PAGE));
        return startSystems;
    }

    /**
     * 获得启车系统信息根据类型
     *
     * @return
     * @throws Exception
     */
    public List<StartSystem> getStartingSystem() {
        // 查询系统分类
        List<StartSystem> startSystems = new LinkedList<>();
        // 查询系统名称系统
        startSystems.addAll(startService.getStartSystemWithStarting(StartStopConstants.SYSTEM_CATEGORY_STARTING_PAGE));
        startService.getStartinSystemWithDeviceInformation(startSystems);
        return startSystems;
    }

    /**
     * 启车中操作(包括启车结束检查)
     *
     * @param operate
     */
    public void operateStarting(String operate) throws Exception {
        switch (operate) {
            case "pause":
                // 暂停启车
                writeSignalByLabel(StartStopConstants.PAUSE_STARTING_LABEL, StartStopConstants.VALUE_TRUE, StartStopConstants.LABEL_TYPE_BOOLEAN);
                sendMessageTemplateByJson(StartStopConstants.URI_START_STATE, StartStopConstants.URI_START_STATE_MESSAGE_PAUSE);
                break;
            case "continueStart":
                // 恢复启车
                writeSignalByLabel(StartStopConstants.PAUSE_STARTING_LABEL, StartStopConstants.VALUE_FALSE, StartStopConstants.LABEL_TYPE_BOOLEAN);
                sendMessageTemplateByJson(StartStopConstants.URI_START_STATE, StartStopConstants.URI_START_STATE_MESSAGE_CONTINUE_START);
                break;
            case "closeStart":
                // 结束启车
                writeSignalByLabel(StartStopConstants.START_DEVICE_LABEL, StartStopConstants.VALUE_FALSE, StartStopConstants.LABEL_TYPE_BOOLEAN);
                writeSignalByLabel(StartStopConstants.PAUSE_STARTING_LABEL, StartStopConstants.VALUE_FALSE, StartStopConstants.LABEL_TYPE_BOOLEAN);
                startService.closeStartOperate();
                sendMessageTemplateByJson(StartStopConstants.URI_START_STATE, StartStopConstants.URI_START_STATE_MESSAGE_CLOSE_START);
                break;
            case "closeExamine":
                // 结束检查
                startService.closeStartOperate();
                sendMessageTemplateByJson(StartStopConstants.URI_START_STATE, StartStopConstants.URI_START_STATE_MESSAGE_CLOSE_EXAMINE);
                break;
            default:
                break;
        }
    }

    /**
     * 发送信号
     *
     * @param label
     * @param value
     * @param boolreal
     * @throws Exception
     */
    public void writeSignalByLabel(String label, int value, int boolreal) throws Exception {
        // 发送命令操作
        Object valueShort = null;

        switch (boolreal) {
            case StartStopConstants.LABEL_TYPE_SHORT:
                valueShort = (short) value;
                break;
            case StartStopConstants.LABEL_TYPE_BOOLEAN:
                valueShort = 1 == value;
                break;
            default:
                break;
        }
        ThingMetricCode startSignalByDataLabel = startService.getStartSignalByDataLabel(label);
        DataModel dataModel = new DataModel();
        dataModel.setThingCode(startSignalByDataLabel.getThingCode());
        dataModel.setMetricCode(startSignalByDataLabel.getMetricCode());
        dataModel.setValue(String.valueOf(valueShort));
        CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(dataModel, "writeSignalByLabel");
        if (cmdSendResponseData.getOkCount() == 0) {
            logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage(),
                    SysException.EC_CMD_FAILED);
        }
        logger.info(CMDLOG, startSignalByDataLabel.getThingCode(), startSignalByDataLabel.getMetricCode(), valueShort);
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
            logger.error("启停下发信号存储错误");
        }
    }

    protected void createMessagingTemplateTimer() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!blockingQueue.isEmpty()) {
                    try {
                        StartMessage startMessage = (StartMessage) blockingQueue.take();
                        messagingTemplate.convertAndSend(startMessage.getLabel(), startMessage.getObject());
                    } catch (Exception e) {
                        logger.error("启车发送任务失败");
                    }
                }
            }
        };
        timer.schedule(timerTask, StartStopConstants.SEND_MESSAGE_CIRCLE_TIME, StartStopConstants.SEND_MESSAGE_WAIT_TIME);
    }


    /**
     * 获取所有启车设备id
     *
     * @param systemIds
     * @return
     */
    public Set<String> getStartDeviceIds(List<String> systemIds) {
        Set<String> startDeviceIds = getAllDeviceIdBySystenIds(systemIds);
        return startDeviceIds;
    }

    /**
     * 根据系统类型，返回设备信息列表
     *
     * @param systemIds 系统信息
     * @return
     */
    private Set<String> getAllDeviceIdBySystenIds(List<String> systemIds) {
        List<String> deviceIds = new ArrayList<>();
        List<StartDevice> startDevices = startService.getDeviceBySystenIds(systemIds);
        for (StartDevice startDevice : startDevices) {
            deviceIds.add(startDevice.getDeviceId());
        }
        HashSet<String> deviceIdHashSet = new HashSet<>(deviceIds);
        return deviceIdHashSet;
    }


    /**
     * 发送每台设备给plc
     *
     * @param getOperateId 本次启车操作id
     * @return
     */
    public void sendStartInformation(Integer getOperateId) throws Exception {
        // 信号发送等待预值
        Thread.sleep(StartStopConstants.SEND_SINGLE_VALUE_WAIT_TIME);
        // 发送变压器的数值
        sendTransformation();
        // 发送包信息
        sendPackageInfromation();
        // 发送区域信息
        sendAreaInfromation();

        // 查询操作设备号及其顺序
        List<StartDeviceInformation> deviceInformationLists = startService.selectDeviceInformationBySystemId(getOperateId, null);
        for (StartDeviceInformation deviceInformation : deviceInformationLists) {
            logger.info("设备{}启动顺序为{}，功率为{}，变压器号为{}，等待时间为{}, 大区区域包信息:{}", deviceInformation.getDeviceId(),
                    deviceInformation.getStartSequence(), deviceInformation.getRateWork(),
                    deviceInformation.getTransformerId(), deviceInformation.getStartWaitTime(), deviceInformation.getStartHierarchy());
            compareAndSendSignal(deviceInformation.getDeviceId(), StartStopConstants.IS_STARTING, "设备是否参与启车", (float) StartStopConstants.VALUE_TRUE);
            compareAndSendSignal(deviceInformation.getDeviceId(), StartStopConstants.IS_REQUIREMENT, "清除启车前置条件", (float) StartStopConstants.VALUE_FALSE);
            compareAndSendSignal(deviceInformation.getDeviceId(), StartStopConstants.RATE_WORK, "起车发送该设备功率", deviceInformation.getRateWork());
            compareAndSendSignal(deviceInformation.getDeviceId(), StartStopConstants.TRANSFORMER_ID, "起车设备所属变压器号", (float) deviceInformation.getTransformerId());
            compareAndSendSignal(deviceInformation.getDeviceId(), StartStopConstants.START_WAIT_TIME, "起车发送该设备等待时间", (float) deviceInformation.getStartWaitTime());
            String[] deviceHierarchy = deviceInformation.getStartHierarchy().split("-");
            compareAndSendSignal(deviceInformation.getDeviceId(), StartStopConstants.DEVICE_REGION_ID, "起车发送大区信息", Float.parseFloat(deviceHierarchy[0]));
            compareAndSendSignal(deviceInformation.getDeviceId(), StartStopConstants.DEVICE_AREA_ID, "起车发送区域信息", Float.parseFloat(deviceHierarchy[1]));
            compareAndSendSignal(deviceInformation.getDeviceId(), StartStopConstants.DEVICE_BAG_ID, "起车发送包信息", Float.parseFloat(deviceHierarchy[2]));
        }


    }

    /**
     * 发送区域信息
     */
    private void sendAreaInfromation() throws Exception {
        // 区域所属大区
        List<StartSingleLabelAndValue> startSingleLabelAndValues = startService.selectAreaBelongRegion(EnumDeviceCode.AREA.getInfo(), StartStopConstants.AREA_BELONG_REGION);
        for (StartSingleLabelAndValue startSingleLabelAndValue : startSingleLabelAndValues) {
            float plcValue = getSystemMessageValue(startSingleLabelAndValue.getDataLabel());
            if (plcValue != startSingleLabelAndValue.getValue()) {
                logger.info("启车区域所属大区datalabel:{}的值为{}", startSingleLabelAndValue.getDataLabel(), startSingleLabelAndValue.getValue());


                ThingMetricCode startSignalByDataLabel = startService.getStartSignalByDataLabel(startSingleLabelAndValue.getDataLabel());
                DataModel dataModel = new DataModel();
                dataModel.setThingCode(startSignalByDataLabel.getThingCode());
                dataModel.setMetricCode(startSignalByDataLabel.getMetricCode());
                dataModel.setValue(String.valueOf(startSingleLabelAndValue.getValue().intValue()));
                CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(dataModel, "sendPackageInfromation");
                if (cmdSendResponseData.getOkCount() == 0) {
                    logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage());
                }
                logger.info(CMDLOG, startSignalByDataLabel.getThingCode(), startSignalByDataLabel.getMetricCode(), startSingleLabelAndValue.getValue());
                logger.info("{}信号下发信息:{}", startSingleLabelAndValue.getDataLabel(), startSingleLabelAndValue.getValue());

            }
        }
    }

    /**
     * 发送包信息
     */
    private void sendPackageInfromation() throws Exception {
        // 包等待时间信息
        List<StartSingleLabelAndValue> startSingleLabelAndValues = startService.selectPackageWaitTime(EnumDeviceCode.PACKAGE.getInfo(), StartStopConstants.WAIT_TIME);
        // 包所属区域
        startSingleLabelAndValues.addAll(startService.selectPackageBelongArea(EnumDeviceCode.PACKAGE.getInfo(), StartStopConstants.BAG_BELONG_AREA));
        // 包所属大区
        startSingleLabelAndValues.addAll(startService.selectPackageBelongRegion(EnumDeviceCode.PACKAGE.getInfo(), StartStopConstants.BAG_BELONG_REGION));
        for (StartSingleLabelAndValue startSingleLabelAndValue : startSingleLabelAndValues) {
            float plcValue = getSystemMessageValue(startSingleLabelAndValue.getDataLabel());
            if (plcValue != startSingleLabelAndValue.getValue()) {
                logger.info("启车包信息 datalabel:{}的值为{}", startSingleLabelAndValue.getDataLabel(), startSingleLabelAndValue.getValue());
                ThingMetricCode startSignalByDataLabel = startService.getStartSignalByDataLabel(startSingleLabelAndValue.getDataLabel());
                DataModel dataModel = new DataModel();
                dataModel.setThingCode(startSignalByDataLabel.getThingCode());
                dataModel.setMetricCode(startSignalByDataLabel.getMetricCode());
                dataModel.setValue(String.valueOf(startSingleLabelAndValue.getValue()));
                CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(dataModel, "sendPackageInfromation");
                if (cmdSendResponseData.getOkCount() == 0) {
                    logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage());
                }
                logger.info(CMDLOG, startSignalByDataLabel.getThingCode(), startSignalByDataLabel.getMetricCode(), startSingleLabelAndValue.getValue());
                logger.info("{}信号下发信息:{}", startSingleLabelAndValue.getDataLabel(), startSingleLabelAndValue.getValue());

            }
        }
    }

    /**
     * 向PLC发送变压器信息
     */
    private void sendTransformation() throws Exception {
        List<StartSingleLabelAndValue> startSingleLabelAndValues = startService.selectTransformerInformation(StartStopConstants.TRANSFORMER_VALUE);
        for (StartSingleLabelAndValue startSingleLabelAndValue : startSingleLabelAndValues) {
            float plcValue = getSystemMessageValue(startSingleLabelAndValue.getDataLabel());
            if (plcValue != startSingleLabelAndValue.getValue()) {
                logger.info("启车变压器datalabel:{}的值为{}", startSingleLabelAndValue.getDataLabel(), startSingleLabelAndValue.getValue());
                ThingMetricCode startSignalByDataLabel = startService.getStartSignalByDataLabel(startSingleLabelAndValue.getDataLabel());
                DataModel dataModel = new DataModel();
                dataModel.setThingCode(startSignalByDataLabel.getThingCode());
                dataModel.setMetricCode(startSignalByDataLabel.getMetricCode());
                dataModel.setValue(String.valueOf(startSingleLabelAndValue.getValue()));
                CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(dataModel, "sendTransformation");
                if (cmdSendResponseData.getOkCount() == 0) {
                    logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage(),
                            SysException.EC_CMD_FAILED);
                }
                logger.info(CMDLOG, startSignalByDataLabel.getThingCode(), startSignalByDataLabel.getMetricCode(), startSingleLabelAndValue.getValue());
            }
        }
    }

    /**
     * 反馈系统类信号点值
     *
     * @param label
     * @return
     */
    private float getSystemMessageValue(String label) {
        ThingMetricCode startSignalByDataLabel = startService.getStartSignalByDataLabel(label);
        DataModelWrapper dataModelWrapper = dataService.getData(startSignalByDataLabel.getThingCode(), startSignalByDataLabel.getMetricCode()).orElse(null);
        if (dataModelWrapper != null) {
            return Float.valueOf(dataModelWrapper.getValue());
        }
        return 0f;
    }


    /**
     * 创建启车任务
     *
     * @param startDeviceIds 启车设备id
     */
    public void createDeviceRequirement(Set<String> startDeviceIds) {
        List<StartDeviceRequirement> deviceRequirements = new ArrayList<>();
        for (String deviceId : startDeviceIds) {
            StartDeviceRequirement deviceRequirement = new StartDeviceRequirement();
            deviceRequirement.setDeviceId(deviceId);
            deviceRequirement.setStartRequirements(startService.getRequirementByDeviceId(deviceId));
            deviceRequirements.add(deviceRequirement);
        }
        // 设置启车前置条件检查项
        startDeviceRequirements = deviceRequirements;
    }

    /**
     * 开始启车自检
     *
     * @param startExamineRules
     */
    public void autoExamineStarting(List<StartExamineRule> startExamineRules) {
        List<String> examineLabel = new ArrayList<>();
        for (StartExamineRule rule : startExamineRules) {
            try {
                StartSignal startSignal = startService.getStartSignalByDeviceId(rule.getExamineDeviceId());
                logger.info("启车自检,检查设备:{},检查内容:{}", rule.getExamineDeviceId(), rule.getExamineName());
                DataModelWrapper data = dataService.getData(startSignal.getDeviceCode(), MetricCodes.STATE).orElse(null);
                if (data != null) {
                    updateExamineRecord(rule, Double.valueOf(data.getValue()));

                }
            } catch (Exception e) {
                logger.error("检查建立失败,错误内容{}", e);
                throw new StartException("自动检查任务失败，请稍后重新提交");
            }
            examineLabel.addAll(startService.selectLabelByDeviceIdAndName(rule.getExamineDeviceId(), rule.getExamineName()));
        }
        startExamineListener.setStartExamineLabels(examineLabel);
        processor.removeListener(startExamineListener);
        processor.addListener(startExamineListener);

    }

    /**
     * 修改检查记录
     *
     * @param startExamineRule
     * @param value
     */
    public void updateExamineRecord(StartExamineRule startExamineRule, Double value) {

        //是否发生改变
        Boolean flag = false;
        Integer examineResult = null;
        String examineInformation = "";
        List<StartExamineRecord> startExamineRecords = startService.getStartExaminRecordByRuleAndOperateId(startExamineRule.getRuleId(), startService.findOperateIdWhenNull());
        if (null == startExamineRecords || startExamineRecords.size() == 0) {
            // 规则校验是否属于本次启车
            return;
        }
        if (estimateExamineResult(startExamineRule, value) && startExamineRecords.get(0).getExamineResult() != StartStopConstants.EXAMINE_RESULT_RIGHT) {
            // 保存正常的检测内容
            examineResult = StartStopConstants.EXAMINE_RESULT_RIGHT;
            examineInformation = null;
            flag = true;
        }
        if (!estimateExamineResult(startExamineRule, value) && startExamineRecords.get(0).getExamineResult() != StartStopConstants.EXAMINE_RESULT_ERROR) {
            // 检测异常时根据检查类型不同判断错误信息
            examineResult = StartStopConstants.EXAMINE_RESULT_ERROR;
            flag = true;
            // 获得故障信息反馈
            examineInformation = getExamineInformation(startExamineRule, value);
        }
        // 阀门判定特殊逻辑
        try {

            StartSignal startSignal = startService.getStartSignalByDeviceId(startExamineRule.getExamineDeviceId());
            DataModelWrapper closeData = dataService.getData(startSignal.getDeviceCode(), MetricCodes.TAP_CLOSE).orElse(null);
            DataModelWrapper openData = dataService.getData(startSignal.getDeviceCode(), MetricCodes.TAP_OPEN).orElse(null);
            if (closeData != null && openData != null && startExamineRule.getExamineType() == StartStopConstants.TAP_ERROR_TYPE && openData.getValue().equals(closeData.getValue())) {
                examineResult = StartStopConstants.EXAMINE_RESULT_ERROR;
                examineInformation = "异常";
                flag = true;
            }

        } catch (Exception e) {
            logger.error("自检阀门判断错误");
        }
        // 发生改变修改逻辑，并通知前段
        if (flag) {
            logger.info("规则{}自检结果状态改变改为{},描述信息为{}", startExamineRule.getRuleId(), examineResult, examineInformation);
            startService.updateStartExamineRecord(startExamineRule.getRuleId(), startService.findOperateIdWhenNull(), examineResult, examineInformation);
            startExamineRecords.get(0).setExamineResult(examineResult);
            startExamineRecords.get(0).setExamineInformation(examineInformation);
            sendMessagingTemplate(StartStopConstants.URI_START_AUTO_EXAMINE, startExamineRecords.get(0));
        }
    }

    /**
     * 获取故障内容和信息
     *
     * @param startExamineRule
     * @param value
     * @return
     */
    private String getExamineInformation(StartExamineRule startExamineRule, Double value) {
        String examineInformation = "";
        switch (startExamineRule.getExamineType()) {
            case StartStopConstants.REMOTE_ERROR_TYPE:
                examineInformation = "就地";
                break;
            case StartStopConstants.DEVICE_ERROR_TYPE:
                examineInformation = startService.getFaultByDeviceId(startExamineRule.getExamineDeviceId());
                break;
            case StartStopConstants.TAP_ERROR_TYPE:
                if (StartStopConstants.TAP_CLOSE.equals(startExamineRule.getExamineName())) {
                    examineInformation = "关到位";
                } else if (StartStopConstants.TAP_OPEN.equals(startExamineRule.getExamineName())) {
                    examineInformation = "开到位";
                }
                break;
            case StartStopConstants.LEVEL_ERROR_TYPE:
                // 保留小数点两位
                DecimalFormat df = new DecimalFormat("#0.00");
                examineInformation = df.format(value) + "m";
                break;

            default:
                break;
        }
        return examineInformation;
    }

    /**
     * 启车自检规则结果判断
     *
     * @param startExamineRule
     * @param value
     * @return
     */
    private Boolean estimateExamineResult(StartExamineRule startExamineRule, Double value) {
        Boolean flag = false;
        switch (startExamineRule.getOperator()) {
            case StartStopConstants.COMPARE_GREATER_THAN:
                if (value > startExamineRule.getCompareValue()) {
                    flag = true;
                }
                break;
            case StartStopConstants.COMPARE_LESS_THAN:
                if (value < startExamineRule.getCompareValue()) {
                    flag = true;
                }
                break;
            case StartStopConstants.COMPARE_EQUAL_TO:
                if (value == startExamineRule.getCompareValue()) {
                    flag = true;
                }
                break;
            case StartStopConstants.COMPARE_GREATER_THAN_AND_EQUAL_TO:
                if (value >= startExamineRule.getCompareValue()) {
                    flag = true;
                }
                break;
            case StartStopConstants.COMPARE_LESS_THAN_AND_EQUAL_TO:
                if (value <= startExamineRule.getCompareValue()) {
                    flag = true;
                }
                break;
            case StartStopConstants.COMPARE_NOT_EQUAL_TO:
                if (value != startExamineRule.getCompareValue()) {
                    flag = true;
                }
                break;
            default:
                break;
        }
        return flag;
    }

    /**
     * 建立启车人工干预
     *
     * @param deviceIds
     */
    public void createManualInterventionRecord(Set<String> deviceIds, Integer operateId) throws Exception {
        for (String deviceId : deviceIds) {
            StartManualInterventionRecord startManualInterventionRecord = new StartManualInterventionRecord();
            startManualInterventionRecord.setDeviceId(deviceId);
            startManualInterventionRecord.setOperateId(operateId);
            StartManualInterventionDevice startManualInterventionDevice = startService.selectManualInterventionStateByDeviceId(deviceId).get(0);
            startManualInterventionRecord.setManualInterventionPerson(startManualInterventionDevice.getPersonId());
            // 发送干预设置到PLC
            if (StartStopConstants.MANUAL_INTERVENTION_FALSE == startManualInterventionDevice.getState()) {
                // 不人工干预
                startManualInterventionRecord.setInterventionState(StartStopConstants.MANUAL_INTERVENTION_FALSE);
            } else {
                // 人工干预
                startManualInterventionRecord.setInterventionState(StartStopConstants.MANUAL_INTERVENTION_TRUE);
            }
            try {
                sendManualInterventionrState(startManualInterventionRecord.getDeviceId(), startManualInterventionRecord.getInterventionState());
            } catch (Exception e) {
                logger.error("设备:{}，人工干预信号发送失败", startManualInterventionRecord.getDeviceId());
                logger.error(e.getMessage());
                throw new StartException("人工干预发送失败，请稍后重新提交");
            }
            // 保存本次启车人工干预记录
            startService.saveManualInterventionRecord(startManualInterventionRecord);
            // 解除所有临时人工干预设置
            startService.updateZgkwStartManualIntervention(null, null, StartStopConstants.MANUAL_INTERVENTION_FALSE, StartStopConstants.MANUAL_INTERVENTION_TEMPORARY);
        }
    }

    /**
     * 发送设备人工干预设置
     *
     * @param deviceId 限定发送设备
     * @param value    发送值
     */

    public void sendManualInterventionrState(String deviceId, Integer value) throws Exception {
        // 发送启车预设定状态
        compareAndSendSignal(deviceId, StartStopConstants.MANUAL_INTERVENTION, "启车人工干预", (float) value);
    }

    /**
     * 启车信号比较是否正确，错误修正
     *
     * @param deviceId
     * @param nameId
     * @param operateUserId
     * @param value
     */
    private void compareAndSendSignal(String deviceId, String nameId, String operateUserId, Float value) throws Exception {
        StartSignal startSignal = startService.getStartSignalByDeviceId(deviceId);
        StartDeviceSignal startDeviceSignal = startService.getStartDeviceSignalById(Integer.parseInt(nameId));
        MetricModel metricModel = tmlMapper.findMetricByMetricName(startDeviceSignal.getName());
        DataModelWrapper dataModelWrapper = dataService.getData(startSignal.getDeviceCode(), metricModel.getMetricCode()).orElse(null);
        float singleValue = 0f;
        if (dataModelWrapper != null&&StringUtils.isNumeric(dataModelWrapper.getValue())) {
            singleValue = Float.valueOf(dataModelWrapper.getValue());
            logger.info("startSingleValue:{}", singleValue);
        }
        if (singleValue != value) {
            operateSignal(deviceId, metricModel.getMetricCode(), operateUserId, value);
        }
    }


    /**
     * 返回总览页面设备信息
     *
     * @return
     */
    public List<StartDevice> getStartBrowseDevice() throws Exception {
        List<StartDevice> startDeviceStates = new ArrayList<>();
        List<String> deviceIds = startService.selectStartDeviceIdBySystemCategory(StartStopConstants.SYSTEM_CATEGORY_BROWSE_PAGE, null);
        // 通过deviceId 查询deviceCode  在查询带煤量的信号数据
        for (String deviceId : deviceIds) {
            StartDevice startDevice = startService.selectStartDeviceByDeviceId(deviceId);
            if (null != startDevice) {
                DataModelWrapper dataModelState = dataService.getData(startDevice.getDeviceCode(), MetricCodes.STATE).orElse(null);
                if (dataModelState != null) {
                    startDevice.setDeviceState(Integer.parseInt(dataModelState.getValue()));
                } else {
                    startDevice.setDeviceState(0);
                }
                DataModelWrapper dataModelCap = dataService.getData(startDevice.getDeviceCode(), MetricCodes.COAL_CAP).orElse(null);
                if (dataModelCap != null) {
                    startDevice.setCoalCapacity(Double.valueOf(dataModelCap.getValue()));
                } else {
                    startDevice.setCoalCapacity(null);
                }
                startDeviceStates.add(startDevice);
            }
        }
        return startDeviceStates;
    }


    /**
     * 启车自检修复
     *
     * @param ruleIds    被修复检查规则id
     * @param tapOperate 阀门修复类型
     * @param userUuid   操作人
     * @throws Exception
     */
    public void handleExamine(List<Integer> ruleIds, String tapOperate, String userUuid) throws Exception {
        for (Integer ruleId : ruleIds) {
            List<StartExamineRule> startExaminRuleByRuleIdAndLabel = startService.getStartExaminRuleByRuleIdAndLabel(ruleId, null);
            StartExamineRule startExamineRule = null;
            if (CollectionUtils.isNotEmpty(startExaminRuleByRuleIdAndLabel)) {
                startExamineRule = startService.getStartExaminRuleByRuleIdAndLabel(ruleId, null).get(0);

                switch (startExamineRule.getExamineType()) {
                    // 控制类型
                    case StartStopConstants.REMOTE_ERROR_TYPE:
                        operateSignal(startExamineRule.getExamineDeviceId(), StartStopConstants.REMOTE_LOCAL_CONTROL, userUuid, (float) startExamineRule.getCompareValue());
                        break;
                    // 故障类别
                    case StartStopConstants.DEVICE_ERROR_TYPE:
                        operateSignal(startExamineRule.getExamineDeviceId(), StartStopConstants.HANDLE_EXCEPTION, userUuid, (float) StartStopConstants.VALUE_TRUE);
                        break;
                    // 阀门开关
                    case StartStopConstants.TAP_ERROR_TYPE:
                        dealTapErrorType(tapOperate, userUuid, startExamineRule);
                        break;
                    // 液位检查
                    case StartStopConstants.LEVEL_ERROR_TYPE:
                        // 不做处理
                        break;
                    default:
                        break;

                }
            }
        }


    }

    /**
     * 处理阀门错误类型
     *
     * @param tapOperate
     * @param userUuid
     * @param startExamineRule
     */
    private void dealTapErrorType(String tapOperate, String userUuid, StartExamineRule startExamineRule) {
        if (StartStopConstants.REMOTE_OPEN_TAP.equals(tapOperate) || StartStopConstants.REMOTE_CLOSE_TAP.equals(tapOperate)) {
            // 用户指定阀门操作类型
            operateSignal(startExamineRule.getExamineDeviceId(), tapOperate, userUuid, (float) StartStopConstants.VALUE_TRUE);
        }
        if (StartStopConstants.TAP_CLOSE.equals(startExamineRule.getExamineName())) {
            //规则判断关阀门
            operateSignal(startExamineRule.getExamineDeviceId(), StartStopConstants.REMOTE_CLOSE_TAP, userUuid, (float) StartStopConstants.VALUE_TRUE);
        }
        if (StartStopConstants.TAP_OPEN.equals(startExamineRule.getExamineName())) {
            //规则判断开阀门
            operateSignal(startExamineRule.getExamineDeviceId(), StartStopConstants.REMOTE_OPEN_TAP, userUuid, (float) StartStopConstants.VALUE_TRUE);
        }
    }

    /**
     * 发送操作信号
     *
     * @param deviceId      设备号
     * @param metricCode    信号值
     * @param operateUserId 操作者id
     * @param value         操作值
     * @throws Exception
     */
    public void operateSignal(String deviceId, String metricCode, String operateUserId, Float value) {
        logger.info("deviceId:{},name:{}启车模块操作信号开始发送。", deviceId, metricCode);
        long begin = System.currentTimeMillis();
        StartSignal startSignal = startService.getStartSignalByDeviceId(deviceId);
        DataModel dataModel = new DataModel();
        dataModel.setThingCode(startSignal.getDeviceCode());
        StartDeviceSignal startDeviceSignal = startService.getStartDeviceSignalById(startSignal.getName());
        MetricModel metricModel = tmlMapper.findMetricByMetricName(startDeviceSignal.getName());
        dataModel.setMetricCode(metricModel.getMetricCode());
        dataModel.setValue(String.valueOf(value));
        CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(dataModel,"发送信号");
        if (cmdSendResponseData.getOkCount() == 0) {
            logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage());
        }
        logger.info(CMDLOG, startSignal.getDeviceCode(), metricCode, value);
        logger.info("deviceId:{},name:{}启车模块操作信号已经发送成功,cost:{}ms", deviceId, metricCode, (System.currentTimeMillis() - begin));
    }

    /**
     * 启车设置人工干预
     *
     * @param deviceId
     * @return
     */
    public String setManualIntervention(String deviceId, String userId) throws Exception {
        String infromation = "success";
        if (startService.judgeStartingState(null, StartStopConstants.START_FINISH_STATE)) {
            // 启车中
            sendManualInterventionrState(deviceId, StartStopConstants.VALUE_TRUE);
            StartSignal startSignal = startService.getStartSignalByDeviceId(deviceId);
            DataModelWrapper dataModelWrapper = dataService.getData(startSignal.getDeviceCode(), MetricCodes.STATE).orElse(null);
            Double value = null;
            if (dataModelWrapper != null) {
                value = Double.parseDouble(dataModelWrapper.getValue());
                if ((short) StartStopConstants.DEVICE_STATE_WORKING != value) {
                    // 设备未启动
                    Integer operteId = startService.findOperateIdWhenNull();
                    startService.updateStartManualInterventionRecord(deviceId, operteId, StartStopConstants.MANUAL_INTERVENTION_TRUE, userId, null);
                } else {
                    // 设备已启动
                    infromation = "manualInterventionerrorByStarting";
                }
            }

        } else {
            // 启车前
            startService.updateZgkwStartManualIntervention(deviceId, userId, StartStopConstants.MANUAL_INTERVENTION_TEMPORARY, StartStopConstants.MANUAL_INTERVENTION_FALSE);
        }
        // 通知前端人工干预变化
        sendMessageTemplateByJson(StartStopConstants.URI_MANUAL_INTERVENTION, StartStopConstants.URI_MANUAL_INTERVENTION_MESSAGE_MANUAL_INTERVENTION_CHANGE);
        return infromation;
    }

    /**
     * 解除人工干预
     *
     * @param deviceId 解除设备id
     * @param userId   解除人id
     * @return
     * @throws Exception
     */
    public String relieveManualIntervention(String deviceId, String userId) throws Exception {
        String information;
        if (startService.judgeStartingState(null, StartStopConstants.START_FINISH_STATE)) {
            Integer operteId = startService.findOperateIdWhenNull();
            sendManualInterventionrState(deviceId, StartStopConstants.VALUE_FALSE);
            startService.updateStartManualInterventionRecord(deviceId, operteId, StartStopConstants.MANUAL_INTERVENTION_REMOVE, null, userId);
            // 通知前端人工干预变化
            sendMessageTemplateByJson(StartStopConstants.URI_MANUAL_INTERVENTION, StartStopConstants.URI_MANUAL_INTERVENTION_MESSAGE_MANUAL_INTERVENTION_CHANGE);
            information = "success";
        } else {
            information = "noStarting";
        }
        return information;
    }

    /**
     * 获得包内设备限制关系
     *
     * @param packageId
     * @return
     */
    public List<StartDeviceRelation> getStartDeviceRelationByPackageId(String packageId, List<String> systemIds) throws Exception {
        Set<String> startDeviceIds = getStartDeviceIds(systemIds);
        List<StartDeviceRelation> relations = getStartDeviceRelationByPackage(packageId, null);
        List<StartDeviceRelation> relationsFilterByDeviceId = filterDeviceRelation(relations, startDeviceIds, StartStopConstants.FILTER_TYPE_DEVICE_ID);
        List<StartDeviceRelation> relationsFilterAll = filterDeviceRelation(relationsFilterByDeviceId, startDeviceIds, StartStopConstants.FILTER_TYPE_CONTROL_DEVICE_ID);
        return relationsFilterAll;
    }

    /**
     * 获取设备关系根据packageId
     *
     * @param packageId
     * @param deviceId
     * @return
     */
    public List<StartDeviceRelation> getStartDeviceRelationByPackage(String packageId, String deviceId) throws Exception {
        List<StartDeviceRelation> startDeviceRelations = startService.selectStartDeviceRelationByPackageIdAndDeviceId(packageId, deviceId);
        for (StartDeviceRelation relation : startDeviceRelations) {
            // 读取设备状态 信号
            Double value;
            DataModelWrapper dataModelWrapper = dataService.getData(relation.getControlDeviceCode(), MetricCodes.STATE).orElse(null);
            if (dataModelWrapper != null) {
                value = Double.parseDouble(dataModelWrapper.getValue());
                relation.setControlDeviceState(value);
                if (value == 1) {
                    relation.setAbnormal(startService.getFaultByDeviceId(relation.getControlDeviceId()));
                }
            }
        }
        return startDeviceRelations;
    }

    /**
     * 根据条件筛选信息
     *
     * @param startDeviceRelations
     * @param startDeviceIds
     * @return
     */
    public List<StartDeviceRelation> filterDeviceRelation(List<StartDeviceRelation> startDeviceRelations, Set<String> startDeviceIds, String fileterType) {
        List<StartDeviceRelation> relationsFilerByDeviceId = new ArrayList<>();
        for (StartDeviceRelation relation : startDeviceRelations) {
            for (String deviceId : startDeviceIds) {
                // 当控制设备条件在本次启车中时加入筛选
                if (deviceId.equals(relation.getDeviceId()) && fileterType.equals(StartStopConstants.FILTER_TYPE_DEVICE_ID)) {
                    relationsFilerByDeviceId.add(relation);
                    break;
                }
                if (deviceId.equals(relation.getControlDeviceId()) && fileterType.equals(StartStopConstants.FILTER_TYPE_CONTROL_DEVICE_ID)) {
                    relationsFilerByDeviceId.add(relation);
                    break;
                }
            }
        }
        return relationsFilerByDeviceId;
    }

    /**
     * 获取设备限制信息
     *
     * @param deviceId
     * @return
     */
    public StartDeviceControlInformation getStartingDeviceControlInformation(String deviceId) throws Exception {

        //本次启车操作id获取
        Integer operateId = startService.findOperateIdWhenNull();
        //获取启车设备控制信息
        StartDeviceControlInformation startDeviceControlInformation = new StartDeviceControlInformation();
        List<StartDeviceRelation> relations = getStartDeviceRelationByPackage(null, deviceId);
        startDeviceControlInformation.setStartDeviceRelationList(relations);
        // 获取设备自检条件
        List<StartRequirement> startRequirements = startService.getRequirementByDeviceId(deviceId);
        for (StartRequirement requirement : startRequirements) {
            // 预设该条件错误
            requirement.setIsAbnormal(StartStopConstants.REQUIRENMENT_FALSE);
            if (judgeSingleRequirement(requirement)) {
                requirement.setIsAbnormal(StartStopConstants.REQUIRENMENT_TRUE);
            }
        }
        startDeviceControlInformation.setStartRequirements(startRequirements);
        // 获得人工干预信息
        List<StartManualInterventionRecord> startManualInterventionRecords = startService.selectStartingManualInterventionRecord(deviceId, operateId, null);
        if (CollectionUtils.isNotEmpty(startManualInterventionRecords)) {
            startDeviceControlInformation.setStartManualInterventionRecord(startManualInterventionRecords.get(0));
        }
        return startDeviceControlInformation;
    }

    /**
     * 根据设备id和name获取设备信号值
     *
     * @param deviceId
     * @param name
     * @return
     * @throws Exception
     */
    public double getValueByDeviceIdAndName(String deviceId, Integer name) throws Exception {
        StartSignal startSignal = startService.getStartSignalByDeviceId(deviceId);
        MetricModel metricModel = tmlMapper.findMetricByMetricName(startService.getStartDeviceSignalById(name).getName());
        DataModelWrapper dataModelWrapper = dataService.getData(startSignal.getDeviceCode(), metricModel.getMetricCode()).orElse(null);
        if (dataModelWrapper != null) {
            return Double.parseDouble(dataModelWrapper.getValue());
        }
        return 0;
    }

    /**
     * 判断单个启车前置条件
     *
     * @param singleRequirement
     * @return
     * @throws Exception
     */
    public Boolean judgeSingleRequirement(StartRequirement singleRequirement) throws Exception {
        Boolean flag = true;
        double value = 0;
        StartSignal startSignal = startService.getStartSignalByDeviceId(singleRequirement.getCompareDeviceId());
        MetricModel metricModel = tmlMapper.findMetricByMetricName(startService.getStartDeviceSignalById(singleRequirement.getCompareName()).getName());
        DataModelWrapper dataModelWrapper = dataService.getData(startSignal.getDeviceCode(), metricModel.getMetricCode()).orElse(null);
        if (dataModelWrapper != null) {
            value = Double.parseDouble(dataModelWrapper.getValue());
        }
        switch (singleRequirement.getOperator()) {
            case StartStopConstants.COMPARE_GREATER_THAN:
                //需要判断大于>
                if (value <= singleRequirement.getCompareValue()) {
                    flag = false;
                }
                break;
            case StartStopConstants.COMPARE_LESS_THAN:
                //需要判断小于<
                if (value >= singleRequirement.getCompareValue()) {
                    flag = false;
                }
                break;
            case StartStopConstants.COMPARE_EQUAL_TO:
                //需要判断等于=
                if (value != singleRequirement.getCompareValue()) {
                    flag = false;
                }
                break;
            case StartStopConstants.COMPARE_GREATER_THAN_AND_EQUAL_TO:
                //需要判断大于等于>=
                if (value < singleRequirement.getCompareValue()) {
                    flag = false;
                }
                break;
            case StartStopConstants.COMPARE_LESS_THAN_AND_EQUAL_TO:
                //需要判断小于等于<=
                if (value > singleRequirement.getCompareValue()) {
                    flag = false;
                }
                break;
            case StartStopConstants.COMPARE_NOT_EQUAL_TO:
                //需要判断不等于
                if (value == singleRequirement.getCompareValue()) {
                    flag = false;
                }
                break;
            default:
                flag = false;
                break;
        }
        return flag;
    }

    /**
     * 发送启车清除信号命令
     *
     * @throws Exception
     */
    public void sendCleanInformation() throws Exception {
        try {
            cleanStartInformation();
            // 清除信号发送完毕
            startService.updateStartOperate(StartStopConstants.START_SEND_CLEAN_STATE);
            sendMessageTemplateByJson(StartStopConstants.URI_START_STATE, StartStopConstants.URI_START_STATE_MESSAGE_SEND_MESSAGE_START);
        } catch (Exception e) {
            logger.error("启车清除信号发送失败,失败原因信息:{}", e.getMessage());
            throw new StartException("启车清除信息发送失败");
        }
    }

    /**
     * 发送启车清除命令
     *
     * @throws Exception
     */
    public void cleanStartInformation() throws Exception {
        // 清除顺序
        writeSignalByLabel(StartStopConstants.CLEAN_LABEL, StartStopConstants.VALUE_TRUE, StartStopConstants.LABEL_TYPE_BOOLEAN);
        logger.info("启车清除信号发送成功");
    }


    /**
     * 完成启车操作
     */
    public void finishStartState() {
        // 取消订阅
        processor.removeListener(startListener);
        // 关闭观察
        StartHandler.setStartDeviceRequirements(null);
        // 关闭启车
        startService.updateStartOperate(StartStopConstants.START_FINISH_STATE);
    }


    /**
     * 修改设备状态
     *
     * @param label 传入标签信号值
     * @param value 数值
     */
    public void updateStartDeviceState(String label, double value) {
        List<String> deviceIds = startService.selectDeviceIdByDatelabel(label, StartStopConstants.DEVICE_STATE);
        // 修改启车记录
        logger.info("修改设备:{}启车状态为{}", deviceIds.get(0), value);
        updateStartDeviceState(deviceIds.get(0), value);

        // 发送频率
        if ((double) StartStopConstants.DEVICE_STATE_WORKING == value) {
            sendFrequency(deviceIds.get(0));
        }
    }

    /**
     * 修改设备状态
     *
     * @param deviceId 设备id
     * @param value    数值
     */
    public void updateStartDeviceStateByDeviceId(String deviceId, double value) {
        logger.info("修改deviceId:{}启车设备状态记录", deviceId);
        Integer findOperateId = startService.findOperateIdWhenNull();
        // 修改该设备记录
        startService.updateStartDeviceState(findOperateId, deviceId, (int) value);
        StartDeviceStateRecord startDeviceStateRecord = new StartDeviceStateRecord();
        startDeviceStateRecord.setOperateId(findOperateId);
        startDeviceStateRecord.setDeviceId(deviceId);
        startDeviceStateRecord.setState((int) value);
        sendMessagingTemplate(StartStopConstants.URI_START_DEVICE_STATE, startDeviceStateRecord);
    }

    /**
     * 发送频率
     *
     * @param deviceId
     */
    public void sendFrequency(String deviceId) {
        StartSingleLabelAndValue singleLabelAndFrequency = startService.selectFrequency(deviceId, StartStopConstants.SET_FREQUENCY, StartStopConstants.START_TYPE_NORMAL, StartStopConstants.TYPE_STARTING);
        if (singleLabelAndFrequency != null) {
            float plcValue = getSystemMessageValue(singleLabelAndFrequency.getDataLabel());
            if (plcValue != singleLabelAndFrequency.getValue()) {
                logger.info("启车频率发送:{}的值为{}", singleLabelAndFrequency.getDataLabel(), singleLabelAndFrequency.getValue());
                while (true) {
                    Boolean sendAnswer = sendSingleLabelAndValue(singleLabelAndFrequency, StartStopConstants.LABEL_TYPE_FLOAT);
                    if (sendAnswer) {
                        break;
                    }
                    logger.error("设备频率条件信号下发失败，10秒钟后重新发送");
                }
            }
        }
    }

    /**
     * 建立区域规则判断开始
     */
    public void createAreaRuleExamine(Integer areaFirstId) {
        // 初始规则范围
        List<StartAreaRule> startAreaRules;
        if (areaFirstId == null) {
            startAreaRules = startService.selectAreaRuleByParentStateAndAreaFirstId(StartStopConstants.START_AREA, null);
        } else {
            startAreaRules = startService.selectAreaRuleByParentStateAndAreaFirstId(StartStopConstants.NO_START_AREA, areaFirstId);
        }
        // 处理每条规则
        for (StartAreaRule startAreaRule : startAreaRules) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    startService.updateStartAreaRecord(startService.findOperateIdWhenNull(), StartStopConstants.AREA_STATE_TRUE, startAreaRule.getRuleId());
                    startAreaByRule(startService.findOperateIdWhenNull(), startAreaRule.getAreaSecondId());
                }
            };
            timer.schedule(task, startAreaRule.getDelayTime());
        }

    }

    /**
     * 当该区域所有规则满足后，区域启动通知
     *
     * @param operateId
     * @param areaSecondId
     */
    public void startAreaByRule(Integer operateId, Integer areaSecondId) {
        List<Integer> recordId = startService.selectAreaRuleRecordByStateAndAreaSecondId(operateId, StartStopConstants.AREA_STATE_FALSE, areaSecondId);
        if (recordId == null || recordId.size() == 0) {
            // 启动新规则计时
            createAreaRuleExamine(areaSecondId);
            // 通知该区域满足
            StartSingleLabelAndValue startSingleLabelAndValue = startService.selectAreaStartLabel(EnumDeviceCode.AREA.getInfo(), areaSecondId, StartStopConstants.AREA_START);
            startSingleLabelAndValue.setValue((float) StartStopConstants.VALUE_TRUE);
            sendAreaStarting(startSingleLabelAndValue);
        }

    }

    /**
     * 区域满足条件信号下发
     *
     * @param startSingleLabelAndValue
     */
    private void sendAreaStarting(StartSingleLabelAndValue startSingleLabelAndValue) {
        while (true) {
            Boolean sendAnswer = sendSingleLabelAndValue(startSingleLabelAndValue, StartStopConstants.LABEL_TYPE_BOOLEAN);
            if (sendAnswer) {
                break;
            }
            logger.error("区域条件信号下发失败，10秒钟后重新发送");
        }
    }

    /**
     * 判断信号是否发送成功
     *
     * @param startSingleLabelAndValue
     * @return
     */
    public Boolean sendSingleLabelAndValue(StartSingleLabelAndValue startSingleLabelAndValue, int type) {
        Boolean flag = true;
        try {
            Thread.sleep(StartStopConstants.SEND_SINGLE_VALUE_ERROR_WAIT_TIME);
            float plcValue = getSystemMessageValue(startSingleLabelAndValue.getDataLabel());
            if (plcValue != startSingleLabelAndValue.getValue()) {
                ThingMetricCode startSignalByDataLabel = startService.getStartSignalByDataLabel(startSingleLabelAndValue.getDataLabel());
                if (startSignalByDataLabel != null) {
                    DataModel dataModel = new DataModel();
                    dataModel.setThingCode(startSignalByDataLabel.getThingCode());
                    dataModel.setMetricCode(startSignalByDataLabel.getMetricCode());
                    if (type == StartStopConstants.LABEL_TYPE_BOOLEAN) {

                        dataModel.setValue(String.valueOf(startSingleLabelAndValue.getValue() == 1));
                    } else {
                        dataModel.setValue(String.valueOf(startSingleLabelAndValue.getValue()));
                    }
                    cmdControlService.sendCmd(dataModel, SessionContext.getCurrentUser().getRequestId());
                }
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 修复自检结果内容
     *
     * @param label 信号来源label
     * @param value 信号值
     */
    public void updateExamineRecordByRule(String label, Double value) {
        List<StartExamineRule> startExamineRules = startService.getStartExaminRuleByRuleIdAndLabel(null, label);
        for (StartExamineRule startExamineRule : startExamineRules) {
            updateExamineRecord(startExamineRule, value);
        }
    }

    /**
     * 观察所有信号点
     *
     * @param startDeviceIds 启车设备id
     */
    public void observeDeviceState(Set<String> startDeviceIds) {
        // 获取所有设备的label
        List<String> startLabels = startService.getLabelBydeviceIdsAndName(startDeviceIds, StartStopConstants.DEVICE_STATE);
        // 加入启车结果监控
        startLabels.add(StartStopConstants.FINISH_STARTING_LABEL);
        // 加入启车暂停状态监控
        startLabels.add(StartStopConstants.PAUSE_STARTING_LABEL);
        // 新建观察者
        startListener.setStartLabels(startLabels);
        // 建立设备观察
        processor.removeListener(startListener);
        processor.addListener(startListener);

    }

}
