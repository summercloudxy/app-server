package com.zgiot.app.server.mapper;

import com.zgiot.common.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TMLMapper {

    @Select("SELECT * FROM `tb_metric` ")
    List<MetricModel> findAllMetrics();

    @Select("SELECT * FROM `tb_thing` ")
    List<ThingModel> findAllThings();

    @Select("SELECT * FROM `tb_thing_properties`")
    public List<ThingPropertyModel> findAllProperties();

    @Select("SELECT * FROM rel_historydata_whitelist")
    public List<ThingMetricModel> findAllHistdataWhitelist();

}