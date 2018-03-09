package com.zgiot.app.server.module.sfsubsc.service.impl;

import com.zgiot.app.server.module.sfsubsc.entity.dto.CardDataDTO;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardTypeDO;
import com.zgiot.app.server.module.sfsubsc.entity.vo.HistoryWashingQuantityVO;
import com.zgiot.app.server.module.sfsubsc.enums.CardTypeEnum;
import com.zgiot.app.server.module.sfsubsc.mapper.SubscCardTypeMapper;
import com.zgiot.app.server.module.sfsubsc.service.SubscCardTypeService;
import com.zgiot.app.server.module.sfsubsc.service.client.CloudServerClient;
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
public class SubscriptionCardServiceImpl implements SubscCardTypeService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionCardServiceImpl.class);


    @Value("${cloud.serviceaccount.token}")
    private String authorization;
    @Value("${cloud.service.ip}")
    private String authServiceIp;


    @Autowired
    private SubscCardTypeMapper subscCardTypeMapper;

    @Autowired
    private DataService dataService;


    @Override
    public List<SubscCardTypeDO> getAllSubscCardTypes() {
        return subscCardTypeMapper.getAllSubscCardTypes();
    }

    @Override
    public CardDataDTO getHistoryWashingQuantity(SubscCardTypeDO subscCardTypeDO) {
        CardDataDTO cardDataDTO = new CardDataDTO();
        HistoryWashingQuantityVO historyWashingQuantityVO = new HistoryWashingQuantityVO();
        historyWashingQuantityVO.setCardTitle(subscCardTypeDO.getCardName());
        String[] cardParamValues = subscCardTypeDO.getCardParamValue().split(",");
        historyWashingQuantityVO.setLegend(cardParamValues);
        List<HistoryWashingQuantityVO.MetricData> metricDatas = new ArrayList<>();
        String[] metricTypes = {MetricCodes.CT_C, MetricCodes.CT_D, MetricCodes.CT_M, MetricCodes.CT_Y, MetricCodes.CT_T};
        for (String metrictype : metricTypes) {
            HistoryWashingQuantityVO.MetricData metricData = historyWashingQuantityVO.new MetricData();
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
        historyWashingQuantityVO.setMetricDatas(metricDatas);
        cardDataDTO.setCardCode(CardTypeEnum.HISTORYWASHINGQUANTITY_ONE.getCardCode());
        cardDataDTO.setCardData(historyWashingQuantityVO);
        return cardDataDTO;
    }

    @Override
    public void getAllCardDatas() {
        List<CardDataDTO> cardDataDTOS = new ArrayList<>();
        List<SubscCardTypeDO> subscCardTypeDOS = getAllSubscCardTypes();
        for (SubscCardTypeDO subscCardTypeDO : subscCardTypeDOS) {
            CardDataDTO cardDataDTO = new CardDataDTO();
            // TODO 其他卡片数据
            if (subscCardTypeDO.getCardCode().equals(CardTypeEnum.HISTORYWASHINGQUANTITY_ONE.getCardCode())) {
                cardDataDTO = getHistoryWashingQuantity(subscCardTypeDO);
                cardDataDTOS.add(cardDataDTO);
            }
        }

        CloudServerClient cloudServerClient = Feign.builder().encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder()).requestInterceptor(new RequestInterceptor() {
                    @Override
                    public void apply(RequestTemplate requestTemplate) {
                        requestTemplate.header("Authorization", authorization);
                    }
                })
                .target(CloudServerClient.class,
                        authServiceIp);
        ServerResponse serverResponse = cloudServerClient.saveAllCardDatas(cardDataDTOS);
        if (serverResponse.getCode() == 0) {
            logger.debug("卡片数据上传CloudServer完成");
        }


    }
}
