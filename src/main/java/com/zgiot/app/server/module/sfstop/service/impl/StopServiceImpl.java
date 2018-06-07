package com.zgiot.app.server.module.sfstop.service.impl;

import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.sfstart.constants.StartConstants;
import com.zgiot.app.server.module.sfstop.constants.StopConstants;
import com.zgiot.app.server.module.sfstop.entity.pojo.*;
import com.zgiot.app.server.module.sfstop.entity.vo.StopExamineResult;
import com.zgiot.app.server.module.sfstop.mapper.StopMapper;
import com.zgiot.app.server.module.sfstop.service.StopService;
import com.zgiot.common.pojo.CurrentUser;
import com.zgiot.common.pojo.SessionContext;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Service
public class StopServiceImpl implements StopService {

    private static final Logger logger = LoggerFactory.getLogger(StopServiceImpl.class);
    @Autowired
    private StopMapper stopMapper;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public StopManualInterventionRecord getStopManualInterventionRecord(int operateId, String thingCode) {
        return stopMapper.getStopManualInterventionRecord(operateId, thingCode);
    }

    @Override
    public List<StopManualInterventionRecord> getStopManualInterventionRecordByLineId(Integer lineId) {
        return stopMapper.getStopManualInterventionRecordByLineId(lineId);
    }

    @Override
    public List<StopOperationRecord> findUnfinishStopOperate(Integer system, Integer startState, Integer finishState) {
        return stopMapper.selectOperateRecordWithoutOperateState(system, startState, finishState);
    }


    @Override
    public void saveStopOperationRecord(StopOperationRecord stopOperationRecord) {
        stopOperationRecord.setCreatedTime(new Date());
        stopOperationRecord.setUpdateTime(new Date());
        CurrentUser currentUser = SessionContext.getCurrentUser();
        if (currentUser != null) {
            stopOperationRecord.setCreateUser(currentUser.getUserId());
            stopOperationRecord.setUpdateUser(currentUser.getUserId());
            stopOperationRecord.setOperateUser(currentUser.getUserId());

        }
        stopOperationRecord.setIsDelete(0);

        stopMapper.saveStopOperationRecord(stopOperationRecord);
    }

    @Override
    public StopOperationRecord getStopOperationRecordByOperateState(int operateState) {
        return stopMapper.getStopOperationRecordByOperateState(operateState);
    }

    @Override
    public Integer getStopOperateId(int system) {
        Integer operateId = null;
        List<StopOperationRecord> stopOperationRecords = stopMapper.selectOperateRecordWithoutOperateState(system, null, StopConstants.STOP_FINISH_STATE);
        if (CollectionUtils.isNotEmpty(stopOperationRecords)) {
            operateId = stopOperationRecords.get(0).getOperateId();
        }
        return operateId;
    }

    @Override
    public void setUpAutoExamine(Set<String> stopDeviceIds, Integer operateId) {


        List<StopExamineRule> stopExamineRules = getStopExamineRuleByDeviceIds(stopDeviceIds);
        // 检查任务建立
        for (StopExamineRule stopExamineRule : stopExamineRules) {
            StopExamineRecord stopExamineRecord = new StopExamineRecord();
            stopExamineRecord.setOperateId(operateId);
            stopExamineRecord.setRuleId(stopExamineRule.getRuleId());
            stopExamineRecord.setExamineType(stopExamineRule.getExamineType());
            stopExamineRecord.setExamineInformation("");
            stopExamineRecord.setExamineResult(StartConstants.EXAMINE_RESULT_NO);
            stopMapper.saveStartExamineRecord(stopExamineRecord);
        }
    }

    @Override
    public List<StopExamineRule> getStopExamineRuleByDeviceIds(Set<String> thingCodes) {
        List<StopExamineRule> startExamineRules = new ArrayList<>();
        for (String thingCode : thingCodes) {
            startExamineRules.addAll(stopMapper.selectStopExamineRuleByThingCode(thingCode));
        }
        return startExamineRules;

    }


    @Override
    public AlertData getMaxLevelAlertData(String thingCode) {
        return stopMapper.getMaxLevelAlertData(thingCode);
    }

    @Override
    public StartOperationRecord getStartOperationRecord(Integer operateState) {
        return stopMapper.getStartOperationRecord(operateState);
    }


    @Override
    public void saveStopChoiceSet(StopChoiceSet stopChoiceSet) {
        stopMapper.saveStopChoiceSet(stopChoiceSet);
    }

    @Override
    public void closeStopOperate(String system) {
        Integer findOperateId = getStopOperateId(Integer.valueOf(system));
        stopMapper.closeOperateStopByOperateId(StopConstants.IS_DELETE, findOperateId);

    }

    @Override
    public boolean judgeStopingState(String system, Integer startState, Integer finishState) {
        List<StopOperationRecord> stopOperationRecords = findUnfinishStopOperate(Integer.valueOf(system), startState, finishState);
        if (CollectionUtils.isNotEmpty(stopOperationRecords)) {
            // 已经存在停车任务
            return true;
        }
        return false;
    }

    @Override
    public void updateStopManualInterventionRecord(String thingCode, Integer operateId, Integer interventionState, String relievePersonId) {
        stopMapper.updateStopManualInterventionRecord(thingCode, operateId, interventionState, relievePersonId);
    }

    @Override
    public void updateStopOperate(String system, int stopFinishState) {

        Integer findOperateId = getStopOperateId(Integer.valueOf(system));
        logger.info("修改{}停车记录状态为{}", findOperateId, stopFinishState);
        stopMapper.updateOperateStateByOperateId(stopFinishState, findOperateId);

    }

    @Override
    public List<StopExamineResult> getStartExaminRecordByOperateId(Integer operateId) {
        return stopMapper.getStartExaminRecordByOperateId(operateId);
    }

    @Override
    public StopExamineRecord getStopExamineRecordByThingCode(Integer operateId, String thingCode) {
        return stopMapper.getStopExamineRecordByThingCode(operateId, thingCode);
    }

    @Override
    public StopExamineRule getStopExamineRuleByRuleId(int ruleId) {
        return stopMapper.getStopExamineRuleByRuleId(ruleId);
    }

    @Override
    public void updateStopExamineResult(int examineResult, int examineId) {
        stopMapper.updateStopExamineResult(examineResult, examineId);
    }

    @Override
    public List<StopExamineRecord> getStopExaminRecordByRuleAndOperateId(Integer ruleId, Integer stopOperateId) {
        return stopMapper.getStopExaminRecordByRuleAndOperateId(ruleId, stopOperateId);
    }

    @Override
    public void updateStopExamineRecord(Integer ruleId, Integer stopOperateId, Integer examineResult, String examineInformation) {
        stopMapper.updateStopExamineRecord(ruleId, stopOperateId, examineResult, examineInformation);
    }
}
