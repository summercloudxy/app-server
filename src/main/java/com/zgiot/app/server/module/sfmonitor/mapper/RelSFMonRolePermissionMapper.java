package com.zgiot.app.server.module.sfmonitor.mapper;

import com.zgiot.app.server.module.sfmonitor.pojo.RelSFMonRolePermission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RelSFMonRolePermissionMapper {

    @Select("SELECT t4.* " +
            "FROM s_user t1,s_user_role t2,s_role t3,rel_sfmon_role_permission t4 " +
            "WHERE t1.uuid = #{userUuid} AND t1.id = t2.user_id " +
            "AND t3.id = t2.role_id AND t3.id = t4.role_id")
    List<RelSFMonRolePermission> getRelSFMonRolePermissionByUser(@Param("userUuid") String userUuid);

}
