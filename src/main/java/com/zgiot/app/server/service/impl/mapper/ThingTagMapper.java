package com.zgiot.app.server.service.impl.mapper;

import com.zgiot.app.server.module.sfmonitor.controller.EquipmentBaseInfo;
import com.zgiot.app.server.module.thingtag.pojo.ThingTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Mapper
public interface ThingTagMapper {

    List<ThingTag> findThingTag(ThingTag thingTag);

    void addThingTag(ThingTag thingTag);

    void updateThingTag(ThingTag thingTag);

    void deleteThingTag(ThingTag thingTag);

    List<EquipmentBaseInfo> getEquipmentByCode(@Param("thingCode") String thingCode,@Param("thingTagCode") String thingTagCode);

    @Select("select b.metric_name from rel_thing_metric_label a,tb_metric b " +
            "where a.thing_code=#{thingCode} and b.metric_name like CONCAT(#{metricName},'%') and a.metric_code=b.metric_code")
    List<String> getMetricNamesByThingCode(@Param("thingCode") String thingCode,@Param("metricName") String metricName);

    @Select("select b.metric_name from rel_thing_metric_label a,tb_metric b " +
            "where a.thing_code=#{thingCode} and  a.metric_code=b.metric_code")
    List<String> getAllMetricNamesByThingCode(@Param("thingCode") String thingCode);
}
