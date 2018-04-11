package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.pojo.TBSystem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TBSystemMapper {

   @Select("SELECT t.id,t.system_name,t.parent_system_id,t.level FROM tb_system t where t.level = #{level}")
    List<TBSystem> getTBSystemALL(@Param("level") int level);

   @Select("SELECT t2.id,t2.thing_code,t2.thing_name,t4.tag_name,t3.create_date " +
           "FROM `rel_thing_system` t1,tb_thing t2,rel_thingtag_thing t3,tb_thing_tag t4 " +
           "WHERE t1.thing_code = t2.thing_code AND t1.thing_code = t3.thing_code " +
           "AND t3.thing_tag_code = t4.code AND t1.system_id = #{systemId}")
    List<DeviceInfo> getDeviceInfoBySystemId(@Param("systemId") int systemId);

   @Select("SELECT * FROM tb_system WHERE parent_system_id = #{id}")
    List<TBSystem> getSystemByParentId(@Param("id") Long id);

   @Select("SELECT * FROM tb_thing" +
           "WHERE thing_code NOT IN (SELECT thing_code FROM rel_thing_system )" +
           "AND thing_type1_code = 'DEVICE'" +
           "AND thing_code LIKE #{thingCode}")
    List<DeviceInfo> getFreeDeviceInfoByThingCode(String thingCode);

}
