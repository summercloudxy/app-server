package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.pojo.TBSystem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TBSystemMapper {

   @Select("SELECT t.id,t.system_name,t.parent_system_id,t.level FROM tb_system t where t.level = #{level}")
    List<TBSystem> getTBSystemALL(@Param("level") int level);


}
