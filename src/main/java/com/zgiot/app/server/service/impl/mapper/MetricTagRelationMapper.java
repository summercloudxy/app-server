package com.zgiot.app.server.service.impl.mapper;

import com.zgiot.app.server.module.metrictag.pojo.MetricTagRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Mapper
public interface MetricTagRelationMapper {

    List<MetricTagRelation> findMetricTagRelation(MetricTagRelation metricTagRelation);

    void addMetricTagRelation(MetricTagRelation metricTagRelation);

    void updateMetricTagRelation(MetricTagRelation metricTagRelation);

    void deleteMetricTagRelation(MetricTagRelation metricTagRelation);

    @Select("select count(b.*) from tb_metric_tag a,rel_metrictag_metric b where a.id=#{wrapperId} and b.code=a.metric_code")
    Integer getMetricCount(@Param("wrapperId") int wrapperId);
}
