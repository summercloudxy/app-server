package com.zgiot.app.server.module.sfstop.service.impl;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopPresetPararmeter;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopPresetPararmeterDTO;
import com.zgiot.app.server.module.sfstop.mapper.StopPresetPararmeterMapper;
import com.zgiot.app.server.module.sfstop.service.StopPresetPararmeterService;
import com.zgiot.common.pojo.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StopPresetPararmeterServiceImpl implements StopPresetPararmeterService {

    @Autowired
    private StopPresetPararmeterMapper stopPresetPararmeterMapper;

    @Override
    public List<StopPresetPararmeter> getStopPresetPararmeterByTC(StopPresetPararmeter stopPresetPararmeter) {
        List<StopPresetPararmeter> stopPresetPararmeterList=stopPresetPararmeterMapper.getStopPresetPararmeterByTC(stopPresetPararmeter);
        return stopPresetPararmeterList;
    }

    @Override
    public void updateStopPresetPararmeterByTC(StopPresetPararmeterDTO stopPresetPararmeterDTO) {
        stopPresetPararmeterMapper.delStopPresetPararmeterByTC(stopPresetPararmeterDTO.getThingCode(),stopPresetPararmeterDTO.getStopType());
        if(stopPresetPararmeterDTO.getStopPresetPararmeterList()!=null){
            String userId=null;
            if(SessionContext.getCurrentUser()!=null){
                userId=SessionContext.getCurrentUser().getUserId();
            }
            Date time=new Date();
            for (StopPresetPararmeter stopPresetPararmeter:stopPresetPararmeterDTO.getStopPresetPararmeterList()) {
                stopPresetPararmeter.setCreateUser(userId);
                stopPresetPararmeter.setUpdateUser(userId);
                stopPresetPararmeter.setCreateTime(time);
                stopPresetPararmeter.setUpdateTime(time);
                stopPresetPararmeterMapper.insertStopPresetPararmeter(stopPresetPararmeter);
            }
        }
    }

}
