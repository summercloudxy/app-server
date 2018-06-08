package com.zgiot.app.server.module.sfstop.mapper;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopMetric;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StopMetricMapper {

    @Select("select * from tb_metric where metric_code=#{metricCode}")
    StopMetric getMetricByCode(@Param("metricCode") String metricCode);

    @Select("select * from tb_metric")
    List<StopMetric> getMetricList();

    @Select("select t.id AS id,t.metric_code AS metricCode,t.metric_name AS metricName from rel_thing_metric_label r LEFT JOIN tb_metric t on r.metric_code=t.metric_code  WHERE r.thing_code=#{thingCode}")
    List<StopMetric> getMetricByThingCode(@Param("thingCode") String thingCode);
}
