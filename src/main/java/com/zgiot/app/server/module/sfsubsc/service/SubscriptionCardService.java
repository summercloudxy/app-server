package com.zgiot.app.server.module.sfsubsc.service;

import com.zgiot.app.server.module.sfsubsc.dto.CardData;
import com.zgiot.app.server.module.sfsubsc.pojo.SubscriptionCardSetting;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jys
 * 卡片配置
 */
@Component
public interface SubscriptionCardService {
    /**
     * 查询用户订阅的卡片
     *
     * @return
     */
    List<SubscriptionCardSetting> getAllSubscriptionCardSetting();

    /**
     * 查询历史入洗量指标数据
     *
     * @param subscriptionCardSetting
     * @return
     */
    CardData getHistoryWashingQuantity(SubscriptionCardSetting subscriptionCardSetting);

    /**
     * 查询所有的卡片数据
     *
     * @return
     */
    void getAllCardDatas();


}
