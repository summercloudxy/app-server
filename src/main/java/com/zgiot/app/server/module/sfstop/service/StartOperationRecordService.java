package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StartOperationRecord;

public interface StartOperationRecordService {

    /**
     * 查询最新的一条
     *
     * @param operateState
     * @return
     */
    StartOperationRecord getStartOperationRecord(Integer operateState);
}
                                                  