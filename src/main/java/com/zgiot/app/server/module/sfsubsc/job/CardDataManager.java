package com.zgiot.app.server.module.sfsubsc.job;

import com.zgiot.app.server.module.sfsubsc.entity.dto.CardDataDTO;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardTypeDO;
import com.zgiot.app.server.module.sfsubsc.enums.CardTypeEnum;
import com.zgiot.app.server.module.sfsubsc.service.SFSubscriptionCardService;
import com.zgiot.app.server.module.sfsubsc.service.SubscCardTypeService;
import com.zgiot.app.server.module.sfsubsc.service.feign.CloudServerFeignClient;
import com.zgiot.common.restcontroller.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡片定时任务
 *
 * @author jys
 */
@Component
public class CardDataManager {

    private static final Logger logger = LoggerFactory.getLogger(CardDataManager.class);
    //注意token后 有空格
    private final static String AUTHORIZATION_PRIFIX = "Bearer ";

    @Value("${cloud.serviceaccount.token}")
    private String authorization;

    @Value("${cloud.service.url}")
    private String cloudServiceUrl;
    @Value("${cloud.service.path}")
    private String cloudServerPath;


    @Autowired
    private CloudServerFeignClient cloudServerFeignClient;
    @Autowired
    private SubscCardTypeService subscCardTypeService;
    @Autowired
    private SFSubscriptionCardService sfSubscriptionCardService;

    /**
     * 历史入洗量，历史产品量，仓位信息，化验数据 10s刷新
     */
    public void getHistoryCardDatas() {
        logger.debug("卡片历史数据开始上传");
        List<CardDataDTO> cardDataDTOS = new ArrayList<>();
        List<SubscCardTypeDO> subscCardTypeDOS = subscCardTypeService.getAllSubscCardTypes();
        for (SubscCardTypeDO subscCardTypeDO : subscCardTypeDOS) {
            CardDataDTO cardDataDTO;
            if (subscCardTypeDO.getCardType().equals(CardTypeEnum.HISTORICAL_WASHING_CAPACITY.getCardCode())) {
                cardDataDTO = subscCardTypeService.getHistoricalWashingCapacity(subscCardTypeDO);
                cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.HISTORICAL_PRODUCT_RATE.getCardCode())) {
                cardDataDTO = subscCardTypeService.getHistoricalProductRate(subscCardTypeDO);
                cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.CHEMICAL_TESTS_DATA.getCardCode())) {
                cardDataDTO = subscCardTypeService.getChemicalTestsData(subscCardTypeDO);
                cardDataDTOS.add(cardDataDTO);
            }
        }
        logger.info("开始卡片历史10S数据上传CloudServer");
        try {
            ServerResponse serverResponse = cloudServerFeignClient.saveAllCardDatas(cardDataDTOS, AUTHORIZATION_PRIFIX + authorization);
            logger.info("10s,clouserver的请求响应：" + serverResponse.getCode());
            if (serverResponse.getCode() == 0) {
                logger.info("10s数据，卡片历史数据上传CloudServer完成");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("10s数据，连接cloudserver异常.URL:" + cloudServiceUrl + cloudServerPath);

        }

        logger.info("开始卡片历史10S数据同步AppServer");
        sfSubscriptionCardService.updateSubCard(cardDataDTOS);

    }

    /**
     * 其他类型卡片数据 5s刷新
     */
    public void getCardDatas() {
        logger.debug("卡片实时数据开始上传");
        List<CardDataDTO> cardDataDTOS = new ArrayList<>();
        List<SubscCardTypeDO> subscCardTypeDOS = subscCardTypeService.getAllSubscCardTypes();
        for (SubscCardTypeDO subscCardTypeDO : subscCardTypeDOS) {
            CardDataDTO cardDataDTO;
            if (subscCardTypeDO.getCardType().equals(CardTypeEnum.INSTANTANEOUS_WASH.getCardCode())) {
                cardDataDTO = subscCardTypeService.getInstantaneousWash(subscCardTypeDO);
                cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.INSTANTANEOUS_PRODUCT_QUANTITY.getCardCode())) {
                cardDataDTO = subscCardTypeService.getInstantaneousProductQuantity(subscCardTypeDO);
                cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.PRODUCT_YIELD.getCardCode())) {
                cardDataDTO = subscCardTypeService.getProductYield(subscCardTypeDO);
                cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.INTELLIGENT_FILTER.getCardCode())) {
                cardDataDTO = subscCardTypeService.getIntelligentFilter(subscCardTypeDO);
                cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.INTELLIGENT_BLOWER.getCardCode())) {
                cardDataDTO = subscCardTypeService.getIntelligentBlower(subscCardTypeDO);
                cardDataDTOS.add(cardDataDTO);
            } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.MIXTURE_OF_RAW_COAL.getCardCode())) {
                cardDataDTO = subscCardTypeService.getMixtureOfRawCoal(subscCardTypeDO);
                cardDataDTOS.add(cardDataDTO);
            }
        }
        logger.info("开始卡片实时5S数据上传CloudServer");
        try {
            ServerResponse serverResponse = cloudServerFeignClient.saveAllCardDatas(cardDataDTOS, AUTHORIZATION_PRIFIX + authorization);
            if (serverResponse.getCode() == 0) {
                logger.info("5s,clouserver的请求响应：" + serverResponse.getCode());
                logger.info("5s卡片实时数据上传CloudServer完成");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("5s连接cloudserver异常.URL:" + cloudServiceUrl + cloudServerPath);
        }

        logger.info("开始卡片实时5S数据同步AppServer");
        sfSubscriptionCardService.updateSubCard(cardDataDTOS);

    }


}
