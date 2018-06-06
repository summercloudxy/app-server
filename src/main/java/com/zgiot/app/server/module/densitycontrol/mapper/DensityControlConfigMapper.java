package com.zgiot.app.server.module.densitycontrol.mapper;

import com.zgiot.app.server.module.densitycontrol.pojo.DensityControlConfig;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DensityControlConfigMapper {

    @Select("SELECT * FROM tb_density_control_config")
    List<DensityControlConfig> getAllDensityControlConfig();

    @Select("SELECT * FROM tb_density_control_config WHERE thing_code = #{thingCode} AND metric_code = #{metricCode}")
    DensityControlConfig getDensityControlConfigByThingMetric(@Param("thingCode") String thingCode, @Param("metricCode") String metricCode);

    @Delete("DELETE FROM tb_density_control_config WHERE term = #{term}")
    void deleteAllDensityControlConfig();

    @Insert("INSERT INTO tb_density_control_config (term,thing_code,metric_code,metric_value,status,update_dt) " +
            "VALUES (#{term},#{thing_code},#{metric_code},#{metric_value},#{status},#{update_dt})")
    void insertDensityControlConfig(DensityControlConfig densityControlConfig);

}
