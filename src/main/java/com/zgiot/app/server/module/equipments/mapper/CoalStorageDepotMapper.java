package com.zgiot.app.server.module.equipments.mapper;


import com.zgiot.app.server.module.equipments.pojo.CoalStorageDepot;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CoalStorageDepotMapper {

    @Insert("INSERT INTO tb_coal_storage_depot(name,number,diameter,level) " +
            "VALUES(#{name},#{number},#{diameter},#{level})")
    void addCoalStorageDepot(CoalStorageDepot coalStorageDepot);

    @Select("SELECT t.id,t.name,t.number,t.diameter,t.level FROM tb_coal_storage_depot t WHERE t.id = #{id}")
    CoalStorageDepot getCoalStorageDepotById(@Param("id") Long id);

    @Select("SELECT t.id,t.name,t.number,t.diameter,t.level FROM tb_coal_storage_depot t ORDER BY t.number")
    List<CoalStorageDepot> getCoalStorageDepotAll();

    @Update("UPDATE tb_coal_storage_depot SET name = #{name},number = #{number}," +
            "diameter = #{diameter},level = #{level} WHERE id = #{id}")
    void editCoalStorageDepot(CoalStorageDepot coalStorageDepot);

    @Delete("DELETE FROM tb_coal_storage_depot where id = #{id}")
    void deleteCoalStorageDepot(@Param("id") Long id);

    @Select("SELECT t.id,t.name,t.number,t.diameter,t.level FROM tb_coal_storage_depot t " +
            "WHERE t.name LIKE #{name} ORDER BY t.number")
    List<CoalStorageDepot> getCoalStorageDepotByName(@Param("name") String name);
}
