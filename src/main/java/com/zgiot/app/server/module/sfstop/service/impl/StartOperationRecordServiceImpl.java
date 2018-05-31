package com.zgiot.app.server.module.sfstop.service.impl;

import com.zgiot.app.server.module.sfstop.entity.pojo.StartOperationRecord;
import com.zgiot.app.server.module.sfstop.mapper.StartOperationRecordMapper;
import com.zgiot.app.server.module.sfstop.service.StartOperationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StartOperationRecordServiceImpl implements StartOperationRecordService {

    @Autowired
    private StartOperationRecordMapper startOperationRecordMapper;


    @Override
    public StartOperationRecord getStartOperationRecord(Integer operateState) {
        return startOperationRecordMapper.getStartOperationRecord(operateState);
    }
}
