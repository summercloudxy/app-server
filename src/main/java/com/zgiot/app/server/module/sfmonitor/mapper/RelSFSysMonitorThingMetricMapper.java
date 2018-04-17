package com.zgiot.app.server.module.sfmonitor.mapper;

import com.zgiot.app.server.module.sfmonitor.pojo.RelSFSysMonitorThingMetric;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RelSFSysMonitorThingMetricMapper {

    @Select("SELECT * FROM rel_sfsysmon_thing_metric WHERE thing_code = #{thingCode}")
    List<RelSFSysMonitorThingMetric> getThingMetricByThingCode(@Param("thingCode") String thingCode);
}
