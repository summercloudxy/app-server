package com.zgiot.app.server.module.sfmonitor.mapper;

import com.zgiot.app.server.module.sfmonitor.pojo.RelSFSysMonitorThingMetric;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RelSFSysMonitorThingMetricMapper {

    @Select("SELECT id,thing_code,thing_name,metric_code,metric_name,is_show_metric,is_join_system FROM rel_sfsysmon_thing_metric WHERE thing_code = #{thingCode}")
    List<RelSFSysMonitorThingMetric> getThingMetricByThingCode(@Param("thingCode") String thingCode);
}
