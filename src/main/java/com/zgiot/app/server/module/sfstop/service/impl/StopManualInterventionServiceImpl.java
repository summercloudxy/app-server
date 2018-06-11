package com.zgiot.app.server.module.sfstop.service.impl;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopManualIntervention;
import com.zgiot.app.server.module.sfstop.mapper.StopManualInterventionMapper;
import com.zgiot.app.server.module.sfstop.service.StopManualInterventionService;
import com.zgiot.common.pojo.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StopManualInterventionServiceImpl implements StopManualInterventionService {
    @Autowired
    private StopManualInterventionMapper stopManualInterventionMapper;

    @Override
    public StopManualIntervention getStopManualInterventionByThingCode(String thingCode) {
        return stopManualInterventionMapper.getStopManualInterventionByThingCode(thingCode);
    }


    @Override
    public void updateStopManualInterventionByTC(StopManualIntervention stopManualIntervention) {
        if (SessionContext.getCurrentUser() != null) {
            stopManualIntervention.setUpdateUser(SessionContext.getCurrentUser().getUserId());
        }
        stopManualIntervention.setUpdateTime(new Date());
        stopManualInterventionMapper.updateStopManualInterventionByTC(stopManualIntervention);
    }
}
