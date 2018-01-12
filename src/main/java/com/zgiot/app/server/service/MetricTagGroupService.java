package com.zgiot.app.server.service;

import com.zgiot.app.server.module.metrictag.pojo.MetricTagGroup;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Service
public interface MetricTagGroupService {

    /**
     * 获取MetricTag信息
     * @param metricTagGroup
     * @return
     */
    List<MetricTagGroup> getMetricTagGroup(MetricTagGroup metricTagGroup);

    /**
     * 新增MetricTagGroup信息
     * @param metricTagGroup
     */
    void addMetricTagGroup(MetricTagGroup metricTagGroup);

    /**
     * 修改MetricTagGroup信息
     * @param metricTagGroup
     */
    void updateMetricTagGroup(MetricTagGroup metricTagGroup);

    /**
     * 删除MetricTagGroup信息
     * @param metricTagGroup
     */
    void deleteMetricTagGroup(MetricTagGroup metricTagGroup);
}
