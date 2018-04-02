package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.pojo.Building;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BuildingMapper {

    @Insert("INSERT INTO tb_building(building_name,max_floor) VALUES(#{buildingName},#{maxFloor})")
    void addBuilding(Building building);

    @Select("SELECT t.id,t.building_name,t.max_floor FROM tb_building t WHERE t.id = #{id}")
    Building getBuildingById(@Param("id") Long id);

    @Select("SELECT t.id,t.building_name,t.max_floor FROM tb_building t ORDER BY t.building_name")
    List<Building> getBuildingAll();

    @Update("UPDATE tb_building SET building_name=#{buildingName},max_floor=#{maxFloor} where id = #{id}")
    void editBuilding(Building building);

    @Delete("DELETE FROM tb_building where id = #{id}")
    void deleteBuilding(@Param("id") Long id);
}
