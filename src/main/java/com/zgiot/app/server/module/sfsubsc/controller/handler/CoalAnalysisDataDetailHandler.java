package com.zgiot.app.server.module.sfsubsc.controller.handler;

import com.zgiot.app.server.module.coalanalysis.mapper.CoalAnalysisMapper;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardTypeDO;
import com.zgiot.app.server.module.sfsubsc.entity.vo.ChemicalTestsDataChartDetailVO;
import com.zgiot.app.server.module.sfsubsc.entity.vo.ChemicalTestsDataListDetailVO;
import com.zgiot.app.server.module.sfsubsc.entity.vo.CoalQualityDetailVO;
import com.zgiot.app.server.module.sfsubsc.service.SubscCardTypeService;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.common.constants.SubscriptionConstants;
import com.zgiot.common.pojo.CoalAnalysisRecord;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zgiot.app.server.module.sfsubsc.enums.CardTypeEnum.COAL_QUALITY;
import static com.zgiot.app.server.module.tcs.pojo.FilterCondition.FilterConditionBuilder.newFilterCondition;
import static com.zgiot.common.constants.SubscriptionConstants.*;
import static com.zgiot.common.constants.SubscriptionConstants.MIXE_COAL_552;

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

        // 2.一期生产指标
        CoalAnalysisRecord p11 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_ONE, WASH_RAW_COAL, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// 原煤
        CoalAnalysisRecord p12 = coalAnalysisMapper.getTopCoalAnalysisRecord(SYSTEM_ONE, CLEAN_COAL_AVG_551, coalQualityDetailVO.getShiftTimeEnd());// 精煤
        CoalAnalysisRecord p13 = coalAnalysisMapper.getTopCoalAnalysisRecord(SYSTEM_ONE, MIXED_COAL_AVG_552, coalQualityDetailVO.getShiftTimeEnd());// 混煤
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
        CoalAnalysisRecord p22 = coalAnalysisMapper.getTopCoalAnalysisRecord(SYSTEM_TWO, CLEAN_COAL_AVG_551, coalQualityDetailVO.getShiftTimeEnd());// 精煤
        CoalAnalysisRecord p23 = coalAnalysisMapper.getTopCoalAnalysisRecord(SYSTEM_TWO, MIXED_COAL_AVG_552, coalQualityDetailVO.getShiftTimeEnd());// 混煤
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
                SYSTEM_ONE, TCS_ORE_FINE, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// TCS精矿
        CoalAnalysisRecord tcs22 = coalAnalysisMapper.getTimeRangeCoalAnalysisRecordAVG(
                SYSTEM_ONE, TCS_ORE_CRUDE, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());// TCS尾矿
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
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        List<Date> dateList = null;
        if (WHITE_SHIFT.equals(coalQualityDetailVO.getShift())) {// 白班
            try {
                calendar.setTime(sdf1.parse(coalQualityDetailVO.getDate()));
            } catch (ParseException e) {
                logger.error("班累计煤质统计详情-白班日期转换异常", e);
            }
            calendar.set(Calendar.HOUR_OF_DAY, WHITE_SHIFT_BEGIN);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            coalQualityDetailVO.setShiftTimeBegin(calendar.getTime());

            calendar.set(Calendar.HOUR_OF_DAY, WHITE_SHIFT_END);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            coalQualityDetailVO.setShiftTimeEnd(calendar.getTime());

            // 获取该班次所有时间
            dateList = coalAnalysisMapper.getTimeRangeTime(MIXE_COAL_552, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());
        }
        if (NIGHT_SHIFT.equals(coalQualityDetailVO.getShift())) {// 夜班
            try {
                calendar.setTime(sdf1.parse(coalQualityDetailVO.getDate()));
            } catch (ParseException e) {
                logger.error("班累计煤质统计详情-夜班日期转换异常1", e);
            }
            calendar.set(Calendar.HOUR_OF_DAY, NIGHT_SHIFT_BEGIN);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            coalQualityDetailVO.setShiftTimeBegin(calendar.getTime());


            try {
                calendar.setTime(DateUtils.addDays(sdf1.parse(coalQualityDetailVO.getDate()), 1));
            } catch (ParseException e) {
                logger.error("班累计煤质统计详情-夜班日期转换异常2", e);
            }
            calendar.set(Calendar.HOUR_OF_DAY, NIGHT_SHIFT_END);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            coalQualityDetailVO.setShiftTimeEnd(calendar.getTime());

            // 获取该班次所有时间
            dateList = coalAnalysisMapper.getTimeRangeTime(MIXE_COAL_552, coalQualityDetailVO.getShiftTimeBegin(), coalQualityDetailVO.getShiftTimeEnd());
        }
        // 设置班次时间
        if (dateList != null && !dateList.isEmpty()) {
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            coalQualityDetailVO.setTimeBegin(sdf2.format(dateList.get(0)));
            coalQualityDetailVO.setTimeEnd(sdf2.format(dateList.get(dateList.size() - 1)));
        }
    }

    private void getMetricData(List<CoalQualityDetailVO.MetricData> list, CoalQualityDetailVO coalQualityDetailVO,
            CoalAnalysisRecord car, String productionName) {
        CoalQualityDetailVO.MetricData metricData = coalQualityDetailVO.new MetricData();
        metricData.setProductionName(productionName);
        DecimalFormat df = new DecimalFormat("#0.00");
        metricData.setAad(df.format(car.getAad() == null ? 0 : car.getAad()));
        metricData.setMt(df.format(car.getMt() == null ? 0 : car.getMt()));
        metricData.setStad(df.format(car.getStad() == null ? 0 : car.getStad()));
        metricData.setQar(df.format(car.getQnetar() == null ? 0 : car.getQnetar()));
        list.add(metricData);
    }

}
