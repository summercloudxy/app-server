package com.zgiot.app.server.module.sfstop.service.impl;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopMetric;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopSysParameter;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetPararmeter;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetPararmeterDTO;
import com.zgiot.app.server.module.sfstop.mapper.StopMetricMapper;
import com.zgiot.app.server.module.sfstop.mapper.StopSysParameterMapper;
import com.zgiot.app.server.module.sfstop.mapper.StopTypeSetPararmeterMapper;
import com.zgiot.app.server.module.sfstop.service.StopTypeSetPararmeterService;
import com.zgiot.app.server.module.util.StringUtils;
import com.zgiot.app.server.service.impl.mapper.MetricMapper;
import com.zgiot.common.pojo.CurrentUser;
import com.zgiot.common.pojo.MetricModel;
import com.zgiot.common.pojo.SessionContext;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StopNormalSetPararmeterServiceImpl implements StopTypeSetPararmeterService {

    @Autowired
    private StopTypeSetPararmeterMapper stopTypeSetPararmeterMapper;

    @Autowired
    private StopMetricMapper stopMetricMapper;

    @Autowired
    private StopSysParameterMapper stopSysParameterMapper;

    @Override
    public List<StopTypeSetPararmeter> getPararmeterNoChecked(StopTypeSetPararmeter stopTypeSetPararmeter) {
        List<StopTypeSetPararmeter> stopTypeSetPararmeterList=stopTypeSetPararmeterMapper.getPararmeterNoChecked(stopTypeSetPararmeter);
        if(stopTypeSetPararmeterList!=null && stopTypeSetPararmeterList.size()>0){
            for (StopTypeSetPararmeter setPararmeter:stopTypeSetPararmeterList) {
                setMetricName(setPararmeter);
                setComparisonOPeratorValue(setPararmeter);
            }
        }
        return stopTypeSetPararmeterList;
    }

    private void setMetricName(StopTypeSetPararmeter setPararmeter) {
        if(setPararmeter.getMetricCode()!=null){
            StopMetric stopMetric=stopMetricMapper.getMetricByCode(setPararmeter.getMetricCode());
            if(stopMetric!=null){
                setPararmeter.setMetricName(stopMetric.getMetricName());
            }
        }
    }

    private void setComparisonOPeratorValue(StopTypeSetPararmeter setPararmeter) {
        if(setPararmeter.getComparisonOperator()!=null){
            StopSysParameter stopSysParameter=stopSysParameterMapper.getStopSysParameterByKey(setPararmeter.getComparisonOperator());
            if(stopSysParameter!=null){
                setPararmeter.setComparisonOperatorValue(stopSysParameter.getParameterValue());
            }
        }
    }

    @Override
    public List<StopTypeSetPararmeter> getPararmeterChecked(StopTypeSetPararmeter stopTypeSetPararmeter) {
        List<StopTypeSetPararmeter> stopTypeSetPararmeterList=stopTypeSetPararmeterMapper.getPararmeterChecked(stopTypeSetPararmeter);
        if(stopTypeSetPararmeterList!=null && stopTypeSetPararmeterList.size()>0){
            for (StopTypeSetPararmeter setPararmeter:stopTypeSetPararmeterList) {
                setMetricName(setPararmeter);
                setComparisonOPeratorValue(setPararmeter);
            }
        }
        return stopTypeSetPararmeterList;
    }

    @Override
    public void updatePararmeterNoChecked(StopTypeSetPararmeterDTO stopTypeSetPararmeterDTO) {
        stopTypeSetPararmeterMapper.delPararmeterNoChecked(stopTypeSetPararmeterDTO.getThingCode(),stopTypeSetPararmeterDTO.getStopType());
        if(stopTypeSetPararmeterDTO.getStopTypeSetPararmeterList()!=null){
            CurrentUser currentUser = SessionContext.getCurrentUser();
            String userId=null;
            if(currentUser!=null){
                userId=currentUser.getUserId();
            }
            Date time=new Date();
            for (StopTypeSetPararmeter stopTypeSetPararmeter:stopTypeSetPararmeterDTO.getStopTypeSetPararmeterList()) {
                stopTypeSetPararmeter.setCreateUser(userId);
                stopTypeSetPararmeter.setUpdateUser(userId);
                stopTypeSetPararmeter.setCreateTime(time);
                stopTypeSetPararmeter.setUpdateTime(time);
                stopTypeSetPararmeter.setIsChecked(null);
                stopTypeSetPararmeterMapper.insertStopNormalSetPararmeter(stopTypeSetPararmeter);
            }
        }
    }

    @Override
    public void updatePararmeterChecked(StopTypeSetPararmeterDTO stopTypeSetPararmeterDTO) {
        stopTypeSetPararmeterMapper.delPararmeterChecked(stopTypeSetPararmeterDTO.getThingCode(),stopTypeSetPararmeterDTO.getStopType());
        if(stopTypeSetPararmeterDTO.getStopTypeSetPararmeterList()!=null){
            CurrentUser currentUser = SessionContext.getCurrentUser();
            String userId=null;
            if(currentUser!=null){
                userId=currentUser.getUserId();
            }
            Date time=new Date();
            for (StopTypeSetPararmeter stopTypeSetPararmeter:stopTypeSetPararmeterDTO.getStopTypeSetPararmeterList()) {
                stopTypeSetPararmeter.setCreateUser(userId);
                stopTypeSetPararmeter.setUpdateUser(userId);
                stopTypeSetPararmeter.setCreateTime(time);
                stopTypeSetPararmeter.setUpdateTime(time);
                stopTypeSetPararmeter.setIsChecked(1);
                stopTypeSetPararmeterMapper.insertStopNormalSetPararmeter(stopTypeSetPararmeter);
            }
        }
    }
}
