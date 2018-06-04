package com.zgiot.app.server.module.sfstop.service.impl;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopManualInterventionRecord;
import com.zgiot.app.server.module.sfstop.mapper.StopManualInterventionRecordMapper;
import com.zgiot.app.server.module.sfstop.service.StopManualInterventionRecordService;
import org.springframework.stereotype.Service;


@Service
public class StopManualInterventionRecordServiceImpl implements StopManualInterventionRecordService {

    private StopManualInterventionRecordMapper stopManualInterventionRecordMapper;

    @Override
    public StopManualInterventionRecord getStopManualInterventionRecord(int operateId, String thingCode) {
        return stopManualInterventionRecordMapper.getStopManualInterventionRecord(operateId, thingCode);
    }
}
