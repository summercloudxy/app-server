package com.zgiot.app.server.module.sfsubsc.mapper;

import com.zgiot.app.server.module.sfsubsc.pojo.SubscriptionCardSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jys
 * 卡片配置
 */
@Mapper
public interface SubscriptionCardSettingMapper {
    /**
     * 查询所有的订阅开篇配置
     *
     * @return
     */
    @Select("select * from tb_sfsubscription_card_setting")
    List<SubscriptionCardSetting> getAllSubscriptionCardSetting();
}
