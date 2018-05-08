package com.zgiot.app.server.module.sfsubsc.mapper;

import com.zgiot.app.server.module.sfsubsc.entity.pojo.SFSubscriptionCard;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardTypeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jys
 * 卡片配置
 */
@Mapper
public interface SubscCardTypeMapper {
    /**
     * 查询所有的订阅卡片配置
     *
     * @return
     */
    @Select("select * from tb_subsc_card_type")
    List<SubscCardTypeDO> getAllSubscCardTypes();

    /**
     * 根据卡片编码查询卡片信息
     *
     * @param cardCode
     * @return
     */
    @Select("select * from tb_subsc_card_type where card_code=#{cardCode} ")
    SubscCardTypeDO getCardTypeByCardCode(@Param("cardCode") String cardCode);

    /**
     * 根据用户uuid查询卡片信息
     *
     * @param userUuid
     * @return
     */
    @Select("select t1.* from tb_subsc_card_type t1,rel_user_card t2 " +
            "where t1.card_code = t2.card_code " +
            "and t2.user_uuid=#{userUuid}")
    List<SubscCardTypeDO> getCardTypeByUuid(@Param("userUuid") String userUuid);

    /**
     * 获取该用户订阅的卡片
     *
     * @param userUuid
     * @return
     */
    @Select("select t1.* from tb_sfsubscription_card t1,rel_user_sfsubscription t2 " +
            "where t1.card_code = t2.card_code " +
            "and t2.user_uuid=#{userUuid} ORDER BY t2.sort")
    List<SFSubscriptionCard> getSubCardTypeByUuid(@Param("userUuid") String userUuid);

}
