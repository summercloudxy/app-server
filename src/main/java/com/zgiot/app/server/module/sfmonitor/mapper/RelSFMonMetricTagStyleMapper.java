package com.zgiot.app.server.module.sfmonitor.mapper;

import com.zgiot.app.server.module.sfmonitor.controller.FindSignalWrapperRes;
import com.zgiot.app.server.module.sfmonitor.controller.SignalWrapperStyleRes;
import com.zgiot.app.server.module.sfmonitor.pojo.RelSFMonMetricTagStyle;
import org.apache.ibatis.annotations.*;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.List;

@Mapper
public interface RelSFMonMetricTagStyleMapper {

    @Select("select * from rel_sfmon_metrictag_style where metric_tag_code=#{metricTagCode} and style_code=#{styleCode}")
    public RelSFMonMetricTagStyle getMetricTagStyle(RelSFMonMetricTagStyle relSFMonMetricTagStyle);

    @Insert("insert into rel_sfmon_metrictag_style(metric_tag_code,style_code,create_date,comment,editor) " +
            "values(#{metricTagCode},#{styleCode},#{createDate},#{comment},#{editor})")
    public void addStyle(RelSFMonMetricTagStyle relSFMonMetricTagStyle);

    @Update("update rel_sfmon_metrictag_style set metric_tag_code=#{metricTagCode},style_code=#{styleCode},create_date=#{createDate}," +
            "comment=#{comment},editor=#{editor} where id=#{id}")
    public void updateStyle(RelSFMonMetricTagStyle relSFMonMetricTagStyle);

    @Delete("delete from rel_sfmon_metrictag_style where id=#{id}")
    public void deleteStyle(@Param("id") int id);

    public List<FindSignalWrapperRes> getSiganlWrapperStyleByWrapperName(@Param("tagName") String tagName);

    public List<FindSignalWrapperRes> getSiganlWrapperStyleByStyleName(@Param("metricName") String metricName);

    public List<FindSignalWrapperRes> fuzzyGetSiganlWrapperStyleByWrapperName(@Param("tagName") String tagName);

    public List<FindSignalWrapperRes> fuzzyGetSiganlWrapperStyleByStyleName(@Param("metricName") String metricName);

    public SignalWrapperStyleRes getSignalWrapperStyleById(@Param("id") int id);

    public List<FindSignalWrapperRes> getAllSignalWrapperStyle();

    @Select("select count(1) from rel_sfmon_metrictag_style")
    public int getSignalWrapperStyleCount();

    @Select("select b.metric_name from tb_sfmonitor_signal a,tb_metric b " +
            "where a.thing_code=#{thingCode} and b.metric_name like #{metricName} and a.metric_code=b.metric_code")
    List<String> getMetricNamesByThingCode(@Param("thingCode") String thingCode,@Param("metricName") String metricName);

    @Select("select b.metric_name from tb_sfmonitor_signal a,tb_metric b " +
            "where a.thing_code=#{thingCode} and  a.metric_code=b.metric_code")
    List<String> getAllMetricNamesByThingCode(@Param("thingCode") String thingCode);
}
