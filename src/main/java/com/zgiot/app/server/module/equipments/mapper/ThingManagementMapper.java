package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.controller.*;
import com.zgiot.app.server.module.equipments.pojo.Thing;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ThingManagementMapper {

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

    @Update("UPDATE tb_thing SET thing_code = #{thingCode},thing_name=#{thingName}," +
            "thing_type1_code=#{thingType1Code},thing_shortname=#{thingShortname} where id = #{id}")
    void editThing(Thing thing);

    /**
     * 获取设备信息列表
     * @param thingCodeList
     * @return
     */
    List<DeviceInfo> getDeviceInfoByThingcode(@Param("thingCodeList") List<String> thingCodeList);

    @Select("SELECT t.id,t.thing_code,t.thing_name,t.thing_type1_code,t.thing_shortname FROM tb_thing t " +
            "WHERE t.thing_code like #{thingCode} AND t.thing_type1_code = #{thingType1Code} " +
            "ORDER BY t.thing_code")
    List<Thing> getThingByCode(@Param("thingCode") String thingCode,@Param("thingType1Code") String thingType1Code);

    @Select("select * from tb_thing where thing_code = #{thingCode}")
    List<Thing> getThingByThingCode(@Param("thingCode") String thingCode);

    @Select("SELECT * FROM tb_thing WHERE thing_code LIKE #{thingCode} AND thing_type1_code = #{thingType1Code}")
    List<Thing> getThingByType(@Param("thingCode") String thingCode, @Param("thingType1Code") String thingType1Code);

    /**
     * 获取部件信息列表
     * @param thingCodeList
     * @return
     */
    List<PartsInfo> getPartsInfoByThingcode(@Param("thingCodeList") List<String> thingCodeList);

    /**
     * 获取阀门信息列表
     * @param thingCodeList
     * @return
     */
    List<ValveInfo> getValveInfoByThingcode(@Param("thingCodeList") List<String> thingCodeList);

    /**
     * 获取闸板信息列表
     * @param thingCodeList
     * @return
     */
    List<FlashboardInfo> getFlashboardInfoByThingcode(@Param("thingCodeList") List<String> thingCodeList);

    /**
     * 获取管道信息列表
     * @param thingCodeList
     * @return
     */
    List<PipeInfo> getPipeInfoByThingcode(@Param("thingCodeList") List<String> thingCodeList);

    /**
     * 获取仪表信息列表
     * @param thingCodeList
     * @return
     */
    List<MeterInfo> getMeterInfoByThingcode(@Param("thingCodeList") List<String> thingCodeList);

    /**
     * 根据设备code
     * @param thingCode
     * @return
     */
    @Select("SELECT id,thing_code,thing_name FROM tb_thing " +
            "WHERE thing_type1_code = 'PARTS' AND thing_code LIKE #{thingCode}")
    List<PartsInfo> getPartsInfoByThingId(@Param("thingCode") String thingCode);

    @Select("SELECT * FROM tb_thing " +
            "WHERE (thing_code LIKE #{code} OR thing_name LIKE #{name}) " +
            "AND thing_type1_code = #{type}")
    List<Thing> getThingByCodeOrName(@Param("code") String code, @Param("name") String name, @Param("type") String type);

}
