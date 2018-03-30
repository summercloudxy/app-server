package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
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

    @Update("UPDATE tb_thing SET thing_code = #{thing_code},thing_name=#{thing_name}," +
            "thing_type1_code=#{thing_type1_code},thing_shortname=#{thing_shortname} where id = #{id}")
    void editThing(Thing thing);

    /**
     * 获取设备信息列表
     * @param thingCodeList
     * @return
     */
    List<DeviceInfo> getDeviceInfoByThingcode(List<String> thingCodeList);

    @Select("SELECT t.id,t.thing_code,t.thing_name,t.thing_type1_code,t.thing_shortname FROM tb_thing t " +
            "where t.thing_code like #{thingCode}  and t.thing_type1_code = #{thingType1Code}" +
            " ORDER BY t.thing_code")
    List<Thing> getThingByCode(@Param("thingCode") String thingCode,@Param("thingType1Code") String thingType1Code);

    @Select("select * from tb_thing where thing_code = #{thingCode}")
    List<Thing> getThingByThingCode(@Param("thingCode") String thingCode);

    @Select("SELECT * FROM tb_thing WHERE thing_code LIKE #{thingCode} AND thing_type1_code = #{thingType1Code}")
    List<Thing> getThingByType(@Param("thingCode") String thingCode, @Param("thingType1Code") String thingType1Code);

}
