package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.sfstop.entity.pojo.*;
import com.zgiot.app.server.module.sfstop.entity.vo.StopExamineResult;

import java.util.List;
import java.util.Set;

public interface StopService {


    /**
     * 查询启车的人工干预解除记录
     *
     * @param operateId
     * @param thingCode
     * @return
     */
    StopManualInterventionRecord getStopManualInterventionRecord(int operateId, String thingCode);

    /**
     * 查询某条启车线下的人工干预解除记录
     *
     * @param lineId
     * @return
     */
    List<StopManualInterventionRecord> getStopManualInterventionRecordByLineId(Integer lineId);

    /**
     * 查询未完成停车任务
     *
     * @param startState
     * @param finishState
     * @return
     */
    List<StopOperationRecord> findUnfinishStopOperate(Integer system, Integer startState, Integer finishState);

    /**
     * 保存新的停车记录
     *
     * @param stopOperationRecord
     */
    void saveStopOperationRecord(StopOperationRecord stopOperationRecord);

    /**
     * 根据操作状态查询停车记录
     *
     * @param operateState
     * @return
     */
    StopOperationRecord getStopOperationRecordByOperateState(int operateState);

    /**
     * 查询停车的操作记录ID
     *
     * @param system
     * @return
     */
    Integer getStopOperateId(int system);

    /**
     * 建立停车自检
     *
     * @param stopDeviceIds
     * @param operateId
     */
    void setUpAutoExamine(Set<String> stopDeviceIds, Integer operateId);

    /**
     * 根据设备查询检查规则
     *
     * @param deviceIds
     * @return
     */
    List<StopExamineRule> getStopExamineRuleByDeviceIds(Set<String> deviceIds);

    /**
     * 查询某个设备级别最高的故障告警信息
     *
     * @param thingCode
     * @return
     */
    AlertData getMaxLevelAlertData(String thingCode);

    /**
     * 查询最新的一条
     *
     * @param operateState
     * @return
     */
    StartOperationRecord getStartOperationRecord(Integer operateState);

    /**
     * 保存停车方案设置
     *
     * @param stopChoiceSet
     */
    void saveStopChoiceSet(StopChoiceSet stopChoiceSet);

    /**
     * 关闭启车操作
     */
    void closeStopOperate(String system);

    /**
     * 判断当前停车状态
     *
     * @param startState  查询状态开始
     * @param finishState 查询状态结尾
     * @return
     */
    boolean judgeStopingState(String system, Integer startState, Integer finishState);

    /**
     * 修改启车中人工干预记录
     *
     * @param thingCode       人工干预设备
     * @param operateId       启车操作id
     * @param state           修改状态
     * @param relievePersonId 干预解除人
     */
    void updateStopManualInterventionRecord(String thingCode, Integer operateId, Integer state, String relievePersonId);

    /**
     * 修改停车状态
     *
     * @param stopFinishState
     */
    void updateStopOperate(String system, int stopFinishState);

    /**
     * 根据规则查询本次停车检查记录
     *
     * @param operateId
     * @return
     */
    List<StopExamineResult> getStartExaminRecordByOperateId(Integer operateId);
}
