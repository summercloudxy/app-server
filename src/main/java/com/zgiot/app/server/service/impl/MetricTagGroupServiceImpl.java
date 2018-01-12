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
    public List<MetricTagGroup> getMetricTagGroup(MetricTagGroup metricTagGroup) {
        List<MetricTagGroup> metricTagGroups = metricTagGroupMapper.getMetricTagGroup(metricTagGroup);
        return metricTagGroups;
    }

    @Override
    public void addMetricTagGroup(MetricTagGroup metricTagGroup) {
        metricTagGroupMapper.addMetricTagGroup(metricTagGroup);
    }

    @Override
    public void updateMetricTagGroup(MetricTagGroup metricTagGroup) {
        metricTagGroupMapper.updateMetricTagGroup(metricTagGroup);
    }

    @Override
    public void deleteMetricTagGroup(MetricTagGroup metricTagGroup) {
        metricTagGroupMapper.deleteMetricTagGroup(metricTagGroup);
    }
}
