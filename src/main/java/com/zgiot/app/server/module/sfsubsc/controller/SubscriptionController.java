package com.zgiot.app.server.module.sfsubsc.controller;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.sfsubsc.entity.dto.CardDataBeanDTO;
import com.zgiot.app.server.module.sfsubsc.entity.dto.CardDataDTO;
import com.zgiot.app.server.module.sfsubsc.entity.dto.CardSubDTO;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.RelUserSfsubscription;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.SFSubscriptionCard;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardTypeDO;
import com.zgiot.app.server.module.sfsubsc.entity.vo.*;
import com.zgiot.app.server.module.sfsubsc.enums.CardTypeEnum;
import com.zgiot.app.server.module.sfsubsc.job.CardDataManager;
import com.zgiot.app.server.module.sfsubsc.service.RelUserSfsubscriptionService;
import com.zgiot.app.server.module.sfsubsc.service.SubscCardTypeService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private RelUserSfsubscriptionService relUserCardService;
    @Autowired
    private CardDataManager cardDataManager;

    /**
     * 获取所有卡片数据
     *
     * @return
     */
    @RequestMapping(value = "/getSubscriptionData", method = RequestMethod.GET)
    public ResponseEntity<String> getSubscriptionData() {
        List<CardDataDTO> cardDataDTOS = new ArrayList<>();
        List<SubscCardTypeDO> subscCardTypeDOS = subscCardTypeService.getAllSubscCardTypes();
        for (SubscCardTypeDO subscCardTypeDO : subscCardTypeDOS) {
            subscCardTypeService.switchCardType(cardDataDTOS, subscCardTypeDO);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(cardDataDTOS), HttpStatus.OK);

    }

    /**
     * 获取该用户所有权限的卡片信息
     *
     * @param userUuid
     * @return
     */
    @RequestMapping(value = "/getCardTypeByUuid/{userUuid}", method = RequestMethod.GET)
    public ResponseEntity<String> getCardTypeByUuid(@PathVariable("userUuid") String userUuid) {
        List<SubscCardTypeDO> subscCardTypeDOS = subscCardTypeService.getCardTypeByUuid(userUuid);
        return new ResponseEntity<>(ServerResponse.buildOkJson(subscCardTypeDOS), HttpStatus.OK);
    }

    /**
     * 获取该用户订阅的卡片
     *
     * @param userUuid
     * @return
     */
    @RequestMapping(value = "/getSubCardTypeByUuid/{userUuid}", method = RequestMethod.GET)
    public ResponseEntity<String> getSubCardTypeByUuid(@PathVariable("userUuid") String userUuid) {
        List<SFSubscriptionCard> sfSubscriptionCardList = subscCardTypeService.getSubCardTypeByUuid(userUuid);

        List<Object> list = new ArrayList<>();
        for (SFSubscriptionCard SFSubscriptionCard : sfSubscriptionCardList) {
            String cardType = SFSubscriptionCard.getCardType();
            String cardCode = SFSubscriptionCard.getCardCode();
            if (CardTypeEnum.HISTORICAL_WASHING_CAPACITY.getCardCode().equals(cardType)) {
                HistoryWashingQuantityVO historyWashingQuantityVO =
                        JSON.parseObject(SFSubscriptionCard.getCardData(), HistoryWashingQuantityVO.class);
                list.add(new CardDataBeanDTO(cardCode, historyWashingQuantityVO));
            } else if (CardTypeEnum.HISTORICAL_PRODUCT_RATE.getCardCode().equals(cardType)) {
                HistoricalProductRateVO historicalProductRateVO =
                        JSON.parseObject(SFSubscriptionCard.getCardData(), HistoricalProductRateVO.class);
                list.add(new CardDataBeanDTO(cardCode, historicalProductRateVO));
            } else if (CardTypeEnum.CHEMICAL_TESTS_DATA.getCardCode().equals(cardType)) {
                ChemicalTestsDataVO chemicalTestsDataVO =
                        JSON.parseObject(SFSubscriptionCard.getCardData(), ChemicalTestsDataVO.class);
                list.add(new CardDataBeanDTO(cardCode, chemicalTestsDataVO));
            } else if (CardTypeEnum.INSTANTANEOUS_WASH.getCardCode().equals(cardType)) {
                InstantaneousWashVo instantaneousWashVo =
                        JSON.parseObject(SFSubscriptionCard.getCardData(), InstantaneousWashVo.class);
                list.add(new CardDataBeanDTO(cardCode, instantaneousWashVo));
            } else if (CardTypeEnum.INSTANTANEOUS_PRODUCT_QUANTITY.getCardCode().equals(cardType)) {
                InstantaneousProductQuantityVO instantaneousProductQuantityVO =
                        JSON.parseObject(SFSubscriptionCard.getCardData(), InstantaneousProductQuantityVO.class);
                list.add(new CardDataBeanDTO(cardCode, instantaneousProductQuantityVO));
            } else if (CardTypeEnum.PRODUCT_YIELD.getCardCode().equals(cardType)) {
                ProductYieldVO productYieldVO =
                        JSON.parseObject(SFSubscriptionCard.getCardData(), ProductYieldVO.class);
                list.add(new CardDataBeanDTO(cardCode, productYieldVO));
            } else if (CardTypeEnum.INTELLIGENT_FILTER.getCardCode().equals(cardType)) {
                IntelligentFilterVO intelligentFilterVO =
                        JSON.parseObject(SFSubscriptionCard.getCardData(), IntelligentFilterVO.class);
                list.add(new CardDataBeanDTO(cardCode, intelligentFilterVO));
            } else if (CardTypeEnum.INTELLIGENT_BLOWER.getCardCode().equals(cardType)) {
                IntelligentBlowerVO intelligentBlowerVO =
                        JSON.parseObject(SFSubscriptionCard.getCardData(), IntelligentBlowerVO.class);
                list.add(new CardDataBeanDTO(cardCode, intelligentBlowerVO));
            } else if (CardTypeEnum.MIXTURE_OF_RAW_COAL.getCardCode().equals(cardType)) {
                MixtureOfRawCoalVO mixtureOfRawCoalVO =
                        JSON.parseObject(SFSubscriptionCard.getCardData(), MixtureOfRawCoalVO.class);
                list.add(new CardDataBeanDTO(cardCode, mixtureOfRawCoalVO));
            } else if (CardTypeEnum.COAL_QUALITY.getCardCode().equals(cardType)) {
                CoalQualityVO coalQualityVO =
                        JSON.parseObject(SFSubscriptionCard.getCardData(), CoalQualityVO.class);
                list.add(new CardDataBeanDTO(cardCode, coalQualityVO));
            } else if (CardTypeEnum.PRODUCTION.getCardCode().equals(cardType)) {
                ProductionVO productionVO =
                        JSON.parseObject(SFSubscriptionCard.getCardData(), ProductionVO.class);
                list.add(new CardDataBeanDTO(cardCode, productionVO));
            }
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(list), HttpStatus.OK);
    }

    /**
     * 修改用户订阅卡片
     *
     * @param bodyStr
     * @return
     */
    @RequestMapping(value = "/userSub", method = RequestMethod.POST)
    public ResponseEntity<String> updateRelUserCard(@RequestBody String bodyStr) {
        CardSubDTO cardSubDTO = JSON.parseObject(bodyStr, CardSubDTO.class);
        relUserCardService.updateRelUserCard(cardSubDTO);

        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 用户卡片置顶
     *
     * @param bodyStr
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.POST)
    public ResponseEntity<String> top(@RequestBody String bodyStr) {
        RelUserSfsubscription relUserSfsubscription = JSON.parseObject(bodyStr, RelUserSfsubscription.class);
        relUserCardService.top(relUserSfsubscription);

        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 卡片job
     *
     * @return
     */
    @RequestMapping(value = "/getProductionCardDatas", method = RequestMethod.GET)
    public ResponseEntity<String> getProductionCardDatas() {
        cardDataManager.getProductionCardDatas();

        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

}
