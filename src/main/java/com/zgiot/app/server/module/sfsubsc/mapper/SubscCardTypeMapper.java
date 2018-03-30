package com.zgiot.app.server.module.sfsubsc.mapper;

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


}