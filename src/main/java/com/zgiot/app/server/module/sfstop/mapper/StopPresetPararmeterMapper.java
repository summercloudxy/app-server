package com.zgiot.app.server.module.sfstop.mapper;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopPresetPararmeter;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 停车预设参数
 */
@Mapper
public interface StopPresetPararmeterMapper {

    @Select("SELECT * FROM `tb_stop_preset_pararmeter` where is_deleted=0 and thing_code=#{thingCode} and stop_type=#{stopType}")
    List<StopPresetPararmeter> getStopPresetPararmeterByTC(StopPresetPararmeter stopPresetPararmeter);

    @Delete("delete  from tb_stop_preset_pararmeter where thing_code=#{thingCode} AND stop_type=#{stopType} ")
    void delStopPresetPararmeterByTC(@Param("thingCode") String thingCode, @Param("stopType") Integer stopType);

    @Insert("insert into tb_stop_preset_pararmeter(metric_code,default_value,type,thing_code,create_user,create_time,update_user,update_time,stop_type) " +
            "VALUES(#{metricCode},#{defaultValue},#{type},#{thingCode},#{createUser},#{createTime},#{updateUser},#{updateTime},#{stopType})")
    void insertStopPresetPararmeter(StopPresetPararmeter stopPresetPararmeter);
}
                                                  