package com.zgiot.app.server.module.sfsubsc.controller.handler;


import com.google.common.collect.Lists;
import com.zgiot.app.server.module.historydata.enums.AccuracyEnum;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardTypeDO;
import com.zgiot.app.server.module.sfsubsc.entity.vo.InstantProductDetailVO;
import com.zgiot.app.server.module.sfsubsc.service.SubscCardTypeService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.constants.SubscriptionConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class InstantProductDetailHandler {
    @Value("${sfsubscription.default-segment.web}")
    private int sfsubscriptionDefaultSegmentWeb;

    @Autowired
    private DataService dataService;
    @Autowired
    private HistoryDataService historyDataService;
    @Autowired
    private SubscCardTypeService subscCardTypeService;


    public InstantProductDetailVO getInstantProductDetail(String cardCode, String dateType) {
        // 查询thingCode
        SubscCardTypeDO cardTypeDO = subscCardTypeService.getCardTypeByCardCode(cardCode);
        String[] cardParamValue = cardTypeDO.getCardParamValue().split(",");
        String[] wastRocks = cardParamValue[2].split("\\+");
        InstantProductDetailVO instantProductDetailVO = new InstantProductDetailVO();
        instantProductDetailVO.setRawCoalName(SubscriptionConstants.RAW_COAL);
        instantProductDetailVO.setCleanCoalName(SubscriptionConstants.CLEAN_COAL);
        instantProductDetailVO.setMixedCoadName(SubscriptionConstants.MIXED_COAL);
        instantProductDetailVO.setWasteRockName(SubscriptionConstants.WASTE_ROCK);
        //时间类型 范围
        Date startDate = null;
        Date endDate = null;
        AccuracyEnum accuracyEnum = null;
        if (SubscriptionConstants.DATE_TYPE_HOUR.equals(dateType)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
            startDate = calendar.getTime();
            endDate = new Date();
            accuracyEnum = AccuracyEnum.SECOND;
        } else if (SubscriptionConstants.DATE_TYPE_DAY.equals(dateType)) {
            Calendar todayStart = Calendar.getInstance();
            todayStart.set(Calendar.HOUR_OF_DAY, 0);
            todayStart.set(Calendar.MINUTE, 0);
            todayStart.set(Calendar.SECOND, 0);
            todayStart.set(Calendar.MILLISECOND, 0);
            startDate = todayStart.getTime();
            endDate = new Date();
            accuracyEnum = AccuracyEnum.MINUTE;
        }


        setCoalCapValue(cardParamValue, wastRocks, instantProductDetailVO);
        BigDecimal wasteRockValue = BigDecimal.ZERO;

        List<String> times = new ArrayList<>();
        List<String> rawCoalCapLists = new ArrayList<>();
        List<String> cleanCoalCapLists = new ArrayList<>();
        List<String> mixedCoalCapLists = new ArrayList<>();
        List<DataModel> wasteRockCapLists1 = new ArrayList<>();
        List<DataModel> wasteRockCapLists2 = new ArrayList<>();


        //原煤
        getRawCoalCapValues(cardParamValue[3], startDate, endDate, accuracyEnum, times, rawCoalCapLists);

        //精煤
        getCleanCoalCapValues(cardParamValue[0], startDate, endDate, accuracyEnum, cleanCoalCapLists);


        //混煤
        getMixedCoalCapValues(cardParamValue[1], startDate, endDate, accuracyEnum, mixedCoalCapLists);


        //矸石1
        getWastRock1CapValues(wastRocks[0], startDate, endDate, accuracyEnum, wasteRockCapLists1);


        //矸石2
        getWastRock2CapValues(wastRocks[0], startDate, endDate, accuracyEnum, wasteRockCapLists2);


        //矸石数据累计
        List<String> wasteRockCapLists = getWasteRockCapValue(wasteRockValue, wasteRockCapLists1, wasteRockCapLists2);
        instantProductDetailVO.setRawCoalCapLists(rawCoalCapLists);
        instantProductDetailVO.setCleanCoalCapLists(cleanCoalCapLists);
        instantProductDetailVO.setMixedCoadCapists(mixedCoalCapLists);
        instantProductDetailVO.setWasteRockCapLists(wasteRockCapLists);

        instantProductDetailVO.setTimes(times);
        instantProductDetailVO.setRawCoalAvgValue(String.valueOf(historyDataService.findAvgValueDataInDuration(cardParamValue[3], MetricCodes.COAL_CAP, startDate, endDate, accuracyEnum) == null ? "" : new BigDecimal(historyDataService.findAvgValueDataInDuration(cardParamValue[3], MetricCodes.COAL_CAP, startDate, endDate, accuracyEnum)).setScale(0, BigDecimal.ROUND_HALF_UP)));
        instantProductDetailVO.setCleanCoalAvgValue(String.valueOf(historyDataService.findAvgValueDataInDuration(cardParamValue[0], MetricCodes.COAL_CAP, startDate, endDate, accuracyEnum) == null ? "" : new BigDecimal(historyDataService.findAvgValueDataInDuration(cardParamValue[0], MetricCodes.COAL_CAP, startDate, endDate, accuracyEnum)).setScale(0, BigDecimal.ROUND_HALF_UP)));
        instantProductDetailVO.setMixedCoadAvgValue(String.valueOf(historyDataService.findAvgValueDataInDuration(cardParamValue[1], MetricCodes.COAL_CAP, startDate, endDate, accuracyEnum) == null ? "" : new BigDecimal(historyDataService.findAvgValueDataInDuration(cardParamValue[1], MetricCodes.COAL_CAP, startDate, endDate, accuracyEnum)).setScale(0, BigDecimal.ROUND_HALF_UP)));

        getWastRockCapAvgValue(wastRocks, instantProductDetailVO, startDate, endDate, accuracyEnum);
        return instantProductDetailVO;

    }

    /**
     * 矸石数据集合
     *
     * @param wasteRockValue
     * @param wasteRockCapLists1
     * @param wasteRockCapLists2
     * @return
     */
    private List<String> getWasteRockCapValue(BigDecimal wasteRockValue, List<DataModel> wasteRockCapLists1, List<DataModel> wasteRockCapLists2) {
        List<String> wasteRockCapLists = new ArrayList<>();
        for (DataModel dataModel1 : wasteRockCapLists1) {
            for (DataModel dataModel2 : wasteRockCapLists2) {
                if (dataModel1.getDataTimeStamp().compareTo(dataModel2.getDataTimeStamp()) == 0) {
                    BigDecimal metricValue = new BigDecimal(dataModel1.getValue()).add(new BigDecimal(dataModel2.getValue()));
                    wasteRockCapLists.add(String.valueOf(metricValue));
                    wasteRockValue = wasteRockValue.add(metricValue);
                    break;
                }
            }
        }
        return wasteRockCapLists;
    }

    /**
     * 矸石2 瞬时带煤量
     *
     * @param wastRock
     * @param startDate
     * @param endDate
     * @param accuracyEnum
     * @param wasteRockCapLists2
     */
    private void getWastRock2CapValues(String wastRock, Date startDate, Date endDate, AccuracyEnum accuracyEnum, List<DataModel> wasteRockCapLists2) {
        Long wastRockDataBatchCount2 = historyDataService.getDataBatchCount(startDate, endDate, wastRock, MetricCodes.COAL_CAP, accuracyEnum);
        List<DataModel> wastRockHistoryDataList2 = new ArrayList<>();
        if (wastRockDataBatchCount2 < sfsubscriptionDefaultSegmentWeb) {
            wastRockHistoryDataList2 = historyDataService.findHistoryDataList(Lists.newArrayList(wastRock), Lists.newArrayList(MetricCodes.COAL_CAP), startDate, endDate, accuracyEnum);
        } else {
            Map<String, List<DataModel>> metricBySegmentMap =
                    historyDataService.findMultiThingsHistoryDataOfMetricBySegment(Lists.newArrayList(wastRock), MetricCodes.COAL_CAP, startDate, endDate, sfsubscriptionDefaultSegmentWeb, false, accuracyEnum);
            if (metricBySegmentMap != null) {
                wastRockHistoryDataList2 = metricBySegmentMap.get(wastRock);
            }
        }

        for (DataModel dataModel : wastRockHistoryDataList2) {
            wasteRockCapLists2.add(dataModel);
        }
    }

    /**
     * 矸石瞬时带煤量平均值
     *
     * @param wastRocks
     * @param instantProductDetailVO
     * @param startDate
     * @param endDate
     * @param accuracyEnum
     */
    private void getWastRockCapAvgValue(String[] wastRocks, InstantProductDetailVO instantProductDetailVO, Date startDate, Date endDate, AccuracyEnum accuracyEnum) {
        BigDecimal wastRocksAvg1 = BigDecimal.ZERO;
        BigDecimal wastRocksAvg2 = BigDecimal.ZERO;

        Double avgValue1 = historyDataService.findAvgValueDataInDuration(wastRocks[0], MetricCodes.COAL_CAP, startDate, endDate, accuracyEnum);
        if (avgValue1 != null) {
            wastRocksAvg1 = new BigDecimal(avgValue1);
        }
        Double avgValue2 = historyDataService.findAvgValueDataInDuration(wastRocks[1], MetricCodes.COAL_CAP, startDate, endDate, accuracyEnum);
        if (avgValue2 != null) {
            wastRocksAvg2 = new BigDecimal(avgValue2);
        }
        if (wastRocksAvg1.add(wastRocksAvg2).compareTo(BigDecimal.ZERO) > 0) {
            instantProductDetailVO.setWasteRockAvgValue(String.valueOf(wastRocksAvg1.add(wastRocksAvg2).divide(new BigDecimal(2), 0, BigDecimal.ROUND_HALF_UP)).equals("0") ? "" : String.valueOf(wastRocksAvg1.add(wastRocksAvg2).divide(new BigDecimal(2), 0, BigDecimal.ROUND_HALF_UP)));
        } else {
            instantProductDetailVO.setWasteRockAvgValue("");
        }
    }

    /**
     * 矸石1 瞬时带煤量
     *
     * @param wastRock
     * @param startDate
     * @param endDate
     * @param accuracyEnum
     * @param wasteRockCapLists1
     */
    private void getWastRock1CapValues(String wastRock, Date startDate, Date endDate, AccuracyEnum accuracyEnum, List<DataModel> wasteRockCapLists1) {
        Long wastRockDataBatchCount1 = historyDataService.getDataBatchCount(startDate, endDate, wastRock, MetricCodes.COAL_CAP, accuracyEnum);
        List<DataModel> wastRockHistoryDataList1 = new ArrayList<>();
        if (wastRockDataBatchCount1 < sfsubscriptionDefaultSegmentWeb) {
            wastRockHistoryDataList1 = historyDataService.findHistoryDataList(Lists.newArrayList(wastRock), Lists.newArrayList(MetricCodes.COAL_CAP), startDate, endDate, accuracyEnum);
        } else {
            Map<String, List<DataModel>> metricBySegmentMap =
                    historyDataService.findMultiThingsHistoryDataOfMetricBySegment(Lists.newArrayList(wastRock), MetricCodes.COAL_CAP, startDate, endDate, sfsubscriptionDefaultSegmentWeb, false, accuracyEnum);
            if (metricBySegmentMap != null) {
                wastRockHistoryDataList1 = metricBySegmentMap.get(wastRock);
            }
        }

        for (DataModel dataModel : wastRockHistoryDataList1) {
            wasteRockCapLists1.add(dataModel);
        }
    }

    /**
     * 混煤 瞬时带煤量数据
     *
     * @param thingCode
     * @param startDate
     * @param endDate
     * @param accuracyEnum
     * @param mixedCoalCapLists
     */
    private void getMixedCoalCapValues(String thingCode, Date startDate, Date endDate, AccuracyEnum accuracyEnum, List<String> mixedCoalCapLists) {
        Long mixedCoalDataBatchCount = historyDataService.getDataBatchCount(startDate, endDate, thingCode, MetricCodes.COAL_CAP, accuracyEnum);
        List<DataModel> mixedCoalHistoryDataList = new ArrayList<>();
        if (mixedCoalDataBatchCount < sfsubscriptionDefaultSegmentWeb) {
            mixedCoalHistoryDataList = historyDataService.findHistoryDataList(Lists.newArrayList(thingCode), Lists.newArrayList(MetricCodes.COAL_CAP), startDate, endDate, accuracyEnum);
        } else {
            Map<String, List<DataModel>> metricBySegmentMap =
                    historyDataService.findMultiThingsHistoryDataOfMetricBySegment(Lists.newArrayList(thingCode), MetricCodes.COAL_CAP, startDate, endDate, sfsubscriptionDefaultSegmentWeb, false, accuracyEnum);
            if (metricBySegmentMap != null) {
                mixedCoalHistoryDataList = metricBySegmentMap.get(thingCode);
            }
        }

        for (DataModel dataModel : mixedCoalHistoryDataList) {
            mixedCoalCapLists.add(dataModel.getValue());
        }
    }

    /**
     * 精煤 瞬时带煤量数据
     *
     * @param thingCode
     * @param startDate
     * @param endDate
     * @param accuracyEnum
     * @param cleanCoalCapLists
     */
    private void getCleanCoalCapValues(String thingCode, Date startDate, Date endDate, AccuracyEnum accuracyEnum, List<String> cleanCoalCapLists) {
        Long cleanCoalDataBatchCount = historyDataService.getDataBatchCount(startDate, endDate, thingCode, MetricCodes.COAL_CAP, accuracyEnum);
        List<DataModel> cleanCoalHistoryDataList = new ArrayList<>();
        if (cleanCoalDataBatchCount < sfsubscriptionDefaultSegmentWeb) {
            cleanCoalHistoryDataList = historyDataService.findHistoryDataList(Lists.newArrayList(thingCode), Lists.newArrayList(MetricCodes.COAL_CAP), startDate, endDate, accuracyEnum);
        } else {
            Map<String, List<DataModel>> metricBySegmentMap =
                    historyDataService.findMultiThingsHistoryDataOfMetricBySegment(Lists.newArrayList(thingCode), MetricCodes.COAL_CAP, startDate, endDate, sfsubscriptionDefaultSegmentWeb, false, accuracyEnum);
            if (metricBySegmentMap != null) {
                cleanCoalHistoryDataList = metricBySegmentMap.get(thingCode);
            }
        }

        for (DataModel dataModel : cleanCoalHistoryDataList) {
            cleanCoalCapLists.add(dataModel.getValue());
        }
    }

    /**
     * 原煤 瞬时带煤量数据
     *
     * @param thingCode
     * @param startDate
     * @param endDate
     * @param accuracyEnum
     * @param times
     * @param rawCoalCapLists
     */
    private void getRawCoalCapValues(String thingCode, Date startDate, Date endDate, AccuracyEnum accuracyEnum, List<String> times, List<String> rawCoalCapLists) {
        Long rawCoalDataBatchCount = historyDataService.getDataBatchCount(startDate, endDate, thingCode, MetricCodes.COAL_CAP, accuracyEnum);
        List<DataModel> rawCoalHistoryDataList = new ArrayList<>();
        if (rawCoalDataBatchCount < sfsubscriptionDefaultSegmentWeb) {
            rawCoalHistoryDataList = historyDataService.findHistoryDataList(Lists.newArrayList(thingCode), Lists.newArrayList(MetricCodes.COAL_CAP), startDate, endDate, accuracyEnum);
        } else {
            Map<String, List<DataModel>> metricBySegmentMap =
                    historyDataService.findMultiThingsHistoryDataOfMetricBySegment(Lists.newArrayList(thingCode), MetricCodes.COAL_CAP, startDate, endDate, sfsubscriptionDefaultSegmentWeb, false, accuracyEnum);
            if (metricBySegmentMap != null) {
                rawCoalHistoryDataList = metricBySegmentMap.get(thingCode);
            }
        }

        for (DataModel dataModel : rawCoalHistoryDataList) {
            times.add(new SimpleDateFormat("HH:mm:ss").format(dataModel.getDataTimeStamp()));
            rawCoalCapLists.add(dataModel.getValue());
        }
    }

    /**
     * 查询瞬时信号值
     *
     * @param cardParamValue
     * @param wastRocks
     * @param instantProductDetailVO
     */
    private void setCoalCapValue(String[] cardParamValue, String[] wastRocks, InstantProductDetailVO instantProductDetailVO) {
        DataModelWrapper rawCoalDataModelWrapper = dataService.getData(cardParamValue[3], MetricCodes.COAL_CAP).orElse(null);
        if (rawCoalDataModelWrapper != null) {
            instantProductDetailVO.setRawCoalCapValue(String.valueOf(new BigDecimal(rawCoalDataModelWrapper.getValue()).setScale(0)));
        } else {
            instantProductDetailVO.setRawCoalCapValue("");
        }
        DataModelWrapper cleanCoalDataModelWrapper = dataService.getData(cardParamValue[0], MetricCodes.COAL_CAP).orElse(null);
        if (cleanCoalDataModelWrapper != null) {
            instantProductDetailVO.setCleanCoalCapValue(String.valueOf(new BigDecimal(cleanCoalDataModelWrapper.getValue()).setScale(0)));
        } else {
            instantProductDetailVO.setCleanCoalCapValue("");
        }
        DataModelWrapper mixedCoalDataModelWrapper = dataService.getData(cardParamValue[1], MetricCodes.COAL_CAP).orElse(null);
        if (mixedCoalDataModelWrapper != null) {
            instantProductDetailVO.setMixedCoadCapValue(String.valueOf(new BigDecimal(mixedCoalDataModelWrapper.getValue()).setScale(0)));
        } else {
            instantProductDetailVO.setMixedCoadCapValue("");
        }

        BigDecimal wastRockValue = BigDecimal.ZERO;
        DataModelWrapper wastRockDataModelWrapper1 = dataService.getData(wastRocks[0], MetricCodes.COAL_CAP).orElse(null);
        if (wastRockDataModelWrapper1 != null) {
            wastRockValue = wastRockValue.add(new BigDecimal(wastRockDataModelWrapper1.getValue()));
        }
        DataModelWrapper wastRockDataModelWrapper2 = dataService.getData(wastRocks[1], MetricCodes.COAL_CAP).orElse(null);
        if (wastRockDataModelWrapper2 != null) {
            wastRockValue = wastRockValue.add(new BigDecimal(wastRockDataModelWrapper2.getValue()));
        }
        instantProductDetailVO.setWasteRockCapValue(String.valueOf(wastRockValue).equals("0") ? "" : String.valueOf(wastRockValue.setScale(0)));
    }
}


