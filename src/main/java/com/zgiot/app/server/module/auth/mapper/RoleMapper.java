package com.zgiot.app.server.module.auth.mapper;

import com.zgiot.app.server.module.auth.controller.role.RoleReturn;
import com.zgiot.app.server.module.auth.pojo.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    void insertRole(Role role);

    void deleteRole(@Param("id") int id);

    void updateRole(Role role);

    List<Role> findRolesByUserId(@Param("userId") long userId);

    List<RoleReturn> getAllRoleInfo();

    RoleReturn getRoleInfoByName(@Param("name") String name);

    RoleReturn getRoleInfoById(@Param("id") int id);

    List<Role> getRoles();

    int getRoleSum();

    Role getRoleByRoleName(@Param("roleName") String roleName);

    @Insert("insert into s_role_authority(authority_id,role_id) values(#{authorityId},#{roleId})")
    void addRole(@Param("authorityId") int authorityId, @Param("roleId") int roleId);

    @Delete("delete from s_role_authority where role_id=#{roleId}")
    void deleteRoleAuthority(@Param("roleId") int roleId);

}