package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.module.metrictag.dao.MetricTagGroupMapper;
import com.zgiot.app.server.module.metrictag.pojo.MetricTagGroup;
import com.zgiot.app.server.service.MetricTagGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangfan on 2018/1/9.
 */
@Service
public class MetricTagGroupServiceImpl implements MetricTagGroupService {

    @Autowired
    MetricTagGroupMapper metricTagGroupMapper;

    @Override
    public MetricTagGroup getMetricTagGroup(Integer metricTagGroupId, String metricTagCode){
        MetricTagGroup metricTagGroup = new MetricTagGroup();
        metricTagGroup.setMetricTagGroupId(metricTagGroupId);
        metricTagGroup.setCode(metricTagCode);
        List<MetricTagGroup> metricTagGroups = metricTagGroupMapper.getMetricTagGroup(metricTagGroup);
        if(null == metricTagGroups){
            return null;
        }
        return metricTagGroups.get(0);
    }

    @Override
    public List<MetricTagGroup> findMetricTagGroup(MetricTagGroup metricTagGroup){
        return metricTagGroupMapper.getMetricTagGroup(metricTagGroup);
    }

    @Override
    public void addMetricTagGroup(MetricTagGroup metricTagGroup){
        metricTagGroupMapper.addMetricTagGroup(metricTagGroup);
    }

    @Override
    public void updateMetricTagGroup(MetricTagGroup metricTagGroup){
        metricTagGroup.setCode(null);
        metricTagGroupMapper.updateMetricTagGroup(metricTagGroup);
    }

    @Override
    public void deleteMetricTagGroup(MetricTagGroup metricTagGroup){
        metricTagGroupMapper.deleteMetricTagGroup(metricTagGroup);
    }
}
