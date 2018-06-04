package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopManualInterventionRecord;

public interface StopManualInterventionRecordService {


    /**
     * 查询启车的人工干预解除记录
     *
     * @param operateId
     * @param thingCode
     * @return
     */
    StopManualInterventionRecord getStopManualInterventionRecord(int operateId, String thingCode);
}
                                                  