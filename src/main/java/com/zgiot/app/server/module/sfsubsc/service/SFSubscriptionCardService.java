package com.zgiot.app.server.module.sfsubsc.service;

import com.zgiot.app.server.module.sfsubsc.entity.dto.CardDataDTO;

import java.util.List;

public interface SFSubscriptionCardService {

    void updateSubCard(List<CardDataDTO> cardDataDTOList);

}
