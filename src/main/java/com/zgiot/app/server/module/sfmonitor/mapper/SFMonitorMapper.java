package com.zgiot.app.server.module.sfmonitor.mapper;
 
import com.zgiot.app.server.module.sfmonitor.pojo.SFMonitor;
import org.apache.ibatis.annotations.*; 
 
import java.util.List; 
 
@Mapper 
public interface SFMonitorMapper { 
 
    @Select("select * from tb_sfmon where id=#{id} order by sort asc") 
    SFMonitor getMonitorById(@Param("id") long id); 
 
    @Insert("insert into tb_sfmon(sfmon_name,user_id,sort,user_name) values(#{sfMonName},#{userId},#{sort},#{userName})") 
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = long.class) 
    void addMonitor(SFMonitor sfMonitor); 
 
    @Select("select * from tb_sfmon where sfmon_name=#{name} and user_name=#{userName}")
    SFMonitor getMonitorByName(@Param("name") String name,@Param("userName") String userName);
 
    @Select("select * from tb_sfmon where user_id=#{userId} order by sort asc")
    List<SFMonitor> getMonitorByUserId(@Param("userId") long userId); 
 
    @Select("select * from tb_sfmon where user_name=#{userName} order by sort asc")
    List<SFMonitor> getMonitorByUserName(@Param("userName") String userName); 
 
    @Update("update tb_sfmon set sfmon_name=#{sfMonName},sort=#{sort} where id=#{id}")
    void editMonitor(@Param("sfMonName") String sfMonName,@Param("sort") float sort,@Param("id") long id);
 
    @Select("select sort from tb_sfmon order by sort desc limit 1") 
    Float getMaxSortMonitor();
 
    @Select("select sort from tb_sfmon order by sort asc limit 1") 
    Float getMinSortMonitor();
 
    @Delete("delete from tb_sfmon where id=#{id}") 
    void deleteMonitor(@Param("id") long id); 
 
} 