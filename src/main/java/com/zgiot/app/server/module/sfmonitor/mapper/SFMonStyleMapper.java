package com.zgiot.app.server.module.sfmonitor.mapper;

import com.zgiot.app.server.module.sfmonitor.pojo.SFMonStyle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SFMonStyleMapper {

    @Select("select * from tb_sfmon_style")
    public List<SFMonStyle> getAllStyle();

    @Select("select code from tb_sfmon_style where name=#{name}")
    public String getCodeByName(@Param("name") String name);
}
