package com.zgiot.app.server.module.sfmonitor.mapper;

import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SFSysMonitorThingTagMapper {

    @Select("SELECT t.*,s.image_name FROM tb_thing_tag t left join tb_sfsysmon_thing_tag_style s on t.code=s.thing_tag_code " +
            "where parent_id = #{id} order by sort,id")
    List<ThingTag> getThingTagByParentId(@Param("id") Long id);

    @Select("SELECT * FROM tb_thing_tag WHERE id = #{id}")
    ThingTag getThingTagById(@Param("id") Long id);

    /**
     * 根据groupId获取ThingTag根节点
     */
    @Select("SELECT * FROM tb_thing_tag WHERE parent_id is null and thing_tag_group_id = #{thingTagGroupId}")
    List<ThingTag> findRootThingTag(@Param("thingTagGroupId") int thingTagGroupId);
}
