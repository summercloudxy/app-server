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
            if (subscCardTypeDO.getCardType().equals(CardTypeEnum.HISTORICAL_WASHING_CAPACITY.getCardCode())) {
                cardDataDTO = subscCardTypeService.getHistoricalWashingCapacity(subscCardTypeDO);
                //cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.HISTORICAL_PRODUCT_RATE.getCardCode())) {

                cardDataDTO = subscCardTypeService.getHistoricalProductRate(subscCardTypeDO);
                //cardDataDTOS.add(cardDataDTO);

            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.INSTANTANEOUS_WASH.getCardCode())) {
                cardDataDTO = subscCardTypeService.getInstantaneousWash(subscCardTypeDO);
                // cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.INSTANTANEOUS_PRODUCT_QUANTITY.getCardCode())) {
                cardDataDTO = subscCardTypeService.getInstantaneousProductQuantity(subscCardTypeDO);
                //cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.PRODUCT_YIELD.getCardCode())) {
                cardDataDTO = subscCardTypeService.getProductYield(subscCardTypeDO);
                //cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.INTELLIGENT_FILTER.getCardCode())) {
                cardDataDTO = subscCardTypeService.getIntelligentFilter(subscCardTypeDO);
                //cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.INTELLIGENT_BLOWER.getCardCode())) {
                cardDataDTO = subscCardTypeService.getIntelligentBlower(subscCardTypeDO);
                //cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.MIXTURE_OF_RAW_COAL.getCardCode())) {
                cardDataDTO = subscCardTypeService.getMixtureOfRawCoal(subscCardTypeDO);
                //cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.CHEMICAL_TESTS_DATA.getCardCode())) {
                cardDataDTO = subscCardTypeService.getChemicalTestsData(subscCardTypeDO);
                cardDataDTOS.add(cardDataDTO);
            }

        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(cardDataDTOS), HttpStatus.OK);

    }


}
