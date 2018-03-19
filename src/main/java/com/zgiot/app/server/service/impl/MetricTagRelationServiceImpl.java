package com.zgiot.app.server.service.impl;


import com.zgiot.app.server.service.impl.mapper.MetricTagRelationMapper;
import com.zgiot.app.server.module.metrictag.pojo.MetricTagRelation;
import com.zgiot.app.server.service.MetricTagRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * Created by wangfan on 2018/1/9.
 */
@Service
public class MetricTagRelationServiceImpl implements MetricTagRelationService {

    @Autowired
    MetricTagRelationMapper metricTagRelationMapper;

    @Override
    public MetricTagRelation getMetricTagRelation(Integer metricTagRelationId) {
        MetricTagRelation metricTagRelation = new MetricTagRelation();
        metricTagRelation.setId(metricTagRelationId);
        List<MetricTagRelation> thingTagRelations = metricTagRelationMapper.findMetricTagRelation(metricTagRelation);
        if(thingTagRelations.isEmpty()){
            return null;
        }
        return thingTagRelations.get(0);
    }

    @Override
    public List<MetricTagRelation> findMetricTagRelation(MetricTagRelation metricTagRelation) {
        return metricTagRelationMapper.findMetricTagRelation(metricTagRelation);
    }

    @Override
    public void addMetricTagRelation(MetricTagRelation metricTagRelation) {
        metricTagRelation.setCreateDate(new Date());
        metricTagRelationMapper.addMetricTagRelation(metricTagRelation);
    }

    @Override
    public void updateMetricTagRelation(MetricTagRelation metricTagRelation) {
        metricTagRelationMapper.updateMetricTagRelation(metricTagRelation);
    }

    @Override
    public void deleteMetricTagRelation(MetricTagRelation metricTagRelation) {
        metricTagRelationMapper.deleteMetricTagRelation(metricTagRelation);
    }
}
