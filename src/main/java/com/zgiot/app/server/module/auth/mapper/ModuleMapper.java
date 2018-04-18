package com.zgiot.app.server.module.auth.mapper;

import com.zgiot.app.server.module.auth.pojo.Module;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ModuleMapper {

    @Select("select * from tb_module where platform_client_id=#{platClientId}")
    List<Module> getModulesByClientId(@Param("platClientId") int platClientId);
}
