package com.zgiot.app.server.module.sfsubsc.service.impl;

import com.zgiot.app.server.module.sfsubsc.entity.dto.CardDataDTO;
import com.zgiot.app.server.module.sfsubsc.mapper.SFSubscriptionCardMapper;
import com.zgiot.app.server.module.sfsubsc.service.SFSubscriptionCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SFSubscriptionCardServiceImpl implements SFSubscriptionCardService {

    @Autowired
    private SFSubscriptionCardMapper sfSubscriptionCardMapper;

    @Override
    public void updateSubCard(List<CardDataDTO> cardDataDTOList) {
        if(cardDataDTOList != null && ! cardDataDTOList.isEmpty()){
            for (CardDataDTO cardDataDTO:cardDataDTOList) {
                sfSubscriptionCardMapper.updateSubCard(cardDataDTO);
            }
        }
    }

}
