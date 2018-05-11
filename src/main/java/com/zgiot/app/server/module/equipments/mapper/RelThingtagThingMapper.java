package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.pojo.RelThingtagThing;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RelThingtagThingMapper {

    @Insert("insert into rel_thingtag_thing(thing_code,thing_tag_code,create_date) " +
            "values(#{thingCode},#{thingTagCode},#{createDate})")
    void addRelThingtagThing(RelThingtagThing relThingtagThing);

    @Delete("delete from rel_thingtag_thing where thing_code = #{thingCode}")
    void deleteRelThingtagThingByThingCode(@Param("thingCode") String thingCode);

    @Select("SELECT * FROM rel_thingtag_thing WHERE thing_tag_code in (" +
            "SELECT code FROM tb_thing_tag WHERE parent_id in(" +
            "SELECT code FROM tb_thing_tag WHERE parent_id = #{id}))")
    List<RelThingtagThing> getRelThingtagThingByTwoLevelId(@Param("id") Long id);

    @Select("SELECT * FROM rel_thingtag_thing WHERE thing_tag_code in " +
            "(SELECT code FROM tb_thing_tag WHERE parent_id = #{id})")
    List<RelThingtagThing> getRelThingtagThingByThreeLevelId(@Param("id") Long id);

    @Select("SELECT * FROM rel_thingtag_thing WHERE thing_tag_code = #{id}")
    List<RelThingtagThing> getRelThingtagThingByThingTagCode(@Param("id") Long id);

}
