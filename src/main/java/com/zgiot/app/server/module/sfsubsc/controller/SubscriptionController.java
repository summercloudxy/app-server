package com.zgiot.app.server.module.sfsubsc.controller;

import com.zgiot.app.server.module.sfsubsc.dto.CardData;
import com.zgiot.app.server.module.sfsubsc.enums.CardTypeEnum;
import com.zgiot.app.server.module.sfsubsc.pojo.SubscriptionCardSetting;
import com.zgiot.app.server.module.sfsubsc.service.SubscriptionCardService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jys
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfsubsc")
public class SubscriptionController {
    @Autowired
    private SubscriptionCardService subscriptionCardService;


    @RequestMapping(value = "/getSubscriptionData", method = RequestMethod.GET)
    public ResponseEntity<String> getSubscriptionData() {
        List<CardData> cardDatas = new ArrayList<>();
        List<SubscriptionCardSetting> allSubscriptionCardSettings = subscriptionCardService.getAllSubscriptionCardSetting();
        for (SubscriptionCardSetting subscriptionCardSetting : allSubscriptionCardSettings) {
            CardData cardData = new CardData();
            if (subscriptionCardSetting.getCardCode().equals(CardTypeEnum.HISTORYWASHINGQUANTITY_ONE.getCardCode())) {
                cardData = subscriptionCardService.getHistoryWashingQuantity(subscriptionCardSetting);
                cardDatas.add(cardData);
            }
            return new ResponseEntity<>(ServerResponse.buildOkJson(cardDatas), HttpStatus.OK);

        }
        return null;

    }


}
