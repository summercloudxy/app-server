package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.pojo.Building;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BuildingMapper {

    @Insert("INSERT INTO tb_building(building_name,max_floor,update_dt) VALUES(#{buildingName},#{maxFloor},NOW())")
    void addBuilding(Building building);

    @Select("SELECT t.id,t.building_name,t.max_floor,t.update_dt FROM tb_building t WHERE t.id = #{id}")
    Building getBuildingById(@Param("id") Long id);

    @Select("SELECT t.id,t.building_name,t.max_floor,t.update_dt FROM tb_building t ORDER BY t.building_name")
    List<Building> getBuildingAll();

    @Update("UPDATE tb_building SET building_name=#{buildingName},max_floor=#{maxFloor},update_dt = NOW() " +
            "where id = #{id}")
    void editBuilding(Building building);

    @Delete("DELETE FROM tb_building where id = #{id}")
    void deleteBuilding(@Param("id") Long id);

    @Select("SELECT t.id,t.building_name,t.max_floor,t.update_dt FROM tb_building t " +
            "WHERE t.building_name LIKE #{buildingName} ORDER BY t.building_name")
    List<Building> getBuildingByBuildingName(@Param("buildingName") String buildingName);

    @Select("SELECT * FROM tb_building")
    List<Building> getAllBuilding();
}
