package com.zgiot.app.server.service;

import com.zgiot.app.server.module.metrictag.pojo.MetricTagRelation;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by wangfan on 2018/1/8.
 */
@Service
public interface MetricTagRelationService {

    /**
     * 获取MetricTagRelation信息
     * @param metricTagRelationId
     * @return
     */
    MetricTagRelation getMetricTagRelation(Integer metricTagRelationId);

    /**
     * 获取MetricTagRelation信息
     * @param metricTagRelation
     * @return
     */
    List<MetricTagRelation> findMetricTagRelation(MetricTagRelation metricTagRelation);

    /**
     * 新增MetricTagRelation信息
     * @param metricTagRelation
     */
    void addMetricTagRelation(MetricTagRelation metricTagRelation);

    /**
     * 修改MetricTagRelation信息
     * @param metricTagRelation
     */
    void updateMetricTagRelation(MetricTagRelation metricTagRelation);

    /**
     * 删除MetricTagRelation信息
     * @param metricTagRelation
     */
    void deleteMetricTagRelation(MetricTagRelation metricTagRelation);
}
