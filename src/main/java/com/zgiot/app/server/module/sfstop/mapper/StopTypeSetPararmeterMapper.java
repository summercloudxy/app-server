package com.zgiot.app.server.module.sfstop.mapper;


import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetPararmeter;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * 停车参数条件
 */
@Mapper
public interface StopTypeSetPararmeterMapper {

    @Select("SELECT p.*,i.thing_name as parentThingName FROM `tb_stop_type_set_pararmeter` p LEFT JOIN tb_stop_information i on p.parent_thing_code=i.thing_code where p.thing_code=#{thingCode} AND p.is_deleted=0 AND p.is_checked is NULL AND p.stop_type=#{stopType}")
    List<StopTypeSetPararmeter> getPararmeterNoChecked(StopTypeSetPararmeter stopTypeSetPararmeter);

    @Select("SELECT p.*,i.thing_name as parentThingName FROM `tb_stop_type_set_pararmeter` p LEFT JOIN tb_stop_information i ON p.parent_thing_code=i.thing_code where p.thing_code=#{thingCode} AND p.is_deleted=0 AND p.is_checked=1 AND p.stop_type=#{stopType}")
    List<StopTypeSetPararmeter> getPararmeterChecked(StopTypeSetPararmeter stopTypeSetPararmeter);

    @Delete("delete from tb_stop_type_set_pararmeter where thing_code=#{thingCode} AND is_checked IS NULL AND stop_type=#{stopType}")
    void delPararmeterNoChecked(@Param("thingCode") String thingCode, @Param("stopType") Integer stopType);

    @Delete("delete from tb_stop_type_set_pararmeter where thing_code=#{thingCode} AND is_checked=1 AND stop_type=#{stopType}")
    void delPararmeterChecked(@Param("thingCode") String thingCode, @Param("stopType") Integer stopType);

    @Insert("insert into tb_stop_type_set_pararmeter(thing_code,metric_code,comparison_operator,comparison_value,parent_thing_code,create_user,create_time,update_user,update_time,is_checked,stop_type,delay_time) VALUES" +
            "(#{thingCode},#{metricCode},#{comparisonOperator},#{comparisonValue},#{parentThingCode},#{createUser},#{createTime},#{updateUser},#{updateTime},#{isChecked},#{stopType},#{delayTime})")
    void insertStopNormalSetPararmeter(StopTypeSetPararmeter stopTypeSetPararmeter);
}
                                                  