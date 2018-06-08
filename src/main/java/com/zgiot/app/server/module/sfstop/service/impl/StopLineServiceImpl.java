package com.zgiot.app.server.module.sfstop.service.impl;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopLine;
import com.zgiot.app.server.module.sfstop.enums.ResultCode;
import com.zgiot.app.server.module.sfstop.mapper.StopLineMapper;
import com.zgiot.app.server.module.sfstop.service.StopLineService;
import com.zgiot.common.pojo.CurrentUser;
import com.zgiot.common.pojo.SessionContext;
import com.zgiot.common.restcontroller.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import com.zgiot.app.server.module.sfstop.mapper.StopLineMapper;
import com.zgiot.app.server.module.sfstop.service.StopLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopLineServiceImpl implements StopLineService {

    private Logger logger = LoggerFactory.getLogger(StopLineServiceImpl.class);

    @Autowired
    private StopLineMapper stopLineMapper;

    @Override
    public List<StopLine> getStopLine(StopLine stopLine) {
        List<StopLine> stopLines = stopLineMapper.getStopLineByAreaId(stopLine.getAreaId());
        return stopLines;
    }

    @Override
    public String validateStopLineName(StopLine stopLine) {
        List<StopLine> stopLines=stopLineMapper.validateStopLineName(stopLine);
        if(stopLines!=null && stopLines.size()>0){
            return ServerResponse.buildJson(ResultCode.NAMEAGAIN.getInfo(),ResultCode.NAMEAGAIN.getCode(),null);
        }
        return null;
    }

    @Override
    public void addStopLine(StopLine stopLine) {
        CurrentUser currentUser = SessionContext.getCurrentUser();
        if(currentUser!=null){
            stopLine.setCreateUser(currentUser.getUserId());
            stopLine.setUpdateUser(currentUser.getUserId());
        }
        stopLine.setCreateTime(new Date());
        stopLine.setUpdateTime(new Date());
        stopLineMapper.addStopLine(stopLine);
        logger.info("添加停车线数据："+stopLine.getId()+"-"+stopLine.getLineName()+"-"+stopLine.getDelayTime());
    }

    @Override
    public void updateStopLine(StopLine stopLine) {
        CurrentUser currentUser = SessionContext.getCurrentUser();
        if(currentUser!=null){
            stopLine.setUpdateUser(currentUser.getUserId());
        }
        stopLine.setUpdateTime(new Date());
        stopLineMapper.updateStopLine(stopLine);
        logger.info("修改停车线数据："+stopLine.getId()+stopLine.getLineName()+stopLine.getDelayTime());
    }

    @Override
    public void deleteStopLine(StopLine stopLine) {
        stopLineMapper.deleteStopLine(stopLine.getId());
        logger.info("物理删除停车线数据：" + stopLine.getId());
    }

    @Override
    public List<StopLine> getStopLineByAreaId(Long areaId) {
        return stopLineMapper.getStopLineByAreaId(areaId);
    }


}
