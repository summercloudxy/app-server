package com.zgiot.app.server.module.sfstop.mapper;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetDelay;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 停车设备延时
 */
@Mapper
public interface StopTypeSetDelayMapper {

    @Select("SELECT d.*,i.thing_name as parentThingName FROM `tb_stop_type_set_delay` d LEFT JOIN tb_stop_information i ON d.parent_thing_code=i.thing_code WHERE d.thing_code = #{thingCode} AND d.is_deleted = 0 AND d.is_checked IS NULL AND d.stop_type=#{stopType}")
    List<StopTypeSetDelay> getStopTypeSetDelayByTCNoChecked(StopTypeSetDelay stopTypeSetDelay);

    @Select("SELECT d.*,i.thing_name as parentThingName FROM `tb_stop_type_set_delay` d LEFT JOIN tb_stop_information i ON d.parent_thing_code=i.thing_code WHERE d.thing_code =#{thingCode} AND d.is_deleted = 0 AND d.is_checked=1 AND d.stop_type=#{stopType}")
    List<StopTypeSetDelay> getStopTypeSetDelayByTCNIsChecked(StopTypeSetDelay stopTypeSetDelay);

    @Delete("delete from tb_stop_type_set_delay where thing_code=#{thingCode} AND is_checked IS NULL AND stop_type=#{stopType}")
    void deleteDelayByTCAndNoChecked(@Param("thingCode") String thingCode, @Param("stopType") Integer stopType);

    @Insert("INSERT INTO tb_stop_type_set_delay (thing_code,delay_time,parent_thing_code,create_user,create_time,update_user," +
            "update_time,is_checked,stop_type) VALUES(#{thingCode},#{delayTime},#{parentThingCode},#{createUser},#{createTime},#{updateUser},#{updateTime},#{isChecked},#{stopType})")
    void insertStopTypeSetDelay(StopTypeSetDelay stopTypeSetDelay);

    @Delete("delete from tb_stop_type_set_delay where thing_code=#{thingCode} AND is_checked=1 AND stop_type=#{stopType}")
    void deleteDelayByTCAndChecked(@Param("thingCode") String thingCode, @Param("stopType") Integer stopType);

    @Select("SELECT * FROM `tb_stop_type_set_delay` where thing_code=#{thingCode} and is_deleted=0 AND is_checked IS NULL AND stop_type=1")
    List<StopTypeSetDelay> getStopTypeSetDelayByThingCode(@Param("thingCode") String thingCode);
}
                                                  