package com.zgiot.app.server.module.alert;

import com.zgiot.app.server.module.alert.mapper.AlertMapper;
import com.zgiot.app.server.module.alert.pojo.*;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.MetricService;
import com.zgiot.app.server.service.ThingService;
import com.zgiot.app.server.service.impl.FileServiceImpl;
import com.zgiot.common.constants.AlertConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private Map<String, Map<String, List<AlertRule>>> paramRuleMap = new ConcurrentHashMap<>();
    private Map<String, Map<String, AlertRule>> protectRuleMap = new ConcurrentHashMap<>();
    private Map<String, Short> metricAlertTypeMap = new HashMap<>();
    private Map<String, Map<String, AlertData>> alertParamDataMap = new ConcurrentHashMap<>();
    private DelayQueue<VerifyDelayed> verifyDelayQueue = new DelayQueue<>();
    private static final String MESSAGE_URI = "/topic/alert/message";
    private static final String REPAIR_URI = "/topic/alert/repair";
    private static final String FEEDBACK_URI = "/topic/alert/feedback";
    private static final String READ_STATE_URI = "/topic/alert/readstate";
    private static final int VERIFY_TO_UNTREATED_PERIOD = 60000;
    private static final int SORT_TYPE_TIME_DESC = 0;
    private static final int SORT_TYPE_TIME_ASC = 1;
    private static final int SORT_TYPE_LEVEL_DESC = 2;
    private static final int SORT_TYPE_LEVEL_ASC = 3;
    private static final int READ_STATE = 1;
    private static final int STATISTICS_TYPE_DEVICE = 1;
    private static final String SPLIT_CHARACTER = "&%";
    @Autowired
    private AlertMapper alertMapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CmdControlService cmdControlService;
    @Autowired
    private MetricService metricService;
    @Autowired
    private ThingService thingService;
    private static final Logger logger = LoggerFactory.getLogger(AlertManager.class);
    private static final int SORT_DESC = 0;
    private static final int SORT_ASC = 1;

    //待解除
    private Map<String, Map<String, AlertData>> relieveAlertDataCache = new ConcurrentHashMap<>();

    //解除报警Map
    private Map<String,Map<String,AlertRelieveTime>> paramRelieveTimeMap=new ConcurrentHashMap<>();


    public void init() {
        initMetricAlertType();
        initParamRuleMap();
        initProtectRuleMap();
        initAlertDataMap();
        initParamRelieveTimeMap();

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
        if (alertDataMap.containsKey(thingCode) && alertDataMap.get(thingCode).containsKey(metricCode)) {
            return alertDataMap.get(thingCode).get(metricCode);
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
            alertDataMetricMap = new ConcurrentHashMap<>();
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

    public List<AlertRule> getParamRules(String thingCode, String metricCode) {
        if (paramRuleMap.containsKey(thingCode)) {
            Map<String, List<AlertRule>> metricRuleMap = paramRuleMap.get(thingCode);
            if (metricRuleMap.containsKey(metricCode)) {
                List<AlertRule> alertRules = metricRuleMap.get(metricCode);
                return alertRules;
            }
        }
        return Collections.emptyList();
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
            if (alertRule.getId() == null) {
                disposeNewAlertRule(type, alertRule);
            } else {
                disposeExistAlerRule(type, alertRule);
            }
        }
        return alertRules;

    }

    private void disposeExistAlerRule(int type, AlertRule alertRule) {
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
    }

    private void disposeNewAlertRule(int type, AlertRule alertRule) {
        alertMapper.insertAlertRule(alertRule);
        if (alertRule.getEnable()) {
            if (type == AlertConstants.TYPE_PARAM) {
                insertParamRule(alertRule);
            } else {
                insertProtectRule(alertRule);
            }
        }
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
        for (Map.Entry<String, Map<String, AlertRule>> entry : protectRuleMap.entrySet()) {
            Map<String, AlertRule> metricRuleMap = entry.getValue();
            if (removeProtectRuleInMetricMap(id, metricRuleMap)) {
                break;
            }
        }

    }

    private boolean removeProtectRuleInMetricMap(Long id, Map<String, AlertRule> metricRuleMap) {
        for (Map.Entry<String, AlertRule> innerEntry : metricRuleMap.entrySet()) {
            AlertRule alertRule = innerEntry.getValue();
            String metricCode = innerEntry.getKey();
            if (alertRule.getId().equals(id)) {
                metricRuleMap.remove(metricCode);
                return true;
            }
        }
        return false;
    }

    private void removeParamRule(Long id) {
        for (Map.Entry<String, Map<String, List<AlertRule>>> entry : paramRuleMap.entrySet()) {
            Map<String, List<AlertRule>> metricRuleMap = entry.getValue();
            if (removeParamRuleInMetricMap(id, metricRuleMap)) {
                break;
            }
        }
    }

    private boolean removeParamRuleInMetricMap(Long id, Map<String, List<AlertRule>> metricRuleMap) {
        for (Map.Entry<String, List<AlertRule>> innerEntry : metricRuleMap.entrySet()) {
            List<AlertRule> alertRules = innerEntry.getValue();
            if (removeParamRuleInRuleList(id, alertRules)) {
                return true;
            }
        }
        return false;
    }

    private boolean removeParamRuleInRuleList(Long id, List<AlertRule> alertRules) {
        for (AlertRule alertRule : alertRules) {
            if (alertRule.getId().equals(id)) {
                alertRules.remove(alertRule);
                return true;
            }
        }
        return false;
    }

    private void updateParamRule(AlertRule alertRule) {
        for (Map.Entry<String, Map<String, List<AlertRule>>> entry : paramRuleMap.entrySet()) {
            Map<String, List<AlertRule>> metricRuleMap = entry.getValue();

            if (updateParamRuleInMetricMap(alertRule, metricRuleMap)) {
                break;
            }
        }
    }

    private boolean updateParamRuleInMetricMap(AlertRule alertRule, Map<String, List<AlertRule>> metricRuleMap) {
        for (Map.Entry<String, List<AlertRule>> innerEntry : metricRuleMap.entrySet()) {
            List<AlertRule> alertRules = innerEntry.getValue();
            if (updateParamRuleInRuleList(alertRule, alertRules)) {
                return true;
            }
        }
        return false;
    }

    private boolean updateParamRuleInRuleList(AlertRule alertRule, List<AlertRule> alertRules) {
        for (AlertRule existAlertRule : alertRules) {
            if (existAlertRule.getId().equals(alertRule.getId())) {
                alertRules.remove(existAlertRule);
                alertRules.add(alertRule);
                return true;
            }
        }
        return false;
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
        //包含
        if (paramRuleMap.containsKey(alertRule.getThingCode())) {
            metricAlertRuleMap = paramRuleMap.get(alertRule.getThingCode());
            if (!metricAlertRuleMap.containsKey(alertRule.getMetricCode())) {
                metricAlertRuleMap.put(alertRule.getMetricCode(), new CopyOnWriteArrayList<>());
            }
            //不包含
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
    private void initParamRuleMap() {
        List<AlertRule> wholeAlertRuleList = alertMapper.getWholeAlertRuleList(AlertConstants.TYPE_PARAM);
        for (AlertRule alertRule : wholeAlertRuleList) {
            insertParamRule(alertRule);

        }
    }

    /**
     * 初始化保护类报警规则
     */
    private void initProtectRuleMap() {
        List<AlertRule> wholeAlertRuleList = alertMapper.getWholeAlertRuleList(AlertConstants.TYPE_PROTECT);
        for (AlertRule alertRule : wholeAlertRuleList) {
            insertProtectRule(alertRule);
        }
    }

    /**
     * 获取报警规则
     *
     * @param filterCondition 筛选条件
     * @return
     */
    public AlertRuleRsp getParamAlertRuleList(FilterCondition filterCondition) {
        Integer pageCount = null;
        if (filterCondition.getCount() != null) {
            Integer paramAlertConfSize = alertMapper.getParamAlertConfSize(filterCondition);
            if (paramAlertConfSize == null) {
                pageCount = 0;
            } else {
                pageCount = (int) Math.ceil((double) paramAlertConfSize / filterCondition.getCount());
            }
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
            getMetricAndSystemName(thingAlertRule);
        }
        AlertRuleRsp alertRuleRsp = new AlertRuleRsp(paramAlertConfList);
        alertRuleRsp.setPageCount(pageCount);
        return alertRuleRsp;
    }

    public AlertRuleRsp getProtAlertRuleList(FilterCondition filterCondition) {
        Integer protAlertRuleCount = alertMapper.getProtAlertRuleCount(filterCondition);
        Integer pageCount = null;
        if (filterCondition.getPage() != null && filterCondition.getCount() != null) {
            filterCondition.setOffset(filterCondition.getPage() * filterCondition.getCount());
            if (protAlertRuleCount == null) {
                pageCount = 0;
            } else {
                pageCount = (int) Math.ceil((double) protAlertRuleCount / filterCondition.getCount());
            }
        }
        List<ThingAlertRule> protAlertRuleList = alertMapper.getProtAlertRuleList(filterCondition);
        for (ThingAlertRule alertRule : protAlertRuleList) {
            getMetricAndSystemName(alertRule);
        }
        AlertRuleRsp alertRuleRsp = new AlertRuleRsp(protAlertRuleList);
        alertRuleRsp.setPageCount(pageCount);
        return alertRuleRsp;

    }

    private void getMetricAndSystemName(AlertData alertData) {
        SystemModel systemModel = alertData.getSystemModel();
        if (systemModel != null) {
            int systemId = systemModel.getId();
            SystemModel systemNameModel = thingService.findSystemById(systemId);
            alertData.setSystemModel(systemNameModel);
        }
        ThingModel thingModel = alertData.getThingModel();
        if (thingModel != null && thingModel.getThingType2Code() != null) {
            CategoryModel categoryModel = thingService.findCategoryByCode(thingModel.getThingType2Code());
            if (categoryModel != null) {
                thingModel.setThingType2Code(categoryModel.getCategoryName());
            }
        }
    }

    private void getMetricAndSystemName(AlertMaskStatistics alertMaskStatistics) {
        Integer systemId = alertMaskStatistics.getSystemId();
        if (systemId != null) {
            SystemModel systemNameModel = thingService.findSystemById(systemId);
            alertMaskStatistics.setSystemName(systemNameModel.getSystemName());
        }
        String metricCode = alertMaskStatistics.getMetricCode();
        MetricModel metric = metricService.getMetric(metricCode);
        if (metric != null) {
            alertMaskStatistics.setAlertInfo(metric.getMetricName());
        }
    }

    private void getMetricAndSystemName(ThingAlertRule alertRule) {
        if (alertRule.getMetricType() != null) {
            alertRule.setMetricTypeName(metricService.getMetricTypeName(alertRule.getMetricType()));
        }
        if (alertRule.getSystemId() != null) {
            SystemModel systemModel = thingService.findSystemById(alertRule.getSystemId());
            if (systemModel != null) {
                alertRule.setSystemName(systemModel.getSystemName());
            }
        }
        if (alertRule.getCategory() != null) {
            CategoryModel categoryModel = thingService.findCategoryByCode(alertRule.getCategory());
            if (categoryModel != null) {
                alertRule.setCategoryName(categoryModel.getCategoryName());
            }
        }

    }

    /**
     * 初始化信号的报警判断类型
     */
    private void initMetricAlertType() {
        List<MetricAlertType> metricAlertTypes = alertMapper.getMetricAlertType();
        for (MetricAlertType alertType : metricAlertTypes) {
            metricAlertTypeMap.put(alertType.getMetricCode(), alertType.getAlertType());
        }
    }

    /**
     * 初始化当前已有报警
     */
    private void initAlertDataMap() {
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
                    metricParamAlertData = new ConcurrentHashMap<>();
                    alertParamDataMap.put(alertData.getThingCode(), metricParamAlertData);
                }
                metricParamAlertData.put(alertData.getMetricCode(), alertData);
            }
            //这是讲数据已处理，已评级的数据放入到延时队列中
            if (AlertConstants.STAGE_VERIFIED.equals(alertData.getAlertStage())) {
                verifyDelayQueue.put(new VerifyDelayed(alertData, VERIFY_TO_UNTREATED_PERIOD));
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
    public Integer sendAlertCmd(String thingCode, String metricCode, AlertMessage alertMessage) {
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
        } else if (AlertConstants.PERMISSION_DISPATCHER.equals(alertMessage.getPermission()) && !alertMessage.getUserId().equals(alertData.getDispatcher())) {
            alertData.setDispatcher(alertMessage.getUserId());
            updateFlag = true;
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
        logger.debug("推送报警消息，报警设备{}，报警内容{}，未发现报警存在", alertData.getThingCode(), alertData.getMetricCode());
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
        verifyDelayQueue.put(new VerifyDelayed(alertData, VERIFY_TO_UNTREATED_PERIOD));
        alertMapper.saveAlertMessage(alertMessage);
        messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
        logger.debug("推送报警消息，报警设备{}，报警内容{}，核实报警存在", alertData.getThingCode(), alertData.getMetricCode());
    }

    /**
     * 申报维修（调度）
     *
     * @param alertData
     */
    private void assignRepair(AlertData alertData, AlertMessage alertMessage) {
        alertData.setAlertStage(AlertConstants.STAGE_REQUEST_REPAIR);
        alertData.setRepair(true);
        alertMapper.saveAlertMessage(alertMessage);
        messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
        messagingTemplate.convertAndSend(REPAIR_URI, alertData);
        logger.debug("推送报警消息，报警设备{}，报警内容{}，申报维修", alertData.getThingCode(), alertData.getMetricCode());
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
        messagingTemplate.convertAndSend(REPAIR_URI, alertData);
        logger.debug("推送报警消息，报警设备{}，报警内容{}，结束维修", alertData.getThingCode(), alertData.getMetricCode());
    }


    /**
     * 申请复位（岗位）
     *
     * @param thingCode
     * @param userId
     * @param permission
     */
    public void requestReset(String thingCode, String userId, String permission) {
        Map<String, AlertData> thingAlertMap = alertDataMap.get(thingCode);
        if (thingAlertMap == null || alertDataMap.isEmpty()) {
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
    }



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
    private void gradeAlert(AlertData alertData, AlertMessage alertMessage) {
        alertData.setAlertStage(AlertConstants.STAGE_VERIFIED);
        alertData.setAlertLevel(Short.parseShort(alertMessage.getInfo()));
        alertData.setVerifyTime(new Date());
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
    private void sceneConfirmReleaseAlert(AlertData alertData, AlertMessage alertMessage, Boolean sceneConfirmState) {
        alertData.setSceneConfirmState(sceneConfirmState);
        alertData.setSceneConfirmTime(new Date());
        alertData.setSceneConfirmUser(alertMessage.getUserId());
        releaseAlert(alertData);

        //报警之后需要设置Map缓存
        clearParamDataMap(alertData.getThingCode(),alertData.getMetricCode());

        clearRelieveAlertDataCache(alertData.getThingCode(),alertData.getMetricCode());

        alertMapper.saveAlertMessage(alertMessage);
        messagingTemplate.convertAndSend(MESSAGE_URI, alertMessage);
        logger.debug("推送报警消息，报警设备{}，报警内容{}，现场确认报警解除状态：{}", alertData.getThingCode(), alertData.getMetricCode(),
                sceneConfirmState);
    }

    /**
     * 建议屏蔽（岗位）
     *
     * @param alertData
     * @param alertMessage
     */
    private void maskAlert(AlertData alertData, AlertMessage alertMessage) {
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
                                                          Integer sortType, Long duration, String thingCode, Integer page, Integer count, Date endTime) {

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
        if (alertMessages != null && !alertMessages.isEmpty()) {
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
     * @param type       0：全部统计信息 1：设备具体统计信息
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
        AlertStatisticsNum statisticsNum =
                alertMapper.getStatisticsInfo(type, alertStage, excluStage, startTime, endTime).get(0);
        statisticsNum.setAlertLevelNumList(alertLevelNumList);
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
            alertStatisticsNum.getAlertLevelNumList().add(alertLevelNum);
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
            List<AlertLevelNum> alertLevelNumList1 = o1.getAlertLevelNumList();
            Integer sumNum1 = 0;
            Integer sumNum2 = 0;
            for (AlertLevelNum levelNum : alertLevelNumList1) {
                sumNum1 += levelNum.getCount();
            }
            o1.setSumNum(sumNum1);
            List<AlertLevelNum> alertLevelNumList2 = o2.getAlertLevelNumList();
            for (AlertLevelNum levelNum : alertLevelNumList2) {
                sumNum2 += levelNum.getCount();
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
        int dayNum = (int) Math.ceil((endTime.getTime() - startTime.getTime()) / (1000 * 3600 * 24f));
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
        Map<String, AlertStatisticsNum> dayWithStatistics = statisticsInfo.stream().collect(Collectors.toMap(AlertStatisticsNum::getDayStr, t -> t));
        List<String> dayList = getDayList(startTime, endTime);
        for (String day : dayList) {
            if (dayWithStatistics.containsKey(day)) {
                counts.add(dayWithStatistics.get(day).getSumNum());
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
    private List<String> getDayList(Date startTime, Date endTime) {
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
        dayList.sort(String::compareTo);
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
     * @return
     */
    public AlertRecord getAlertDataList(FilterCondition filterCondition) {
        Integer pageCount = null;
        Long queryTime;
        if (filterCondition.getStage() == null || AlertConstants.STAGE_UNRELEASE.equals(filterCondition.getStage())) {
            filterCondition.setExcluStage(AlertConstants.STAGE_RELEASE);
        }
        if (filterCondition.getEndTime() == null) {
            queryTime = System.currentTimeMillis();
            filterCondition.setEndTime(new Date(queryTime));
            if (filterCondition.getDuration() != null) {
                filterCondition.setStartTime(new Date(queryTime - filterCondition.getDuration()));
            }
        } else {
            queryTime = filterCondition.getEndTime().getTime();
            if (filterCondition.getDuration() != null) {
                filterCondition.setStartTime(new Date(queryTime - filterCondition.getDuration()));
            }
        }
        if (filterCondition.getPage() != null && filterCondition.getCount() != null) {
            filterCondition.setOffset(filterCondition.getPage() * filterCondition.getCount());
            Integer wholeCount = alertMapper.getAlertDataListCount(filterCondition);
            if (wholeCount == null) {
                pageCount = 0;
            } else {
                pageCount = (int) Math.ceil((double) wholeCount / filterCondition.getCount());
            }
        }
        AlertRecord alertRecord = new AlertRecord();
        alertRecord.setPageCount(pageCount);
        alertRecord.setQueryTime(queryTime);
        List<AlertData> alertDataList = alertMapper.getAlertDataList(filterCondition);
        for (AlertData alertData : alertDataList) {
            getMetricAndSystemName(alertData);
        }
        alertRecord.setAlertDataList(alertMapper.getAlertDataList(filterCondition));
        return alertRecord;
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
                    Short alertLevel = value.getAlertLevel();
                    if (alertLevel != null && alertLevel > level) {
                        level = alertLevel;
                    }
                }
            }
            levelMap.put(thingCode, level);
        }
        return levelMap;
    }


    public List<AlertData> getSeriousAlertLevelInList(List<String> thingCodeList, int count) {
        TreeSet<AlertData> alertDataSet = new TreeSet<>((AlertData o1, AlertData o2) -> {
            if (o2.getAlertLevel() == null) {
                return -1;
            }
            if (o2.getAlertLevel().equals(o1.getAlertLevel())) {
                if (o2.getAlertDateTime().equals(o1.getAlertDateTime())) {
                    return o2.getThingCode().compareTo(o1.getThingCode());
                }
                return o2.getAlertDateTime().compareTo(o1.getAlertDateTime());
            }
            return o2.getAlertLevel().compareTo(o1.getAlertLevel());
        });
        Map<String, AlertData> seriousAlertDataMap = new HashMap<>();
        for (String thingCode : thingCodeList) {
            getSeriousAlertMap(seriousAlertDataMap, thingCode);
        }
        alertDataSet.addAll(seriousAlertDataMap.values());
        List<AlertData> alertData = new ArrayList<>(alertDataSet);
        if (alertData.size() > count) {
            alertData = alertData.subList(0, count);
        }

        return alertData;
    }

    void getSeriousAlertMap(Map<String, AlertData> seriousAlertDataMap, String thingCode) {
        short level = 0;
        if (seriousAlertDataMap.containsKey(thingCode)) {
            level = seriousAlertDataMap.get(thingCode).getAlertLevel();
        }
        if (alertDataMap.containsKey(thingCode)) {
            Map<String, AlertData> metricAlertDataMap = alertDataMap.get(thingCode);
            for (Map.Entry<String, AlertData> alertDataEntry : metricAlertDataMap.entrySet()) {
                AlertData value = alertDataEntry.getValue();
                Short alertLevel = value.getAlertLevel();
                if (alertLevel != null && alertLevel > level) {
                    seriousAlertDataMap.put(thingCode, value);
                }
            }
        }
    }

    /**
     * 设置参数类报警规则的设备、信号可设置范围
     *
     * @param alertRules
     */
    public List<AlertRule> setParamConfigurationList(List<AlertRule> alertRules) {
        Set<String> inputAlertRulesCode = alertRules.stream()
                .map(alertRule -> alertRule.getThingCode() + SPLIT_CHARACTER + alertRule.getMetricCode())
                .collect(Collectors.toSet());
        List<AlertRule> paramConfigurationList = alertMapper.getParamConfigurationList(null);
        Set<String> existAlertRulesCode = paramConfigurationList.stream()
                .map(alertRule -> alertRule.getThingCode() + SPLIT_CHARACTER + alertRule.getMetricCode())
                .collect(Collectors.toSet());
        Collection<String> duplicateCodes = CollectionUtils.intersection(inputAlertRulesCode, existAlertRulesCode);
        Collection<String> addCodes = CollectionUtils.subtract(inputAlertRulesCode, existAlertRulesCode);
        List<AlertRule> addList = codesToAlertRule(addCodes);
        List<AlertRule> duplicateList = codesToAlertRule(duplicateCodes);
        if (!addList.isEmpty()) {
            alertMapper.setParamConfigurationList(addList);
        }
        return duplicateList;
    }


    public List<ConfigurableAlertRule> getParamConfigurationList(String assetType, String category, String metricCode,
                                                                 String metricType, String thingStartCode) {
        List<ThingModel> thingCodeByAssetAndCategory = thingService.getThingCodeByAssetAndCategory(assetType, category, metricCode, metricType, thingStartCode);
        List<AlertRule> paramConfigurationList = alertMapper.getParamConfigurationList(metricCode);
        Set<String> thingSet = paramConfigurationList.stream().map(AlertRule::getThingCode).collect(Collectors.toSet());
        List<ConfigurableAlertRule> configurableAlertRules = new ArrayList<>(thingCodeByAssetAndCategory.size());
        for (ThingModel thingModel : thingCodeByAssetAndCategory) {
            ConfigurableAlertRule configurableAlertRule = new ConfigurableAlertRule();
            configurableAlertRule.setThingCode(thingModel.getThingCode());
            configurableAlertRule.setThingName(thingModel.getThingName());
            configurableAlertRule.setMetricCode(metricCode);
            if (thingSet.contains(thingModel.getThingCode())) {
                configurableAlertRule.setConfigured(true);
            }
            configurableAlertRules.add(configurableAlertRule);
        }
        return configurableAlertRules;
    }

    private List<AlertRule> codesToAlertRule(Collection<String> codeCollection) {
        return codeCollection.stream().map((String code) -> {
            AlertRule alertRule = new AlertRule();
            String[] codes = code.split(SPLIT_CHARACTER);
            alertRule.setThingCode(codes[0]);
            alertRule.setMetricCode(codes[1]);
            return alertRule;
        }).collect(Collectors.toList());
    }

    public AlertRule getParamThreshold(String thingCode, String metricCode) {
        return alertMapper.getParamThreshold(thingCode, metricCode);
    }

    public void setParamThreshlold(AlertRule alertRule) {
        AlertRule paramThreshold = getParamThreshold(alertRule.getThingCode(), alertRule.getMetricCode());
        if (paramThreshold == null || paramThreshold.getId() == null) {
            alertMapper.insertParamThreshold(alertRule);
        } else {
            alertMapper.setParamThreshold(alertRule);
        }
    }


    /**
     * 获取屏蔽统计信息
     *
     * @param filterCondition
     * @return
     */
    public AlertMaskRsp getMaskStatisticInfo(FilterCondition filterCondition) {
        Integer pageCount = null;
        Long queryTime;
        if (filterCondition.getEndTime() == null) {
            queryTime = System.currentTimeMillis();
            filterCondition.setEndTime(new Date(queryTime));
        } else {
            queryTime = filterCondition.getEndTime().getTime();
        }
        if (filterCondition.getCount() != null && filterCondition.getPage() != null) {
            filterCondition.setOffset(filterCondition.getPage() * filterCondition.getCount());
            Integer wholeCount = alertMapper.getMaskStatisticsInfoCount(filterCondition);
            if (wholeCount != null) {
                pageCount = (int) Math.ceil((double) wholeCount / filterCondition.getCount());
            }
        }
        AlertMaskRsp alertMaskRsp = new AlertMaskRsp();
        List<AlertMaskStatistics> maskStatisticsInfo = alertMapper.getMaskStatisticsInfo(filterCondition);
        for (AlertMaskStatistics alertMaskStatistics : maskStatisticsInfo) {
            getMetricAndSystemName(alertMaskStatistics);
        }
        alertMaskRsp.setAlertMaskStatisticsInfo(maskStatisticsInfo);
        alertMaskRsp.setPageCount(pageCount);
        alertMaskRsp.setQueryTime(queryTime);
        return alertMaskRsp;
    }

    /**
     * 获取具体的屏蔽信息
     *
     * @param filterCondition
     * @return
     */
    public List<AlertMaskInfo> getDetailMaskInfo(FilterCondition filterCondition) {
        List<AlertMaskInfo> alertMaskInfo = alertMapper.getAlertMaskInfo(filterCondition);
        alertMaskInfo.sort(Comparator.comparingInt((AlertMaskInfo t) -> t.getAlertMasks().size()).reversed());
        return alertMaskInfo;
    }

    /**
     * 获取待解除报警Map
     * @return
     */
    public Map<String, Map<String, AlertData>> getRelieveAlertDataCache() {
        return relieveAlertDataCache;
    }

    /**
     * 清除报警Map中的对象
     */
    public void clearParamDataMap(String thingCode,String metriCode){
        if(alertParamDataMap.containsKey(thingCode) && alertParamDataMap.get(thingCode).containsKey(metriCode)){
            alertParamDataMap.get(thingCode).remove(metriCode);
        }
    }

    /**
     * 清除待解除报警map中的对象
     */
    public void clearRelieveAlertDataCache(String thingCode,String metriCode){
        if(relieveAlertDataCache.containsKey(thingCode) && relieveAlertDataCache.get(thingCode).containsKey(metriCode)){
            relieveAlertDataCache.get(thingCode).remove(metriCode);
        }
    }

    /**
     *初始化报警解除时间Map
     */
    private void initParamRelieveTimeMap() {
        List<AlertRelieveTime> wholeAlertRelieveTime=alertMapper.getWholeAlertRelieveTimeList();
        for (AlertRelieveTime alertRelieveTime:wholeAlertRelieveTime) {
            insertRelieveTime(alertRelieveTime);
        }
    }

    /**
     * 向Map集合中放入参数
     * @param alertRelieveTime
     */
    private void insertRelieveTime(AlertRelieveTime alertRelieveTime) {
        Map<String,AlertRelieveTime> metricRelieveTimeMap;
        if(paramRelieveTimeMap.containsKey(alertRelieveTime.getThingCode())){
            metricRelieveTimeMap = paramRelieveTimeMap.get(alertRelieveTime.getThingCode());
        }else{
            metricRelieveTimeMap=new ConcurrentHashMap<>();
            paramRelieveTimeMap.put(alertRelieveTime.getThingCode(),metricRelieveTimeMap);
        }
        metricRelieveTimeMap.put(alertRelieveTime.getMetricCode(),alertRelieveTime);
    }

    public Map<String, Map<String, AlertRelieveTime>> getParamRelieveTimeMap() {
        return paramRelieveTimeMap;
    }
}
