package com.zgiot.app.server.module.auth.mapper;

import com.zgiot.app.server.module.auth.pojo.RelRoleAuthorityGroup;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RelRoleAuthorityGroupMapper {
    @Select("select count(1) from rel_role_authority_group where authority_group_id=#{authGroupId}")
    int getRoleCountByAuthGroupId(@Param("authGroupId") int authGroupId);

    @Insert("insert into rel_role_authority_group(role_id,authority_group_id,enabled) values(#{roleId},#{authorityGroupId},#{enable})")
    void insertRelRoleAuthorityGroup(RelRoleAuthorityGroup relRoleAuthorityGroup);

    @Delete("delete from rel_role_authority_group where role_id=#{roleId}")
    void deleteRelRoleAuthorityGroup(@Param("roleId") int roleId);

    @Select("select * from rel_role_authority_group where role_id=#{roleId}")
    List<RelRoleAuthorityGroup> getRelRoleAuthorityByRoleId(@Param("roleId") int roleId);
}
