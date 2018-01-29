package com.zgiot.app.server.module.metrictag.dao;

import com.zgiot.app.server.module.metrictag.pojo.MetricTag;
import com.zgiot.app.server.module.metrictag.pojo.MetricTagGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Mapper
public interface MetricTagGroupMapper {

    List<MetricTagGroup> findMetricTagGroup(MetricTagGroup metricTagGroup);

    void addMetricTagGroup(MetricTagGroup metricTagGroup);

    void updateMetricTagGroup(MetricTagGroup metricTagGroup);

    void deleteMetricTagGroup(MetricTagGroup metricTagGroup);

}
