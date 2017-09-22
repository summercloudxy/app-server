package com.zgiot.app.server.mapper;

import com.zgiot.common.pojo.MetricModel;
import com.zgiot.common.pojo.ThingModel;
import com.zgiot.common.pojo.ThingPropertyModel;
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

    @Select("SELECT * FROM `tb_thing` where thing_code = #{thingCode}")
    public ThingModel getThingProperties(@Param("thingCode") String thingCode);

    @Select("SELECT * FROM `tb_thing_properties` where thing_code = #{thingCode} and prop_type=#{propType}")
    public List<ThingPropertyModel> getProperties(@Param("thingCode") String thingCode, @Param("propType") String propType);

    @Select("SELECT * FROM `tb_thing_properties`")
    public List<ThingPropertyModel> findAllProperties();

}