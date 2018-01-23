package com.zgiot.app.server.service.impl;


import com.zgiot.app.server.module.metrictag.dao.MetricTagRelationMapper;
import com.zgiot.app.server.module.metrictag.pojo.MetricTagRelation;
import com.zgiot.app.server.module.thingtag.pojo.ThingTagRelation;
import com.zgiot.app.server.service.MetricTagRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        metricTagRelation.setMetricTagRelationId(metricTagRelationId);
        List<MetricTagRelation> thingTagRelations = metricTagRelationMapper.getMetricTagRelation(metricTagRelation);
        if(null == thingTagRelations){
            return null;
        }
        return thingTagRelations.get(0);
    }

    @Override
    public List<MetricTagRelation> findMetricTagRelation(MetricTagRelation metricTagRelation) {
        List<MetricTagRelation> metricTagRelations = metricTagRelationMapper.getMetricTagRelation(metricTagRelation);
        return metricTagRelations;
    }

    @Override
    public void addMetricTagRelation(MetricTagRelation metricTagRelation) {
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
