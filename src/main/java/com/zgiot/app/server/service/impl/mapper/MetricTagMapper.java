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
    @Select("select * from tb_metric_tag order by create_date desc")
    List<MetricTag> findMetricTag(MetricTag metricTag);

    @Select("select * from tb_metric_tag where tag_name=#{name}")
    MetricTag getMetricTagByName(@Param("name") String name);

    @Select("select * from tb_metric_tag where tag_name like #{name}")
    List<MetricTag> getMetricTag(@Param("name") String name);

    @Select("select * from tb_metric_tag")
    List<MetricTag> getAllMetricTag();

    @Delete("delete from tb_metric_tag where id=#{id}")
    void delMetricTag(@Param("id") int id);

    @Delete("delete from rel_sfmon_tag_displayzone where metric_tag_code=#{metricTagCode}")
    void delRelMetricTagDisplayzone(@Param("metricTagCode") String metricTagCode);

    void addMetricTag(MetricTag metricTag);

    void updateMetricTag(MetricTag metricTag);

    void deleteMetricTag(MetricTag metricTag);

    @Select("select b.name from rel_sfmon_tag_displayzone a,tb_sfmon_displayzone b where a.metric_tag_code=#{metricTagCode} and a.zone_id=b.id")
    String getMetricTagZone(@Param("metricTagCode") String metricTagCode);

    @Select("select count(1) from tb_metric_tag")
    int getMetricTagCount();

    @Select("select * from tb_metric_tag where id=#{id}")
    MetricTag getMetricTagById(@Param("id") int id);

    @Select("select a.tag_name from tb_metric_tag a,tb_metric_tag_group b where b.tag_group_name=#{groupName} and a.metric_tag_group_id=b.id")
    List<String> getAllSignalWrapper(@Param("groupName") String groupName);
}
