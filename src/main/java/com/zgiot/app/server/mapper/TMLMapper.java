package com.zgiot.app.server.mapper;

import com.zgiot.common.pojo.MetricModel;
import com.zgiot.common.pojo.ThingMetricModel;
import com.zgiot.common.pojo.ThingModel;
import com.zgiot.common.pojo.ThingPropertyModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface TMLMapper {

    @Select("SELECT * FROM `tb_metric` ")
    List<MetricModel> findAllMetrics();

    @Select("SELECT * FROM `tb_thing` ")
    List<ThingModel> findAllThings();

    @Select("SELECT * FROM `tb_thing_properties`")
    List<ThingPropertyModel> findAllProperties();

    @Select("SELECT * FROM rel_historydata_whitelist")
    public List<ThingMetricModel> findAllHistdataWhitelist();

    @Select("SELECT metric_code FROM `rel_thing_metric_label` WHERE thing_code = #{thingCode}")
    Set<String> findMetricsOfThing(String thingCode);


    @Select("SELECT * FROM `tb_building`")
    List<BuildingModel> findAllBuildings();

    @Select("SELECT * FROM `tb_system`")
    List<SystemModel> findAllSystems();
}