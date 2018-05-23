package com.zgiot.app.server.module.sfsubsc.service;

import com.zgiot.app.server.module.sfsubsc.entity.dto.CardSubDTO;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.RelUserSfsubscription;

public interface RelUserSfsubscriptionService {

    /**
     * 修改用户订阅卡片
     *
     * @param cardSubDTO
     */
    void updateRelUserCard(CardSubDTO cardSubDTO);

    /**
     * 用户卡片置顶
     *
     * @param relUserSfsubscription
     */
    void top(RelUserSfsubscription relUserSfsubscription);

}
