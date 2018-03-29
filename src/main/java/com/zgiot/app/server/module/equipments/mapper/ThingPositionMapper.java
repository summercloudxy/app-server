package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.pojo.ThingPosition;
import org.apache.ibatis.annotations.Insert;
import org.mapstruct.Mapper;

@Mapper
public interface ThingPositionMapper {

    @Insert("INSERT INTO tb_thing_position(thing_code,building_id,floor,location_area,location_x,location_y) " +
            "values(#{thingCode},#{buildingId},#{floor},#{locationArea},#{locationAreaX},#{locationY})")
    void addThingPosition(ThingPosition thingPosition);

}
