package com.zgiot.app.server.module.equipments.mapper;


import com.zgiot.app.server.module.equipments.pojo.ThingProperties;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ThingPropertiesMapper {

    @Insert("INSERT INTO tb_thing_properties(thing_code,prop_key,prop_value,prop_type) " +
            "VALUES(#{thingCode},#{propKey},#{propValue},#{propType})")
    void addThingProperties(ThingProperties thingProperties);

    @Delete("DELETE FROM tb_thing_properties where thing_code = #{thingCode}")
    void deleteThingPropertiesByThingCode(@Param("thing_code") String thing_code);

    List<ThingProperties> getThingPropertiesByThingCode(List<String> thingCodeList);

}
