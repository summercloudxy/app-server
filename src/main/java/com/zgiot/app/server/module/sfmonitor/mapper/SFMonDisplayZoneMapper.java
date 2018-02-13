package com.zgiot.app.server.module.sfmonitor.mapper;

import com.zgiot.app.server.module.sfmonitor.pojo.SFMonDisplayZone;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SFMonDisplayZoneMapper {

    @Select("select * from tb_sfmon_displayzone")
    List<SFMonDisplayZone> getDisplayZone();

    @Insert("insert into rel_sfmon_tag_displayzone(metric_tag_code,zone_id) values(#{metricTagCode}.#{zoneId})")
    void addRelSFMonTagDisplayZone(@Param("metricTagCode") String metricTagCode,@Param("zoneId") int zoneId);

    @Delete("delete from rel_sfmon_tag_displayzone where metric_tag_code=#{metricTagCode}")
    void deleteRelSFMonTagDisplayZone(@Param("metricTagCode") String metricTagCode);
}
