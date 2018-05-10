package com.zgiot.app.server.module.auth.mapper;

import com.zgiot.app.server.module.auth.pojo.PlatformClient;
import com.zgiot.app.server.module.auth.pojo.UserPlatformClient;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlatformClientMapper {

    @Select("SELECT * FROM `tb_platform_client` ")
    List<PlatformClient> findAll();

    @Insert("INSERT INTO rel_user_platformclient (user_id , platform_client_id) \n" +
            " VALUES (#{userId} , #{platformClientId})")
    void insertUserClient(UserPlatformClient obj);

    @Delete("delete from rel_user_platformclient where user_id=#{userId}")
    void deleteClientByUser(long userId);

    @Select("SELECT c.* FROM `rel_user_platformclient` uc, `tb_platform_client` c " +
            "WHERE c.`id`=uc.`platform_client_id` AND uc.`user_id`=#{userId}")
    List<PlatformClient> findClientsByUser(long userId);

}