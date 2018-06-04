package com.zgiot.app.server.module.sfstop.service.impl;

import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.sfstart.constants.StartConstants;
import com.zgiot.app.server.module.sfstop.constants.StopConstants;
import com.zgiot.app.server.module.sfstop.entity.pojo.*;
import com.zgiot.app.server.module.sfstop.mapper.StopMapper;
import com.zgiot.app.server.module.sfstop.service.StopService;
import com.zgiot.common.pojo.CurrentUser;
import com.zgiot.common.pojo.SessionContext;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Service
public class StopServiceImpl implements StopService {
    @Autowired
    private StopMapper stopMapper;

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
            stopExamineRecord.setStopThingCode(stopExamineRule.getStopThingCode());
            stopExamineRecord.setRuleId(stopExamineRule.getRuleId());
            stopExamineRecord.setExamineType(stopExamineRule.getExamineType());
            stopExamineRecord.setExamineInformation("");
            stopExamineRecord.setExamineResult(StartConstants.EXAMINE_RESULT_NO);
            stopMapper.saveStartExamineRecord(stopExamineRecord);
        }
    }

    @Override
    public List<StopExamineRule> getStopExamineRuleByDeviceIds(Set<String> deviceIds) {
        List<StopExamineRule> startExamineRules = new ArrayList<>();
        for (String device : deviceIds) {
            startExamineRules.addAll(stopMapper.selectStopExamineRuleByDeviceId(device));
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
}
