package com.zgiot.app.server.module.manufacturer.mapper;

import com.zgiot.app.server.module.manufacturer.pojo.Manufacturer;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ManufacturerMapper {

    @Insert("INSERT INTO tb_manufacturer(thing_type1_code,manufacturer_code,manufacturer_name," +
            "manufacturer_short_name,manufacturer_address,manufacturer_web,manufacturer_tel,update_date)" +
            " VALUES(#{thingType1Code},#{manufacturerCode},#{manufacturerName},#{manufacturerShortName}," +
            "#{manufacturerAddress},#{manufacturerWeb},#{manufacturerTel},NOW())")
    void addManufacturer(Manufacturer manufacturer);

    @Update("UPDATE tb_manufacturer SET thing_type1_code=#{thingType1Code},manufacturer_code=#{manufacturerCode}," +
            "manufacturer_name=#{manufacturerName},manufacturer_short_name=#{manufacturerShortName}," +
            "manufacturer_address=#{manufacturerAddress},manufacturer_web=#{manufacturerWeb}," +
            "manufacturer_tel=#{manufacturerTel},update_dt = NOW() " +
            "where id = #{id}")
    void editManufacturer(Manufacturer manufacturer);

    @Delete("DELETE FROM tb_manufacturer where id = #{id}")
    void deleteManufacturer(@Param("id") Long id);

    @Select("SELECT t.id,t.thing_type1_code,t.manufacturer_code,t.manufacturer_name,t.manufacturer_short_name," +
            "t.manufacturer_address,t.manufacturer_web,t.manufacturer_tel,t.update_date FROM tb_Manufacturer t " +
            "WHERE t.thing_type1_code = #{thingType1Code} ORDER BY t.manufacturer_name")
    List<Manufacturer> getManufacturerByType(@Param("thingType1Code") String thingType1Code);

    @Select("SELECT t.id,t.thing_type1_code,t.manufacturer_code,t.manufacturer_name,t.manufacturer_short_name," +
            "t.manufacturer_address,t.manufacturer_web,t.manufacturer_tel,t.update_date FROM tb_Manufacturer t " +
            "WHERE t.thing_type1_code = #{thingType1Code} AND t.manufacturer_code LIKE #{manufacturerCode}" +
            "UNION(" +
            "SELECT t.id,t.thing_type1_code,t.manufacturer_code,t.manufacturer_name,t.manufacturer_short_name," +
            "t.manufacturer_address,t.manufacturer_web,t.manufacturer_tel,t.update_date FROM tb_Manufacturer t " +
            "WHERE t.thing_type1_code = #{thingType1Code} AND t.manufacturer_name LIKE #{manufacturerName}" +
            ")")
    List<Manufacturer> getManufacturerByCodeOrName(@Param("thingType1Code") String thingType1Code,
                                                   @Param("manufacturerCode") String manufacturerCode,
                                                    @Param("manufacturerName") String manufacturerName);
}
