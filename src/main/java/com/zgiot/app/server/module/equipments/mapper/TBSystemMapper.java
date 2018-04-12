package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.pojo.TBSystem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TBSystemMapper {

    @Select("SELECT t.id,t.system_name,t.parent_system_id,t.level FROM tb_system t where t.level = #{level}")
    List<TBSystem> getTBSystemALL(@Param("level") int level);

    List<String> getThingCodeBySystemId(@Param("systemIdList") List<Long> systemIdList);

    @Select("SELECT * FROM tb_system WHERE id = #{id}")
    TBSystem getSystemById(@Param("id") Long id);

    @Select("SELECT * FROM tb_system WHERE parent_system_id = #{id}")
    List<TBSystem> getSystemByParentId(@Param("id") Long id);

    @Select("SELECT t1.* FROM tb_thing t1,rel_thing_system t2,tb_thing_position t3 " +
            "WHERE t1.thing_code = t2.thing_code " +
            "AND t1.thing_code = t3.thing_code AND t2.system_id = -1 " +
            "AND t1.thing_type1_code = 'DEVICE' " +
            "AND t1.thing_code LIKE #{thingCode} " +
            "AND t3.location_area = #{areaId}")
    List<DeviceInfo> getFreeDeviceInfo(@Param("thingCode") String thingCode, @Param("areaId") Long areaId);
}
