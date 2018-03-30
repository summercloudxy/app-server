package com.zgiot.app.server.service.impl.mapper;

import com.zgiot.app.server.module.metrictag.pojo.MetricTagRelation;
import com.zgiot.app.server.module.sfmonitor.controller.FindSignalWrapperRes;
import com.zgiot.app.server.module.sfmonitor.controller.SignalWrapperRelateMetric;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Mapper
public interface MetricTagRelationMapper {

    List<MetricTagRelation> findMetricTagRelation(MetricTagRelation metricTagRelation);

    void addMetricTagRelation(MetricTagRelation metricTagRelation);

    void updateMetricTagRelation(MetricTagRelation metricTagRelation);

    void deleteMetricTagRelation(MetricTagRelation metricTagRelation);

    @Select("select count(1) from tb_metric_tag a,rel_metrictag_metric b where a.id=#{wrapperId} and b.metric_tag_code=a.code")
    Integer getMetricCount(@Param("wrapperId") int wrapperId);

    List<FindSignalWrapperRes> findSignalWrapperByWrapperName(@Param("tagName") String tagName);

    List<FindSignalWrapperRes> fuzzyFindSignalWrapperByWrapperName(@Param("tagName") String tagName);

    List<FindSignalWrapperRes> findSignalWrapperByMetricName(@Param("metricName") String metricName);

    List<FindSignalWrapperRes> fuzzyFindSignalWrapperByMetricName(@Param("metricName") String metricName);

    List<FindSignalWrapperRes> findAllSignalWrapper();

    SignalWrapperRelateMetric findTagById(@Param("id") int id);

    @Select("select a.metric_name from tb_metric a, rel_metrictag_metric b where b.id=#{id} and b.metric_code=a.metric_code")
    String getMetricNameByTagId(@Param("id") int id);

    @Delete("delete from rel_metrictag_metric where id=#{id}")
    void deleteSignalWrapperConfig(@Param("id") int id);

    @Select("select count(1)\n" +
            " from tb_metric_tag a,tb_metric b,rel_metrictag_metric c where a.code=c.metric_tag_code\n" +
            " and c.metric_code=b.metric_code order by c.create_date desc")
    int getSignalWrapperItemCount();

    @Select("select tag_name from tb_metric_tag")
    List<String> getAllTagName();

    @Select("select a.metric_code as metric_code from rel_metrictag_metric a,tb_metric_tag b " +
            "where b.tag_name=#{tagName} and b.code=a.metric_tag_code")
    List<String> getAllMetric(@Param("tagName") String tagName);
}
