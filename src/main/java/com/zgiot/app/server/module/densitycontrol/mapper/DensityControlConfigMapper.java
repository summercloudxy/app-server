package com.zgiot.app.server.module.densitycontrol.mapper;

import com.zgiot.app.server.module.densitycontrol.pojo.DensityControlConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DensityControlConfigMapper {

    @Select("SELECT * FROM tb_density_control_config")
    List<DensityControlConfig> getAllDensityControlConfig();

    @Select("SELECT * FROM tb_density_control_config WHERE thing_code = #{thingCode} AND metric_code = #{metricCode}")
    DensityControlConfig getDensityControlConfigByThingMetric(@Param("thingCode") String thingCode, @Param("metricCode") String metricCode);

    @Update("UPDATE tb_density_control_config SET metric_value = #{metricValue}, update_dt = #{updateDt} " +
            "WHERE thing_code = #{thingCode} AND metric_code = #{metricCode}")
    void updateDensityControlConfig(DensityControlConfig densityControlConfig);

}
