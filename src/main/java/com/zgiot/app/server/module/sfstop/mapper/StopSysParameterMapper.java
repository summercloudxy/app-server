package com.zgiot.app.server.module.sfstop.mapper;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopSysParameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StopSysParameterMapper {

    @Select("SELECT * FROM `tb_stop_sys_parameter` where is_deleted=0")
    List<StopSysParameter> getStopSysParameterList();

    @Select("SELECT * FROM `tb_stop_sys_parameter` WHERE parameter_key=#{comparisonOperator} LIMIT 1")
    StopSysParameter getStopSysParameterByKey(@Param("comparisonOperator") String comparisonOperator);
}
                                                  