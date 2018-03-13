package com.zgiot.app.server.service.impl.mapper;

import com.zgiot.app.server.module.metrictag.pojo.MetricTag;
import com.zgiot.common.pojo.MetricModel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MetricMapper {
    @Select("select metric_name from tb_metric where metric_name like #{metricName}")
    List<String> findParameterMetric(@Param("metricName") String metricName);

    @Select("select metric_name from tb_metric")
    List<String> findAllParameterMetric();

    @Select("select * from tb_metric where metric_name=#{metricName}")
    MetricModel getMetric(@Param("metricName") String metricName);
}
