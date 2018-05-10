package com.zgiot.app.server.module.auth.mapper;

import com.zgiot.app.server.module.auth.pojo.RelAuthorityGroupAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RelAuthorityGroupAuthorityMapper {
    void insertRelAuthGroupAuth(List<RelAuthorityGroupAuthority> relAuthorityGroupAuthorities);

    void deleteRelAuthGroupAuthByAuthGroupId(@Param("authGroupId") int authGroupId);

    List<RelAuthorityGroupAuthority> getRelAuthorityGroupAuthority(@Param("authGroupId") int authGroupId);
}
