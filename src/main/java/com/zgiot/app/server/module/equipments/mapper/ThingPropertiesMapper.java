package com.zgiot.app.server.module.equipments.mapper;


import com.zgiot.app.server.module.equipments.pojo.ThingProperties;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ThingPropertiesMapper {

    @Insert("INSERT INTO tb_thing_properties(thing_code,prop_key,prop_value,prop_type) " +
            "VALUES(#{thing_code},#{prop_key},#{prop_value},#{prop_type})")
    void addThingProperties(ThingProperties thingProperties);

    @Delete("DELETE FROM tb_thing_properties where thing_code = #{thing_code}")
    void deleteThingPropertiesByThingCode(@Param("thing_code") String thing_code);
}
