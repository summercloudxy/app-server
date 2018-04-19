package com.zgiot.app.server.module.userfavor.mapper;

import com.zgiot.app.server.module.userfavor.pojo.UserFavor;
import com.zgiot.app.server.module.userfavor.pojo.PadModule;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PadModuleMapper {
    @Select("select * from tb_pad_module")
    List<PadModule> getPadModule();

    @Select("select * from tb_pad_module a ,(select * from rel_user_favor where user_uuid=#{userUuid}) b " +
            "where a.id=b.pad_module_id")
    List<PadModule> getPadModuleByUuid(@Param("userUuid") String userUuid);

    @Select("select * from tb_pad_module where is_default_favor=1")
    List<PadModule> getCommonPadModule();

    @Insert("insert into rel_user_favor values(#{userUuid},#{padModuleId},#{sort})")
    void addUserFavor(UserFavor userFavor);

    @Delete("delete from rel_user_favor where user_uuid=#{userUuid}")
    void deleteUserFavor(@Param("userUuid") String userUuid);

    @Select("select max(sort) from rel_user_favor where user_uuid=#{userUuid}")
    Float getMaxSort(@Param("userUuid") String userUuid);

    @Select("select * from rel_user_favor where user_uuid=#{userUuid} order by sort")
    List<UserFavor> getUserFavor(@Param("userUuid") String userUuid);

    @Select("select * from rel_user_favor where user_uuid=#{userUuid} and pad_module_id=#{padModuleId}")
    UserFavor getUserFavorById(@Param("userUuid") String userUuid, @Param("padModuleId") long padModuleId);
}
