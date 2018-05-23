package com.zgiot.app.server.module.auth.mapper;

import com.zgiot.app.server.module.auth.pojo.Role;
import com.zgiot.app.server.module.auth.pojo.RoleUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleUserMapper {

    @Insert("INSERT INTO s_user_role (role_id, user_id) VALUES (#{roleId}, #{userId}) ")
    void insertRoleUser(RoleUser obj);

    @Delete("DELETE from s_user_role WHERE user_id=#{userId}")
    void deleteByUser(@Param("userId") long userId);

    @Delete("DELETE from s_user_role WHERE role_id=#{roleId}")
    void deleteByRole(int roleId);

    @Select("SELECT r.* FROM `s_user_role` ur, `s_role` r WHERE r.`id`=ur.`role_id` AND ur.`user_id`=#{userId}")
    List<Role> findRolesByUser(long userId);

}
