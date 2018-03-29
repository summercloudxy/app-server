package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.controller.EquipmentInfo;
import com.zgiot.app.server.module.equipments.pojo.Thing;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ThingMapper {

    @Insert("insert into tb_thing(parent_thing_id,thing_code,thing_name,thing_type1_code,thing_shortname) " +
            "values(#{parentThingId},#{thingCode},#{thingName},#{thingType1Code},#{thingShortName})")
    void addThing(Thing thing);

    @Select("SELECT t.id,t.thing_code,t.thing_name,t.thing_type1_code,t.thing_shortname FROM tb_thing t " +
            "where t.id = #{id}")
    Thing getThingById(@Param("id") Long id);

    @Select("SELECT t.id,t.thing_code,t.thing_name,t.thing_type1_code,t.thing_shortname FROM tb_thing t " +
            "ORDER BY t.thing_code")
    List<Thing> getThingAll();

    @Delete("DELETE FROM tb_thing where id = #{id}")
    void deleteThingById(@Param("id") Long id);

    @Update("UPDATE tb_thing SET thing_code = #{thing_code},thing_name=#{thing_name}," +
            "thing_type1_code=#{thing_type1_code},thing_shortname=#{thing_shortname} where id = #{id}")
    void editThing(Thing thing);

    List<EquipmentInfo> getEquipmentInfoByThingcode(List<String> thingCodeList);

}
