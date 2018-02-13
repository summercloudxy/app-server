package com.zgiot.app.server.service.impl.mapper;

import com.zgiot.app.server.module.metrictag.pojo.MetricTag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Mapper
public interface MetricTagMapper {

    List<MetricTag> findMetricTag(MetricTag metricTag);

    @Select("select * from tb_thing_tag where tag_name=#{name}")
    MetricTag getMetricTagByName(@Param("name") String name);

    @Select("select * from tb_thing_tag where tag_name like #{name}'%'")
    List<MetricTag> getMetricTag(@Param("name") String name);

    @Delete("delete from tb_thing_tag where metricTagId=#{id}")
    void deleteThingTag(@Param("id") int id);

    void addMetricTag(MetricTag metricTag);

    void updateMetricTag(MetricTag metricTag);

    void deleteMetricTag(MetricTag metricTag);
}
