package com.zgiot.app.server.module.userfavor.mapper;

import com.zgiot.app.server.module.userfavor.pojo.UserFavor;
import com.zgiot.app.server.module.userfavor.pojo.PadModule;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PadModuleMapper {
    @Select("select * from tb_pad_module")
    List<PadModule> getPadModule();

    @Select("select * from tb_pad_module a ," +
            "(select * from rel_user_favor where user_uuid=#{userUuid} and client_id = #{clientId}) b " +
            "where a.id=b.module_id")
    List<PadModule> getPadModuleByUuid(@Param("userUuid") String userUuid, @Param("clientId") long clientId);

    @Select("select * from tb_pad_module where is_default_favor=1")
    List<PadModule> getCommonPadModule();

    @Insert("insert into rel_user_favor(user_uuid,module_id,sort,client_id) values(#{userUuid},#{moduleId},#{sort},#{clientId})")
    void addUserFavor(UserFavor userFavor);

    @Delete("delete from rel_user_favor where user_uuid=#{userUuid} and client_id = #{clientId}")
    void deleteUserFavor(@Param("userUuid") String userUuid, @Param("clientId") long clientId);

    @Select("select max(sort) from rel_user_favor where user_uuid=#{userUuid} and client_id = #{clientId}")
    Float getMaxSort(@Param("userUuid") String userUuid, @Param("clientId") long clientId);

    @Select("select * from rel_user_favor where user_uuid=#{userUuid} and client_id = #{clientId} order by sort")
    List<UserFavor> getUserFavor(@Param("userUuid") String userUuid, @Param("clientId") long clientId);

    @Select("select * from rel_user_favor where user_uuid=#{userUuid} and module_id=#{moduleId} and client_id = #{clientId}")
    UserFavor getUserFavorById(@Param("userUuid") String userUuid, @Param("moduleId") long moduleId, @Param("clientId") long clientId);
}
