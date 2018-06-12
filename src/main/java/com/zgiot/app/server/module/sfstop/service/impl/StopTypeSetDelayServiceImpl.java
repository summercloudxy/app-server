package com.zgiot.app.server.module.sfstop.service.impl;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetDelay;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetDelayDTO;
import com.zgiot.app.server.module.sfstop.entity.vo.StopThing;
import com.zgiot.app.server.module.sfstop.mapper.StopTypeSetDelayMapper;
import com.zgiot.app.server.module.sfstop.service.StopTypeSetDelayService;
import com.zgiot.common.pojo.CurrentUser;
import com.zgiot.common.pojo.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StopTypeSetDelayServiceImpl implements StopTypeSetDelayService {

    @Autowired
    private StopTypeSetDelayMapper stopTypeSetDelayMapper;

    @Override
    public List<StopTypeSetDelay> getStopTypeSetDelayByTCNoChecked(StopTypeSetDelay stopTypeSetDelay) {
        List<StopTypeSetDelay> stopTypeSetDelayList=stopTypeSetDelayMapper.getStopTypeSetDelayByTCNoChecked(stopTypeSetDelay);
        return stopTypeSetDelayList;
    }

    @Override
    public List<StopTypeSetDelay> getStopTypeSetDelayByTCNIsChecked(StopTypeSetDelay stopTypeSetDelay) {
        List<StopTypeSetDelay> stopTypeSetDelayList=stopTypeSetDelayMapper.getStopTypeSetDelayByTCNIsChecked(stopTypeSetDelay);
        return stopTypeSetDelayList;
    }

    @Override
    public void updateDelayByCodeNoChecked(StopTypeSetDelayDTO stopTypeSetDelayDTO) {
        stopTypeSetDelayMapper.deleteDelayByTCAndNoChecked(stopTypeSetDelayDTO.getThingCode(),stopTypeSetDelayDTO.getStopType());

        if(stopTypeSetDelayDTO.getStopTypeSetDelayList()!=null){
            CurrentUser currentUser = SessionContext.getCurrentUser();
            String userId=null;
            if(currentUser!=null){
                userId=currentUser.getUserId();
            }
            Date time=new Date();

            for (StopTypeSetDelay stopTypeSetDelay:stopTypeSetDelayDTO.getStopTypeSetDelayList()) {
                stopTypeSetDelay.setCreateUser(userId);
                stopTypeSetDelay.setUpdateUser(userId);
                stopTypeSetDelay.setCreateTime(time);
                stopTypeSetDelay.setUpdateTime(time);
                stopTypeSetDelay.setIsChecked(null);
                stopTypeSetDelayMapper.insertStopTypeSetDelay(stopTypeSetDelay);
            }
        }
    }

    @Override
    public void updateDelayByCodeChecked(StopTypeSetDelayDTO stopTypeSetDelayDTO) {
        stopTypeSetDelayMapper.deleteDelayByTCAndChecked(stopTypeSetDelayDTO.getThingCode(),stopTypeSetDelayDTO.getStopType());
        if(stopTypeSetDelayDTO.getStopTypeSetDelayList()!=null){
            CurrentUser currentUser = SessionContext.getCurrentUser();
            String userId=null;
            if(currentUser!=null){
                userId=currentUser.getUserId();
            }
            Date time=new Date();

            for (StopTypeSetDelay stopTypeSetDelay:stopTypeSetDelayDTO.getStopTypeSetDelayList()) {
                stopTypeSetDelay.setCreateUser(userId);
                stopTypeSetDelay.setUpdateUser(userId);
                stopTypeSetDelay.setCreateTime(time);
                stopTypeSetDelay.setUpdateTime(time);
                stopTypeSetDelay.setIsChecked(1);
                stopTypeSetDelayMapper.insertStopTypeSetDelay(stopTypeSetDelay);
            }
        }
    }

    @Override
    public List<StopThing> getParentStopTypeSetDelay(String thingCode) {
        return stopTypeSetDelayMapper.getParentStopTypeSetDelay(thingCode);
    }

}
