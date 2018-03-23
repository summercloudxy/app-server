package com.zgiot.app.server.module.sfmonitor.mapper;

import com.zgiot.app.server.module.sfmonitor.pojo.SFMonEquipMonitorInfo;
import com.zgiot.app.server.module.sfmonitor.pojo.SFMonStyle;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface SFMonEquipMonitorInfoMapper {
    @Insert("insert into tb_sfmon_equipmonitor_info(thing_code,thing_name,config_progress,editor,create_date,comment) " +
            "values(#{thingCode},#{thingName},#{configProgress},#{editor},#{createDate},#{comment})")
    public void addEquipmentConfig(SFMonEquipMonitorInfo sfMonEquipMonitorInfo);

    @Select("select * from tb_sfmon_equipmonitor_info where thing_code=#{thingCode}")
    public SFMonEquipMonitorInfo getEquiupmentInfo(@Param("thingCode") String thingCode);

    @Select("select * from tb_sfmon_equipmonitor_info where id=#{id}")
    public SFMonEquipMonitorInfo getEquiupmentInfoById(@Param("id") int id);

    @Delete("delete from tb_sfmon_equipmonitor_info where id=#{id}")
    public void deleteEquipmentBaseInfo(@Param("id") int id);

    public void updateEquipmonitorInfo(SFMonEquipMonitorInfo sfMonEquipMonitorInfo);

    @Select("select * from tb_sfmon_equipmonitor_info order by create_date desc")
    public List<SFMonEquipMonitorInfo> getEquipmentInfoPage();

    @Select("select count(1) from tb_sfmon_equipmonitor_info")
    public int getEquipmentMonitorCount();

    public List<SFMonEquipMonitorInfo> getEquipmentMonitorInfo(SFMonEquipMonitorInfo sfMonEquipMonitorInfo);

    @Select("select thing_name from tb_sfmon_equipmonitor_info where thing_name like #{thingName} group by thing_name")
    public List<String> getEquipmentNames(@Param("thingName") String thingName);
}
