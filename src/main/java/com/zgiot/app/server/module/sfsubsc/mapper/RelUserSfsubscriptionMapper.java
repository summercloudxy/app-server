package com.zgiot.app.server.module.sfsubsc.mapper;

import com.zgiot.app.server.module.sfsubsc.entity.pojo.RelUserSfsubscription;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RelUserSfsubscriptionMapper {

    @Insert("insert into rel_user_sfsubscription(client_id,card_code,sort,user_uuid) " +
            "values (#{clientId},#{cardCode},#{sort},#{userUuid})")
    void addRelUserSfsubscription(RelUserSfsubscription relUserSfsubscription);

    @Delete("delete from rel_user_sfsubscription where user_uuid = #{userUuid} and client_id = #{clientId}")
    void deleteRelUserSfsubscription(@Param("userUuid") String userUuid, @Param("clientId") String clientId);

    @Select("select * from rel_user_sfsubscription where user_uuid = #{userUuid} " +
            "and card_code <> #{cardCode} order by sort")
    List<RelUserSfsubscription> getTop(@Param("userUuid") String userUuid, @Param("cardCode") String cardCode);

}
