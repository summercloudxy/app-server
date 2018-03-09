package com.zgiot.app.server.module.sfsubsc.controller;

import com.zgiot.app.server.module.sfsubsc.entity.dto.CardDataDTO;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardTypeDO;
import com.zgiot.app.server.module.sfsubsc.enums.CardTypeEnum;
import com.zgiot.app.server.module.sfsubsc.service.SubscCardTypeService;
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
    private SubscCardTypeService subscCardTypeService;


    @RequestMapping(value = "/getSubscriptionData", method = RequestMethod.GET)
    public ResponseEntity<String> getSubscriptionData() {
        List<CardDataDTO> cardDataDTOS = new ArrayList<>();
        List<SubscCardTypeDO> subscCardTypeDOS = subscCardTypeService.getAllSubscCardTypes();
        for (SubscCardTypeDO subscCardTypeDO : subscCardTypeDOS) {
            CardDataDTO cardDataDTO = new CardDataDTO();
            if (subscCardTypeDO.getCardCode().equals(CardTypeEnum.HISTORYWASHINGQUANTITY_ONE.getCardCode())) {
                cardDataDTO = subscCardTypeService.getHistoryWashingQuantity(subscCardTypeDO);
                cardDataDTOS.add(cardDataDTO);
            }
            return new ResponseEntity<>(ServerResponse.buildOkJson(cardDataDTOS), HttpStatus.OK);

        }
        return null;

    }


}
