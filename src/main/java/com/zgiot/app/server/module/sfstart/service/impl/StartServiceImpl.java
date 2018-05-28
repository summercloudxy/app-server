package com.zgiot.app.server.module.sfstart.service.impl;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.dataprocessor.DataProcessor;
import com.zgiot.app.server.module.sfstart.StartExamineListener;
import com.zgiot.app.server.module.sfstart.StartListener;
import com.zgiot.app.server.module.sfstart.constants.StartStopConstants;
import com.zgiot.app.server.module.sfstart.controller.StartController;
import com.zgiot.app.server.module.sfstart.mapper.StartMapper;
import com.zgiot.app.server.module.sfstart.pojo.*;
import com.zgiot.app.server.module.sfstart.service.StartService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.impl.mapper.MetricMapper;
import com.zgiot.app.server.service.impl.mapper.TMLMapper;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.MetricModel;
import com.zgiot.common.pojo.ThingMetricLabel;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StartServiceImpl implements StartService {

    private static final Logger logger = LoggerFactory.getLogger(StartServiceImpl.class);
    @Autowired
    private StartMapper startMapper;
    @Autowired
    private DataService dataService;
    @Autowired
    private MetricMapper metricMapper;


    @Autowired
    @Qualifier("wsProcessor")
    private DataProcessor processor;
    @Autowired
    private StartExamineListener startExamineListener;

    @Autowired
    private StartListener startListener;
    @Autowired
    private TMLMapper tmlMapper;

    @Override
    public List<StartOperationRecord> findUnfinishStartOperate(Integer startState, Integer finishState) {
        List<StartOperationRecord> queryStartSystems = startMapper.selectOperateRecordWithoutOperateState(startState, finishState);
        return queryStartSystems;
    }


    @Override
    public List<StartSystem> getStartSystem(Integer productionLine, Integer type, int systemCategory) {
        // 根据期数和类型查询
        List<StartSystem> queryStartSystems = startMapper.selectSystem(productionLine, type, systemCategory);
        return queryStartSystems;
    }

    @Override
    public List<StartSystem> getStartSystemWithStarting(int systemCategory) {
        // 根据期数和类型查询
        List<StartSystem> queryStartSystems = startMapper.selectSystemWithStarting(systemCategory);
        return queryStartSystems;
    }

    @Override
    public void getStartinSystemWithDeviceInformation(List<StartSystem> startSystems) {
        for (StartSystem startSystem : startSystems) {
            Integer findOperateId = findOperateIdWhenNull();
            List<StartDevice> startDevices = startMapper.selectStartingDeviceBySystemIdAndOperateId(findOperateId, startSystem.getSystemId());
            startSystem.setStartDevices(startDevices);
        }
    }

    @Override
    public Integer findOperateIdWhenNull() {
        Integer operateId = null;
        List<StartOperationRecord> startOperationRecords = findUnfinishStartOperate(null, StartStopConstants.START_FINISH_STATE);
        if (CollectionUtils.isNotEmpty(startOperationRecords)) {
            operateId = startOperationRecords.get(0).getOperateId();
        }
        return operateId;
    }

    @Override
    public List<StartDeviceStateRecord> getStartDeviceState() {
        Integer findOperateId = findOperateIdWhenNull();
        List<StartDeviceStateRecord> startDeviceStateRecords = startMapper.selectStartDeviceState(findOperateId, null);
        // 增加设备异常状态下反馈设备异常信息
        for (StartDeviceStateRecord record : startDeviceStateRecords) {
            if (StartStopConstants.DEVICE_STATE_ERROR == record.getState()) {
                record.setExceptionCause(getFaultByDeviceId(record.getDeviceId()));
            }
        }
        return startDeviceStateRecords;
    }

    @Override
    public String getFaultByDeviceId(String deviceId) {
        String fault = "";
        List<StartFaultInformation> startFaultInformations = startMapper.selectFaultByExamineDeviceId(deviceId);
        for (StartFaultInformation startFaultInformation : startFaultInformations) {
            Double error = null;
            try {
                // 获取一个thingCode 和 metricName
                MetricModel metricModel = metricMapper.getMetric(startFaultInformation.getFaultName());
                DataModelWrapper dataModelWrapper = dataService.getData(startFaultInformation.getDeviceCode(), metricModel.getMetricCode()).orElse(null);
                if (dataModelWrapper != null) {
                    if ("ture".equals(dataModelWrapper.getValue()) ) {
                        fault += startFaultInformation.getFaultName() + ";";
                    }
                }
            } catch (Exception e) {
                logger.error("启车自检故障类，故障信息获取错误");
            }
        }
        return fault;
    }

    @Override
    public void closeStartOperate() {
        Integer findOperateId = findOperateIdWhenNull();
        processor.removeListener(startListener);
        StartController.startDeviceRequirements = null;
        processor.removeListener(startExamineListener);
        startMapper.closeOperateStateByOperateId(StartStopConstants.IS_DELETE, findOperateId);
    }

    @Override
    public boolean judgeStartingState(Integer startState, Integer finishState) {
        List<StartOperationRecord> querystartOperationRecord = findUnfinishStartOperate(startState, finishState);
        if (CollectionUtils.isNotEmpty(querystartOperationRecord)) {
            // 已经存在启车任务
            return true;
        }
        return false;
    }

    @Override
    public boolean isfullyExamine() {
        boolean flag = false;
        Integer operateId = findOperateIdWhenNull();
        List<StartExamineRecord> startExamineRecords = new ArrayList<>();
        startExamineRecords.addAll(startMapper.selectFullyExamineErrorByOperateId(operateId, StartStopConstants.REMOTE_ERROR_TYPE));
        startExamineRecords.addAll(startMapper.selectFullyExamineErrorByOperateId(operateId, StartStopConstants.DEVICE_ERROR_TYPE));
        if (CollectionUtils.isEmpty(startExamineRecords)) {
            flag = true;
        }
        return flag;
    }

    @Override
    public List<StartDevice> getDeviceBySystenIds(List<String> systemIds) {
        // 系统内公用设备
        List<String> synergicSystems = findSynergicSystems(systemIds);
        systemIds.addAll(synergicSystems);
        List<StartDevice> startDevices = startMapper.selectDeviceBySystemId(systemIds);
        return startDevices;
    }

    @Override
    public List<String> findSynergicSystems(List<String> systemIds) {
        List<String> synergicSystems = startMapper.selectSynergicSystem(systemIds);
        return synergicSystems;
    }

    @Override
    public List<String> getLabelBydeviceIdsAndName(Set<String> deviceIds, String name) {
        List<String> label = new ArrayList<>();
        for (String deviceId : deviceIds) {
            label.addAll(startMapper.selectLabelByDeviceIdAndName(deviceId, name));
        }
        return label;
    }

    @Override
    public void updateStartOperate(Integer state) {
        Integer findOperateId = findOperateIdWhenNull();
        logger.info("修改{}启车记录状态为{}", findOperateId, state);
        startMapper.updateOperateStateByOperateId(state, findOperateId);
    }

    @Override
    public List<StartDeviceInformation> selectDeviceInformationBySystemId(Integer operateId, Integer ruleId) {
        return startMapper.selectDeviceInformationBySystemId(operateId, null);
    }

    @Override
    public List<StartRequirement> getRequirementByDeviceId(String deviceId) {
        List<StartRequirement> startRequirement = startMapper.selectRequirementByDeviceId(deviceId);
        return startRequirement;
    }

    @Override
    public List<StartExamineRule> getStartExamineRuleByDeviceIds(Set<String> deviceIds) {
        List<StartExamineRule> startExamineRules = new ArrayList<>();
        for (String device : deviceIds) {
            startExamineRules.addAll(startMapper.selectStartExamineRuleByDeviceId(device));
        }
        return startExamineRules;
    }

    @Override
    public List<String> selectLabelByDeviceIdAndName(String deviceId, String name) {
        return startMapper.selectLabelByDeviceIdAndName(deviceId, name);
    }

    @Override
    public List<StartManualInterventionDevice> selectManualInterventionStateByDeviceId(String deviceId) {
        return startMapper.selectManualInterventionStateByDeviceId(deviceId);
    }

    @Override
    public void saveManualInterventionRecord(StartManualInterventionRecord startManualInterventionRecord) {
        startMapper.saveManualInterventionRecord(startManualInterventionRecord);
    }

    @Override
    public void updateZgkwStartManualIntervention(String deviceId, String userId, Integer state, Integer beforeState) {
        startMapper.updateZgkwStartManualIntervention(deviceId, userId, state, beforeState);
    }

    @Override
    public void setUpAreaExamine(Integer operateId) {

        List<StartAreaRule> startAreaRules = startMapper.selectAreaRuleByParentStateAndAreaFirstId(null, null);
        for (StartAreaRule startAreaRule : startAreaRules) {
            startMapper.saveStartAreaRecord(startAreaRule.getRuleId(), startAreaRule.getAreaSecondId(), operateId);
        }
    }

    @Override
    public void saveDeviceStateRecord(Set<String> startDeviceIds, Integer operateId) {
        // 根据不同系统，获取对应启车设备信息
        for (String startDeviceId : startDeviceIds) {
            StartDeviceStateRecord startDeviceStateRecord = new StartDeviceStateRecord();
            startDeviceStateRecord.setOperateId(operateId);
            startDeviceStateRecord.setDeviceId(startDeviceId);
            // 初始状态都是未启动
            startDeviceStateRecord.setState(StartStopConstants.DEVICE_STATE_STANDBY_MODE);
            startDeviceStateRecord.setCreateTime(new Date());
            startDeviceStateRecord.setUpdateTime(new Date());
            startMapper.saveStartDeviceStateRecord(startDeviceStateRecord);
        }

    }

    @Override
    public List<StartSystem> getStartBrowseSystem() {

        // 查询系统分类
        List<StartSystem> startSystems = new LinkedList<>();
        // 查询系统名称系统
        startSystems.addAll(getStartSystemWithStarting(StartStopConstants.SYSTEM_CATEGORY_BROWSE_PAGE));
        for (StartSystem startSystem : startSystems) {
            List<StartDevice> startDevices = startMapper.selectStartingDeviceBySystemIdAndOperateId(null, startSystem.getSystemId());
            startSystem.setStartDevices(startDevices);
        }
        return startSystems;
    }

    @Override
    public List<StartSystem> getStartBrowseDeprotSystem() {
        // 查询系统分类
        List<StartSystem> startSystems = getStartSystemWithStarting(StartStopConstants.SYSTEM_CATEGORY_COAL_DEPOT_PAGE);
        for (StartSystem startSystem : startSystems) {
            List<StartBrowseCoalDevice> startDevices = startMapper.selectStartBrowseCoalDeviceBySystemId(startSystem.getSystemId());
            startSystem.setStartCoalDevice(startDevices);
        }
        return startSystems;
    }

    @Override
    public List<StartDevice> testGetDevice() {
        List<StartDevice> startDevices = startMapper.selectTestDevice();
        return startDevices;
    }

    @Override
    public StartOperationRecord saveStartOperationRecord(List<String> systemIds, String userId) {
        StartOperationRecord startOperationRecord = new StartOperationRecord();
        startOperationRecord.setCreateUser(userId);
        String jsonSystemIds = JSON.toJSONString(systemIds);
        startOperationRecord.setSystemIds(jsonSystemIds);
        // 记录当前起车任务状态
        startOperationRecord.setOperateState(StartStopConstants.START_PREPARE_STATE);
        startMapper.saveStartOperationRecord(startOperationRecord);
        // 设置本次启车操作id
        StartController.setOperateId(startOperationRecord.getOperateId());
        return startOperationRecord;
    }

    @Override
    public void setUpAutoExamine(Set<String> deviceIds, Integer operateId) {

        List<StartExamineRule> startExamineRules = getStartExamineRuleByDeviceIds(deviceIds);
        // 检查任务建立
        for (StartExamineRule startExamineRule : startExamineRules) {
            StartExamineRecord startExamineRecord = new StartExamineRecord();
            startExamineRecord.setOperateId(operateId);
            startExamineRecord.setStartDeviceId(startExamineRule.getStartDeviceId());
            startExamineRecord.setRuleId(startExamineRule.getRuleId());
            startExamineRecord.setExamineType(startExamineRule.getExamineType());
            startExamineRecord.setExamineInformation("");
            startExamineRecord.setExamineResult(StartStopConstants.EXAMINE_RESULT_NO);
            startMapper.saveStartExamineRecord(startExamineRecord);
        }
    }

    @Override
    public List<StartExamineRecord> getAutoExamineRecord() {
        List<StartExamineRecord> startExamineRecords = null;
        Integer operateId = findOperateIdWhenNull();
        if (operateId != null) {
            startExamineRecords = startMapper.getStartExaminRecordByRuleAndOperateId(null, operateId);
        }
        return startExamineRecords;
    }

    @Override
    public List<StartExamineRule> getStartExaminRuleByRuleIdAndLabel(Integer ruleId, String label) {
        return startMapper.getStartExaminRuleByRuleIdAndLabel(ruleId, label);
    }

    @Override
    public List<StartSystem> getAutoExamineSystem() {
        // 查询系统分类
        List<StartSystem> startSystems = new LinkedList<>();
        // 查询系统名称系统
        startSystems.addAll(getStartSystemWithStarting(StartStopConstants.SYSTEM_CATEGORY_STARTING_PAGE));
        getAutoExamineSystemWithDeviceInformation(startSystems);
        return startSystems;
    }

    @Override
    public void getAutoExamineSystemWithDeviceInformation(List<StartSystem> startSystems) {
        for (StartSystem startSystem : startSystems) {
            Integer findOperateId = findOperateIdWhenNull();
            List<StartDevice> startDevices = startMapper.selectAutoExamineDeviceBySystemIdAndOperateId(findOperateId, startSystem.getSystemId());
            startSystem.setStartDevices(startDevices);
        }
    }

    @Override
    public List<StartManualInterventionRecord> getManualInterventionScopeStart(String deviceCode) {
        // 启车前
        List<StartManualInterventionRecord> startManualInterventionDevices = null;
        if (judgeStartingState(null, StartStopConstants.START_FINISH_STATE)) {
            // 启车中
            Integer operateId = findOperateIdWhenNull();
            startManualInterventionDevices = startMapper.selectStartingManualInterventionScopeByLikeDeviceCode(deviceCode, operateId);
        } else {
            // 启车前可设置干预范围
            startManualInterventionDevices = startMapper.selectManualInterventionScopeByLikeDeviceCodeAndState(StartStopConstants.MANUAL_INTERVENTION_FALSE, deviceCode);
        }
        return startManualInterventionDevices;
    }

    @Override
    public void updateStartManualInterventionRecord(String deviceId, Integer operateId, Integer state, String interventionPersonId, String relievePersonId) {
        startMapper.updateStartManualInterventionRecord(deviceId, operateId, state, interventionPersonId, relievePersonId);
    }

    @Override
    public StartManualInterventionRecord getManualInterventionDeviceInformation(String deviceId) {
        // 获取人工干预设备基本信息
        StartManualInterventionRecord startManualInterventionRecord = startMapper.selectManualInterventionInformation(deviceId);
        StartDeviceInformation deviceInformation = startMapper.selectDeviceInformationByDeviceId(deviceId);
        // 获取人工干预设备所属区域
        if (deviceInformation != null &&startManualInterventionRecord!=null&& deviceInformation.getStartHierarchy() != null) {
            String[] deviceHierarchy = deviceInformation.getStartHierarchy().split("-");
            StartAreaInformation startAreaInformation = startMapper.selectAreaInformationByAreaId(deviceHierarchy[1]);
            startManualInterventionRecord.setAreaName(startAreaInformation.getAreaName());
        }
        return startManualInterventionRecord;
    }

    @Override
    public List<StartManualInterventionRecord> getManualInterventionRecord() {
        List<StartManualInterventionRecord> startManualInterventionRecords = null;
        if (judgeStartingState(null, StartStopConstants.START_FINISH_STATE)) {
            // 启车中
            Integer operateId = findOperateIdWhenNull();
            startManualInterventionRecords = startMapper.selectStartingManualInterventionRecord(null, operateId, StartStopConstants.MANUAL_INTERVENTION_TRUE);
        } else {
            // 启车前
            startManualInterventionRecords = startMapper.selectManualInterventionRecordByBefore();
        }
        for (StartManualInterventionRecord startManualInterventionRecord : startManualInterventionRecords) {
            StartManualInterventionRecord manualInterventionrInformation =
                    getManualInterventionDeviceInformation(startManualInterventionRecord.getDeviceId());
            if(manualInterventionrInformation!=null){
                // 补充区域信息
                startManualInterventionRecord.setAreaName(manualInterventionrInformation.getAreaName());
                // 补充系统信息
                startManualInterventionRecord.setSystemName(manualInterventionrInformation.getSystemName());
                // 补充楼层信息
                startManualInterventionRecord.setFloor(manualInterventionrInformation.getFloor());
            }

        }
        return startManualInterventionRecords;
    }

    @Override
    public List<StartPackage> getStartingPackage() {
        // 查询系统分类
        List<StartPackage> startPackages = new LinkedList<>();
        // 查询系统名称系统
        startPackages.addAll(startMapper.selectStartingPackage());
        getStartinPackageWithDeviceInformation(startPackages);
        return startPackages;
    }

    @Override
    public void getStartinPackageWithDeviceInformation(List<StartPackage> startPackages) {
        for (StartPackage startPackage : startPackages) {
            Integer findOperateId = findOperateIdWhenNull();
            List<StartDevice> startDevices = startMapper.selectStartingDeviceByPackageIdAndOperateId(findOperateId, startPackage.getPackageId());
            startPackage.setStartDevices(startDevices);
        }
    }

    @Override
    public List<StartDeviceRelation> selectStartDeviceRelationByPackageIdAndDeviceId(String packageId, String deviceId) {
        return startMapper.selectStartDeviceRelationByPackageIdAndDeviceId(packageId, deviceId);
    }

    @Override
    public List<StartManualInterventionRecord> selectStartingManualInterventionRecord(String deviceId, Integer operateId, Integer state) {
        return startMapper.selectStartingManualInterventionRecord(deviceId, operateId, state);
    }

    @Override
    public Set<String> getAllDeviceIdBySystenIds(List<String> systemIds) {
        List<String> deviceIds = new ArrayList<>();
        List<StartDevice> startDevices = getDeviceBySystenIds(systemIds);
        for (StartDevice startDevice : startDevices) {
            deviceIds.add(startDevice.getDeviceId());
        }
        HashSet<String> deviceIdHashSet = new HashSet<>(deviceIds);
        return deviceIdHashSet;
    }

    @Override
    public List<String> selectStartDeviceIdBySystemCategory(Integer systemCategory, Integer systemType) {
        return startMapper.selectStartDeviceIdBySystemCategory(systemCategory, systemType);
    }

    @Override
    public StartSignal getStartSignalByDeviceId(String deviceId) {
        List<StartSignal> startSignals = startMapper.getStartSignalByDeviceId(deviceId);
        if (CollectionUtils.isNotEmpty(startSignals)) {
            return startSignals.get(0);
        }
        return null;
    }

    @Override
    public StartDevice selectStartDeviceByDeviceId(String deviceId) {
        return startMapper.selectStartDeviceByDeviceId(deviceId);
    }

    @Override
    public List<StartSingleLabelAndValue> selectTransformerInformation(String labelName) {
        return startMapper.selectTransformerInformation(labelName);
    }

    @Override
    public List<StartExamineRecord> getStartExaminRecordByRuleAndOperateId(Integer ruleId, Integer operateId) {
        return startMapper.getStartExaminRecordByRuleAndOperateId(ruleId, operateId);
    }

    @Override
    public void updateStartExamineRecord(Integer ruleId, Integer operateId, Integer examineResult, String examineInformation) {
        startMapper.updateStartExamineRecord(ruleId, operateId, examineResult, examineInformation);
    }

    @Override
    public StartDeviceSignal getStartDeviceSignalById(int id) {
        return startMapper.getStartDeviceSignalById(id);
    }

    @Override
    public List<StartSingleLabelAndValue> selectPackageWaitTime(String deviceCodePart, String name) {
        return startMapper.selectPackageWaitTime(deviceCodePart, name);
    }

    @Override
    public List<StartSingleLabelAndValue> selectPackageBelongArea(String deviceCodePart, String name) {
        return startMapper.selectPackageBelongArea(deviceCodePart, name);
    }

    @Override
    public List<StartSingleLabelAndValue> selectPackageBelongRegion(String deviceCodePart, String name) {
        return startMapper.selectPackageBelongRegion(deviceCodePart, name);
    }

    @Override
    public List<StartSingleLabelAndValue> selectAreaBelongRegion(String deviceCodePart, String name) {
        return startMapper.selectAreaBelongRegion(deviceCodePart, name);
    }

    @Override
    public List<String> selectDeviceIdByDatelabel(String datalabel, String labelName) {
        return startMapper.selectDeviceIdByDatelabel(datalabel, labelName);
    }

    @Override
    public void updateStartDeviceState(Integer operateId, String deviceId, Integer value) {
        startMapper.updateStartDeviceState(operateId, deviceId, value);
    }

    @Override
    public StartSingleLabelAndValue selectFrequency(String deviceId, String name, String startType, Integer type) {
        return startMapper.selectFrequency(deviceId, name, startType, type);
    }

    @Override
    public List<StartAreaRule> selectAreaRuleByParentStateAndAreaFirstId(Integer parentState, Integer areaFirstId) {
        return startMapper.selectAreaRuleByParentStateAndAreaFirstId(parentState, areaFirstId);
    }

    @Override
    public void updateStartAreaRecord(Integer operateId, Integer state, Integer areaRuleId) {
        startMapper.updateStartAreaRecord(operateId, state, areaRuleId);
    }

    @Override
    public List<Integer> selectAreaRuleRecordByStateAndAreaSecondId(Integer operateId, Integer state, Integer areaSecondId) {
        return startMapper.selectAreaRuleRecordByStateAndAreaSecondId(operateId, state, areaSecondId);
    }

    @Override
    public StartSingleLabelAndValue selectAreaStartLabel(String deviceCodePart, Integer number, String name) {
        return startMapper.selectAreaStartLabel(deviceCodePart, number, name);
    }

    @Override
    public ThingMetricCode getStartSignalByDataLabel(String dataLabel) {
        ThingMetricCode thingMetricCode = new ThingMetricCode();
        ThingMetricLabel thingMetricLabel = tmlMapper.getThingMetricLabel(dataLabel);
        if (thingMetricLabel != null) {

            thingMetricCode.setThingCode(thingMetricLabel.getThingCode());
            thingMetricCode.setMetricCode(thingMetricLabel.getMetricCode());
        } else {
            StartSignal startSignal = startMapper.getStartSignalByDataLabel(dataLabel);
            thingMetricCode.setThingCode(startSignal.getDeviceCode());
            StartDeviceSignal startDeviceSignal = startMapper.getStartDeviceSignalById(startSignal.getName());
            MetricModel metricModel = tmlMapper.findMetricByMetricName(startDeviceSignal.getName());
            thingMetricCode.setMetricCode(metricModel.getMetricCode());
        }
        return thingMetricCode;
    }

    @Override
    public String getMetricCodeByStartDeviceSignalId(int id) {
        //液位单独处理
        if(id==1093){
            return "LEVEL";
        }else {
            StartDeviceSignal startDeviceSignal = startMapper.getStartDeviceSignalById(id);
            MetricModel metricModel = tmlMapper.findMetricByMetricName(startDeviceSignal.getName());
            return metricModel.getMetricCode();
        }

    }


}
