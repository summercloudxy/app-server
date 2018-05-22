package com.zgiot.app.server.module.sfsubsc.controller.handler;

import com.zgiot.app.server.module.coalanalysis.mapper.CoalAnalysisMapper;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardTypeDO;
import com.zgiot.app.server.module.sfsubsc.entity.vo.*;
import com.zgiot.app.server.module.sfsubsc.mapper.SubscCardTypeMapper;
import com.zgiot.app.server.module.sfsubsc.service.SubscCardTypeService;
import com.zgiot.app.server.module.sfsubsc.util.MetricValueUtil;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.constants.SubscriptionConstants;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zgiot.app.server.module.sfsubsc.enums.CardTypeEnum.COAL_QUALITY;
import static com.zgiot.app.server.module.sfsubsc.enums.CardTypeEnum.PRODUCTION;
import static com.zgiot.app.server.module.tcs.pojo.FilterCondition.FilterConditionBuilder.newFilterCondition;
import static com.zgiot.common.constants.SubscriptionConstants.*;

/**
 * 化验数据处理
 *
 * @author jys
 */
@Component
public class CoalAnalysisDataDetailHandler {

    private static final Logger logger = LoggerFactory.getLogger(CoalAnalysisDataDetailHandler.class);
    @Autowired
    private SubscCardTypeService subscCardTypeService;
    @Autowired
    private CoalAnalysisMapper coalAnalysisMapper;
    @Autowired
    private SubscCardTypeMapper subscCardTypeMapper;
    @Autowired
    private HistoryDataService historyDataService;

    public ResponseEntity<String> getCoalAnalysisDataChartDetail(String cardCode, String dateType, String chartType) {
        SubscCardTypeDO cardTypeDO = subscCardTypeService.getCardTypeByCardCode(cardCode);
        String[] cardParamValue = cardTypeDO.getCardParamValue().split(",");
        Date startDate = null;
        Date endDate = null;
        if (SubscriptionConstants.DATE_TYPE_DAY.equals(dateType)) {
            Calendar todayStart = Calendar.getInstance();
            todayStart.set(Calendar.HOUR_OF_DAY, 0);
            todayStart.set(Calendar.MINUTE, 0);
            todayStart.set(Calendar.SECOND, 0);
            todayStart.set(Calendar.MILLISECOND, 0);
            startDate = todayStart.getTime();
            endDate = new Date();

        } else if (SubscriptionConstants.DATE_TYPE_WEEK.equals(dateType)) {
            Calendar sevenc = Calendar.getInstance();  //获取最近一周的日期
            sevenc.setTime(new Date());
            sevenc.add(Calendar.DATE, -6);
            startDate = sevenc.getTime();
            endDate = new Date();

        } else if (SubscriptionConstants.DATE_TYPE_MONTH.equals(dateType)) {
            Calendar onemonth = Calendar.getInstance();//获取最近30的日期
            onemonth.setTime(new Date());
            onemonth.add(Calendar.DATE, -29);
            startDate = onemonth.getTime();
            endDate = new Date();
        }
        if (SubscriptionConstants.CHART_TYPE.equals(chartType)) {
            ChemicalTestsDataChartDetailVO chemicalTestsDataChartDetailVO = getCoalAnalysisDataChartDetail(cardParamValue, startDate, endDate);
            return new ResponseEntity<>(ServerResponse.buildOkJson(chemicalTestsDataChartDetailVO), HttpStatus.OK);
        } else if (SubscriptionConstants.LIST_TYPE.equals(chartType)) {
            List<ChemicalTestsDataListDetailVO> chemicalTestsDataLIstDetailVOList = getCoalAnalysisDataListDetail(cardParamValue, startDate, endDate);
            return new ResponseEntity<>(ServerResponse.buildOkJson(chemicalTestsDataLIstDetailVOList), HttpStatus.OK);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(""), HttpStatus.FORBIDDEN);
    }

    /**
     * 查询化验数据列表
     *
     * @param cardParamValue
     * @param startDate
     * @param endDate
     */
    private List<ChemicalTestsDataListDetailVO> getCoalAnalysisDataListDetail(String[] cardParamValue, Date startDate, Date endDate) {

        List<CoalAnalysisRecord> allAnalysisRecords = new ArrayList<>();
        FilterCondition cleanCoalfilterCondition = newFilterCondition().startTime(startDate).endTime(endDate).addTarget(SubscriptionConstants.CLEAN_COAL_551).build();
        cleanCoalfilterCondition.setSample(cardParamValue[0]);
        List<CoalAnalysisRecord> cleanCoalAnalysisRecords = coalAnalysisMapper.getRecordsMatchCondition(cleanCoalfilterCondition);

        FilterCondition mixedCoadfilterCondition = newFilterCondition().startTime(startDate).endTime(endDate).addTarget(SubscriptionConstants.MIXE_COAL_552).build();
        mixedCoadfilterCondition.setSample(cardParamValue[1]);
        List<CoalAnalysisRecord> mixedCoalAnalysisRecords = coalAnalysisMapper.getRecordsMatchCondition(mixedCoadfilterCondition);

        allAnalysisRecords.addAll(cleanCoalAnalysisRecords);
        allAnalysisRecords.addAll(mixedCoalAnalysisRecords);


        Map<Date, List<CoalAnalysisRecord>> maps = new HashMap<>();
        for (CoalAnalysisRecord coalAnalysisRecord : allAnalysisRecords) {
            Date key = coalAnalysisRecord.getTime();
            if (maps.containsKey(key)) {
                List<CoalAnalysisRecord> list = maps.get(key);
                list.add(coalAnalysisRecord);
            } else {
                List<CoalAnalysisRecord> list = new ArrayList<>();
                list.add(coalAnalysisRecord);
                maps.put(key, list);
            }
        }
        List<ChemicalTestsDataListDetailVO> chemicalTestsDataListDetailVOList = new ArrayList<>();
        for (Map.Entry<Date, List<CoalAnalysisRecord>> entry : maps.entrySet()) {
            ChemicalTestsDataListDetailVO chemicalTestsDataLIstDetailVO = new ChemicalTestsDataListDetailVO();
            chemicalTestsDataLIstDetailVO.setCollectDate(new SimpleDateFormat("yyyy-MM-dd").format(entry.getKey()));
            chemicalTestsDataLIstDetailVO.setCollectTime(new SimpleDateFormat("HH:mm").format(entry.getKey()));
            List<CoalAnalysisRecord> coalAnalysisRecords = entry.getValue();
            for (CoalAnalysisRecord coalAnalysisRecord : coalAnalysisRecords) {
                if (coalAnalysisRecord.getSample().equals(cardParamValue[0])) {
                    chemicalTestsDataLIstDetailVO.setCleanCoalAad(String.valueOf(coalAnalysisRecord.getAad()));
                    chemicalTestsDataLIstDetailVO.setCleanCoalStad(String.valueOf(coalAnalysisRecord.getStad()));
                } else if (coalAnalysisRecord.getSample().equals(cardParamValue[1])) {
                    chemicalTestsDataLIstDetailVO.setMixedCoalAad(String.valueOf(coalAnalysisRecord.getAad()));
                    chemicalTestsDataLIstDetailVO.setMixedCoalMt(String.valueOf(coalAnalysisRecord.getMt()));

                }
            }
            chemicalTestsDataListDetailVOList.add(chemicalTestsDataLIstDetailVO);
        }
        return chemicalTestsDataListDetailVOList;
    }

    /**
     * 查询化验数据折线图
     *
     * @param cardParamValue
     * @param startDate
     * @param endDate
     */
    private ChemicalTestsDataChartDetailVO getCoalAnalysisDataChartDetail(String[] cardParamValue, Date
            startDate, Date endDate) {

        ChemicalTestsDataChartDetailVO chemicalTestsDataChartDetailVO = new ChemicalTestsDataChartDetailVO();

        chemicalTestsDataChartDetailVO.setCleanCoalName(SubscriptionConstants.CLEAN_COAL);
        chemicalTestsDataChartDetailVO.setMixedCoadName(SubscriptionConstants.MIXED_COAL);
        chemicalTestsDataChartDetailVO.setCleanCoalAadName(SubscriptionConstants.ASSAY_AAD_NAME);
        chemicalTestsDataChartDetailVO.setMixedCoadAadName(SubscriptionConstants.ASSAY_AAD_NAME);
        //精煤 混煤 灰分数据
        FilterCondition cleanCoalfilterCondition = newFilterCondition().startTime(startDate).endTime(endDate).addTarget(SubscriptionConstants.CLEAN_COAL_551).sortType(0).build();
        cleanCoalfilterCondition.setSample(cardParamValue[0]);
        List<CoalAnalysisRecord> cleanCoalAnalysisRecords = coalAnalysisMapper.getRecordsMatchCondition(cleanCoalfilterCondition);


        FilterCondition mixedCoadfilterCondition = newFilterCondition().startTime(startDate).endTime(endDate).addTarget(SubscriptionConstants.MIXE_COAL_552).sortType(0).build();
        mixedCoadfilterCondition.setSample(cardParamValue[1]);
        List<CoalAnalysisRecord> mixedCoalAnalysisRecords = coalAnalysisMapper.getRecordsMatchCondition(mixedCoadfilterCondition);

        if (CollectionUtils.isNotEmpty(cleanCoalAnalysisRecords)) {
            chemicalTestsDataChartDetailVO.setCleanCoalAadValue(String.valueOf(cleanCoalAnalysisRecords.get(cleanCoalAnalysisRecords.size() - 1).getAad()));
        } else {
            chemicalTestsDataChartDetailVO.setCleanCoalAadValue("");
        }

        if (CollectionUtils.isNotEmpty(mixedCoalAnalysisRecords)) {
            chemicalTestsDataChartDetailVO.setMixedCoadAadValue(String.valueOf(mixedCoalAnalysisRecords.get(mixedCoalAnalysisRecords.size() - 1).getAad()));
        } else {
            chemicalTestsDataChartDetailVO.setMixedCoadAadValue("");
        }


        List<String> times = new ArrayList<>();
        List<String> cleanCoalAadValues = new ArrayList<>();
        List<String> cleanCoalStadValues = new ArrayList<>();
        List<String> mixedCoalAadValues = new ArrayList<>();
        List<String> mixedCoalMtValues = new ArrayList<>();

        BigDecimal cleanCoalAadValue = BigDecimal.ZERO;
        BigDecimal mixedCoalAadValue = BigDecimal.ZERO;

        for (CoalAnalysisRecord coalAnalysisRecord : cleanCoalAnalysisRecords) {
            times.add(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(coalAnalysisRecord.getTime()));
            cleanCoalAadValues.add(String.valueOf(coalAnalysisRecord.getAad()));
            cleanCoalStadValues.add(String.valueOf(coalAnalysisRecord.getStad()));
            cleanCoalAadValue = cleanCoalAadValue.add(new BigDecimal(coalAnalysisRecord.getAad()));
        }
        for (CoalAnalysisRecord coalAnalysisRecord : mixedCoalAnalysisRecords) {
            mixedCoalAadValues.add(String.valueOf(coalAnalysisRecord.getAad()));
            mixedCoalMtValues.add(String.valueOf(coalAnalysisRecord.getMt()));
            mixedCoalAadValue = mixedCoalAadValue.add(new BigDecimal(coalAnalysisRecord.getAad()));
        }

        if (mixedCoalAadValue.compareTo(BigDecimal.ZERO) > 0) {
            chemicalTestsDataChartDetailVO.setCleanCoalAadAvgValue(String.valueOf(cleanCoalAadValue.divide(new BigDecimal(cleanCoalAadValues.size()), 2, BigDecimal.ROUND_HALF_UP)));
        } else {
            chemicalTestsDataChartDetailVO.setCleanCoalAadAvgValue("");
        }


        if (cleanCoalAadValue.compareTo(BigDecimal.ZERO) > 0) {
            chemicalTestsDataChartDetailVO.setMixedCoadAadAvgValue(String.valueOf(mixedCoalAadValue.divide(new BigDecimal(mixedCoalAadValues.size()), 2, BigDecimal.ROUND_HALF_UP)));
        } else {
            chemicalTestsDataChartDetailVO.setMixedCoadAadAvgValue("");
        }


        chemicalTestsDataChartDetailVO.setTimes(times);
        chemicalTestsDataChartDetailVO.setCleanCoalAadValues(cleanCoalAadValues);
        chemicalTestsDataChartDetailVO.setCleanCoalStadValues(cleanCoalStadValues);
        chemicalTestsDataChartDetailVO.setMixedCoalAadValues(mixedCoalAadValues);
        chemicalTestsDataChartDetailVO.setMixedCoalMtValues(mixedCoalMtValues);
        chemicalTestsDataChartDetailVO.setAshContentCleanCoalScope(SubscriptionConstants.ASH_CONTENT_CLEAN_COAL_SCOPE);
        chemicalTestsDataChartDetailVO.setAshContentMixedCoalScope(SubscriptionConstants.ASH_CONTENT_MIXED_COAL_SCOPE);
        chemicalTestsDataChartDetailVO.setSulfurContentScope(SubscriptionConstants.SULFUR_CONTENT_SCOPE);
        chemicalTestsDataChartDetailVO.setMoistureContentScope(SubscriptionConstants.MOISTURE_CONTENT_SCOPE);

        return chemicalTestsDataChartDetailVO;
    }

    /**
     * 班累计煤质统计详情
     *
     * @param date  日期
     * @param shift 班次
     * @return
     */
    public CoalQualityDetailVO getCoalQualityDetail(String date, String shift) {
        CoalQualityDetailVO coalQualityDetailVO = new CoalQualityDetailVO();
        coalQualityDetailVO.setCardTitle(COAL_QUALITY.getCardTypeName());
        coalQualityDetailVO.setDate(date);
        coalQualityDetailVO.setShift(shift);

        // 1.设置班次开始和结束时间
        setShiftTime(coalQualityDetailVO);

        if (coalQualityDetailVO.getShiftTimeBegin().getTime() > new Date().getTime()) {
            return null;
        }

        // 2.一期生产指标
        CoalAnalysisRecord p11 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_ONE, WASH_RAW_COAL, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// 原煤
        CoalAnalysisRecord p12 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_ONE, CLEAN_COAL_551, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// 精煤
        CoalAnalysisRecord p13 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_ONE, MIXE_COAL_552, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// 混煤
        CoalAnalysisRecord p14 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_ONE, FILTERPRESS_SLURRY, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// 煤泥
        List<CoalQualityDetailVO.MetricData> production1 = new ArrayList<>();
        getMetricData(production1, coalQualityDetailVO, p11, RAW_COAL);
        getMetricData(production1, coalQualityDetailVO, p12, CLEAN_COAL);
        getMetricData(production1, coalQualityDetailVO, p13, MIXED_COAL);
        getMetricData(production1, coalQualityDetailVO, p14, SLURRY);
        coalQualityDetailVO.setProduction1(production1);

        // 3.二期生产指标
        CoalAnalysisRecord p21 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_TWO, WASH_RAW_COAL, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// 原煤
        CoalAnalysisRecord p22 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_TWO, CLEAN_COAL_551, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// 精煤
        CoalAnalysisRecord p23 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_TWO, MIXE_COAL_552, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// 混煤
        CoalAnalysisRecord p24 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_TWO, FILTERPRESS_SLURRY, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// 煤泥
        List<CoalQualityDetailVO.MetricData> production2 = new ArrayList<>();
        getMetricData(production2, coalQualityDetailVO, p21, RAW_COAL);
        getMetricData(production2, coalQualityDetailVO, p22, CLEAN_COAL);
        getMetricData(production2, coalQualityDetailVO, p23, MIXED_COAL);
        getMetricData(production2, coalQualityDetailVO, p24, SLURRY);
        coalQualityDetailVO.setProduction2(production2);

        // TODO 4.一期矸石带煤
        CoalQualityDetailVO.RockData rockData1 = coalQualityDetailVO.new RockData();
        rockData1.setPowderRock("");
        rockData1.setBlockRock("");
        coalQualityDetailVO.setRock1(rockData1);

        // TODO 5.二期矸石带煤
        CoalQualityDetailVO.RockData rockData2 = coalQualityDetailVO.new RockData();
        rockData2.setPowderRock("");
        rockData2.setBlockRock("");
        coalQualityDetailVO.setRock2(rockData2);

        // 6.一期TCS
        CoalAnalysisRecord tcs11 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_ONE, TCS_ORE_FINE, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// TCS精矿
        CoalAnalysisRecord tcs12 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_ONE, TCS_ORE_CRUDE, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// TCS尾矿
        List<CoalQualityDetailVO.MetricData> tcs1 = new ArrayList<>();
        getMetricData(tcs1, coalQualityDetailVO, tcs11, ORE_FINE);
        getMetricData(tcs1, coalQualityDetailVO, tcs12, ORE_CRUDE);
        coalQualityDetailVO.setTcs1(tcs1);

        // 7.二期TCS
        CoalAnalysisRecord tcs21 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_TWO, TCS_ORE_FINE, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// TCS精矿
        CoalAnalysisRecord tcs22 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_TWO, TCS_ORE_CRUDE, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// TCS尾矿
        List<CoalQualityDetailVO.MetricData> tcs2 = new ArrayList<>();
        getMetricData(tcs2, coalQualityDetailVO, tcs21, ORE_FINE);
        getMetricData(tcs2, coalQualityDetailVO, tcs22, ORE_CRUDE);
        coalQualityDetailVO.setTcs2(tcs2);

        // TODO 8.销售煤

        return coalQualityDetailVO;
    }

    /**
     * 设置班次开始和结束时间
     *
     * @param coalQualityDetailVO
     */
    private void setShiftTime(CoalQualityDetailVO coalQualityDetailVO) {
        List<Date> dateList = null;
        if (WHITE_SHIFT.equals(coalQualityDetailVO.getShift())) {// 白班
            setWhiteShiftTime(coalQualityDetailVO);

            // 获取该班次所有时间
            dateList = coalAnalysisMapper.getTimeRangeTime(MIXE_COAL_552, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());
        }
        if (NIGHT_SHIFT.equals(coalQualityDetailVO.getShift())) {// 夜班
            setNightShiftTime(coalQualityDetailVO);

            // 获取该班次所有时间
            dateList = coalAnalysisMapper.getTimeRangeTime(MIXE_COAL_552, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());
        }
        // 设置班次时间
        if (dateList != null && !dateList.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            coalQualityDetailVO.setTimeBegin(sdf.format(dateList.get(0)));
            coalQualityDetailVO.setTimeEnd(sdf.format(dateList.get(dateList.size() - 1)));
        } else {
            coalQualityDetailVO.setTimeBegin("");
            coalQualityDetailVO.setTimeEnd("");
        }
    }

    private void setWhiteShiftTime(DetailParent detailParent) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            calendar.setTime(sdf.parse(detailParent.getDate()));
        } catch (ParseException e) {
            logger.error("班累计详情-白班日期转换异常", e);
        }
        calendar.set(Calendar.HOUR_OF_DAY, WHITE_SHIFT_BEGIN);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        detailParent.setShiftTimeBegin(calendar.getTime());

        calendar.set(Calendar.HOUR_OF_DAY, WHITE_SHIFT_END);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        detailParent.setShiftTimeEnd(calendar.getTime());
    }

    private void setNightShiftTime(DetailParent detailParent) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            calendar.setTime(sdf.parse(detailParent.getDate()));
        } catch (ParseException e) {
            logger.error("班累计详情-夜班日期转换异常1", e);
        }
        calendar.set(Calendar.HOUR_OF_DAY, NIGHT_SHIFT_BEGIN);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        detailParent.setShiftTimeBegin(calendar.getTime());


        try {
            calendar.setTime(DateUtils.addDays(sdf.parse(detailParent.getDate()), 1));
        } catch (ParseException e) {
            logger.error("班累计详情-夜班日期转换异常2", e);
        }
        calendar.set(Calendar.HOUR_OF_DAY, NIGHT_SHIFT_END);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        detailParent.setShiftTimeEnd(calendar.getTime());
    }

    private void getMetricData(List<CoalQualityDetailVO.MetricData> list, CoalQualityDetailVO coalQualityDetailVO,
                               CoalAnalysisRecord car, String productionName) {
        CoalQualityDetailVO.MetricData metricData = coalQualityDetailVO.new MetricData();
        metricData.setProductionName(productionName);
        metricData.setAad("");
        metricData.setMt("");
        metricData.setStad("");
        metricData.setQar("");
        if (null != car) {
            metricData.setAad(MetricValueUtil.formartPoint2(car.getAad() == null ? 0 : car.getAad()));
            metricData.setMt(MetricValueUtil.formartPoint2(car.getMt() == null ? 0 : car.getMt()));
            metricData.setStad(MetricValueUtil.formartPoint2(car.getStad() == null ? 0 : car.getStad()));
            metricData.setQar(MetricValueUtil.formartPoint2(car.getQnetar() == null ? 0 : car.getQnetar()));
        }
        list.add(metricData);
    }

    /**
     * 班累计生产统计详情
     *
     * @param date
     * @param shift
     * @return
     */
    public ProductionDetailVO getProductionDetail(String date, String shift) {
        ProductionDetailVO productionDetailVO = new ProductionDetailVO();
        productionDetailVO.setCardTitle(PRODUCTION.getCardTypeName());
        productionDetailVO.setDate(date);
        productionDetailVO.setShift(shift);

        if (WHITE_SHIFT.equals(productionDetailVO.getShift())) {// 白班
            setWhiteShiftTime(productionDetailVO);
        }
        if (NIGHT_SHIFT.equals(productionDetailVO.getShift())) {// 夜班
            setNightShiftTime(productionDetailVO);
        }
        productionDetailVO.setTimeBegin("");
        productionDetailVO.setTimeEnd("");

        if (productionDetailVO.getShiftTimeBegin().getTime() > new Date().getTime()) {
            return null;
        }

        // 获取相关设备编号
        SubscCardTypeDO subscCardTypeDO = subscCardTypeMapper.getCardTypeByCardCode(PRODUCTION.getCardCode());
        String[] thingCodesArr = subscCardTypeDO.getCardParamValue().split(";");

        String metricCode = MetricCodes.CT_C;
        Date startDate = productionDetailVO.getShiftTimeBegin();
        Date endDate = productionDetailVO.getShiftTimeEnd();

        // 1.生产量
        List<ProductionDetailVO.MetricData> productionQuantity = new ArrayList<>();

        // 原煤
        Map<String, List<DataModel>> totalMap = historyDataService.findMultiThingsHistoryDataOfMetric(
                Arrays.asList(thingCodesArr[0].split(",")), metricCode, startDate, endDate);
        ProductionDetailVO.MetricData rawCoal = productionDetailVO.new MetricData();
        rawCoal.setName(RAW_COAL);
        BigDecimal total = getDataValue(totalMap);
        rawCoal.setValue1(MetricValueUtil.formartPoint2(total));
        productionQuantity.add(rawCoal);

        // 精煤
        Map<String, List<DataModel>> cleanCoalMap = historyDataService.findMultiThingsHistoryDataOfMetric(
                Arrays.asList(thingCodesArr[1].split(",")), metricCode, startDate, endDate);
        ProductionDetailVO.MetricData cleanCoal = productionDetailVO.new MetricData();
        cleanCoal.setName(CLEAN_COAL);
        BigDecimal clean = getDataValue(cleanCoalMap);
        BigDecimal cleanYield = total.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : (new BigDecimal(100).multiply(clean).divide(total, 2, BigDecimal.ROUND_HALF_UP));
        cleanCoal.setValue1(MetricValueUtil.formartPoint2(clean));
        cleanCoal.setValue2(MetricValueUtil.formartPoint2(cleanYield));
        productionQuantity.add(cleanCoal);

        // 混煤
        Map<String, List<DataModel>> mixedCoalMap = historyDataService.findMultiThingsHistoryDataOfMetric(
                Arrays.asList(thingCodesArr[2].split(",")), metricCode, startDate, endDate);
        ProductionDetailVO.MetricData mixedCoal = productionDetailVO.new MetricData();
        mixedCoal.setName(MIXED_COAL);
        BigDecimal mixed = getDataValue(mixedCoalMap);
        BigDecimal mixedYield = total.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : (new BigDecimal(100).multiply(mixed).divide(total, 2, BigDecimal.ROUND_HALF_UP));
        mixedCoal.setValue1(MetricValueUtil.formartPoint2(mixed));
        mixedCoal.setValue2(MetricValueUtil.formartPoint2(mixedYield));
        productionQuantity.add(mixedCoal);

        // 煤泥
        Map<String, List<DataModel>> coalMudMap = historyDataService.findMultiThingsHistoryDataOfMetric(
                Arrays.asList(thingCodesArr[3].split(",")), metricCode, startDate, endDate);
        ProductionDetailVO.MetricData coalMud = productionDetailVO.new MetricData();
        coalMud.setName(SLURRY);
        BigDecimal mud = getDataValue(coalMudMap);
        BigDecimal mudYield = total.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : (new BigDecimal(100).multiply(mud).divide(total, 2, BigDecimal.ROUND_HALF_UP));
        coalMud.setValue1(MetricValueUtil.formartPoint2(mud));
        coalMud.setValue2(MetricValueUtil.formartPoint2(mudYield));
        productionQuantity.add(coalMud);

        // 综合产率
        String totalYield = MetricValueUtil.formartPoint2(cleanYield.add(mixedYield).add(mudYield));
        productionQuantity.get(0).setValue2(totalYield);

        productionDetailVO.setProductionQuantity(productionQuantity);

        // 2.库存量
        List<ProductionDetailVO.MetricData> stockQuantity = new ArrayList<>();
        // 8#
        ProductionDetailVO.MetricData coal8 = productionDetailVO.new MetricData();
        coal8.setName(COAL_8);
        coal8.setValue1("");
        stockQuantity.add(coal8);

        // 13#
        ProductionDetailVO.MetricData coal13 = productionDetailVO.new MetricData();
        coal13.setName(COAL_13);
        coal13.setValue1("");
        stockQuantity.add(coal13);

        // 精煤仓
        Map<String, List<DataModel>> cleanStockMap = historyDataService.findMultiThingsHistoryDataOfMetric(
                Arrays.asList(thingCodesArr[4].split(",")), MetricCodes.AMOUNT, startDate, endDate);
        ProductionDetailVO.MetricData cleanStock = productionDetailVO.new MetricData();
        cleanStock.setName(CLEAN_WAREHOUSE);
        cleanStock.setValue1(MetricValueUtil.formartPoint2(getDataValue(cleanStockMap)));
        stockQuantity.add(cleanStock);

        // 混煤仓
        Map<String, List<DataModel>> mixedStockMap = historyDataService.findMultiThingsHistoryDataOfMetric(
                Arrays.asList(thingCodesArr[5].split(",")), MetricCodes.AMOUNT, startDate, endDate);
        ProductionDetailVO.MetricData mixedStock = productionDetailVO.new MetricData();
        mixedStock.setName(MIXED_WAREHOUSE);
        mixedStock.setValue1(MetricValueUtil.formartPoint2(getDataValue(mixedStockMap)));
        stockQuantity.add(mixedStock);

        // 煤泥库存
        ProductionDetailVO.MetricData slurry = productionDetailVO.new MetricData();
        slurry.setName(SLURRY_WAREHOUSE);
        slurry.setValue1("");
        stockQuantity.add(slurry);

        productionDetailVO.setStockQuantity(stockQuantity);

        // 3.水位
        List<ProductionDetailVO.MetricData> waterLevel = new ArrayList<>();
        // 一期
        ProductionDetailVO.MetricData water1 = productionDetailVO.new MetricData();
        water1.setName(TERM_ONE);
        water1.setValue1("");
        water1.setValue2("");
        waterLevel.add(water1);

        // 二期
        ProductionDetailVO.MetricData water2 = productionDetailVO.new MetricData();
        water2.setName(TERM_TWO);
        water2.setValue1("");
        water2.setValue2("");
        waterLevel.add(water2);

        productionDetailVO.setWaterLevel(waterLevel);

        // 4.外运计划
        List<ProductionDetailVO.MetricData> outboundPlan = new ArrayList<>();
        // 精煤
        ProductionDetailVO.MetricData cleanPlan = productionDetailVO.new MetricData();
        cleanPlan.setName(CLEAN_COAL);
        cleanPlan.setValue1("");
        cleanPlan.setValue2("");
        cleanPlan.setValue3("");
        outboundPlan.add(cleanPlan);

        // 混煤
        ProductionDetailVO.MetricData mixedPlan = productionDetailVO.new MetricData();
        mixedPlan.setName(MIXED_COAL);
        mixedPlan.setValue1("");
        mixedPlan.setValue2("");
        mixedPlan.setValue3("");
        outboundPlan.add(mixedPlan);

        productionDetailVO.setOutboundPlan(outboundPlan);

        // 5.实际外运
        List<ProductionDetailVO.MetricData> outboundActual = new ArrayList<>();
        // 精煤
        ProductionDetailVO.MetricData cleanActual = productionDetailVO.new MetricData();
        cleanActual.setName(CLEAN_COAL);
        cleanActual.setValue1("");
        cleanActual.setValue2("");
        outboundActual.add(cleanActual);

        // 混煤
        ProductionDetailVO.MetricData mixedActual = productionDetailVO.new MetricData();
        mixedActual.setName(MIXED_COAL);
        mixedActual.setValue1("");
        mixedActual.setValue2("");
        outboundActual.add(mixedActual);

        productionDetailVO.setOutboundActual(outboundActual);

        // 6.月累积量
        List<ProductionDetailVO.MetricData> outboundTotalMonth = new ArrayList<>();
        // 精煤
        ProductionDetailVO.MetricData cleanTotalMonth = productionDetailVO.new MetricData();
        cleanTotalMonth.setName(CLEAN_COAL);
        cleanTotalMonth.setValue1("");
        cleanTotalMonth.setValue2("");
        outboundTotalMonth.add(cleanTotalMonth);

        // 混煤
        ProductionDetailVO.MetricData mixedTotalMonth = productionDetailVO.new MetricData();
        mixedTotalMonth.setName(MIXED_COAL);
        mixedTotalMonth.setValue1("");
        mixedTotalMonth.setValue2("");
        outboundTotalMonth.add(mixedTotalMonth);

        productionDetailVO.setOutboundTotalMonth(outboundTotalMonth);

        return productionDetailVO;
    }

    private BigDecimal getDataValue(Map<String, List<DataModel>> map) {
        BigDecimal totalValue = BigDecimal.ZERO;
        for (List<DataModel> dataModelList : map.values()) {
            if (dataModelList != null && !dataModelList.isEmpty()) {
                for (DataModel dataModel : dataModelList) {
                    String value = dataModel.getValue();
                    totalValue = totalValue.add(new BigDecimal(value == null || value == "" ? "0" : value));
                }
            }
        }
        return totalValue;
    }

}
