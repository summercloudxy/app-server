package com.zgiot.app.server.module.sfsubsc.service;

import com.zgiot.app.server.module.sfsubsc.entity.dto.CardDataDTO;
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
    CardDataDTO getHistoryWashingQuantity(SubscCardTypeDO subscCardTypeDO);

    /**
     * 查询所有的卡片数据
     *
     * @return
     */
    void getAllCardDatas();


}
