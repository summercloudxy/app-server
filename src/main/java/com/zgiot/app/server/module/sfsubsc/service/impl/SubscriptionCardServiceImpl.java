package com.zgiot.app.server.module.sfsubsc.service.impl;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.sfsubsc.client.CloudServerClient;
import com.zgiot.app.server.module.sfsubsc.dto.CardData;
import com.zgiot.app.server.module.sfsubsc.enums.CardTypeEnum;
import com.zgiot.app.server.module.sfsubsc.mapper.SubscriptionCardSettingMapper;
import com.zgiot.app.server.module.sfsubsc.pojo.SubscriptionCardSetting;
import com.zgiot.app.server.module.sfsubsc.service.SubscriptionCardService;
import com.zgiot.app.server.module.sfsubsc.vo.HistoryWashingQuantity;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.restcontroller.ServerResponse;
import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author
 */
@Service
public class SubscriptionCardServiceImpl implements SubscriptionCardService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionCardServiceImpl.class);


    @Value("${cloud.serviceaccount.token}")
    private String authorization;
    @Value("${cloud.service.ip}")
    private String authServiceIp;


    @Autowired
    private SubscriptionCardSettingMapper subscriptionCardSettingMapper;

    @Autowired
    private DataService dataService;

    @Override
    public List<SubscriptionCardSetting> getAllSubscriptionCardSetting() {
        return subscriptionCardSettingMapper.getAllSubscriptionCardSetting();
    }

    @Override
    public CardData getHistoryWashingQuantity(SubscriptionCardSetting subscriptionCardSetting) {
        CardData cardData = new CardData();
        HistoryWashingQuantity historyWashingQuantity = new HistoryWashingQuantity();
        historyWashingQuantity.setCardTitle(subscriptionCardSetting.getCardName());

        String[] cardParamValues = subscriptionCardSetting.getCardParamValue().split(",");
        historyWashingQuantity.setLegend(cardParamValues);
        List<HistoryWashingQuantity.MetricData> metricDatas = new ArrayList<>();
        String[] metricTypes = {MetricCodes.CT_C, MetricCodes.CT_D, MetricCodes.CT_M, MetricCodes.CT_Y, MetricCodes.CT_T};
        for (String metrictype : metricTypes) {
            HistoryWashingQuantity.MetricData metricData = historyWashingQuantity.new MetricData();
            switch (metrictype) {
                case MetricCodes.CT_C:
                    metricData.setMetricIndex("班");
                    break;
                case MetricCodes.CT_D:
                    metricData.setMetricIndex("日");
                    break;
                case MetricCodes.CT_M:
                    metricData.setMetricIndex("月");
                    break;
                case MetricCodes.CT_Y:
                    metricData.setMetricIndex("年");
                    break;
                case MetricCodes.CT_T:
                    metricData.setMetricIndex("总");
                    break;
                default:
                    metricData.setMetricIndex("");
                    break;
            }

            Optional<DataModelWrapper> firstMetirc = dataService.getData(cardParamValues[0], metrictype);

            if (firstMetirc.isPresent()) {
                metricData.setFirstMetricValue(firstMetirc.get().getValue());
            } else {
                metricData.setFirstMetricValue("");
            }

            Optional<DataModelWrapper> secondMetirc = dataService.getData(cardParamValues[1], metrictype);

            if (secondMetirc.isPresent()) {
                metricData.setSecondMetricValue(secondMetirc.get().getValue());
            } else {
                metricData.setSecondMetricValue("");
            }


            if (StringUtils.isNotBlank(metricData.getFirstMetricValue()) && StringUtils.isNotBlank(metricData.getSecondMetricValue())) {
                BigDecimal totalValue = new BigDecimal(metricData.getFirstMetricValue()).add(new BigDecimal(metricData.getSecondMetricValue()));
                BigDecimal fistValue = new BigDecimal(metricData.getFirstMetricValue()).divide(totalValue, 3, BigDecimal.ROUND_HALF_UP);
                metricData.setFirstMetricPecent((fistValue).multiply(new BigDecimal(100)).setScale(1) + "%");
                BigDecimal secondvalue = (new BigDecimal(metricData.getSecondMetricValue()).divide(totalValue, 3, BigDecimal.ROUND_HALF_UP));
                metricData.setSecondMetricPecent(secondvalue.multiply(new BigDecimal(100)).setScale(1) + "%");
            } else if (StringUtils.isNotBlank(metricData.getFirstMetricValue()) && StringUtils.isBlank(metricData.getSecondMetricValue())) {
                metricData.setFirstMetricPecent("100%");
            } else if (StringUtils.isBlank(metricData.getFirstMetricValue()) && StringUtils.isNotBlank(metricData.getSecondMetricValue())) {
                metricData.setSecondMetricPecent("100%");
            } else {
                metricData.setSecondMetricPecent("");
                metricData.setFirstMetricValue("");
            }
            metricDatas.add(metricData);
        }
        historyWashingQuantity.setMetricDatas(metricDatas);
        cardData.setCardCode(CardTypeEnum.HISTORYWASHINGQUANTITY_ONE.getCardCode());
        cardData.setCardData(JSON.toJSONString(historyWashingQuantity).replace("\"", "'"));
        //.replace("\"", "'"));
        return cardData;
    }

    @Override
    public void getAllCardDatas() {
        List<CardData> cardDatas = new ArrayList<>();
        List<SubscriptionCardSetting> allSubscriptionCardSettings = getAllSubscriptionCardSetting();
        for (SubscriptionCardSetting subscriptionCardSetting : allSubscriptionCardSettings) {
            CardData cardData = new CardData();
            if (subscriptionCardSetting.getCardCode().equals(CardTypeEnum.HISTORYWASHINGQUANTITY_ONE.getCardCode())) {
                cardData = getHistoryWashingQuantity(subscriptionCardSetting);
                cardDatas.add(cardData);
            }
        }

        CloudServerClient cloudServerClient = Feign.builder().encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder()).requestInterceptor(new RequestInterceptor() {
                    @Override
                    public void apply(RequestTemplate requestTemplate) {
                        requestTemplate.header("Authorization", authorization);

                    }
                })
                //.logger(new Logger.JavaLogger().appendToFile("D:/logs/http.log"))
                //.logLevel(Logger.Level.FULL)
                .target(CloudServerClient.class,
                        authServiceIp);
        ServerResponse serverResponse = cloudServerClient.saveAllCardDatas(cardDatas);
        if (serverResponse.getCode() == 0) {
            logger.debug("卡片数据上传CloudServer完成");
        }


    }
}
