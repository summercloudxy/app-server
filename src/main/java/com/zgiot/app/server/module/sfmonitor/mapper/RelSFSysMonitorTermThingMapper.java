package com.zgiot.app.server.module.sfmonitor.mapper;

import com.zgiot.app.server.module.sfmonitor.pojo.RelSFSysMonitorTermThing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface RelSFSysMonitorTermThingMapper {
    @Select("select count(DISTINCT term_id) from rel_sfsysmon_term_thing where thing_tag_code = #{thingTagCode}")
    short getTermCountByThingTagCode(@Param("thingTagCode") String thingTagCode);

    List<RelSFSysMonitorTermThing> getSFSysMonitorThing(RelSFSysMonitorTermThing sFSysMonitorTermThingRel);

    List<RelSFSysMonitorTermThing> getSFSpeMonitorThingProtect(RelSFSysMonitorTermThing monitorThing);

    List<String> getThingCodeList(RelSFSysMonitorTermThing monitorThing);
}
