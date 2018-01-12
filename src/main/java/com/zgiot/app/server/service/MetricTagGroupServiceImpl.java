package com.zgiot.app.server.service;

import com.zgiot.app.server.module.metrictag.dao.MetricTagGroupMapper;
import com.zgiot.app.server.module.metrictag.pojo.MetricTagGroup;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by chenting on 2018/1/12.
 */
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
        metricTagGroup.setCode(null);
        metricTagGroupMapper.updateMetricTagGroup(metricTagGroup);
    }

    @Override
    public void deleteMetricTagGroup(MetricTagGroup metricTagGroup) {
        metricTagGroupMapper.deleteMetricTagGroup(metricTagGroup);
    }
}
