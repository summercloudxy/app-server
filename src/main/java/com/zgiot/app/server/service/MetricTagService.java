package com.zgiot.app.server.service;

import com.zgiot.app.server.module.metrictag.pojo.MetricTag;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
public interface MetricTagService {
    
    /**
     * 获取MetricTag信息
     * @param metricTag
     * @return
     */
    List<MetricTag> getMetricTag(MetricTag metricTag);

    /**
     * 新增MetricTag信息
     * @param metricTag
     */
    void addMetricTag(MetricTag metricTag);

    /**
     * 修改MetricTag信息
     * @param metricTag
     */
    void updateMetricTag(MetricTag metricTag);

    /**
     * 删除MetricTag信息
     * @param metricTag
     */
    void deleteMetricTag(MetricTag metricTag);
}
