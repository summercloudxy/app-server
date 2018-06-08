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


    void updateStopManualInterventionByTC(StopManualIntervention stopManualIntervention);
}
                                                  