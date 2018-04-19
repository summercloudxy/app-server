package com.zgiot.app.server.module.sfmonitor.mapper;

import com.zgiot.app.server.module.sfmonitor.pojo.RelSFMonMetrictypeMetric;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RelSFMonMetrictypeMetricMapper {

    List<RelSFMonMetrictypeMetric> getRelSFMonMetrictypeMetricByThingCode(@Param("thingCodeList") List<String> thingCodeList);

}
