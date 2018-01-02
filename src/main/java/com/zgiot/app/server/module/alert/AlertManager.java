package com.zgiot.app.server.module.alert;

import com.zgiot.app.server.module.alert.mapper.AlertMapper;
import com.zgiot.app.server.module.alert.pojo.*;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.impl.FileServiceImpl;
import com.zgiot.common.constants.AlertConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.DelayQueue;
import java.util.stream.Collectors;

/**
 * Created by xiayun on 2017/9/25.
 */
@Service
@Transactional
public class AlertManager {
    private Map<String, Map<String, AlertData>> alertDataMap = new ConcurrentHashMap<>();
    // private Set<AlertData> verifySet = new HashSet<>();
    private Map<String, Map<String, List<AlertRule>>> paramRuleMap = new ConcurrentHashMap<>();
    private Map<String, Map<String, AlertRule>> protectRuleMap = new ConcurrentHashMap<>();
    private Map<String, Short> metricAlertTypeMap = new HashMap<>();
    private Map<String, Map<String, AlertData>> alertParamDataMap = new ConcurrentHashMap<>();
    private DelayQueue<VerifyDelayed> verifyDelayQueue = new DelayQueue<>();
    private static String MESSAGE_URI = "/topic/alert/message";
    private static String REPAIR_URI = "/topic/alert/repair";
    private static String FEEDBACK_URI = "/topic/alert/feedback";
    private static String READ_STATE_URI = "/topic/alert/readstate";
    // private static String REQ_RESET_URI= "topic/alert/reset";
    private static final int VERIFY_TO_UNTREATED_PERIOD = 60000;
    private static final int SORT_TYPE_TIME_DESC = 0;
    private static final int SORT_TYPE_TIME_ASC = 1;
    private static final int SORT_TYPE_LEVEL_DESC = 2;
    private static final int SORT_TYPE_LEVEL_ASC = 3;
    private static final int READ_STATE = 1;
    private static final int STATISTICS_TYPE_DEVICE = 1;
    @Autowired
    private AlertMapper alertMapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CmdControlService cmdControlService;
    private static final Logger logger = LoggerFactory.getLogger(AlertManager.class);
    private static final int SORT_DESC = 0;
    private static final int SORT_ASC = 1;

    @PostConstruct
    void init() {
        initMetricAlertType();
        initParamRuleMap();
        initProtectRuleMap();
        initAlertDataMap();
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                    AlertData alertData = verifyDelayQueue.take().getAlertData();
                    if (AlertConstants.STAGE_VERIFIED.equals(alertData.getAlertStage())) {
                        alertData.setAlertStage(AlertConstants.STAGE_UNTREATED);
                        updateAlert(alertData);
                    }
                } catch (Exception e) {
                    logger.error("get verified alert data error");
                }
            }
        });
        thread.start();
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
        alertMapper.createAlertData(alertData);
        alertMapper.createAlertDataBackup(alertData);
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
        if (getAlertDataByThingAndMetricCode(thingCode, info) != null) {
            throw new SysException("the same alert info already exist", SysException.EC_UNKNOWN);
        }
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
        alertMessage.setTime(new Date());
        alertMessage.setUserId(userId);
        alertMessage.setPermission(permission);
        alertMapper.saveAlertMessage(alertMessage);
        alertMessage.setType(AlertConstants.MESSAGE_TYPE_SET_LEVEL);
        alertMapper.saveAlertMessage(alertMessage);
    }

    /**
     * 报警解除
     *
     * @param alertData
     */
    public void releaseAlert(AlertData alertData) {
        alertData.setAlertStage(AlertConstants.STAGE_RELEASE);
        alertData.setReleaseTime(new Date());
        if (alertDataMap.containsKey(alertData.getThingCode())) {
            Map<String, AlertData> alertDataMetricMap = alertDataMap.get(alertData.getThingCode());
            if (alertDataMetricMap.containsKey(alertData.getMetricCode())) {
                alertDataMetricMap.remove(alertData.getMetricCode());
            }
        }
        alertMapper.releaseAlertData(alertData);
        alertMapper.releaseAlertDataBackup(alertData);

        logger.debug("报警解除：thingCode {},metricCode {}", alertData.getThingCode(), alertData.getMetricCode());
    }

    /**
     * 更新报警信息
     *
     * @param alertData
     */
    public void updateAlert(AlertData alertData) {
        alertMapper.updateAlertData(alertData);
        alertMapper.updateAlertDataBackup(alertData);
    }

    public Map<String, Map<String, List<AlertRule>>> getParamRuleMap() {
        return paramRuleMap;
    }

    public Map<String, Map<String, AlertRule>> getProtectRuleMap() {
        return protectRuleMap;
    }

    /**
     * 更新报警规则
     * 
     * @param alertRules
     */
    public List<AlertRule> updateRule(List<AlertRule> alertRules, int type) {
        for (AlertRule alertRule : alertRules) {
            if (alertRule.getId() != null) {
                alertMapper.updateAlertRule(alertRule);
                if (alertRule.getEnable()) {
                    if (type == AlertConstants.TYPE_PARAM) {
                        updateParamRule(alertRule);
                    } else {
                        updateProtectRule(alertRule);
                    }
                } else {
                    if (type == AlertConstants.TYPE_PARAM) {
                        removeParamRule(alertRule.getId());
                    } else {
                        removeProtectRule(alertRule.getId());
                    }
                }
            } else {
                alertMapper.insertAlertRule(alertRule);
                if (alertRule.getEnable()) {
                    if (type == AlertConstants.TYPE_PARAM) {
                        insertParamRule(alertRule);
                    } else {
                        insertProtectRule(alertRule);
                    }
                }
            }
        }
        return alertRules;

    }

    public void deleteRule(List<Long> ids, int type) {
        alertMapper.deleteAlertRules(ids);
        for (Long id : ids) {
            if (type == AlertConstants.TYPE_PARAM) {
                removeParamRule(id);
            } else {
                removeProtectRule(id);
            }
        }

    }

    private void removeProtectRule(Long id) {
        outer: for (Map.Entry<String, Map<String, AlertRule>> entry : protectRuleMap.entrySet()) {
            Map<String, AlertRule> metricRuleMap = entry.getValue();
            for (Map.Entry<String, AlertRule> innerEntry : metricRuleMap.entrySet()) {
                AlertRule alertRule = innerEntry.getValue();
                String metricCode = innerEntry.getKey();
                if (alertRule.getId().equals(id)) {
                    metricRuleMap.remove(metricCode);
                    break outer;
                }
            }
        }

    }

    private void removeParamRule(Long id) {
        outer: for (Map.Entry<String, Map<String, List<AlertRule>>> entry : paramRuleMap.entrySet()) {
            Map<String, List<AlertRule>> metricRuleMap = entry.getValue();
            for (Map.Entry<String, List<AlertRule>> innerEntry : metricRuleMap.entrySet()) {
                List<AlertRule> alertRules = innerEntry.getValue();
                for (AlertRule alertRule : alertRules) {
                    if (alertRule.getId().equals(id)) {
                        alertRules.remove(alertRule);
                        break outer;
                    }
                }
            }
        }
    }

    private void updateParamRule(AlertRule alertRule) {
        for (Map.Entry<String, Map<String, List<AlertRule>>> entry : paramRuleMap.entrySet()) {
            Map<String, List<AlertRule>> metricRuleMap = entry.getValue();
            outer: for (Map.Entry<String, List<AlertRule>> innerEntry : metricRuleMap.entrySet()) {
                List<AlertRule> alertRules = innerEntry.getValue();
                for (AlertRule existAlertRule : alertRules) {
                    if (existAlertRule.getId().equals(alertRule.getId())) {
                        alertRules.remove(existAlertRule);
                        alertRules.add(alertRule);
                        break outer;
                    }
                }
            }
        }
    }

    private void updateProtectRule(AlertRule alertRule) {
        for (Map.Entry<String, Map<String, AlertRule>> entry : protectRuleMap.entrySet()) {
            Map<String, AlertRule> metricRuleMap = entry.getValue();
            for (Map.Entry<String, AlertRule> innerEntry : metricRuleMap.entrySet()) {
                AlertRule existAlertRule = innerEntry.getValue();
                String metricCode = innerEntry.getKey();
                if (existAlertRule.getId().equals(alertRule.getId())) {
                    metricRuleMap.put(metricCode, alertRule);
                    break;
                }
            }
        }
    }

    private void insertParamRule(AlertRule alertRule) {
        Map<String, List<AlertRule>> metricAlertRuleMap;
        if (paramRuleMap.containsKey(alertRule.getThingCode())) {
            metricAlertRuleMap = paramRuleMap.get(alertRule.getThingCode());
            if (!metricAlertRuleMap.containsKey(alertRule.getMetricCode())) {
                metricAlertRuleMap.put(alertRule.getMetricCode(), new CopyOnWriteArrayList<>());
            }
        } else {
            metricAlertRuleMap = new ConcurrentHashMap<>();
            paramRuleMap.put(alertRule.getThingCode(), metricAlertRuleMap);
            metricAlertRuleMap.put(alertRule.getMetricCode(), new CopyOnWriteArrayList<>());
        }
        List<AlertRule> alertRules = metricAlertRuleMap.get(alertRule.getMetricCode());
        alertRules.add(alertRule);
    }

    private void insertProtectRule(AlertRule alertRule) {
        Map<String, AlertRule> metricAlertRuleMap;
        if (protectRuleMap.containsKey(alertRule.getThingCode())) {
            metricAlertRuleMap = protectRuleMap.get(alertRule.getThingCode());
        } else {
            metricAlertRuleMap = new ConcurrentHashMap<>();
            protectRuleMap.put(alertRule.getThingCode(), metricAlertRuleMap);
        }
        metricAlertRuleMap.put(alertRule.getMetricCode(), alertRule);
    }

    /**
     * 初始化参数类报警规则
     */
    public void initParamRuleMap() {
        List<AlertRule> wholeAlertRuleList = alertMapper.getWholeAlertRuleList(AlertConstants.TYPE_PARAM);
        for (AlertRule alertRule : wholeAlertRuleList) {
            insertParamRule(alertRule);
        }
    }

    /**
     * 初始化保护类报警规则
     */
    public void initProtectRuleMap() {
        List<AlertRule> wholeAlertRuleList = alertMapper.getWholeAlertRuleList(AlertConstants.TYPE_PROTECT);
        for (AlertRule alertRule : wholeAlertRuleList) {
            insertProtectRule(alertRule);
        }
    }

    /**
     * 获取报警规则
     * 
     * @param filterCondition
     *            筛选条件
     * @return
     */
    public AlertRuleRsp getParamAlertRuleList(FilterCondition filterCondition) {
        Integer pageCount = null;
        if (filterCondition.getCount() != null) {
            Integer paramAlertConfSize = alertMapper.getParamAlertConfSize(filterCondition);
            pageCount = (int) Math.ceil((double) paramAlertConfSize / filterCondition.getCount());
        }
        if (filterCondition.getPage() != null && filterCondition.getCount() != null) {
            filterCondition.setOffset(filterCondition.getPage() * filterCondition.getCount());
        }
        List<ThingAlertRule> paramAlertConfList = alertMapper.getParamAlertConfList(filterCondition);
        for (ThingAlertRule thingAlertRule : paramAlertConfList) {
            String thingCode = thingAlertRule.getThingCode();
            String metricCode = thingAlertRule.getMetricCode();
            List<AlertRule> alertRuleList = alertMapper.getAlertRuleList(thingCode, metricCode, filterCondition);
            thingAlertRule.setAlertRules(alertRuleList);
        }
        AlertRuleRsp alertRuleRsp = new AlertRuleRsp(paramAlertConfList);
        alertRuleRsp.setPageCount(pageCount);
        return alertRuleRsp;
    }

    public AlertRuleRsp getProtAlertRuleList(FilterCondition filterCondition) {
        if (filterCondition.getPage() != null && filterCondition.getCount() != null) {
            filterCondition.setOffset(filterCondition.getPage() * filterCondition.getCount());
        }
        List<ThingAlertRule> protAlertRuleList = alertMapper.getProtAlertRuleList(filterCondition);
        Integer protAlertRuleCount = alertMapper.getProtAlertRuleCount(filterCondition);
        AlertRuleRsp alertRuleRsp = new AlertRuleRsp(protAlertRuleList);
        alertRuleRsp.setPageCount(protAlertRuleCount);
        return alertRuleRsp;

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
            if (alertData.getAlertType().equals(AlertConstants.TYPE_PARAM)) {
                Map<String, AlertData> metricParamAlertData;
                if (alertParamDataMap.containsKey(alertData.getThingCode())) {
                    metricParamAlertData = alertParamDataMap.get(alertData.getThingCode());
                } else {
                    metricParamAlertData = new HashMap<>();
                    alertParamDataMap.put(alertData.getThingCode(), metricParamAlertData);
                }
                metricParamAlertData.put(alertData.getMetricCode(), alertData);
            }
            if (AlertConstants.STAGE_VERIFIED.equals(alertData.getAlertStage())) {
                verifyDelayQueue.put(new VerifyDelayed(alertData, VERIFY_TO_UNTREATED_PERIOD));
                // verifySet.add(alertData);
            }
        }
    }

    /**
     * 获取已有参数报警
     *
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
        boolean updateFlag = false;
        if (alertData == null) {
            throw new SysException("this alert does not exist", SysException.EC_UNKNOWN);
        }
        alertMessage.setTime(new Date());
        alertMessage.setAlertId(alertData.getId());
        if (AlertConstants.PERMISSION_POSTWORKER.equals(alertMessage.getPermission())) {
            if (!alertMessage.getUserId().equals(alertData.getPostWorker())) {
                alertData.setPostWorker(alertMessage.getUserId());
                updateFlag = true;
            }
        } else if (AlertConstants.PERMISSION_DISPATCHER.equals(alertMessage.getPermission())) {
            if (!alertMessage.getUserId().equals(alertData.getDispatcher())) {
                alertData.setDispatcher(alertMessage.getUserId());
                updateFlag = true;
            }
        }
        switch (alertMessage.getType()) {
            case AlertConstants.MESSAGE_TYPE_REQ_VERIFY: // 请求核实报警（调度）
            case AlertConstants.MESSAGE_TYPE_REQ_REPAIR: // 申请维修（岗位）
            case AlertConstants.MESSAGE_TYPE_OTHER: // 用户自定义
                alertMapper.saveAlertMessage(alertMessage);
                messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
                logger.debug("推送报警消息，报警设备{}，报警内容{}，消息类型为{}", alertData.getThingCode(), alertData.getMetricCode(),
                        alertMessage.getType());
                break;
            case AlertConstants.MESSAGE_TYPE_RECOMMENDED_SHIELDING: // 建议屏蔽（岗位）
                maskAlert(alertData, alertMessage);
                break;
            case AlertConstants.MESSAGE_TYPE_NOT_FOUND: // 未发现报警存在（岗位）
                notFoundAlert(alertData, alertMessage);
                updateFlag = true;
                break;
            case AlertConstants.MESSAGE_TYPE_VERIFIED: // 核实报警存在（岗位）
                checkAlert(alertData, alertMessage);
                updateFlag = true;
                break;
            case AlertConstants.MESSAGE_TYPE_ASSIGN_REPAIR: // 申报维修（调度）
                assignRepair(alertData, alertMessage);
                updateFlag = true;
                break;
            case AlertConstants.MESSAGE_TYPE_REPAIR_START: // 开始维修（岗位）
                startRepair(alertData, alertMessage.getUserId());
                updateFlag = true;
                break;
            case AlertConstants.MESSAGE_TYPE_REPAIR_END: // 结束维修（岗位）
                endRepair(alertData);
                updateFlag = true;
                break;
            case AlertConstants.MESSAGE_TYPE_REQ_SCENE_CONFIRM: // 现场确认（调度）
                if (!alertData.isRecovery()) {
                    throw new SysException("the alert is not recovery", SysException.EC_UNKNOWN);
                }
                alertMapper.saveAlertMessage(alertMessage);
                messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
                logger.debug("推送报警消息，报警设备{}，报警内容{}，消息类型为{}", alertData.getThingCode(), alertData.getMetricCode(),
                        alertMessage.getType());
                break;
            case AlertConstants.MESSAGE_TYPE_SCENE_CONFIRM_RELEASE: // 现场确认报警已解除（岗位）
                sceneConfirmReleaseAlert(alertData, alertMessage, true);
                break;
            case AlertConstants.MESSAGE_TYPE_SCENE_CONFIRM_DIS_RELEASE: // 现场确认报警未解除
                sceneConfirmReleaseAlert(alertData, alertMessage, false);
                break;
            // case AlertConstants.MESSAGE_REQ_RESET: // 申请复位（岗位）
            // requestReset(alertData, alertMessage);
            // updateFlag = true;
            // break;
            // case AlertConstants.MESSAGE_RESET: // 复位（调度）
            // reset(alertData, alertMessage, requestId);
            // break;
            case AlertConstants.MESSAGE_TYPE_SET_LEVEL: // 报警评级（调度）
                gradeAlert(alertData, alertMessage);
                updateFlag = true;
                break;
            case AlertConstants.MESSAGE_TYPE_REQ_FEEDBACK: // 请求反馈
                messagingTemplate.convertAndSend(FEEDBACK_URI, alertMessage.getPermission());
                break;
            default:
                break;
        }

        if (updateFlag) {
            updateAlert(alertData);
        }
        return alertMessage.getId();
    }

    /**
     * 未发现报警存在（岗位）
     * 
     * @param alertData
     * @param alertMessage
     */
    private void notFoundAlert(AlertData alertData, AlertMessage alertMessage) {
        alertData.setReporter(alertMessage.getUserId());
        alertMapper.saveAlertMessage(alertMessage);
        messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
        logger.debug("推送报警消息，报警设备{}，报警内容{}，未发现报警存在", alertData.getThingCode(), alertData.getMetricCode(),
                alertMessage.getType());
    }

    /**
     * 核实报警存在（岗位）
     *
     * @param alertData
     * @param alertMessage
     */
    private void checkAlert(AlertData alertData, AlertMessage alertMessage) {
        alertData.setAlertStage(AlertConstants.STAGE_VERIFIED);
        alertData.setVerifyTime(new Date());
        alertData.setReporter(alertMessage.getUserId());
        // updateAlert(alertData);
        // verifySet.add(alertData);
        verifyDelayQueue.put(new VerifyDelayed(alertData, VERIFY_TO_UNTREATED_PERIOD));
        alertMapper.saveAlertMessage(alertMessage);
        messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
        logger.debug("推送报警消息，报警设备{}，报警内容{}，核实报警存在", alertData.getThingCode(), alertData.getMetricCode(),
                alertMessage.getType());
    }

    /**
     * 申报维修（调度）
     *
     * @param alertData
     */
    private void assignRepair(AlertData alertData, AlertMessage alertMessage) {
        alertData.setAlertStage(AlertConstants.STAGE_REQUEST_REPAIR);
        alertData.setRepair(true);
        // updateAlert(alertData);
        alertMapper.saveAlertMessage(alertMessage);
        messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
        messagingTemplate.convertAndSend(REPAIR_URI, alertData);
        logger.debug("推送报警消息，报警设备{}，报警内容{}，申报维修", alertData.getThingCode(), alertData.getMetricCode(),
                alertMessage.getType());
    }

    /**
     * 开始维修（岗位）
     *
     * @param alertData
     * @param userId
     */
    private void startRepair(AlertData alertData, String userId) {
        alertData.setAlertStage(AlertConstants.STAGE_REPAIRING);
        alertData.setRepairConfirmUser(userId);
        alertData.setRepairStartTime(new Date());
        // updateAlert(alertData);
        messagingTemplate.convertAndSend(REPAIR_URI, alertData);
        logger.debug("推送报警消息，报警设备{}，报警内容{}，开始维修", alertData.getThingCode(), alertData.getMetricCode());
    }

    /**
     * 结束维修（岗位）
     *
     * @param alertData
     */
    private void endRepair(AlertData alertData) {
        alertData.setRepair(false);
        alertData.setAlertStage(AlertConstants.STAGE_REPAIRED);
        alertData.setRepairEndTime(new Date());
        // updateAlert(alertData);
        messagingTemplate.convertAndSend(REPAIR_URI, alertData);
        logger.debug("推送报警消息，报警设备{}，报警内容{}，结束维修", alertData.getThingCode(), alertData.getMetricCode());
    }

    // public void requestReset(AlertData alertData, AlertMessage alertMessage) {
    // alertData.setManualIntervention(true);
    // // updateAlert(alertData);
    // alertMapper.saveAlertMessage(alertMessage);
    // messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
    // logger.debug("推送报警消息，报警设备{}，报警内容{}，申请复位", alertData.getThingCode(),
    // alertData.getMetricCode());
    // }

    /**
     * 申请复位（岗位）
     * 
     * @param thingCode
     * @param userId
     * @param permission
     */
    public void requestReset(String thingCode, String userId, String permission) {
        Map<String, AlertData> thingAlertMap = alertDataMap.get(thingCode);
        if (thingAlertMap == null || alertDataMap.size() == 0) {
            throw new SysException("the thing does not have alert date", SysException.EC_UNKNOWN);
        }
        for (Map.Entry<String, AlertData> entry : thingAlertMap.entrySet()) {
            AlertData alertData = entry.getValue();
            alertData.setManualIntervention(true);
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.setTime(new Date());
            alertMessage.setAlertId(alertData.getId());
            alertMessage.setUserId(userId);
            alertMessage.setPermission(permission);
            alertMessage.setType(AlertConstants.MESSAGE_REQ_RESET);
            alertMapper.saveAlertMessage(alertMessage);
            updateAlert(alertData);
            messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
            logger.debug("推送报警消息，报警设备{}，报警内容{}，申请复位", alertData.getThingCode(), alertData.getMetricCode());
        }
        // messagingTemplate.convertAndSend(REQ_RESET_URI,thingCode);
    }

    // public void reset(AlertData alertData, AlertMessage alertMessage, String
    // requestId) {
    // DataModel dataModel = new DataModel();
    // dataModel.setThingCode(alertData.getThingCode());
    // dataModel.setMetricCode(MetricCodes.RESET);
    // dataModel.setValue(Boolean.TRUE.toString());
    // cmdControlService.sendCmd(dataModel, requestId);
    // alertMapper.saveAlertMessage(alertMessage);
    // messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
    // logger.debug("推送报警消息，报警设备{}，报警内容{}，进行复位", alertData.getThingCode(),
    // alertData.getMetricCode());
    // }

    /**
     * 复位（调度）
     * 
     * @param thingCode
     * @param requestId
     */
    public void reset(String thingCode, String requestId) {
        DataModel dataModel = new DataModel();
        dataModel.setThingCode(thingCode);
        dataModel.setMetricCode(MetricCodes.RESET);
        dataModel.setValue(Boolean.TRUE.toString());
        CmdControlService.CmdSendResponseData resetSendResponseData = cmdControlService.sendCmd(dataModel, requestId);
        if (resetSendResponseData.getOkCount() <= 0) {
            throw new SysException("下发复位信号失败，失败原因：" + resetSendResponseData.getErrorMessage(),
                    SysException.EC_CMD_FAILED);
        }
        Map<String, AlertData> metricAlertDataMap = alertDataMap.get(thingCode);
        if (metricAlertDataMap != null && metricAlertDataMap.containsKey(MetricCodes.WARNING)) {
            dataModel.setMetricCode(MetricCodes.ALERT_CONFIRM);
            CmdControlService.CmdSendResponseData alertConfirmSendResponseData =
                    cmdControlService.sendCmd(dataModel, requestId);
            if (alertConfirmSendResponseData.getOkCount() <= 0) {
                throw new SysException("下发报警确认信号失败，失败原因：" + alertConfirmSendResponseData.getErrorMessage(),
                        SysException.EC_CMD_FAILED);
            }
        }
        logger.debug("报警设备{}进行复位操作", thingCode);
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
        // updateAlert(alertData);
        // verifySet.add(alertData);
        verifyDelayQueue.put(new VerifyDelayed(alertData, VERIFY_TO_UNTREATED_PERIOD));
        alertMapper.saveAlertMessage(alertMessage);
        messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
        logger.debug("推送报警消息，报警设备{}，报警内容{}，报警评级", alertData.getThingCode(), alertData.getMetricCode());
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
        messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
        logger.debug("推送报警消息，报警设备{}，报警内容{}，现场确认报警解除状态：", alertData.getThingCode(), alertData.getMetricCode(),
                sceneConfirmState);
    }

    /**
     * 建议屏蔽（岗位）
     *
     * @param alertData
     * @param alertMessage
     */
    public void maskAlert(AlertData alertData, AlertMessage alertMessage) {
        String[] maskIds = alertMessage.getInfo().split(";");
        List<AlertMask> alertMasks = new ArrayList<>();
        for (String maskId : maskIds) {
            AlertMask alertMask = new AlertMask();
            alertMask.setAlertId(alertData.getId());
            alertMask.setParamValue(alertData.getParamValue());
            alertMask.setParamLower(alertData.getParamLower());
            alertMask.setParamUpper(alertData.getParamUpper());
            alertMask.setMaskId(Integer.parseInt(maskId));
            alertMask.setTime(new Date());
            alertMask.setUserId(alertMessage.getUserId());
            alertMasks.add(alertMask);
        }
        alertMapper.saveAlertShield(alertMasks);
        alertMapper.saveAlertMessage(alertMessage);
        messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
        logger.debug("推送报警消息，报警设备{}，报警内容{}，建议屏蔽，屏蔽内容为：{}", alertData.getThingCode(), alertData.getMetricCode(),
                alertMessage.getInfo());
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
    // @Scheduled(cron = "0/10 * * * * ?")
    // public void checkVerifiedAlert() {
    // for (AlertData alertData : verifySet) {
    // if (new Date().getTime() - alertData.getVerifyTime().getTime() >
    // VERIFY_TO_UNTREATED_PERIOD) {
    // alertData.setAlertStage(AlertConstants.STAGE_UNTREATED);
    // updateAlert(alertData);
    // verifySet.remove(alertData);
    // }
    // }
    // }

    /**
     * 消息已读
     *
     * @param messageIds
     */
    public void setRead(List<Integer> messageIds) {
        alertMapper.setRead(messageIds, READ_STATE);
        messagingTemplate.convertAndSend(READ_STATE_URI, messageIds);
        logger.debug("报警消息变更为已读状态,消息id列表为：{}", messageIds);

    }

    /**
     * 获取报警记录，按设备进行分组
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
    public List<AlertRecord> getAlertDataListGroupByThing(String stage, List<Integer> levels, List<Short> types,
            List<Integer> buildingIds, List<Integer> floors, List<Integer> systems, String assetType, String category,
            Integer sortType, Long duration, String thingCode, Integer page, Integer count, Date timeStamp) {

        Date endTime = timeStamp;
        Date startTime = null;
        Integer offset = null;
        if (duration != null) {
            startTime = new Date(endTime.getTime() - duration);
        }
        if (page != null && count != null) {
            offset = page * count;
        }
        String excluStage = null;
        if (stage == null) {
            excluStage = AlertConstants.STAGE_RELEASE;
        }
        if (types != null) {
            types.add(AlertConstants.TYPE_USER);
        }
        List<AlertRecord> alertRecords =
                alertMapper.getAlertDataListGroupByThing(stage, excluStage, levels, types, buildingIds, floors, systems,
                        assetType, category, sortType, startTime, endTime, thingCode, offset, count);
        sortRecords(sortType, alertRecords);
        if (page != null && count != null) {
            List<AlertRecord> alertRecordsPaged = pagingRecords(page, count, alertRecords);
            disposeImageAndMessage(stage, alertRecordsPaged);
            return alertRecordsPaged;
        }
        disposeImageAndMessage(stage, alertRecords);
        return alertRecords;
    }

    private void disposeImageAndMessage(String stage, List<AlertRecord> alertRecordsPaged) {
        disposeImage(alertRecordsPaged);
        if (!AlertConstants.STAGE_RELEASE.equals(stage)) {
            getAlertMessage(alertRecordsPaged);
        }
    }

    private List<AlertRecord> pagingRecords(Integer page, Integer count, List<AlertRecord> alertRecords) {
        List<AlertRecord> result = new ArrayList<>();
        if (page * count < alertRecords.size()) {
            if ((page + 1) * count < alertRecords.size()) {
                result = alertRecords.subList(page * count, (page + 1) * count);
            } else {
                result = alertRecords.subList(page * count, alertRecords.size());
            }
        }
        return result;
    }

    private void disposeImage(List<AlertRecord> alertRecords) {
        for (AlertRecord alertRecord : alertRecords) {
            List<AlertData> alertDatas = alertRecord.getAlertDataList();
            for (AlertData alertData : alertDatas) {
                transImageStrToList(alertData);
                // countUnreadMessage(alertData);
            }
        }
    }

    private void getAlertMessage(List<AlertRecord> alertRecords) {
        for (AlertRecord alertRecord : alertRecords) {
            List<AlertData> alertDataList = alertRecord.getAlertDataList();
            for (AlertData alertData : alertDataList) {
                List<AlertMessage> alertMessage = alertMapper.getAlertMessage(alertData.getId());
                alertData.setAlertMessageList(alertMessage);

            }
        }
    }

    /**
     * 报警记录排序
     *
     * @param sortType
     * @param alertRecords
     */
    private void sortRecords(Integer sortType, List<AlertRecord> alertRecords) {
        if (sortType == null) {
            sortType = SORT_TYPE_TIME_DESC;
        }
        if (sortType.equals(SORT_TYPE_LEVEL_DESC)) {
            sortByLevel(alertRecords, SORT_DESC);
        } else if (sortType.equals(SORT_TYPE_TIME_DESC)) {
            sortByTime(alertRecords, SORT_DESC);
        } else if (sortType.equals(SORT_TYPE_LEVEL_ASC)) {
            sortByLevel(alertRecords, SORT_ASC);
        } else if (sortType.equals(SORT_TYPE_TIME_ASC)) {
            sortByTime(alertRecords, SORT_ASC);
        }
    }

    /**
     * 报警列表按时间排序
     *
     * @param alertRecords
     */
    private void sortByTime(List<AlertRecord> alertRecords, int type) {
        alertRecords.sort((AlertRecord o1, AlertRecord o2) -> {
            AlertData alertData1 = o1.getAlertDataList().get(0);
            AlertData alertData2 = o2.getAlertDataList().get(0);
            if (alertData1.getAlertLevel() == null && alertData2.getAlertLevel() == null) {
                return alertData2.getAlertDateTime().compareTo(alertData1.getAlertDateTime());
            } else if (alertData1.getAlertLevel() == null) {
                return -1;
            } else if (alertData2.getAlertLevel() == null) {
                return 1;
            }
            if (type == SORT_DESC) {
                return alertData2.getAlertDateTime().compareTo(alertData1.getAlertDateTime());
            } else {
                return alertData1.getAlertDateTime().compareTo(alertData2.getAlertDateTime());
            }

        });
    }

    /**
     * 报警列表按等级排序
     *
     * @param alertRecords
     */
    private void sortByLevel(List<AlertRecord> alertRecords, int type) {
        alertRecords.sort((AlertRecord o1, AlertRecord o2) -> {
            AlertData alertData1 = o1.getAlertDataList().get(0);
            AlertData alertData2 = o2.getAlertDataList().get(0);
            if (alertData1.getAlertLevel() == null && alertData2.getAlertLevel() == null) {
                return alertData2.getAlertDateTime().compareTo(alertData1.getAlertDateTime());
            } else if (alertData1.getAlertLevel() == null) {
                return -1;
            } else if (alertData2.getAlertLevel() == null) {
                return 1;
            }
            if (alertData1.getAlertLevel().equals(alertData2.getAlertLevel())) {
                return alertData2.getAlertDateTime().compareTo(alertData1.getAlertDateTime());
            } else {
                if (type == SORT_DESC) {
                    return alertData2.getAlertLevel().compareTo(alertData1.getAlertLevel());
                } else {
                    return alertData1.getAlertLevel().compareTo(alertData2.getAlertLevel());
                }
            }
        });
    }

    /**
     * 计算未读的消息数目
     *
     * @param alertData
     */
    private void countUnreadMessage(AlertData alertData) {
        List<AlertMessage> alertMessages = alertData.getAlertMessageList();
        int messageUnreadCount = 0;
        if (alertMessages != null && alertMessages.size() != 0) {
            for (AlertMessage alertMessage : alertMessages) {
                if (!alertMessage.getRead()) {
                    messageUnreadCount++;
                }
            }
        }
        alertData.setMessageUnreadCount(messageUnreadCount);
    }

    private void transImageStrToList(AlertData alertData) {
        String feedBackImage = alertData.getFeedBackImage();
        if (StringUtils.isNotBlank(feedBackImage)) {
            String[] images = feedBackImage.split(";");
            List<String> imageList = Arrays.asList(images);
            alertData.setFeedBackImageList(imageList);
        }
    }

    /**
     * 获取报警屏蔽信息
     * 
     * @param thingCode
     * @param metricCode
     * @param startTime
     * @param endTime
     * @return
     */
    // public List<AlertMask> getAlertShieldInfo(String thingCode, String
    // metricCode, Date startTime, Date endTime){
    // List
    // }

    /**
     * 反馈图片视频信息
     *
     * @param thingCode
     * @param metricCode
     * @param uri
     * @param type
     */
    public void feedback(String thingCode, String metricCode, String uri, int type) {
        AlertData alertData = getAlertDataByThingAndMetricCode(thingCode, metricCode);
        if (alertData == null) {
            throw new SysException("this alert does not exist", SysException.EC_UNKNOWN);
        }
        if (type == FileServiceImpl.IMAGE) {
            alertData.setFeedBackImage(uri);
        } else if (type == FileServiceImpl.VIDEO) {
            alertData.setFeedBackVideo(uri);
        }
        updateAlert(alertData);
    }

    /**
     * 删除视频信息
     * 
     * @param thingCode
     * @param metricCode
     */
    public void delVideo(String thingCode, String metricCode) {
        AlertData alertData = getAlertDataByThingAndMetricCode(thingCode, metricCode);
        alertData.setFeedBackVideo(null);
        updateAlert(alertData);
    }

    /**
     * 获取统计信息
     * 
     * @param type
     *            0：全部统计信息 1：设备具体统计信息
     * @param alertStage
     * @param startTime
     * @param endTime
     * @return
     */
    public AlertStatisticsRsp getStatisticsInfo(int type, String alertStage, Date startTime, Date endTime) {
        AlertStatisticsRsp alertStatisticsRsp = new AlertStatisticsRsp();
        String excluStage = null;
        if (alertStage == null || AlertConstants.STAGE_UNRELEASE.equals(alertStage)) {
            excluStage = AlertConstants.STAGE_RELEASE;
        }
        if (type == STATISTICS_TYPE_DEVICE) {
            getThingStatisticsInfo(type, alertStage, startTime, endTime, alertStatisticsRsp, excluStage);

        } else {
            getStageStatisticsInfo(type, startTime, endTime, alertStatisticsRsp);
        }
        return alertStatisticsRsp;
    }

    /**
     * 获取不同报警阶段统计信息
     * 
     * @param type
     * @param startTime
     * @param endTime
     * @param alertStatisticsRsp
     */
    private void getStageStatisticsInfo(int type, Date startTime, Date endTime, AlertStatisticsRsp alertStatisticsRsp) {
        AlertStatisticsNum wholeStatisticsNum = getAlertStatisticsNum(type, null, null, startTime, endTime);
        AlertStatisticsNum releaseStatisticsNum =
                getAlertStatisticsNum(type, AlertConstants.STAGE_RELEASE, null, startTime, endTime);
        AlertStatisticsNum unReleaseStatisticsNum =
                getAlertStatisticsNum(type, null, AlertConstants.STAGE_RELEASE, startTime, endTime);
        alertStatisticsRsp.setWholeStatisticsInfo(wholeStatisticsNum);
        alertStatisticsRsp.setReleaseStatisticsInfo(releaseStatisticsNum);
        alertStatisticsRsp.setUnReleaseStatisticsInfo(unReleaseStatisticsNum);
    }

    private AlertStatisticsNum getAlertStatisticsNum(int type, String alertStage, String excluStage, Date startTime,
            Date endTime) {
        List<AlertLevelNum> alertLevelNumList =
                alertMapper.getLevelStatisticsInfo(type, alertStage, excluStage, startTime, endTime);
        Map<Integer, Integer> alertLevelMap = new HashMap<>();
        for (AlertLevelNum alertLevelNum : alertLevelNumList) {
            alertLevelMap.put(alertLevelNum.getAlertLevel(), alertLevelNum.getCount());
        }
        AlertStatisticsNum statisticsNum =
                alertMapper.getStatisticsInfo(type, alertStage, excluStage, startTime, endTime).get(0);
        statisticsNum.setAlertLevelNums(alertLevelMap);
        return statisticsNum;
    }

    /**
     * 获取一个设备报警统计信息
     * 
     * @param type
     * @param alertStage
     * @param startTime
     * @param endTime
     * @param alertStatisticsRsp
     * @param excluStage
     */
    private void getThingStatisticsInfo(int type, String alertStage, Date startTime, Date endTime,
            AlertStatisticsRsp alertStatisticsRsp, String excluStage) {
        Map<String, AlertStatisticsNum> alertStatisticsNumMap = new HashMap<>();
        List<AlertLevelNum> levelStatisticsInfo =
                alertMapper.getLevelStatisticsInfo(type, alertStage, excluStage, startTime, endTime);
        for (AlertLevelNum alertLevelNum : levelStatisticsInfo) {
            String thingCode = alertLevelNum.getThingCode();
            AlertStatisticsNum alertStatisticsNum;
            if (alertStatisticsNumMap.containsKey(thingCode)) {
                alertStatisticsNum = alertStatisticsNumMap.get(thingCode);
            } else {
                alertStatisticsNum = new AlertStatisticsNum();
                alertStatisticsNumMap.put(thingCode, alertStatisticsNum);
                alertStatisticsNum.setThingCode(thingCode);
            }
            alertStatisticsNum.getAlertLevelNums().put(alertLevelNum.getAlertLevel(), alertLevelNum.getCount());
        }
        List<AlertStatisticsNum> alertStatisticsNums = sortAlertStatisticsNum(alertStatisticsNumMap);
        alertStatisticsRsp.setDetailStatisticsInfo(alertStatisticsNums);
    }

    /**
     * 按报警总数排序
     * 
     * @param alertStatisticsNumMap
     * @return
     */
    private List<AlertStatisticsNum> sortAlertStatisticsNum(Map<String, AlertStatisticsNum> alertStatisticsNumMap) {
        List<AlertStatisticsNum> alertStatisticsNums = new ArrayList<>(alertStatisticsNumMap.values());
        alertStatisticsNums.sort((AlertStatisticsNum o1, AlertStatisticsNum o2) -> {
            Collection<Integer> count1 = o1.getAlertLevelNums().values();
            Integer sumNum1 = 0;
            Integer sumNum2 = 0;
            for (Integer i : count1) {
                sumNum1 += i;
            }
            o1.setSumNum(sumNum1);
            Collection<Integer> count2 = o2.getAlertLevelNums().values();
            for (Integer i : count2) {
                sumNum2 += i;
            }
            o2.setSumNum(sumNum2);
            if (sumNum1.equals(sumNum2)) {
                return o1.getThingCode().compareTo(o2.getThingCode());
            }
            return sumNum2.compareTo(sumNum1);
        });
        return alertStatisticsNums;
    }

    /**
     * 获取不同类型的报警数量统计信息
     * 
     * @param startTime
     * @param endTime
     */
    public AlertStatisticsRsp getTypeStatisticsInfo(Date startTime, Date endTime) {
        AlertStatisticsRsp alertStatisticsRsp = new AlertStatisticsRsp();
        int dayNum = (int) (endTime.getTime() - startTime.getTime()) / (1000 * 3600 * 24);
        List<AlertStatisticsNum> paramStatisticsInfo =
                alertMapper.getTypeStatisticsInfo(AlertConstants.TYPE_PARAM, startTime, endTime);
        List<AlertStatisticsNum> protectStatisticsInfo =
                alertMapper.getTypeStatisticsInfo(AlertConstants.TYPE_PROTECT, startTime, endTime);
        List<AlertStatisticsNum> faultStatisticsInfo =
                alertMapper.getTypeStatisticsInfo(AlertConstants.TYPE_FAULT, startTime, endTime);
        List<Integer> paramCounts;
        List<Integer> protectCounts;
        List<Integer> faultCounts;
        if (paramStatisticsInfo.size() == dayNum) {
            paramCounts = paramStatisticsInfo.stream().map(AlertStatisticsNum::getSumNum).collect(Collectors.toList());
        } else {
            paramCounts = disposeDaysWithoutData(startTime, endTime, paramStatisticsInfo);
        }
        if (protectStatisticsInfo.size() == dayNum) {
            protectCounts =
                    protectStatisticsInfo.stream().map(AlertStatisticsNum::getSumNum).collect(Collectors.toList());
        } else {
            protectCounts = disposeDaysWithoutData(startTime, endTime, protectStatisticsInfo);
        }
        if (faultStatisticsInfo.size() == dayNum) {
            faultCounts = faultStatisticsInfo.stream().map(AlertStatisticsNum::getSumNum).collect(Collectors.toList());
        } else {
            faultCounts = disposeDaysWithoutData(startTime, endTime, faultStatisticsInfo);
        }
        alertStatisticsRsp.setParamStatisticsInfo(paramCounts);
        alertStatisticsRsp.setProtectStatisticsInfo(protectCounts);
        alertStatisticsRsp.setFaultStatisticsInfo(faultCounts);
        return alertStatisticsRsp;
    }

    /**
     * 将没有统计数据的日期的报警条数填充为0
     * 
     * @param startTime
     * @param endTime
     * @param statisticsInfo
     * @return
     */
    private List<Integer> disposeDaysWithoutData(Date startTime, Date endTime,
            List<AlertStatisticsNum> statisticsInfo) {
        List<Integer> counts = new ArrayList<>();
        List<String> dayList = getDayList(startTime, endTime);
        int i = 0;
        for (AlertStatisticsNum alertStatisticsNum : statisticsInfo) {
            if (alertStatisticsNum.getDayStr().equals(dayList.get(i))) {
                counts.add(alertStatisticsNum.getSumNum());
                i++;
            } else {
                counts.add(0);
            }
        }
        return counts;
    }

    /**
     * 获取一段时间内的日期列表
     * 
     * @param startTime
     * @param endTime
     * @return
     */
    public List<String> getDayList(Date startTime, Date endTime) {
        List<String> dayList = new ArrayList<>();
        Long endTimeStamp = endTime.getTime();
        Long startTimeStamp = startTime.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        while (endTimeStamp > startTimeStamp) {
            String dayStr = simpleDateFormat.format(endTimeStamp);
            dayList.add(dayStr);
            endTimeStamp = endTimeStamp - 3600 * 1000 * 24;
        }
        String startStr = simpleDateFormat.format(startTimeStamp);
        if (!dayList.get(dayList.size() - 1).equals(startStr)) {
            dayList.add(startStr);
        }
        return dayList;

    }

    /**
     * 获取维修时长统计信息
     * 
     * @param startTime
     * @param endTime
     * @param alertLevel
     * @return
     */
    public List<AlertRepairStatistics> getRepairStatisticsInfo(Date startTime, Date endTime, Short alertLevel) {
        return alertMapper.getRepairStatisticsInfo(startTime, endTime, alertLevel);
    }

    /**
     * 获取报警列表，不按设备分组
     * 
     * @param stage
     * @param assetType
     * @param category
     * @param sortType
     * @param duration
     * @param thingCode
     * @param page
     * @param count
     * @return
     */
    public List<AlertData> getAlertDataList(String stage, Integer level, Short type, Integer system, String assetType,
            String category, Integer sortType, Long duration, String thingCode, Integer page, Integer count) {
        Date endTime = null;
        Date startTime = null;
        Integer offset = null;
        String excluStage = null;
        if (stage == null || AlertConstants.STAGE_UNRELEASE.equals(stage)) {
            excluStage = AlertConstants.STAGE_RELEASE;
        }
        if (duration != null) {
            endTime = new Date();
            startTime = new Date(endTime.getTime() - duration);
        }
        if (page != null && count != null) {
            offset = page * count;
        }
        return alertMapper.getAlertDataList(stage, excluStage, level, type, system, assetType, category, sortType,
                startTime, endTime, thingCode, offset, count);
    }

    /**
     * 获取每个设备最高的报警等级
     * 
     * @param thingCodeList
     * @return
     */
    public Map<String, Short> getSeriousAlertLevel(List<String> thingCodeList) {
        Map<String, Short> levelMap = new HashMap<>();
        for (String thingCode : thingCodeList) {
            short level = 0;
            if (alertDataMap.containsKey(thingCode)) {
                Map<String, AlertData> metricAlertDataMap = alertDataMap.get(thingCode);
                for (Map.Entry<String, AlertData> alertDataEntry : metricAlertDataMap.entrySet()) {
                    AlertData value = alertDataEntry.getValue();
                    if (AlertConstants.STAGE_UNTREATED.equals(value.getAlertStage())) {
                        Short alertLevel = value.getAlertLevel();
                        if (alertLevel != null && alertLevel > level) {
                            level = alertLevel;
                        }
                    }
                }
            }
            levelMap.put(thingCode, level);
        }
        return levelMap;
    }

    /**
     * 设置参数类报警规则的设备、信号可设置范围
     * 
     * @param alertRules
     */
    public List<AlertRule> setParamConfigurationList(List<AlertRule> alertRules) {
        Set<String> inputAlertRulesCode = alertRules.stream()
                .map((AlertRule alertRule) -> alertRule.getThingCode() + "-" + alertRule.getMetricCode())
                .collect(Collectors.toSet());
        List<AlertRule> paramConfigurationList = alertMapper.getParamConfigurationList();
        Set<String> existAlertRulesCode = paramConfigurationList.stream()
                .map((AlertRule alertRule) -> alertRule.getThingCode() + "-" + alertRule.getMetricCode())
                .collect(Collectors.toSet());
        Collection<String> duplicateCodes = CollectionUtils.intersection(inputAlertRulesCode, existAlertRulesCode);
        Collection<String> addCodes = CollectionUtils.subtract(inputAlertRulesCode, existAlertRulesCode);
        List<AlertRule> addList = codesToAlertRule(addCodes);
        List<AlertRule> duplicateList = codesToAlertRule(duplicateCodes);
        if(addList.size()>0) {
            alertMapper.setParamConfigurationList(addList);
        }
        return duplicateList;
    }

    private List<AlertRule> codesToAlertRule(Collection<String> codeCollection) {
        return codeCollection.stream().map((String code) -> {
            AlertRule alertRule = new AlertRule();
            String[] codes = code.split("-");
            alertRule.setThingCode(codes[0]);
            alertRule.setMetricCode(codes[1]);
            return alertRule;
        }).collect(Collectors.toList());
    }

    public AlertRule getParamThreshold(String thingCode, String metricCode) {
        return alertMapper.getParamThreshold(thingCode, metricCode);
    }

    public void setParamThreshlold(AlertRule alertRule) {
        alertMapper.setParamThreshold(alertRule);
    }

}
