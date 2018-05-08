package com.zgiot.app.server.module.sfsubsc.service.impl;

import com.zgiot.app.server.module.sfsubsc.constants.SFSubscConstant;
import com.zgiot.app.server.module.sfsubsc.entity.dto.CardSubDTO;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.RelUserSfsubscription;
import com.zgiot.app.server.module.sfsubsc.mapper.RelUserSfsubscriptionMapper;
import com.zgiot.app.server.module.sfsubsc.service.RelUserSfsubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RelUserSfsubscriptionServiceImpl implements RelUserSfsubscriptionService {

    @Autowired
    private RelUserSfsubscriptionMapper relUserCardMapper;

    @Transactional
    public void updateRelUserCard(CardSubDTO cardSubDTO) {
        relUserCardMapper.deleteRelUserSfsubscription(cardSubDTO.getUserUuid(), SFSubscConstant.PAD_CLIENT_ID);
        List<String> cardCodeList = cardSubDTO.getCardCodeList();
        if (cardCodeList != null && !cardCodeList.isEmpty()) {
            for (int i = 0; i < cardCodeList.size(); i++) {
                RelUserSfsubscription relUserSfsubscription = new RelUserSfsubscription();
                relUserSfsubscription.setClientId(SFSubscConstant.PAD_CLIENT_ID);
                relUserSfsubscription.setCardCode(cardCodeList.get(i));
                relUserSfsubscription.setSort(i + 1);
                relUserSfsubscription.setUserUuid(cardSubDTO.getUserUuid());
                relUserCardMapper.addRelUserSfsubscription(relUserSfsubscription);
            }
        }
    }

    @Transactional
    public void top(RelUserSfsubscription relUserSfsubscription) {
        String userUuid = relUserSfsubscription.getUserUuid();
        String cardCode = relUserSfsubscription.getCardCode();
        List<RelUserSfsubscription> relUserSfsubscriptionList = relUserCardMapper.getTop(userUuid, cardCode);
        relUserCardMapper.deleteRelUserSfsubscription(userUuid, SFSubscConstant.PAD_CLIENT_ID);
        List<RelUserSfsubscription> newList = new ArrayList<>();
        newList.add(new RelUserSfsubscription(SFSubscConstant.PAD_CLIENT_ID, cardCode, 1, userUuid));
        if (relUserSfsubscriptionList != null && !relUserSfsubscriptionList.isEmpty()) {
            for (int i = 0; i < relUserSfsubscriptionList.size(); i++) {
                RelUserSfsubscription userSub = relUserSfsubscriptionList.get(i);
                userSub.setSort(i + 2);
                newList.add(userSub);
            }
        }

        for (RelUserSfsubscription userSub : newList) {
            relUserCardMapper.addRelUserSfsubscription(userSub);
        }
    }

}
