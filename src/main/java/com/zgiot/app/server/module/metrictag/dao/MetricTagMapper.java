package com.zgiot.app.server.module.metrictag.dao;

import com.zgiot.app.server.module.metrictag.pojo.MetricTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Mapper
public interface MetricTagMapper {

    List<MetricTag> getMetricTag(MetricTag metricTag);

    void addMetricTag(MetricTag metricTag);

    void updateMetricTag(MetricTag metricTag);

    void deleteMetricTag(MetricTag metricTag);
}
