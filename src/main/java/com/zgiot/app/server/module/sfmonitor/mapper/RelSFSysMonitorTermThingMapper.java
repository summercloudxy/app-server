package com.zgiot.app.server.module.sfmonitor.mapper;

import com.zgiot.app.server.module.sfmonitor.pojo.SFSysMonitorThing;
import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RelSFSysMonitorTermThingMapper {
    @Select("select count(DISTINCT term_id) from rel_sfsysmon_term_thing where thing_tag_code = #{thingTagCode}")
    short getTermCountByThingTagCode(@Param("thingTagCode") String thingTagCode);

    List<SFSysMonitorThing> getSFSysMonitorThing(SFSysMonitorThing sFSysMonitorThing);
}
