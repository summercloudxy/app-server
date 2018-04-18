package com.zgiot.app.server.module.auth.mapper;

import com.zgiot.app.server.module.auth.controller.AuthorityGroup.PlatformClientAndModule;
import com.zgiot.app.server.module.auth.pojo.AuthorityGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthorityGroupMapper {

    List<AuthorityGroup> getAuthorityByModuleAndClientId(@Param("platformClientId") int platformClientId, @Param("moduleId") int moduleId);

    int getAuthGroupCountByModuleAndClientId(@Param("platformClientId") int platformClientId, @Param("moduleId") int moduleId);

    AuthorityGroup getAuthGroupByName(@Param("name") String name);

    void addAuthGroup(AuthorityGroup authorityGroup);

    PlatformClientAndModule getClientAndModuleByAuthGroupId(@Param("authorityGroupId") int authorityGroupId);

    void deleteAuthGroupById(@Param("authGroupId") int authGroupId);

    void upgradeAuthGroup(AuthorityGroup authorityGroup);

    List<AuthorityGroup> selectAuthGroupByUserName(@Param("personName") String personName);
}
