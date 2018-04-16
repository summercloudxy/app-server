package com.zgiot.app.server.module.auth.mapper;

import com.zgiot.app.server.module.auth.pojo.AuthorityType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthorityTypeMapper {
    @Select("select * from rel_authority_type where platform_client_id=#{platformClientId} and module_id=#{moduleId}")
    List<AuthorityType> getAuthorityTypeByClientAndModuleId(@Param("platformClientId") int platformClientId, @Param("moduleId") int moduleId);
}
