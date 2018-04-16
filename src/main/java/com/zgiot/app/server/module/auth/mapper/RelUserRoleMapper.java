package com.zgiot.app.server.module.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RelUserRoleMapper {
    @Select("select count(1) from s_user_role where role_id=#{roleId} ")
    int getUserCount(@Param("roleId") int roleId);
}
