package com.zgiot.app.server.module.sfmonitor.mapper;

import com.zgiot.app.server.module.sfmonitor.pojo.RelSFMonMetrictypeMetric;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RelSFMonMetrictypeMetricMapper {

    @Select("select * from rel_sfmon_metrictype_metric")
    List<RelSFMonMetrictypeMetric> getAllRelSFMonMetrictypeMetric();

}
