package com.zgiot.app.server.module.sfsubsc.mapper;

import com.zgiot.app.server.module.sfsubsc.entity.dto.CardDataDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SFSubscriptionCardMapper {

    @Update("update tb_sfsubscription_card set card_data = #{cardData} where card_code = #{cardCode}")
    void updateSubCard(CardDataDTO cardDataDTO);

}
