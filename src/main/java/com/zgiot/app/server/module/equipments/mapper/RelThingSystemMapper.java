package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.pojo.RelThingSystem;
import org.apache.ibatis.annotations.*;

@Mapper
public interface RelThingSystemMapper {

    @Insert("INSERT INTO rel_thing_system(thing_code,system_id,update_time) " +
            "VALUES(#{thingCode},#{systemId},NOW())")
    void addRelThingSystem(RelThingSystem relThingSystem);

    @Delete("DELETE FROM rel_thing_system where thing_code = #{thingCode}")
    void deleteRelThingSystemByThingCode(@Param("thingCode") String thingCode);
}
