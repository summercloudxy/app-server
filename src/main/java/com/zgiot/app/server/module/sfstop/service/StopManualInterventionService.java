package com.zgiot.app.server.module.sfstop.service;


import com.zgiot.app.server.module.sfstop.entity.pojo.StopManualIntervention;

/**
 * 人工干预
 */
public interface StopManualInterventionService {


    /**
     * 根据thingcode查询人工干预记录
     *
     * @param thingCode
     * @return
     */
    StopManualIntervention getStopManualInterventionByThingCode(String thingCode);

    /**
     * 更新人工干预的状态
     *
     * @param state
     * @param thingCode
     */
    void updateStopManualInterventionState(Integer state, String thingCode);

    void updateStopManualInterventionByTC(StopManualIntervention stopManualIntervention);
}
                                                  