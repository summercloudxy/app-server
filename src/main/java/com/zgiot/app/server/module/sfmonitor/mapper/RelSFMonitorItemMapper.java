package com.zgiot.app.server.module.sfmonitor.mapper;
 
import com.zgiot.app.server.module.sfmonitor.controller.MonitorItemInfo;
import com.zgiot.app.server.module.sfmonitor.pojo.RelSFMonItem;
import org.apache.ibatis.annotations.*; 
 
import java.util.List; 
 
@Mapper 
public interface RelSFMonitorItemMapper { 

    List<MonitorItemInfo> getRelSFMonitorItemByMonId(@Param("sfMonId") long sfMonId); 
 
    @Select("select * from rel_sfmon_item where sfmon_id=#{sfMonId} order by sort asc") 
    List<RelSFMonItem> getRelSFMonitorItem(@Param("sfMonId") long sfMonId); 
 
    @Select("select * from rel_sfmon_item where id=#{id}") 
    RelSFMonItem getRelSFMonitorItemById(@Param("id") long id); 
 
    @Insert("insert into rel_sfmon_item(sfmon_id,sort,view_type_id,thing_code,metric_code) values(#{sfMonId},#{sort},#{viewTypeId},#{thingCode},#{metricCode})")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = long.class)
    void addRelSFMonitorItem(RelSFMonItem relSFMonItem);
 
    @Delete("delete from rel_sfmon_item where sfmon_id=#{sfMonId}") 
    void deleteRelRelSFMonitorItem(@Param("sfMonId") long sfMonId); 
 
    @Select("select sort from rel_sfmon_item where sfmon_id=#{sfMonId} order by sort desc limit 1") 
    Float getMaxSortFromMonitorByMonId(@Param("sfMonId") long sfMonId);
 
    @Select("select sort from rel_sfmon_item where sfmon_id=#{sfMonId} order by sort asc limit 1") 
    Float getMinSortFromMonitorByMonId(@Param("sfMonId") long sfMonId);
 
    @Delete("delete from rel_sfmon_item where id=#{id}") 
    void deleteMetricMonitor(@Param("id") long id); 
 
    @Update("update rel_sfmon_item set sort=#{sort} where id=#{id}") 
    void modifyMonItem(@Param("id") long id, @Param("sort") float sort);
 
    @Select("select count(distinct(thing_code)) from rel_sfmon_item where sfmon_id=#{sfMonId}") 
    int getEquipmentCount(@Param("sfMonId") long sfMonId); 
} 