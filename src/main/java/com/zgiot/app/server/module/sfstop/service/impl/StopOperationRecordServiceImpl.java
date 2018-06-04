package com.zgiot.app.server.module.sfstop.service.impl;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopOperationRecord;
import com.zgiot.app.server.module.sfstop.mapper.StopOperationRecordMapper;
import com.zgiot.app.server.module.sfstop.service.StopOperationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StopOperationRecordServiceImpl implements StopOperationRecordService {

   @Autowired
    private StopOperationRecordMapper stopOperationRecordMapper;

    @Override
    public List<StopOperationRecord> findUnfinishStopOperate(Integer system, Integer startState, Integer finishState) {
        return stopOperationRecordMapper.selectOperateRecordWithoutOperateState(system, startState, finishState);
    }

    @Override
    public void saveStopOperationRecord(StopOperationRecord stopOperationRecord) {
        stopOperationRecordMapper.saveStopOperationRecord(stopOperationRecord);
    }

    @Override
    public StopOperationRecord getStopOperationRecordByOperateState(int operateState) {
        return stopOperationRecordMapper.getStopOperationRecordByOperateState(operateState);
    }
}
