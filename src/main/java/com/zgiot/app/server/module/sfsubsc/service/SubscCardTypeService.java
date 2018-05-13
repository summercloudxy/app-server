package com.zgiot.app.server.module.sfsubsc.service;

import com.zgiot.app.server.module.sfsubsc.entity.dto.CardDataDTO;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.SFSubscriptionCard;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardTypeDO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jys
 * 卡片配置
 */
@Component
public interface SubscCardTypeService {
    /**
     * 查询用户订阅的卡片
     *
     * @return
     */
    List<SubscCardTypeDO> getAllSubscCardTypes();

    /**
     * 查询历史入洗量指标数据
     *
     * @param subscCardTypeDO
     * @return
     */
    CardDataDTO getHistoricalWashingCapacity(SubscCardTypeDO subscCardTypeDO);


    /**
     * 查询历史产品量
     *
     * @param subscCardTypeDO
     * @return
     */
    CardDataDTO getHistoricalProductRate(SubscCardTypeDO subscCardTypeDO);

    /**
     * 查询原煤配比
     *
     * @param subscCardTypeDO
     * @return
     */
    CardDataDTO getMixtureOfRawCoal(SubscCardTypeDO subscCardTypeDO);

    /**
     * 查询瞬时入洗量
     *
     * @param subscCardTypeDO
     * @return
     */
    CardDataDTO getInstantaneousWash(SubscCardTypeDO subscCardTypeDO);

    /**
     * 查询瞬时产品量
     *
     * @param subscCardTypeDO
     * @return
     */
    CardDataDTO getInstantaneousProductQuantity(SubscCardTypeDO subscCardTypeDO);


    /**
     * 查询产品产率
     *
     * @param subscCardTypeDO
     * @return
     */
    CardDataDTO getProductYield(SubscCardTypeDO subscCardTypeDO);


    /**
     * 查询化验数据
     *
     * @param subscCardTypeDO
     * @return
     */
    CardDataDTO getChemicalTestsData(SubscCardTypeDO subscCardTypeDO);


    /**
     * 查询智能压滤
     *
     * @param subscCardTypeDO
     * @return
     */
    CardDataDTO getIntelligentFilter(SubscCardTypeDO subscCardTypeDO);

    /**
     * 查询智能鼓风
     *
     * @param subscCardTypeDO
     * @return
     */
    CardDataDTO getIntelligentBlower(SubscCardTypeDO subscCardTypeDO);


    /**
     * 判断卡片类型数据
     *
     * @param cardDataDTOS
     * @param subscCardTypeDO
     */
    void switchCardType(List<CardDataDTO> cardDataDTOS, SubscCardTypeDO subscCardTypeDO);


    /**
     * 根据卡片编码查询卡片信息
     *
     * @param cardCode
     * @return
     */
    SubscCardTypeDO getCardTypeByCardCode(String cardCode);


    /**
     * 获取该用户所有权限的卡片
     *
     * @param userUuid
     * @return
     */
    List<SubscCardTypeDO> getCardTypeByUuid(String userUuid);

    /**
     * 获取该用户订阅的卡片
     *
     * @param userUuid
     * @return
     */
    List<SFSubscriptionCard> getSubCardTypeByUuid(String userUuid);

}
