package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.pojo.ThingPosition;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ThingPositionMapper {

    @Insert("INSERT INTO tb_thing_position(thing_code,building_id,floor,location_area,location_x,location_y) " +
            "values(#{thingCode},#{buildingId},#{floor},#{locationArea},#{locationX},#{locationY})")
    void addThingPosition(ThingPosition thingPosition);

    @Delete("delete from tb_thing_position where thing_code = #{thingCode}")
    void deleteThingPosition(@Param("thingCode") String thingCode);

}
