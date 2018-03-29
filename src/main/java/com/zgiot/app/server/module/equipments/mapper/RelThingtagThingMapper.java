package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.pojo.RelThingtagThing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RelThingtagThingMapper {

    @Select("SELECT * FROM rel_thingtag_thing WHERE thing_tag_code in " +
            "(SELECT code FROM tb_thing_tag WHERE parent_id = #{id})")
    List<RelThingtagThing> getRelThingtagThingByThreeLevelId(@Param("id") Long id);

}
