package com.zgiot.app.server.module.alert;

import com.zgiot.app.server.module.alert.pojo.*;
import com.zgiot.app.server.service.impl.AlertMapper;
import com.zgiot.app.server.service.impl.CmdControlServiceImpl;
import com.zgiot.common.constants.AlertConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xiayun on 2017/9/25.
 */
@Service
public class AlertManager {
    private Map<String, Map<String, AlertData>> alertDataMap = new ConcurrentHashMap<>();
    private Set<AlertData> verifySet = new HashSet<>();
    private Map<String, Map<String, List<AlertRule>>> paramRuleMap;
    private Map<String, Map<String, AlertRule>> protectRuleMap;
    private Map<String, Short> metricAlertTypeMap = new HashMap<>();
    private Map<String, Map<String, AlertData>> alertParamDataMap = new ConcurrentHashMap<>();
    private String uri = "";
    private String repair_uri = "";
    private String feedback_uri = "";
    private String read_state_uri = "";
    private static final int VERIFY_TO_UNTREATED_PERIOD = 60000;
    @Autowired
    private AlertMapper alertMapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CmdControlServiceImpl cmdControlService;
    private static final Logger logger = LoggerFactory.getLogger(AlertManager.class);

    @PostConstruct
    void init() {
        initMetricAlertType();
        updateParamRuleMap();
        updateProtectRuleMap();
        initAlertDataMap();
    }

    /**
     * 获取当前报警信息
     * 
     * @param thingCode
     * @param metricCode
     * @return
     */
    public AlertData getAlertDataByThingAndMetricCode(String thingCode, String metricCode) {
        if (alertDataMap.containsKey(thingCode)) {
            if (alertDataMap.get(thingCode).containsKey(metricCode)) {
                return alertDataMap.get(thingCode).get(metricCode);
            }
        }
        return null;
    }

    /**
     * 生成报警
     * 
     * @param alertData
     */
    public void generateAlert(AlertData alertData) {
        Map<String, AlertData> alertDataMetricMap;
        if (alertDataMap.containsKey(alertData.getThingCode())) {
            alertDataMetricMap = alertDataMap.get(alertData.getThingCode());
            alertDataMetricMap.put(alertData.getMetricCode(), alertData);
        } else {
            alertDataMetricMap = new HashMap<>();
            alertDataMetricMap.put(alertData.getMetricCode(), alertData);
            alertDataMap.put(alertData.getThingCode(), alertDataMetricMap);
        }
        alertMapper.createAlertDate(alertData);
        logger.info("插入", alertData);
    }

    /**
     * 人为生成报警
     * 
     * @param thingCode
     * @param info
     * @param userId
     * @param permission
     */
    public void generateManuAlert(String thingCode, String info, String userId, String permission) {
        AlertData alertData = new AlertData();
        alertData.setThingCode(thingCode);
        alertData.setAlertDateTime(new Date());
        alertData.setMetricCode(info);
        alertData.setAlertInfo(info);
        alertData.setAlertStage(AlertConstants.STAGE_NOT_VERIFY);
        alertData.setReporter(userId);
        alertData.setAlertSource(AlertConstants.SOURCE_USER);
        alertData.setAlertType(AlertConstants.TYPE_USER);
        generateAlert(alertData);
        AlertMessage alertMessage = new AlertMessage();
        alertMessage.setAlertId(alertData.getId());
        alertMessage.setType(AlertConstants.MESSAGE_TYPE_NEW_ALERT);
        alertMessage.setUserId(userId);
        alertMessage.setPermission(permission);
        alertMapper.saveAlertMessage(alertMessage);
    }

    /**
     * 报警解除
     * 
     * @param alertData
     */
    public void releaseAlert(AlertData alertData) {
        alertData.setAlertStage(AlertConstants.STAGE_RELEASE);
        if (alertDataMap.containsKey(alertData.getThingCode())) {
            Map<String, AlertData> alertDataMetricMap = alertDataMap.get(alertData.getThingCode());
            if (alertDataMetricMap.containsKey(alertData.getMetricCode())) {
                alertDataMetricMap.remove(alertData.getMetricCode());
            }
        }
        alertMapper.updateAlertDate(alertData);
        logger.debug("报警解除：thingCode {},metricCode {}", alertData.getThingCode(), alertData.getMetricCode());
    }

    /**
     * 更新报警信息
     * 
     * @param alertData
     */
    public void updateAlert(AlertData alertData) {
        alertMapper.updateAlertDate(alertData);
    }

    public Map<String, Map<String, List<AlertRule>>> getParamRuleMap() {
        return paramRuleMap;
    }

    public Map<String, Map<String, AlertRule>> getProtectRuleMap() {
        return protectRuleMap;
    }

    /**
     * 更新参数类报警规则
     */
    public void updateParamRuleMap() {
        List<AlertRule> wholeAlertRuleList = alertMapper.getAlertRuleList(AlertConstants.TYPE_PARAM);
        paramRuleMap = new ConcurrentHashMap<>();
        for (AlertRule alertRule : wholeAlertRuleList) {
            Map<String, List<AlertRule>> metricAlertRuleMap;
            if (paramRuleMap.containsKey(alertRule.getThingCode())) {
                metricAlertRuleMap = paramRuleMap.get(alertRule.getThingCode());
                if (!metricAlertRuleMap.containsKey(alertRule.getMetricCode())) {
                    metricAlertRuleMap.put(alertRule.getMetricCode(), new ArrayList<>());
                }
            } else {
                metricAlertRuleMap = new HashMap<>();
                paramRuleMap.put(alertRule.getThingCode(), metricAlertRuleMap);
                metricAlertRuleMap.put(alertRule.getMetricCode(), new ArrayList<>());
            }
            List<AlertRule> alertRules = metricAlertRuleMap.get(alertRule.getMetricCode());
            alertRules.add(alertRule);
        }
    }

    /**
     * 更新保护类报警规则
     */
    public void updateProtectRuleMap() {
        List<AlertRule> wholeAlertRuleList = alertMapper.getAlertRuleList(AlertConstants.TYPE_PROTECT);
        protectRuleMap = new ConcurrentHashMap<>();
        for (AlertRule alertRule : wholeAlertRuleList) {
            Map<String, AlertRule> metricAlertRuleMap;
            if (protectRuleMap.containsKey(alertRule.getThingCode())) {
                metricAlertRuleMap = protectRuleMap.get(alertRule.getThingCode());
            } else {
                metricAlertRuleMap = new HashMap<>();
                protectRuleMap.put(alertRule.getThingCode(), metricAlertRuleMap);
            }
            metricAlertRuleMap.put(alertRule.getMetricCode(), alertRule);
        }
    }

    /**
     * 初始化信号的报警判断类型
     */
    public void initMetricAlertType() {
        List<MetricAlertType> metricAlertTypes = alertMapper.getMetricAlertType();
        for (MetricAlertType alertType : metricAlertTypes) {
            metricAlertTypeMap.put(alertType.getMetricCode(), alertType.getAlertType());
        }
    }

    /**
     * 初始化当前已有报警
     */
    public void initAlertDataMap() {
        List<AlertData> currentAlertData = alertMapper.getCurrentAlertData();
        for (AlertData alertData : currentAlertData) {
            Map<String, AlertData> metricAlertData;
            if (alertDataMap.containsKey(alertData.getThingCode())) {
                metricAlertData = alertDataMap.get(alertData.getThingCode());
            } else {
                metricAlertData = new HashMap<>();
                alertDataMap.put(alertData.getThingCode(), metricAlertData);
            }
            metricAlertData.put(alertData.getMetricCode(), alertData);
            if(alertData.getAlertType().equals(AlertConstants.TYPE_PARAM)){
                Map<String, AlertData> metricParamAlertData;
                if (alertParamDataMap.containsKey(alertData.getThingCode())) {
                    metricParamAlertData = alertParamDataMap.get(alertData.getThingCode());
                } else {
                    metricParamAlertData = new HashMap<>();
                    alertParamDataMap.put(alertData.getThingCode(), metricParamAlertData);
                }
                metricParamAlertData.put(alertData.getMetricCode(), alertData);
            }
        }
    }


    /**
     * 获取已有参数报警
     * @return
     */
    public Map<String, Map<String, AlertData>> getAlertParamDataMap() {
        return alertParamDataMap;
    }

    /**
     * 获取信号的报警类型
     * 
     * @return
     */
    public Map<String, Short> getMetricAlertTypeMap() {
        return metricAlertTypeMap;
    }

    /**
     * 进行报警操作
     * 
     * @param thingCode
     * @param metricCode
     * @param alertMessage
     * @return
     * @throws Exception
     */
    public Integer sendAlertCmd(String thingCode, String metricCode, AlertMessage alertMessage, String requestId)
            throws Exception {
        AlertData alertData = getAlertDataByThingAndMetricCode(thingCode, metricCode);
        if (alertData == null) {
            throw new Exception();
        }
        alertMessage.setTime(new Date());
        switch (alertMessage.getType()) {
            case AlertConstants.MESSAGE_TYPE_REQ_VERIFY: // 请求核实报警（调度）
            case AlertConstants.MESSAGE_TYPE_NOT_FOUND: // 未发现报警存在（岗位）
            case AlertConstants.MESSAGE_TYPE_REQ_REPAIR: // 申请维修（岗位）
            case AlertConstants.MESSAGE_TYPE_RECOMMENDED_SHIELDING: // 建议屏蔽（岗位）
            case AlertConstants.MESSAGE_TYPE_OTHER: // 用户自定义
                alertMapper.saveAlertMessage(alertMessage);
                messagingTemplate.convertAndSend(uri, alertMessage);
            case AlertConstants.MESSAGE_TYPE_VERIFIED: // 核实报警存在（岗位）
                checkAlert(alertData, alertMessage);
            case AlertConstants.MESSAGE_TYPE_ASSIGN_REPAIR: // 申报维修（调度）
                assignRepair(alertData, alertMessage);
            case AlertConstants.MESSAGE_TYPE_REPAIR_START: // 开始维修（岗位）
                startRepair(alertData, alertMessage.getUserId());
            case AlertConstants.MESSAGE_TYPE_REPAIR_END: // 结束维修（岗位）
                endRepair(alertData);
            case AlertConstants.MESSAGE_TYPE_REQ_SCENE_CONFIRM: // 现场确认（调度）
                if (!alertData.isRecovery()) {
                    throw new Exception();
                }
                alertMapper.saveAlertMessage(alertMessage);
                messagingTemplate.convertAndSend(uri, alertMessage);
            case AlertConstants.MESSAGE_TYPE_SCENE_CONFIRM_RELEASE: // 现场确认报警已解除（岗位）
                sceneConfirmReleaseAlert(alertData, alertMessage, true);
            case AlertConstants.MESSAGE_TYPE_SCENE_CONFIRM_DIS_RELEASE: // 现场确认报警未解除
                sceneConfirmReleaseAlert(alertData, alertMessage, false);
            case AlertConstants.MESSAGE_REQ_RESET: // 申请复位（岗位）
                requestReset(alertData, alertMessage);
            case AlertConstants.MESSAGE_RESET: // 复位（调度）
                reset(alertData, alertMessage, requestId);
            case AlertConstants.MESSAGE_TYPE_SET_LEVEL: // 报警评级（调度）
                gradeAlert(alertData, alertMessage);
            case AlertConstants.MESSAGE_TYPE_REQ_FEEDBACK: // 请求反馈
                messagingTemplate.convertAndSend(feedback_uri, alertMessage.getPermission());

        }
        return alertMessage.getId();
    }

    /**
     * 核实报警存在（岗位）
     * 
     * @param alertData
     * @param alertMessage
     */
    public void checkAlert(AlertData alertData, AlertMessage alertMessage) {
        alertData.setAlertStage(AlertConstants.STAGE_VERIFIED);
        alertData.setVerifyTime(new Date());
        updateAlert(alertData);
        verifySet.add(alertData);
        alertMapper.saveAlertMessage(alertMessage);
        messagingTemplate.convertAndSend(uri, alertMessage);
    }

    /**
     * 申报维修（调度）
     * 
     * @param alertData
     */
    public void assignRepair(AlertData alertData, AlertMessage alertMessage) {
        alertData.setAlertStage(AlertConstants.STAGE_REQUEST_REPAIR);
        alertData.setRepair(true);
        updateAlert(alertData);
        alertMapper.saveAlertMessage(alertMessage);
        messagingTemplate.convertAndSend(uri, alertMessage);
        messagingTemplate.convertAndSend(repair_uri, alertData);
    }

    /**
     * 开始维修（岗位）
     * 
     * @param alertData
     * @param userId
     */
    public void startRepair(AlertData alertData, String userId) {
        alertData.setAlertStage(AlertConstants.STAGE_REPAIRING);
        alertData.setRepairConfirmUser(userId);
        alertData.setRepairStartTime(new Date());
        updateAlert(alertData);
        messagingTemplate.convertAndSend(repair_uri, alertData);
    }

    /**
     * 结束维修（岗位）
     * 
     * @param alertData
     */
    public void endRepair(AlertData alertData) {
        alertData.setAlertStage(AlertConstants.STAGE_REPAIRED);
        alertData.setRepairEndTime(new Date());
        updateAlert(alertData);
        messagingTemplate.convertAndSend(repair_uri, alertData);
    }

    /**
     * 申请复位（岗位）
     * 
     * @param alertData
     * @param alertMessage
     */
    public void requestReset(AlertData alertData, AlertMessage alertMessage) {
        alertData.setManualIntervention(true);
        updateAlert(alertData);
        alertMapper.saveAlertMessage(alertMessage);
        messagingTemplate.convertAndSend(uri, alertMessage);
    }

    /**
     * 复位（调度）
     * 
     * @param alertData
     * @param alertMessage
     */
    public void reset(AlertData alertData, AlertMessage alertMessage, String requestId) {
        DataModel dataModel = new DataModel();
        dataModel.setThingCode(alertData.getThingCode());
        dataModel.setMetricCode(MetricCodes.RESET);
        dataModel.setValue(Boolean.TRUE.toString());
        cmdControlService.sendCmd(dataModel, requestId);
        alertMapper.saveAlertMessage(alertMessage);
        messagingTemplate.convertAndSend(uri, alertMessage);
    }

    /**
     * 报警评级（调度）
     * 
     * @param alertData
     * @param alertMessage
     */
    public void gradeAlert(AlertData alertData, AlertMessage alertMessage) {
        alertData.setAlertStage(AlertConstants.STAGE_VERIFIED);
        alertData.setAlertLevel(Short.parseShort(alertMessage.getInfo()));
        alertData.setVerifyTime(new Date());
        updateAlert(alertData);
        verifySet.add(alertData);
        messagingTemplate.convertAndSend(uri, alertMessage);
    }

    /**
     * 现场确认报警解除状态（岗位）
     * 
     * @param alertData
     * @param alertMessage
     * @param sceneConfirmState
     */
    public void sceneConfirmReleaseAlert(AlertData alertData, AlertMessage alertMessage, Boolean sceneConfirmState) {
        alertData.setSceneConfirmState(sceneConfirmState);
        alertData.setSceneConfirmTime(new Date());
        alertData.setSceneConfirmUser(alertMessage.getUserId());
        releaseAlert(alertData);
        alertMapper.saveAlertMessage(alertMessage);
        messagingTemplate.convertAndSend(uri, alertMessage);
    }

    /**
     * 获取报警的消息内容
     * 
     * @param alertId
     * @return
     */
    public List<AlertMessage> getAlertMessage(int alertId) {
        return alertMapper.getAlertMessage(alertId);
    }

    /**
     * 检查未处理的报警
     */
    // @Scheduled
    public void checkVerifiedAlert() {
        for (AlertData alertData : verifySet) {
            if (new Date().getTime() - alertData.getVerifyTime().getTime() > VERIFY_TO_UNTREATED_PERIOD) {
                alertData.setAlertStage(AlertConstants.STAGE_UNTREATED);
                verifySet.remove(alertData);
            }
        }
    }

    /**
     * 消息已读
     * 
     * @param messageId
     */
    public void setRead(int messageId) {
        alertMapper.setRead(messageId);
        messagingTemplate.convertAndSend(read_state_uri, messageId);
    }

    /**
     * 获取当前报警记录
     * 
     * @param stage
     * @param levels
     * @param types
     * @param buildingIds
     * @param floors
     * @param systems
     * @param sortType
     * @return
     */
    public List<AlertRecord> getAlertDataList(String stage, List<Integer> levels, List<Short> types,
            List<Integer> buildingIds, List<Integer> floors, List<Integer> systems, Integer sortType, Long duration) {
        Date endTime = new Date();
        Date startTime = new Date(endTime.getTime() - duration);
        return alertMapper.getAlertDataList(stage, levels, types, buildingIds, floors, systems, sortType, startTime,
                endTime);
    }

}
