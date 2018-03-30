package com.zgiot.app.server.module.sfsubsc.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zgiot.app.server.module.coalanalysis.mapper.CoalAnalysisMapper;
import com.zgiot.app.server.module.sfsubsc.entity.dto.CardDataDTO;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardTypeDO;
import com.zgiot.app.server.module.sfsubsc.entity.vo.*;
import com.zgiot.app.server.module.sfsubsc.enums.CardTypeEnum;
import com.zgiot.app.server.module.sfsubsc.mapper.SubscCardTypeMapper;
import com.zgiot.app.server.module.sfsubsc.service.SubscCardTypeService;
import com.zgiot.app.server.module.sfsubsc.util.MetricValueUtil;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.constants.*;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author
 */
@Service
public class SubscriptionCardServiceImpl implements SubscCardTypeService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionCardServiceImpl.class);


    @Autowired
    private SubscCardTypeMapper subscCardTypeMapper;

    @Autowired
    private DataService dataService;
    @Autowired
    private HistoryDataService historyDataService;
    @Autowired
    private CoalAnalysisMapper coalAnalysisMapper;


    @Override
    public List<SubscCardTypeDO> getAllSubscCardTypes() {
        return subscCardTypeMapper.getAllSubscCardTypes();
    }

    @Override
    public CardDataDTO getHistoricalWashingCapacity(SubscCardTypeDO subscCardTypeDO) {
        logger.debug("历史入洗量卡片：thingCode{}", subscCardTypeDO.getCardParamValue());
        CardDataDTO cardDataDTO = new CardDataDTO();
        HistoryWashingQuantityVO historyWashingQuantityVO = new HistoryWashingQuantityVO();
        historyWashingQuantityVO.setCardTitle(subscCardTypeDO.getCardName());
        String[] cardParamValues = subscCardTypeDO.getCardParamValue().split(",");
        historyWashingQuantityVO.setLegend(cardParamValues);
        List<HistoryWashingQuantityVO.MetricData> metricDatas = new ArrayList<>();
        String[] metricTypes = {MetricCodes.CT_C, MetricCodes.CT_D, MetricCodes.CT_M, MetricCodes.CT_Y, MetricCodes.CT_T};
        for (String metrictype : metricTypes) {
            HistoryWashingQuantityVO.MetricData metricData = historyWashingQuantityVO.new MetricData();
            //指标标题
            metricData.setMetricIndex(getMetricIndex(metrictype));
            //带煤量指标值
            getBeltMetricValueByCT(cardParamValues, metricDatas, metrictype, metricData);
        }
        historyWashingQuantityVO.setMetricDatas(metricDatas);
        cardDataDTO.setCardCode(subscCardTypeDO.getCardCode());
        cardDataDTO.setCardData(JSON.toJSONString(historyWashingQuantityVO));
        return cardDataDTO;
    }

    /**
     * 指标标题
     *
     * @param metrictype
     */
    private String getMetricIndex(String metrictype) {
        String metricIndex;
        switch (metrictype) {
            case MetricCodes.CT_C:
                metricIndex = SubscriptionConstants.METRIC_NAME_CT_C;
                break;
            case MetricCodes.CT_D:
                metricIndex = SubscriptionConstants.METRIC_NAME_CT_D;
                break;
            case MetricCodes.CT_M:
                metricIndex = SubscriptionConstants.METRIC_NAME_CT_M;
                break;
            case MetricCodes.CT_Y:
                metricIndex = SubscriptionConstants.METRIC_NAME_CT_Y;
                break;
            case MetricCodes.CT_T:
                metricIndex = SubscriptionConstants.METRIC_NAME_CT_T;
                break;
            default:
                metricIndex = "";
                break;
        }
        return metricIndex;
    }

    /**
     * 皮带带煤量指标值
     *
     * @param cardParamValues
     * @param metricDatas
     * @param metrictype
     * @param metricData
     */
    private void getBeltMetricValueByCT(String[] cardParamValues, List<HistoryWashingQuantityVO.MetricData> metricDatas, String metrictype, HistoryWashingQuantityVO.MetricData metricData) {
        String firstMetric = "";
        String secondMetric = "";
        Optional<DataModelWrapper> firstMetirc = dataService.getData(cardParamValues[0], metrictype);
        if (firstMetirc.isPresent()) {
            if (metrictype.equals(MetricCodes.CT_C) || metrictype.equals(MetricCodes.CT_D)) {
                firstMetric = firstMetirc.get().getValue();
                metricData.setFirstMetricValue(MetricValueUtil.formart(firstMetirc.get().getValue()) + SubscriptionConstants.UNIT_T);
            } else {
                BigDecimal metricValue = new BigDecimal(firstMetirc.get().getValue()).divide(new BigDecimal(10000), 3, BigDecimal.ROUND_HALF_UP);
                firstMetric = String.valueOf(metricValue);
                metricData.setFirstMetricValue(firstMetric + SubscriptionConstants.UNIT_THOUSAND_T);
            }
        } else {
            metricData.setFirstMetricValue("");
        }
        Optional<DataModelWrapper> secondMetirc = dataService.getData(cardParamValues[1], metrictype);
        if (secondMetirc.isPresent()) {
            if (metrictype.equals(MetricCodes.CT_C) || metrictype.equals(MetricCodes.CT_D)) {
                secondMetric = secondMetirc.get().getValue();
                metricData.setSecondMetricValue(MetricValueUtil.formart(secondMetirc.get().getValue()) + SubscriptionConstants.UNIT_T);
            } else {
                BigDecimal metricValue = new BigDecimal(secondMetirc.get().getValue()).divide(new BigDecimal(10000), 3, BigDecimal.ROUND_HALF_UP);
                secondMetric = String.valueOf(metricValue);
                metricData.setSecondMetricValue(firstMetric + SubscriptionConstants.UNIT_THOUSAND_T);
            }
        } else {
            metricData.setSecondMetricValue("");
        }
        if (StringUtils.isNotBlank(firstMetric) && StringUtils.isNotBlank(secondMetric)) {
            BigDecimal totalValue = new BigDecimal(firstMetric).add(new BigDecimal(secondMetric));
            BigDecimal fistPrecent = new BigDecimal(firstMetric).divide(totalValue, 3, BigDecimal.ROUND_HALF_UP);
            metricData.setFirstMetricPecent((fistPrecent).multiply(new BigDecimal(100)).setScale(1) + "%");
            BigDecimal secondPrecent = (new BigDecimal(secondMetric).divide(totalValue, 3, BigDecimal.ROUND_HALF_UP));
            metricData.setSecondMetricPecent(secondPrecent.multiply(new BigDecimal(100)).setScale(1) + "%");
        } else {
            metricData.setSecondMetricPecent("");
            metricData.setFirstMetricPecent("");
        }

        metricDatas.add(metricData);
    }

    @Override
    public CardDataDTO getHistoricalProductRate(SubscCardTypeDO subscCardTypeDO) {
        logger.debug("历史产品量卡片：thingCode{}", subscCardTypeDO.getCardParamValue());
        CardDataDTO cardDataDTO = new CardDataDTO();
        HistoricalProductRateVO historicalProductRateVO = new HistoricalProductRateVO();
        historicalProductRateVO.setCardTitle(subscCardTypeDO.getCardName());
        String[] cardParamValues = subscCardTypeDO.getCardParamValue().split(",");
        historicalProductRateVO.setLegend(new String[]{SubscriptionConstants.RAW_COAL, SubscriptionConstants.MIXED_COAL, SubscriptionConstants.WASTE_ROCK});

        List<HistoricalProductRateVO.MetricData> metricDataList = new ArrayList<>();
        String[] metricTypes = {MetricCodes.CT_C, MetricCodes.CT_D, MetricCodes.CT_M, MetricCodes.CT_Y, MetricCodes.CT_T};
        for (String metrictype : metricTypes) {
            HistoricalProductRateVO.MetricData metricData = historicalProductRateVO.new MetricData();
            metricData.setMetricIndex(getMetricIndex(metrictype));
            //历史产品量指标值


            String cleanCoalValue = getCleanCoalValue(dataService.getData(cardParamValues[0], metrictype), metrictype, metricData);
            String mixedCoalValue = getMixedCoalValue(dataService.getData(cardParamValues[1], metrictype), metrictype, metricData);
            String wasteRockValue = getWasteRockValue(cardParamValues[2], metrictype, metricData);


            if (StringUtils.isNotBlank(cleanCoalValue) && StringUtils.isNotBlank(mixedCoalValue) && StringUtils.isNotBlank(wasteRockValue)) {

                BigDecimal totalValue = new BigDecimal(cleanCoalValue).add(new BigDecimal(mixedCoalValue)).add(new BigDecimal(wasteRockValue));
                BigDecimal fistPrecent = new BigDecimal(cleanCoalValue).divide(totalValue, 3, BigDecimal.ROUND_HALF_UP);
                metricData.setCleanCoalPrecent((fistPrecent).multiply(new BigDecimal(100)).setScale(1) + "%");

                BigDecimal secondPrecent = new BigDecimal(mixedCoalValue).divide(totalValue, 3, BigDecimal.ROUND_HALF_UP);
                metricData.setMixedCoalPrecent((secondPrecent).multiply(new BigDecimal(100)).setScale(1) + "%");

                BigDecimal thirdPrecent = new BigDecimal(1.00).subtract(fistPrecent).subtract(secondPrecent);
                metricData.setWasteRockPrecent((thirdPrecent).multiply(new BigDecimal(100)).setScale(1) + "%");

            } else {
                metricData.setCleanCoalPrecent("");
                metricData.setMixedCoalPrecent("");
                metricData.setWasteRockPrecent("");
            }
            metricDataList.add(metricData);

        }
        historicalProductRateVO.setMetricDatas(metricDataList);
        cardDataDTO.setCardCode(subscCardTypeDO.getCardCode());
        cardDataDTO.setCardData(JSON.toJSONString(historicalProductRateVO));
        return cardDataDTO;
    }

    /**
     * 历史产品量 矸石指标数据
     *
     * @param wasteRocks
     * @param metrictype
     * @param metricData
     * @return
     */
    private String getWasteRockValue(String wasteRocks, String metrictype, HistoricalProductRateVO.MetricData metricData) {
        String[] wasteRockCodes = wasteRocks.split("\\+");
        BigDecimal wasteRockValue = BigDecimal.ZERO;
        for (String wasteRockCode : wasteRockCodes) {
            Optional<DataModelWrapper> thirdMetirc = dataService.getData(wasteRockCode, metrictype);
            if (thirdMetirc.isPresent()) {
                if (metrictype.equals(MetricCodes.CT_C) || metrictype.equals(MetricCodes.CT_D)) {
                    wasteRockValue = wasteRockValue.add(new BigDecimal(thirdMetirc.get().getValue()));
                    metricData.setWasteRockValue(MetricValueUtil.formart(wasteRockValue) + SubscriptionConstants.UNIT_T);
                } else {
                    wasteRockValue = wasteRockValue.add(new BigDecimal(thirdMetirc.get().getValue()).divide(new BigDecimal(10000), 3, BigDecimal.ROUND_HALF_UP));
                    metricData.setWasteRockValue(MetricValueUtil.formart(wasteRockValue) + SubscriptionConstants.UNIT_THOUSAND_T);
                }
            } else {
                metricData.setWasteRockValue("");
            }
        }

        return String.valueOf(wasteRockValue);
    }

    /**
     * 历史产品量 混煤指标数据
     *
     * @param data
     * @param metrictype
     * @param metricData
     * @return
     */
    private String getMixedCoalValue(Optional<DataModelWrapper> data, String metrictype, HistoricalProductRateVO.MetricData metricData) {
        String mixedCoalValue = "";
        Optional<DataModelWrapper> secondMetirc = data;
        if (secondMetirc.isPresent()) {
            if (metrictype.equals(MetricCodes.CT_C) || metrictype.equals(MetricCodes.CT_D)) {
                mixedCoalValue = secondMetirc.get().getValue();
                metricData.setMixedCoalValue(MetricValueUtil.formart(mixedCoalValue) + SubscriptionConstants.UNIT_T);
            } else {
                mixedCoalValue = String.valueOf(new BigDecimal(secondMetirc.get().getValue()).divide(new BigDecimal(10000), 3, BigDecimal.ROUND_HALF_UP));
                metricData.setMixedCoalValue(MetricValueUtil.formart(mixedCoalValue) + SubscriptionConstants.UNIT_THOUSAND_T);
            }
        } else {
            metricData.setMixedCoalValue("");
        }
        return mixedCoalValue;
    }

    /**
     * 历史产品量  精煤指标数据
     *
     * @param data
     * @param metrictype
     * @param metricData
     * @return
     */
    private String getCleanCoalValue(Optional<DataModelWrapper> data, String metrictype, HistoricalProductRateVO.MetricData metricData) {
        String cleanCoalValue = "";
        Optional<DataModelWrapper> firstMetirc = data;
        if (firstMetirc.isPresent()) {
            if (metrictype.equals(MetricCodes.CT_C) || metrictype.equals(MetricCodes.CT_D)) {
                cleanCoalValue = firstMetirc.get().getValue();
                metricData.setCleanCoalValue(MetricValueUtil.formart(cleanCoalValue) + SubscriptionConstants.UNIT_T);
            } else {
                cleanCoalValue = String.valueOf(new BigDecimal(firstMetirc.get().getValue()).divide(new BigDecimal(10000), 3, BigDecimal.ROUND_HALF_UP));
                metricData.setCleanCoalValue(MetricValueUtil.formart(cleanCoalValue) + SubscriptionConstants.UNIT_THOUSAND_T);
            }
        } else {
            metricData.setCleanCoalValue("");
        }
        return cleanCoalValue;
    }


    @Override
    public CardDataDTO getMixtureOfRawCoal(SubscCardTypeDO subscCardTypeDO) {
        logger.debug("原煤配比卡片：thingCode{}", subscCardTypeDO.getCardParamValue());
        CardDataDTO cardDataDTO = new CardDataDTO();
        MixtureOfRawCoalVO mixtureOfRawCoalVO = new MixtureOfRawCoalVO();
        if (subscCardTypeDO.getCardCode().contains(GlobalConstants.SYSTEM_01)) {
            mixtureOfRawCoalVO.setSystemName(GlobalConstants.SYSTEM_ONE);
        } else if (subscCardTypeDO.getCardCode().contains(GlobalConstants.SYSTEM_02)) {
            mixtureOfRawCoalVO.setSystemName(GlobalConstants.SYSTEM_TWO);
        }
        mixtureOfRawCoalVO.setCardTitle(subscCardTypeDO.getCardName());
        String cardParamValue = subscCardTypeDO.getCardParamValue();
        //查询8#煤设备，13#煤设备
        List<String> thingCodes = getMixtureOfRawCoalThingCodes(cardParamValue);

        //瞬时量统计
        List<MixtureOfRawCoalVO.MetricData> instantMetrics = getInstantMetrics(mixtureOfRawCoalVO, thingCodes);
        mixtureOfRawCoalVO.setInstantMetrics(instantMetrics);
        //计算瞬时量比例
        getInstantMetricPercent(mixtureOfRawCoalVO, instantMetrics);


        //班累计统计
        List<MixtureOfRawCoalVO.MetricData> teamMetrics = getTeamMetrics(mixtureOfRawCoalVO, thingCodes);
        mixtureOfRawCoalVO.setTeamMetrics(teamMetrics);

        //计算班累计比例
        getTeamMetricPercent(mixtureOfRawCoalVO, teamMetrics);
        cardDataDTO.setCardCode(subscCardTypeDO.getCardCode());
        cardDataDTO.setCardData(JSON.toJSONString(mixtureOfRawCoalVO));
        return cardDataDTO;
    }

    /**
     * 计算班累计比例
     *
     * @param mixtureOfRawCoalVO
     * @param instantMetrics
     */
    private void getTeamMetricPercent(MixtureOfRawCoalVO mixtureOfRawCoalVO, List<MixtureOfRawCoalVO.MetricData> instantMetrics) {
        if (StringUtils.isNotEmpty(instantMetrics.get(0).getMetricValue()) && StringUtils.isNotEmpty(instantMetrics.get(1).getMetricValue())) {
            BigDecimal instantMetric1 = new BigDecimal(instantMetrics.get(0).getMetricValue());
            BigDecimal instantMetric2 = new BigDecimal(instantMetrics.get(1).getMetricValue());
            if (instantMetric1.compareTo(instantMetric2) > 0) {
                BigDecimal percent = instantMetric1.divide(instantMetric2, 2, BigDecimal.ROUND_HALF_UP);
                mixtureOfRawCoalVO.setTeamPercent(percent + ":1");
            } else if (instantMetric1.compareTo(instantMetric2) < 0) {
                BigDecimal percent = instantMetric2.divide(instantMetric1, 2, BigDecimal.ROUND_HALF_UP);
                mixtureOfRawCoalVO.setTeamPercent("1:" + percent);
            } else {
                mixtureOfRawCoalVO.setTeamPercent("1:1");
            }
        } else {
            mixtureOfRawCoalVO.setTeamPercent("");
        }
    }

    /**
     * 班累计统计
     *
     * @param mixtureOfRawCoalVO
     * @param thingCodes
     * @return
     */
    private List<MixtureOfRawCoalVO.MetricData> getTeamMetrics(MixtureOfRawCoalVO mixtureOfRawCoalVO, List<String> thingCodes) {
        List<MixtureOfRawCoalVO.MetricData> teamMetrics = new ArrayList<>();
        for (String thingCode : thingCodes) {
            MixtureOfRawCoalVO.MetricData metricData = mixtureOfRawCoalVO.new MetricData();
            if (StringUtils.isNotEmpty(thingCode)) {
                metricData.setThingCode(thingCode);
                DataModelWrapper instantDataMode = dataService.getData(thingCode, MetricCodes.CT_C).orElse(null);
                if (instantDataMode != null) {
                    metricData.setMetricValue(MetricValueUtil.formart(instantDataMode.getValue()));
                }
            } else {
                metricData.setThingCode("");
                metricData.setMetricValue("");
            }
            teamMetrics.add(metricData);
        }
        return teamMetrics;
    }

    /**
     * 计算瞬时量比例
     *
     * @param mixtureOfRawCoalVO
     * @param instantMetrics
     */
    private void getInstantMetricPercent(MixtureOfRawCoalVO mixtureOfRawCoalVO, List<MixtureOfRawCoalVO.MetricData> instantMetrics) {
        if (StringUtils.isNotEmpty(instantMetrics.get(0).getMetricValue()) && StringUtils.isNotEmpty(instantMetrics.get(1).getMetricValue())) {
            BigDecimal instantMetric1 = new BigDecimal(instantMetrics.get(0).getMetricValue());
            BigDecimal instantMetric2 = new BigDecimal(instantMetrics.get(1).getMetricValue());
            if (instantMetric1.compareTo(instantMetric2) > 0) {
                BigDecimal percent = instantMetric1.divide(instantMetric2, 2, BigDecimal.ROUND_HALF_UP);
                mixtureOfRawCoalVO.setInstantPercent(percent + ":1");
            } else if (instantMetric1.compareTo(instantMetric2) < 0) {
                BigDecimal percent = instantMetric2.divide(instantMetric1, 2, BigDecimal.ROUND_HALF_UP);
                mixtureOfRawCoalVO.setInstantPercent("1:" + percent);
            } else {
                mixtureOfRawCoalVO.setInstantPercent("1:1");
            }
        } else {
            mixtureOfRawCoalVO.setInstantPercent("");
        }
    }

    /**
     * 瞬时量统计
     *
     * @param mixtureOfRawCoalVO
     * @param thingCodes
     * @return
     */
    private List<MixtureOfRawCoalVO.MetricData> getInstantMetrics(MixtureOfRawCoalVO mixtureOfRawCoalVO, List<String> thingCodes) {
        List<MixtureOfRawCoalVO.MetricData> instantMetrics = new ArrayList<>();
        for (String thingCode : thingCodes) {
            MixtureOfRawCoalVO.MetricData metricData = mixtureOfRawCoalVO.new MetricData();
            if (StringUtils.isNotEmpty(thingCode)) {
                metricData.setThingCode(thingCode);
                DataModelWrapper instantDataMode = dataService.getData(thingCode, MetricCodes.COAL_CAP).orElse(null);
                if (instantDataMode != null) {
                    metricData.setMetricValue(MetricValueUtil.formart(instantDataMode.getValue()));
                }
            } else {
                metricData.setThingCode("");
                metricData.setMetricValue("");
            }
            instantMetrics.add(metricData);
        }
        return instantMetrics;
    }

    /**
     * 查询设备编号
     *
     * @param cardParamValue
     * @return
     */
    private List<String> getMixtureOfRawCoalThingCodes(String cardParamValue) {
        List<String> thingCodes = new ArrayList<>();
        DataModelWrapper coal8DataModel = dataService.getData(cardParamValue, MetricCodes.COAL_8_DEVICE).orElse(null);
        if (coal8DataModel != null) {
            thingCodes.add(coal8DataModel.getValue());
        } else {
            thingCodes.add("");
        }

        DataModelWrapper coal13DataModel = dataService.getData(cardParamValue, MetricCodes.COAL_13_DEVICE).orElse(null);
        if (coal13DataModel != null) {
            thingCodes.add(coal13DataModel.getValue());
        } else {
            thingCodes.add("");
        }
        return thingCodes;
    }


    @Override
    public CardDataDTO getInstantaneousWash(SubscCardTypeDO subscCardTypeDO) {
        logger.debug("瞬时入洗量卡片：thingCode{}", subscCardTypeDO.getCardParamValue());
        CardDataDTO cardDataDTO = new CardDataDTO();
        InstantaneousWashVo instantaneousWashVo = new InstantaneousWashVo();
        instantaneousWashVo.setCardTitle(subscCardTypeDO.getCardName());
        if (subscCardTypeDO.getCardCode().contains(GlobalConstants.SYSTEM_01)) {
            instantaneousWashVo.setSystemName(GlobalConstants.SYSTEM_ONE);
        } else if (subscCardTypeDO.getCardCode().contains(GlobalConstants.SYSTEM_02)) {
            instantaneousWashVo.setSystemName(GlobalConstants.SYSTEM_TWO);
        }
        String[] cardParamValues = subscCardTypeDO.getCardParamValue().split(",");
        instantaneousWashVo.setThingCode1(cardParamValues[0]);
        instantaneousWashVo.setThingCode2(cardParamValues[1]);
        String thingCoalCap1 = "";
        String thingCoalCap2 = "";
        DataModelWrapper instantaneousWash1 = dataService.getData(cardParamValues[0], MetricCodes.COAL_CAP).orElse(null);
        if (instantaneousWash1 != null) {
            thingCoalCap1 = MetricValueUtil.formart(instantaneousWash1.getValue());
        }

        DataModelWrapper instantaneousWash2 = dataService.getData(cardParamValues[1], MetricCodes.COAL_CAP).orElse(null);
        if (instantaneousWash2 != null) {
            thingCoalCap2 = MetricValueUtil.formart(instantaneousWash2.getValue());
        }

        BigDecimal instantaneousWashTotal = new BigDecimal(StringUtils.isEmpty(thingCoalCap1) ? "0" : thingCoalCap1).add(new BigDecimal(StringUtils.isEmpty(thingCoalCap1) ? "0" : thingCoalCap2));
        instantaneousWashVo.setInstantaneousValue1(thingCoalCap1);
        instantaneousWashVo.setInstantaneousValue2(thingCoalCap2);
        instantaneousWashVo.setInstantaneousTotalValue(String.valueOf(instantaneousWashTotal));

        String thingCtc1 = "";
        String thingCtc2 = "";

        DataModelWrapper team1 = dataService.getData(cardParamValues[0], MetricCodes.COAL_CAP).orElse(null);
        if (team1 != null) {
            thingCtc1 = MetricValueUtil.formart(team1.getValue());
        }

        DataModelWrapper team2 = dataService.getData(cardParamValues[1], MetricCodes.COAL_CAP).orElse(null);
        if (team2 != null) {
            thingCtc2 = MetricValueUtil.formart(team2.getValue());
        }
        BigDecimal teamTatol = new BigDecimal(StringUtils.isEmpty(thingCtc1) ? "0" : thingCtc1).add(new BigDecimal(StringUtils.isEmpty(thingCtc2) ? "0" : thingCtc2));
        instantaneousWashVo.setTeamValue1(thingCtc1);
        instantaneousWashVo.setTeamValue2(thingCtc2);
        instantaneousWashVo.setTeamTotalValue(String.valueOf(teamTatol));
        cardDataDTO.setCardCode(subscCardTypeDO.getCardCode());
        cardDataDTO.setCardData(JSON.toJSONString(instantaneousWashVo));
        return cardDataDTO;
    }

    @Override
    public CardDataDTO getInstantaneousProductQuantity(SubscCardTypeDO subscCardTypeDO) {
        logger.debug("瞬时产品量卡片：thingCode{}", subscCardTypeDO.getCardParamValue());
        CardDataDTO cardDataDTO = new CardDataDTO();
        InstantaneousProductQuantityVO instantaneousProductQuantityVO = new InstantaneousProductQuantityVO();
        instantaneousProductQuantityVO.setCardTitle(subscCardTypeDO.getCardName());
        instantaneousProductQuantityVO.setCleanCoalName(SubscriptionConstants.CLEAN_COAL);
        instantaneousProductQuantityVO.setMixedCoalName(SubscriptionConstants.MIXED_COAL);
        instantaneousProductQuantityVO.setWasteRockName(SubscriptionConstants.WASTE_ROCK);
        String[] cardParamValues = subscCardTypeDO.getCardParamValue().split(",");
        DataModelWrapper cleanCoalInstantaneousWash = dataService.getData(cardParamValues[0], MetricCodes.COAL_CAP).orElse(null);
        instantaneousProductQuantityVO.setCleanCoalInstantaneousValue(cleanCoalInstantaneousWash == null ? "" : MetricValueUtil.formart(cleanCoalInstantaneousWash.getValue()));

        DataModelWrapper cleanCoalTeamWash = dataService.getData(cardParamValues[0], MetricCodes.CT_C).orElse(null);
        instantaneousProductQuantityVO.setCleanCoalTeamValue(cleanCoalTeamWash == null ? "" : MetricValueUtil.formart(cleanCoalTeamWash.getValue()));

        DataModelWrapper mixedCoalInstantaneousWash = dataService.getData(cardParamValues[1], MetricCodes.COAL_CAP).orElse(null);
        instantaneousProductQuantityVO.setMixedCoalInstantaneousValue(mixedCoalInstantaneousWash == null ? "" : MetricValueUtil.formart(mixedCoalInstantaneousWash.getValue()));

        DataModelWrapper mixedCoalTeamWash = dataService.getData(cardParamValues[0], MetricCodes.CT_C).orElse(null);
        instantaneousProductQuantityVO.setMixedCoalTeamValue(mixedCoalTeamWash == null ? "" : MetricValueUtil.formart(mixedCoalTeamWash.getValue()));
        String[] wasteRocks = cardParamValues[2].split("\\+");


        DataModelWrapper wasteRockInstantaneousWash1 = dataService.getData(wasteRocks[0], MetricCodes.COAL_CAP).orElse(null);
        DataModelWrapper wasteRockInstantaneousWash2 = dataService.getData(wasteRocks[1], MetricCodes.COAL_CAP).orElse(null);
        String wasteRockInsValue1 = "";
        String wasteRockInsValue2 = "";
        if (wasteRockInstantaneousWash1 != null) {
            wasteRockInsValue1 = MetricValueUtil.formart(wasteRockInstantaneousWash1.getValue());
        }
        if (wasteRockInstantaneousWash2 != null) {
            wasteRockInsValue2 = MetricValueUtil.formart(wasteRockInstantaneousWash2.getValue());
        }
        BigDecimal wasteRockInstantaneousWash = new BigDecimal(StringUtils.isEmpty(wasteRockInsValue1) ? "0" : wasteRockInsValue1).add(new BigDecimal(StringUtils.isEmpty(wasteRockInsValue2) ? "0" : wasteRockInsValue2));
        instantaneousProductQuantityVO.setWasteRockInstantaneousValue(String.valueOf(wasteRockInstantaneousWash).equals("0") ? "" : MetricValueUtil.formart(wasteRockInstantaneousWash));


        DataModelWrapper wasteRockTeamWash1 = dataService.getData(wasteRocks[0], MetricCodes.CT_C).orElse(null);
        DataModelWrapper wasteRockTeamWash2 = dataService.getData(wasteRocks[1], MetricCodes.CT_C).orElse(null);
        String wasteRockTeamValue1 = "";
        String wasteRockTeamValue2 = "";
        if (wasteRockTeamWash1 != null) {
            wasteRockTeamValue1 = MetricValueUtil.formart(wasteRockTeamWash1.getValue());
        }
        if (wasteRockTeamWash2 != null) {
            wasteRockTeamValue2 = MetricValueUtil.formart(wasteRockTeamWash2.getValue());
        }
        BigDecimal wasteRockTeamWash = new BigDecimal(StringUtils.isEmpty(wasteRockTeamValue1) ? "0" : wasteRockTeamValue1).add(new BigDecimal(StringUtils.isEmpty(wasteRockTeamValue2) ? "0" : wasteRockTeamValue2));
        instantaneousProductQuantityVO.setWasteRockTeamValue(String.valueOf(wasteRockTeamWash).equals("0") ? "" : MetricValueUtil.formart(wasteRockTeamWash));
        cardDataDTO.setCardCode(subscCardTypeDO.getCardCode());
        cardDataDTO.setCardData(JSON.toJSONString(instantaneousProductQuantityVO));

        return cardDataDTO;
    }

    @Override
    public CardDataDTO getProductYield(SubscCardTypeDO subscCardTypeDO) {
        logger.debug("产品产率卡片：thingCode{}", subscCardTypeDO.getCardParamValue());
        CardDataDTO cardDataDTO = new CardDataDTO();
        ProductYieldVO productYieldVO = new ProductYieldVO();
        productYieldVO.setCardTitle(subscCardTypeDO.getCardName());
        //1301+1302,702,1552,1553 解析thingCode 原煤 精煤 混煤 煤泥
        String[] cardParamValues = subscCardTypeDO.getCardParamValue().split(",");
        String[] rawCoals = cardParamValues[0].split("\\+");
        //产品产率班累计
        getProductYieldOfMetricCTC(productYieldVO, cardParamValues, rawCoals);
        //产品产率 瞬时量
        getProductYieldOfMetricCAP(productYieldVO, cardParamValues, rawCoals);

        cardDataDTO.setCardCode(subscCardTypeDO.getCardCode());
        cardDataDTO.setCardData(JSON.toJSONString(productYieldVO));

        return cardDataDTO;
    }

    /**
     * 产品产率 瞬时量
     *
     * @param productYieldVO
     * @param cardParamValues
     * @param rawCoals
     */
    private void getProductYieldOfMetricCAP(ProductYieldVO productYieldVO, String[] cardParamValues, String[] rawCoals) {
        //原煤瞬时量
        BigDecimal rawCoalCapValue = BigDecimal.ZERO;
        long millis = System.currentTimeMillis();
        DataModel rawCoalCap1 = historyDataService.findClosestHistoryData(Lists.newArrayList(rawCoals[0]), Lists.newArrayList(MetricCodes.COAL_CAP), new Date(millis - 240000));
        if (rawCoalCap1 != null) {
            rawCoalCapValue = rawCoalCapValue.add(new BigDecimal(rawCoalCap1.getValue()));
        }
        DataModel rawCoalCap2 = historyDataService.findClosestHistoryData(Lists.newArrayList(rawCoals[1]), Lists.newArrayList(MetricCodes.COAL_CAP), new Date(millis - 240000));
        if (rawCoalCap2 != null) {
            rawCoalCapValue = rawCoalCapValue.add(new BigDecimal(rawCoalCap2.getValue()));
        }
        //精煤瞬时量
        String cleanCoalCapValue = "";
        DataModelWrapper cleanCoalCap = dataService.getData(cardParamValues[1], MetricCodes.COAL_CAP).orElse(null);
        if (cleanCoalCap != null) {
            cleanCoalCapValue = MetricValueUtil.formart(cleanCoalCap.getValue());
        }
        //混煤瞬时量
        String mixedCoalCapValue = "";
        DataModel mixedCoalCap = historyDataService.findClosestHistoryData(Lists.newArrayList(cardParamValues[2]), Lists.newArrayList(MetricCodes.COAL_CAP), new Date(System.currentTimeMillis() - 60000));
        if (mixedCoalCap != null) {
            mixedCoalCapValue = MetricValueUtil.formart(mixedCoalCap.getValue());
        }
        //煤泥瞬时量
        String slurryCoalCapValue = "";
        DataModel slurryCoalCap = historyDataService.findClosestHistoryData(Lists.newArrayList(cardParamValues[3]), Lists.newArrayList(MetricCodes.COAL_CAP), new Date(System.currentTimeMillis() - 10000));
        if (slurryCoalCap != null) {
            slurryCoalCapValue = MetricValueUtil.formart(slurryCoalCap.getValue());
        }
        //矸石瞬时量
        BigDecimal wasteRockCapVale = rawCoalCapValue.
                subtract(new BigDecimal(StringUtils.isEmpty(cleanCoalCapValue) ? "0" : cleanCoalCapValue)).
                subtract(new BigDecimal(StringUtils.isEmpty(mixedCoalCapValue) ? "0" : mixedCoalCapValue)).
                subtract(new BigDecimal(StringUtils.isEmpty(slurryCoalCapValue) ? "0" : slurryCoalCapValue));
        String wasteRockCapValue = "";
        if (wasteRockCapVale.compareTo(BigDecimal.ZERO) > -1) {
            wasteRockCapValue = String.valueOf(wasteRockCapVale);
        }
        productYieldVO.setRawCoalCapValue(MetricValueUtil.formart(rawCoalCapValue));
        productYieldVO.setCleanCoalCapValue(cleanCoalCapValue);
        productYieldVO.setMixedCoalCapValue(mixedCoalCapValue);
        productYieldVO.setSlurryCapValue(slurryCoalCapValue);
        productYieldVO.setWasteRockCapValue(wasteRockCapValue);
    }

    /**
     * 产品产率 班累计
     *
     * @param productYieldVO
     * @param cardParamValues
     * @param rawCoals
     */
    private void getProductYieldOfMetricCTC(ProductYieldVO productYieldVO, String[] cardParamValues, String[] rawCoals) {
        List<ProductYieldVO.MetricData> metricDataList = new ArrayList<>();
        DataModelWrapper rawCoaldata1 = dataService.getData(rawCoals[0], MetricCodes.CT_C).orElse(null);
        DataModelWrapper rawCoaldata2 = dataService.getData(rawCoals[1], MetricCodes.CT_C).orElse(null);
        String rawCoalValue1 = "";
        String rawCoalValue2 = "";
        if (rawCoaldata1 != null) {
            rawCoalValue1 = MetricValueUtil.formart(rawCoaldata1.getValue());
        }
        if (rawCoaldata2 != null) {
            rawCoalValue2 = MetricValueUtil.formart(rawCoaldata2.getValue());
        }
        //原煤班累计总计
        BigDecimal rawCoalTotalValue = new BigDecimal(StringUtils.isEmpty(rawCoalValue1) ? "0" : rawCoalValue1).add(new BigDecimal(StringUtils.isEmpty(rawCoalValue2) ? "0" : rawCoalValue2));

        //精煤班累计
        BigDecimal cleanCoalTeamValue = BigDecimal.ZERO;
        DataModelWrapper cleanCoalData = dataService.getData(cardParamValues[1], MetricCodes.CT_C).orElse(null);
        if (cleanCoalData != null) {
            cleanCoalTeamValue = new BigDecimal(cleanCoalData.getValue());
        }
        //混煤班累计
        BigDecimal mixedCoalTeamValue = BigDecimal.ZERO;
        DataModelWrapper mixedCoalData = dataService.getData(cardParamValues[2], MetricCodes.CT_C).orElse(null);
        if (mixedCoalData != null) {
            mixedCoalTeamValue = new BigDecimal(mixedCoalData.getValue());
        }
        //煤泥班累计
        BigDecimal slurryTeamValue = BigDecimal.ZERO;
        DataModelWrapper slurryData = dataService.getData(cardParamValues[3], MetricCodes.CT_C).orElse(null);
        if (slurryData != null) {
            slurryTeamValue = new BigDecimal(slurryData.getValue());
        }
        ProductYieldVO.MetricData metricData1 = productYieldVO.new MetricData();
        ProductYieldVO.MetricData metricData2 = productYieldVO.new MetricData();
        ProductYieldVO.MetricData metricData3 = productYieldVO.new MetricData();
        ProductYieldVO.MetricData metricData4 = productYieldVO.new MetricData();
        //精煤
        metricData1.setThingName(SubscriptionConstants.CLEAN_COAL);
        if (cleanCoalTeamValue.compareTo(BigDecimal.ZERO) != 0) {
            metricData1.setThingCodeMetricPercent(cleanCoalTeamValue.divide(rawCoalTotalValue, 1, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100).setScale(0)) + "%");
        } else {
            metricData1.setThingCodeMetricPercent("");
        }
        //混煤
        metricData2.setThingName(SubscriptionConstants.MIXED_COAL);
        if (mixedCoalTeamValue.compareTo(BigDecimal.ZERO) != 0) {
            metricData2.setThingCodeMetricPercent(mixedCoalTeamValue.divide(rawCoalTotalValue, 1, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100).setScale(0)) + "%");
        } else {
            metricData2.setThingCodeMetricPercent("");
        }
        //煤泥
        metricData3.setThingName(SubscriptionConstants.SLURRY);
        if (slurryTeamValue.compareTo(BigDecimal.ZERO) != 0) {
            metricData3.setThingCodeMetricPercent(slurryTeamValue.divide(rawCoalTotalValue, 1, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100).setScale(0)) + "%");
        } else {
            metricData3.setThingCodeMetricPercent("");
        }

        //矸石
        metricData4.setThingName(SubscriptionConstants.WASTE_ROCK);
        if ((rawCoalTotalValue.subtract(cleanCoalTeamValue).subtract(mixedCoalTeamValue).subtract(slurryTeamValue)).compareTo(BigDecimal.ZERO) > 0) {
            metricData4.setThingCodeMetricPercent((rawCoalTotalValue.subtract(cleanCoalTeamValue).subtract(mixedCoalTeamValue).subtract(slurryTeamValue)).divide(rawCoalTotalValue, 1, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100).setScale(0)) + "%");
        } else {
            metricData4.setThingCodeMetricPercent("");
        }

        metricDataList.add(metricData1);
        metricDataList.add(metricData2);
        metricDataList.add(metricData3);
        metricDataList.add(metricData4);
        productYieldVO.setTeamMetricDatas(metricDataList);
    }

    @Override
    public CardDataDTO getChemicalTestsData(SubscCardTypeDO subscCardTypeDO) {
        logger.debug("化验数据卡片：thingCode{}", subscCardTypeDO.getCardParamValue());
        CardDataDTO cardDataDTO = new CardDataDTO();
        ChemicalTestsDataVO chemicalTestsDataVO = new ChemicalTestsDataVO();
        chemicalTestsDataVO.setCardTitle(subscCardTypeDO.getCardName());
        String[] cardParamValue = subscCardTypeDO.getCardParamValue().split(",");

        List<ChemicalTestsDataVO.MetricData> metricDataList1 = getChemicalTestsDataOf551(chemicalTestsDataVO, cardParamValue);
        List<ChemicalTestsDataVO.MetricData> metricDataList2 = getChemicalTestsDataOf552(chemicalTestsDataVO, cardParamValue);
        chemicalTestsDataVO.setCleanCoalMetric(metricDataList1);
        chemicalTestsDataVO.setMixedCoalMetric(metricDataList2);
        cardDataDTO.setCardCode(subscCardTypeDO.getCardCode());
        cardDataDTO.setCardData(JSON.toJSONString(chemicalTestsDataVO));
        return cardDataDTO;
    }

    /**
     * 552 设备 灰分 水分
     *
     * @param chemicalTestsDataVO
     * @param cardParamValue
     * @return
     */
    private List<ChemicalTestsDataVO.MetricData> getChemicalTestsDataOf552(ChemicalTestsDataVO chemicalTestsDataVO, String[] cardParamValue) {
        List<ChemicalTestsDataVO.MetricData> metricDataList2 = new ArrayList<>();
        List<CoalAnalysisRecord> top2CoalAnalysisRecords2 = coalAnalysisMapper.getTop2CoalAnalysisRecord(cardParamValue[1], SubscriptionConstants.MIXE_COAL_552);
        if (CollectionUtils.isNotEmpty(top2CoalAnalysisRecords2)) {
            chemicalTestsDataVO.setMixedCoalSamplingTime(new SimpleDateFormat("HH:mm").format(top2CoalAnalysisRecords2.get(0).getTime()));
            CoalAnalysisRecord coalAnalysisRecord = top2CoalAnalysisRecords2.get(0);
            CoalAnalysisRecord preCoalAnalysisRecord = top2CoalAnalysisRecords2.get(1);
            ChemicalTestsDataVO.MetricData metricData3 = get552AadMetricValue(chemicalTestsDataVO, coalAnalysisRecord, preCoalAnalysisRecord);
            metricDataList2.add(metricData3);
            //精煤水分
            ChemicalTestsDataVO.MetricData metricData4 = get552MtMetricValue(chemicalTestsDataVO, coalAnalysisRecord, preCoalAnalysisRecord);
            metricDataList2.add(metricData4);
        } else {
            chemicalTestsDataVO.setCleanCoalSamplingTime("");
            ChemicalTestsDataVO.MetricData metricData1 = chemicalTestsDataVO.new MetricData();
            metricData1.setPercent("");
            metricData1.setFlag("");
            metricData1.setUnusual("");
            metricData1.setMetricName(SubscriptionConstants.ASH_CONTENT);
            metricData1.setMetricValue("");
            metricDataList2.add(metricData1);
            ChemicalTestsDataVO.MetricData metricData2 = chemicalTestsDataVO.new MetricData();
            metricData2.setPercent("");
            metricData2.setFlag("");
            metricData2.setUnusual("");
            metricData2.setMetricName(SubscriptionConstants.MOISTURE_CONTENT);
            metricData2.setMetricValue("");
            metricDataList2.add(metricData2);

        }
        return metricDataList2;
    }

    /**
     * 552 水分
     *
     * @param chemicalTestsDataVO
     * @param coalAnalysisRecord
     * @param preCoalAnalysisRecord
     * @return
     */
    private ChemicalTestsDataVO.MetricData get552MtMetricValue(ChemicalTestsDataVO chemicalTestsDataVO, CoalAnalysisRecord coalAnalysisRecord, CoalAnalysisRecord preCoalAnalysisRecord) {
        ChemicalTestsDataVO.MetricData metricData4 = chemicalTestsDataVO.new MetricData();
        metricData4.setMetricName(SubscriptionConstants.MOISTURE_CONTENT);
        metricData4.setMetricValue(String.valueOf(coalAnalysisRecord.getMt()));
        String startScope = SubscriptionConstants.MOISTURE_CONTENT_SCOPE.split("-")[0];
        String endScope = SubscriptionConstants.MOISTURE_CONTENT_SCOPE.split("-")[1];
        metricData4.setStartScope(startScope);
        metricData4.setEndScope(endScope);
        if (preCoalAnalysisRecord.getMt() != null) {
            if (coalAnalysisRecord.getMt().compareTo(preCoalAnalysisRecord.getMt()) > 0) {
                metricData4.setPercent(String.valueOf(BigDecimal.valueOf(coalAnalysisRecord.getMt()).subtract(BigDecimal.valueOf(preCoalAnalysisRecord.getMt())).setScale(2)));
                metricData4.setFlag(SubscriptionConstants.FLAG_UP);
            } else if (coalAnalysisRecord.getMt().compareTo(preCoalAnalysisRecord.getMt()) < 0) {
                metricData4.setPercent(String.valueOf(BigDecimal.valueOf(preCoalAnalysisRecord.getMt()).subtract(BigDecimal.valueOf(coalAnalysisRecord.getMt())).setScale(2)));
                metricData4.setFlag(SubscriptionConstants.FLAG_DOWN);
            } else {
                metricData4.setPercent("0");
                metricData4.setFlag(SubscriptionConstants.FLAG_UP);

            }
        }
        //计算是否是异常数据
        if (!metricData4.getMetricValue().equals("null")) {
            if (new BigDecimal(metricData4.getMetricValue()).compareTo(new BigDecimal(startScope)) > 0 && new BigDecimal(endScope).compareTo(new BigDecimal(metricData4.getMetricValue())) > 0) {
                metricData4.setUnusual("0");
            } else {
                metricData4.setUnusual("1");
            }
        } else {
            metricData4.setUnusual("1");
        }
        return metricData4;
    }

    /**
     * 552 灰分
     *
     * @param chemicalTestsDataVO
     * @param coalAnalysisRecord
     * @param preCoalAnalysisRecord
     * @return
     */
    private ChemicalTestsDataVO.MetricData get552AadMetricValue(ChemicalTestsDataVO chemicalTestsDataVO, CoalAnalysisRecord coalAnalysisRecord, CoalAnalysisRecord preCoalAnalysisRecord) {
        //精煤灰分
        ChemicalTestsDataVO.MetricData metricData3 = chemicalTestsDataVO.new MetricData();
        metricData3.setMetricName(SubscriptionConstants.ASH_CONTENT);
        metricData3.setMetricValue(String.valueOf(coalAnalysisRecord.getAad()));

        String startScope = SubscriptionConstants.ASH_CONTENT_MIXED_COAL_SCOPE.split("-")[0];
        String endScope = SubscriptionConstants.ASH_CONTENT_MIXED_COAL_SCOPE.split("-")[1];
        metricData3.setStartScope(startScope);
        metricData3.setEndScope(endScope);

        if (preCoalAnalysisRecord.getAad() != null) {
            if (coalAnalysisRecord.getAad().compareTo(preCoalAnalysisRecord.getAad()) > 0) {
                metricData3.setPercent(String.valueOf(BigDecimal.valueOf(coalAnalysisRecord.getAad()).subtract(BigDecimal.valueOf(preCoalAnalysisRecord.getAad())).setScale(2)));
                metricData3.setFlag(SubscriptionConstants.FLAG_UP);
            } else if (coalAnalysisRecord.getAad().compareTo(preCoalAnalysisRecord.getAad()) < 0) {
                metricData3.setPercent(String.valueOf(BigDecimal.valueOf(preCoalAnalysisRecord.getAad()).subtract(BigDecimal.valueOf(coalAnalysisRecord.getAad())).setScale(2)));
                metricData3.setFlag(SubscriptionConstants.FLAG_DOWN);
            } else {
                metricData3.setPercent("0");
                metricData3.setFlag(SubscriptionConstants.FLAG_UP);

            }
        }

        //计算是否是异常数据
        if (new BigDecimal(metricData3.getMetricValue()).compareTo(new BigDecimal(startScope)) > 0 && new BigDecimal(endScope).compareTo(new BigDecimal(metricData3.getMetricValue())) > 0) {
            metricData3.setUnusual("0");
        } else {
            metricData3.setUnusual("1");
        }

        return metricData3;
    }

    /**
     * 551设备 灰分 硫分数据
     *
     * @param chemicalTestsDataVO
     * @param cardParamValue
     */
    private List<ChemicalTestsDataVO.MetricData> getChemicalTestsDataOf551(ChemicalTestsDataVO chemicalTestsDataVO, String[] cardParamValue) {
        List<ChemicalTestsDataVO.MetricData> metricDataList1 = new ArrayList<>();
        //551 设备
        List<CoalAnalysisRecord> top2CoalAnalysisRecords1 = coalAnalysisMapper.getTop2CoalAnalysisRecord(cardParamValue[0], SubscriptionConstants.CLEAN_COAL_551);
        if (CollectionUtils.isNotEmpty(top2CoalAnalysisRecords1)) {
            chemicalTestsDataVO.setCleanCoalSamplingTime(new SimpleDateFormat("HH:mm").format(top2CoalAnalysisRecords1.get(0).getTime()));
            CoalAnalysisRecord coalAnalysisRecord = top2CoalAnalysisRecords1.get(0);
            CoalAnalysisRecord preCoalAnalysisRecord = top2CoalAnalysisRecords1.get(1);
            ChemicalTestsDataVO.MetricData metricData1 = get551AadMetricVlaue(chemicalTestsDataVO, coalAnalysisRecord, preCoalAnalysisRecord);
            metricDataList1.add(metricData1);
            ChemicalTestsDataVO.MetricData metricData2 = get551StadMetricValue(chemicalTestsDataVO, coalAnalysisRecord, preCoalAnalysisRecord);
            metricDataList1.add(metricData2);
        } else {
            chemicalTestsDataVO.setCleanCoalSamplingTime("");
            ChemicalTestsDataVO.MetricData metricData1 = chemicalTestsDataVO.new MetricData();
            metricData1.setPercent("");
            metricData1.setFlag("");
            metricData1.setUnusual("");
            metricData1.setMetricName(SubscriptionConstants.ASH_CONTENT);
            metricData1.setMetricValue("");
            metricDataList1.add(metricData1);
            ChemicalTestsDataVO.MetricData metricData2 = chemicalTestsDataVO.new MetricData();
            metricData2.setPercent("");
            metricData2.setFlag("");
            metricData2.setUnusual("");
            metricData2.setMetricName(SubscriptionConstants.SULFUR_CONTENT);
            metricData2.setMetricValue("");
            metricDataList1.add(metricData2);

        }
        return metricDataList1;
    }

    /**
     * 查询 551 硫分数据
     *
     * @param chemicalTestsDataVO
     * @param coalAnalysisRecord
     * @param preCoalAnalysisRecord
     * @return
     */
    private ChemicalTestsDataVO.MetricData get551StadMetricValue(ChemicalTestsDataVO chemicalTestsDataVO, CoalAnalysisRecord coalAnalysisRecord, CoalAnalysisRecord preCoalAnalysisRecord) {
        //精煤硫分
        ChemicalTestsDataVO.MetricData metricData2 = chemicalTestsDataVO.new MetricData();
        metricData2.setMetricName(SubscriptionConstants.SULFUR_CONTENT);
        metricData2.setMetricValue(String.valueOf(coalAnalysisRecord.getStad()));
        String startScope2 = SubscriptionConstants.SULFUR_CONTENT_SCOPE.split("-")[0];
        String endScope2 = SubscriptionConstants.SULFUR_CONTENT_SCOPE.split("-")[1];
        metricData2.setStartScope(startScope2);
        metricData2.setEndScope(endScope2);
        if (preCoalAnalysisRecord.getStad() != null) {
            if (coalAnalysisRecord.getStad().compareTo(preCoalAnalysisRecord.getStad()) > 0) {
                metricData2.setPercent(String.valueOf(BigDecimal.valueOf(coalAnalysisRecord.getStad()).subtract(BigDecimal.valueOf(preCoalAnalysisRecord.getStad())).setScale(2)));
                metricData2.setFlag(SubscriptionConstants.FLAG_UP);
            } else if (coalAnalysisRecord.getStad().compareTo(preCoalAnalysisRecord.getStad()) < 0) {
                metricData2.setPercent(String.valueOf(BigDecimal.valueOf(preCoalAnalysisRecord.getStad()).subtract(BigDecimal.valueOf(coalAnalysisRecord.getStad())).setScale(2)));
                metricData2.setFlag(SubscriptionConstants.FLAG_DOWN);
            } else {
                metricData2.setPercent("0");
                metricData2.setFlag(SubscriptionConstants.FLAG_UP);

            }
        }
        //计算是否是异常数据
        if (new BigDecimal(metricData2.getMetricValue()).compareTo(new BigDecimal(startScope2)) > 0 && new BigDecimal(endScope2).compareTo(new BigDecimal(metricData2.getMetricValue())) > 0) {
            metricData2.setUnusual("0");
        } else {
            metricData2.setUnusual("1");
        }
        return metricData2;
    }

    /**
     * 查询 552 水分数据
     *
     * @param chemicalTestsDataVO
     * @param coalAnalysisRecord
     * @param preCoalAnalysisRecord
     * @return
     */
    private ChemicalTestsDataVO.MetricData get551AadMetricVlaue(ChemicalTestsDataVO chemicalTestsDataVO, CoalAnalysisRecord coalAnalysisRecord, CoalAnalysisRecord preCoalAnalysisRecord) {
        //精煤灰分
        ChemicalTestsDataVO.MetricData metricData1 = chemicalTestsDataVO.new MetricData();
        metricData1.setMetricName(SubscriptionConstants.ASH_CONTENT);
        metricData1.setMetricValue(String.valueOf(coalAnalysisRecord.getAad()));
        String startScope1 = SubscriptionConstants.ASH_CONTENT_CLEAN_COAL_SCOPE.split("-")[0];
        String endScope1 = SubscriptionConstants.ASH_CONTENT_CLEAN_COAL_SCOPE.split("-")[1];
        metricData1.setStartScope(startScope1);
        metricData1.setEndScope(endScope1);

        if (preCoalAnalysisRecord.getAad() != null) {
            if (coalAnalysisRecord.getAad().compareTo(preCoalAnalysisRecord.getAad()) > 0) {
                metricData1.setPercent(String.valueOf(BigDecimal.valueOf(coalAnalysisRecord.getAad()).subtract(BigDecimal.valueOf(preCoalAnalysisRecord.getAad())).setScale(2)));
                metricData1.setFlag(SubscriptionConstants.FLAG_UP);
            } else if (coalAnalysisRecord.getAad().compareTo(preCoalAnalysisRecord.getAad()) < 0) {
                metricData1.setPercent(String.valueOf(BigDecimal.valueOf(preCoalAnalysisRecord.getAad()).subtract(BigDecimal.valueOf(coalAnalysisRecord.getAad())).setScale(2)));
                metricData1.setFlag(SubscriptionConstants.FLAG_DOWN);
            } else {
                metricData1.setPercent("0");
                metricData1.setFlag(SubscriptionConstants.FLAG_UP);

            }
        }

        //计算是否是异常数据
        if (new BigDecimal(metricData1.getMetricValue()).compareTo(new BigDecimal(startScope1)) > 0 && new BigDecimal(endScope1).compareTo(new BigDecimal(metricData1.getMetricValue())) > 0) {
            metricData1.setUnusual("0");
        } else {
            metricData1.setUnusual("1");
        }
        return metricData1;
    }

    @Override
    public CardDataDTO getIntelligentFilter(SubscCardTypeDO subscCardTypeDO) {
        logger.debug("智能压滤卡片：thingCode{}", subscCardTypeDO.getCardParamValue());
        CardDataDTO cardDataDTO = new CardDataDTO();

        IntelligentFilterVO intelligentFilterVO = new IntelligentFilterVO();
        intelligentFilterVO.setCardTitle(subscCardTypeDO.getCardName());
        String[] cardParamValues = subscCardTypeDO.getCardParamValue().split(",");
        List<IntelligentFilterVO.ThickenerMetric> thickenerMetrics = new ArrayList<>();


        IntelligentFilterVO.ThickenerMetric thickenerMetric1 = intelligentFilterVO.new ThickenerMetric();
        thickenerMetric1.setThingCode(cardParamValues[0].split("\\.")[0]);
        DataModelWrapper thickenerDataModelWrapper1 = dataService.getData(cardParamValues[0], FilterPressMetricConstants.FEED_PUMP_CURRENT).orElse(null);
        if (thickenerDataModelWrapper1 != null) {
            thickenerMetric1.setCurrentValue(MetricValueUtil.formart(thickenerDataModelWrapper1.getValue()));
        } else {
            thickenerMetric1.setCurrentValue("");
        }
        thickenerMetrics.add(thickenerMetric1);


        IntelligentFilterVO.ThickenerMetric thickenerMetric2 = intelligentFilterVO.new ThickenerMetric();
        thickenerMetric2.setThingCode(cardParamValues[1].split("\\.")[0]);
        DataModelWrapper thickenerDataModelWrapper2 = dataService.getData(cardParamValues[1], FilterPressMetricConstants.FEED_PUMP_CURRENT).orElse(null);
        if (thickenerDataModelWrapper2 != null) {
            thickenerMetric2.setCurrentValue(MetricValueUtil.formart(thickenerDataModelWrapper2.getValue()));
        } else {
            thickenerMetric2.setCurrentValue("");
        }
        thickenerMetrics.add(thickenerMetric2);


        if (StringUtils.isNotEmpty(cardParamValues[2])) {
            IntelligentFilterVO.ThickenerMetric thickenerMetric3 = intelligentFilterVO.new ThickenerMetric();
            thickenerMetric3.setThingCode(cardParamValues[2].split("\\.")[0]);
            DataModelWrapper thickenerDataModelWrapper3 = dataService.getData(cardParamValues[2], FilterPressMetricConstants.FEED_PUMP_CURRENT).orElse(null);
            if (thickenerDataModelWrapper3 != null) {
                thickenerMetric3.setCurrentValue(MetricValueUtil.formart(thickenerDataModelWrapper3.getValue()));
            } else {
                thickenerMetric3.setCurrentValue("");
            }

            thickenerMetrics.add(thickenerMetric3);
        }
        intelligentFilterVO.setThickenerMetrics(thickenerMetrics);
        String[] plates = cardParamValues[3].split("\\+");
        String plateValue1 = "";
        String plateValue2 = "";
        String plateValue3 = "";
        String plateValue4 = "";
        String plateValue5 = "";
        String plateValue6 = "";
        DataModelWrapper plateDataModelWrapper1 = dataService.getData(plates[0], FilterPressMetricConstants.PLATE_CNT).orElse(null);
        if (plateDataModelWrapper1 != null) {
            plateValue1 = plateDataModelWrapper1.getValue();
        }
        DataModelWrapper plateDataModelWrapper2 = dataService.getData(plates[1], FilterPressMetricConstants.PLATE_CNT).orElse(null);
        if (plateDataModelWrapper2 != null) {
            plateValue2 = plateDataModelWrapper2.getValue();
        }
        DataModelWrapper plateDataModelWrapper3 = dataService.getData(plates[2], FilterPressMetricConstants.PLATE_CNT).orElse(null);
        if (plateDataModelWrapper3 != null) {
            plateValue3 = plateDataModelWrapper3.getValue();
        }
        DataModelWrapper plateDataModelWrapper4 = dataService.getData(plates[3], FilterPressMetricConstants.PLATE_CNT).orElse(null);
        if (plateDataModelWrapper4 != null) {
            plateValue4 = plateDataModelWrapper4.getValue();
        }
        DataModelWrapper plateDataModelWrapper5 = dataService.getData(plates[4], FilterPressMetricConstants.PLATE_CNT).orElse(null);
        if (plateDataModelWrapper5 != null) {
            plateValue5 = plateDataModelWrapper5.getValue();
        }
        DataModelWrapper plateDataModelWrapper6 = dataService.getData(plates[5], FilterPressMetricConstants.PLATE_CNT).orElse(null);
        if (plateDataModelWrapper6 != null) {
            plateValue6 = plateDataModelWrapper6.getValue();
        }
        intelligentFilterVO.setPlateName(Lists.newArrayList(plates[0], plates[1], plates[2], plates[3], plates[4], plates[5]));
        intelligentFilterVO.setPlateCnt(Lists.newArrayList(plateValue1, plateValue2, plateValue3, plateValue4, plateValue5, plateValue6));
        cardDataDTO.setCardCode(subscCardTypeDO.getCardCode());
        cardDataDTO.setCardData(JSON.toJSONString(intelligentFilterVO));

        return cardDataDTO;
    }

    @Override
    public CardDataDTO getIntelligentBlower(SubscCardTypeDO subscCardTypeDO) {
        logger.debug("智能鼓风卡片：thingCode{}", subscCardTypeDO.getCardParamValue());
        CardDataDTO cardDataDTO = new CardDataDTO();
        IntelligentBlowerVO intelligentBlowerVO = new IntelligentBlowerVO();
        intelligentBlowerVO.setCardTitle(subscCardTypeDO.getCardName());
        String[] cardParamValue = subscCardTypeDO.getCardParamValue().split(",");
        //查询空压机状态
        List<IntelligentBlowerVO.MompressorMetric> mompressorMetrics = getMompressorMetrics(intelligentBlowerVO, cardParamValue);
        //查询管道压力的状态
        List<String> pipelineMetrics = getPipelineMetric(cardParamValue);
        intelligentBlowerVO.setMompressorMetrics(mompressorMetrics);
        intelligentBlowerVO.setPressCurs(pipelineMetrics);
        cardDataDTO.setCardCode(subscCardTypeDO.getCardCode());
        cardDataDTO.setCardData(JSON.toJSONString(intelligentBlowerVO));
        return cardDataDTO;
    }

    /**
     * 查询管道压力的状态
     *
     * @param cardParamValue
     * @return
     */
    private List<String> getPipelineMetric(String[] cardParamValue) {
        //管道压力
        List<String> pipelineMetrics = new ArrayList<>();
        List<String> pipelineList = Lists.newArrayList(cardParamValue[6], cardParamValue[7]);
        for (String pipeline : pipelineList) {

            DataModelWrapper pipelineData = dataService.getData(pipeline, BellowsConstants.METRIC_PRESSURE).orElse(null);
            if (pipelineData != null) {
                String pipelineMetricValue = pipelineData.getValue();
                pipelineMetrics.add(pipelineMetricValue);
            } else {
                pipelineMetrics.add("");
            }
        }
        return pipelineMetrics;
    }

    /**
     * 查询空压机状态
     *
     * @param intelligentBlowerVO
     * @param cardParamValue
     * @return
     */
    private List<IntelligentBlowerVO.MompressorMetric> getMompressorMetrics(IntelligentBlowerVO intelligentBlowerVO, String[] cardParamValue) {
        //空压机状态
        List<IntelligentBlowerVO.MompressorMetric> mompressorMetrics = new ArrayList<>();
        List<String> mompressorList = Lists.newArrayList(cardParamValue[0], cardParamValue[1], cardParamValue[2], cardParamValue[3], cardParamValue[4], cardParamValue[5]);
        for (String mompressor : mompressorList) {
            IntelligentBlowerVO.MompressorMetric mompressorMetric = intelligentBlowerVO.new MompressorMetric();
            mompressorMetric.setThingCode(mompressor);
            //不考虑轻故障，当重故障信号使能时显示【故障】状态，当不是重故障时判断【运行/待机】信号，当为待机时显示【待机】状态，否则判断【加载/卸载】信号并显示对应的状态；
            //0  待机    1 加载  2 卸载  故障3    RunState 用于前端颜色显示 不是真实信号状态
            getMompressorFaultStates(mompressor, mompressorMetric);
            mompressorMetrics.add(mompressorMetric);
        }
        return mompressorMetrics;
    }

    /**
     * 空压机的设备是否有重故障
     *
     * @param mompressor
     * @param mompressorMetric
     */
    private void getMompressorFaultStates(String mompressor, IntelligentBlowerVO.MompressorMetric mompressorMetric) {
        DataModelWrapper mompressorData = dataService.getData(mompressor, FilterPressMetricConstants.FAULT).orElse(null);
        if (mompressorData != null) {
            String faultValue = mompressorData.getValue();
            if (SubscriptionConstants.FAULT_1.equals(faultValue)) {
                mompressorMetric.setRunState("3");
                mompressorMetric.setRunStateName(SubscriptionConstants.FAULT);
            } else {
                //空压机的运行、待机状态
                getMomPressorRunStates(mompressor, mompressorMetric);
            }

        } else {
            mompressorMetric.setRunState("");
            mompressorMetric.setRunStateName("");
        }
    }

    /**
     * 空压机的运行、待机状态
     *
     * @param mompressor
     * @param mompressorMetric
     */
    private void getMomPressorRunStates(String mompressor, IntelligentBlowerVO.MompressorMetric mompressorMetric) {
        DataModelWrapper runStateData = dataService.getData(mompressor, MetricCodes.RUN_STATE).orElse(null);
        if (runStateData != null) {
            String runStates = runStateData.getValue();
            if (SubscriptionConstants.RUN_STATE_START_1.equals(runStates)) {
                //空压机加载，卸载状态
                getMompressorLoadStates(mompressor, mompressorMetric);
            } else if (SubscriptionConstants.RUN_STATE_STOP_0.equals(runStates)) {
                mompressorMetric.setRunState("0");
                mompressorMetric.setRunStateName(SubscriptionConstants.RUN_STATE_STOP);
            } else {
                mompressorMetric.setRunState("");
                mompressorMetric.setRunStateName("");
            }
        }
    }

    /**
     * 空压机加载，卸载状态
     *
     * @param mompressor
     * @param mompressorMetric
     */
    private void getMompressorLoadStates(String mompressor, IntelligentBlowerVO.MompressorMetric mompressorMetric) {
        DataModelWrapper loadStateData = dataService.getData(mompressor, MetricCodes.LOAD_STAT).orElse(null);
        if (loadStateData != null) {
            String loadStates = loadStateData.getValue();
            if (SubscriptionConstants.LOAD_STATE_LOAD_1.equals(loadStates)) {
                mompressorMetric.setRunState("1");
                mompressorMetric.setRunStateName(SubscriptionConstants.LOAD_STATE_LOAD);
            } else if (SubscriptionConstants.LOAD_STATE_LOAD_0.equals(loadStates)) {
                mompressorMetric.setRunState("2");
                mompressorMetric.setRunStateName(SubscriptionConstants.LOAD_STATE_UNLOAD);
            } else {
                mompressorMetric.setRunState("");
                mompressorMetric.setRunStateName("");
            }
        }
    }


    @Override
    public void switchCardType(List<CardDataDTO> cardDataDTOS, SubscCardTypeDO subscCardTypeDO) {
        CardDataDTO cardDataDTO;
        if (subscCardTypeDO.getCardType().equals(CardTypeEnum.HISTORICAL_WASHING_CAPACITY.getCardCode())) {
            cardDataDTO = getHistoricalWashingCapacity(subscCardTypeDO);
            cardDataDTOS.add(cardDataDTO);
        } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.HISTORICAL_PRODUCT_RATE.getCardCode())) {
            cardDataDTO = getHistoricalProductRate(subscCardTypeDO);
            cardDataDTOS.add(cardDataDTO);
        } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.INSTANTANEOUS_WASH.getCardCode())) {
            cardDataDTO = getInstantaneousWash(subscCardTypeDO);
            cardDataDTOS.add(cardDataDTO);
        } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.INSTANTANEOUS_PRODUCT_QUANTITY.getCardCode())) {
            cardDataDTO = getInstantaneousProductQuantity(subscCardTypeDO);
            cardDataDTOS.add(cardDataDTO);
        } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.PRODUCT_YIELD.getCardCode())) {
            cardDataDTO = getProductYield(subscCardTypeDO);
            cardDataDTOS.add(cardDataDTO);
        } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.INTELLIGENT_FILTER.getCardCode())) {
            cardDataDTO = getIntelligentFilter(subscCardTypeDO);
            cardDataDTOS.add(cardDataDTO);
        } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.INTELLIGENT_BLOWER.getCardCode())) {
            cardDataDTO = getIntelligentBlower(subscCardTypeDO);
            cardDataDTOS.add(cardDataDTO);
        } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.MIXTURE_OF_RAW_COAL.getCardCode())) {
            cardDataDTO = getMixtureOfRawCoal(subscCardTypeDO);
            cardDataDTOS.add(cardDataDTO);
        } else if (subscCardTypeDO.getCardType().equals(CardTypeEnum.CHEMICAL_TESTS_DATA.getCardCode())) {
            cardDataDTO = getChemicalTestsData(subscCardTypeDO);
            cardDataDTOS.add(cardDataDTO);
        }
    }

    @Override
    public SubscCardTypeDO getCardTypeByCardCode(String cardCode) {
        return subscCardTypeMapper.getCardTypeByCardCode(cardCode);
    }


}

