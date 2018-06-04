package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopOperationRecord;

import java.util.List;

public interface StopOperationRecordService {


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


}
