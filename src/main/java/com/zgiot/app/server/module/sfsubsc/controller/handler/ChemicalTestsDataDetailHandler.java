package com.zgiot.app.server.module.sfsubsc.controller.handler;

import com.zgiot.app.server.module.coalanalysis.mapper.CoalAnalysisMapper;
import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardTypeDO;
import com.zgiot.app.server.module.sfsubsc.entity.vo.ChemicalTestsDataChartDetailVO;
import com.zgiot.app.server.module.sfsubsc.entity.vo.ChemicalTestsDataListDetailVO;
import com.zgiot.app.server.module.sfsubsc.service.SubscCardTypeService;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.common.constants.SubscriptionConstants;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zgiot.app.server.module.tcs.pojo.FilterCondition.FilterConditionBuilder.newFilterCondition;

/**
 * 化验数据处理
 *
 * @author jys
 */
@Component
public class ChemicalTestsDataDetailHandler {
    @Autowired
    private SubscCardTypeService subscCardTypeService;

    @Autowired
    private CoalAnalysisMapper coalAnalysisMapper;

    public ResponseEntity<String> getChemicalTestsDataDetail(String cardCode, String dateType, String chartType) {
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
            ChemicalTestsDataChartDetailVO chemicalTestsDataChartDetailVO = getChemicalTestsDataChartDetail(cardParamValue, startDate, endDate);
            return new ResponseEntity<>(ServerResponse.buildOkJson(chemicalTestsDataChartDetailVO), HttpStatus.OK);
        } else if (SubscriptionConstants.LIST_TYPE.equals(chartType)) {
            List<ChemicalTestsDataListDetailVO> chemicalTestsDataLIstDetailVOList = getChemicalTestsDataListDetail(cardParamValue, startDate, endDate);
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
    private List<ChemicalTestsDataListDetailVO> getChemicalTestsDataListDetail(String[] cardParamValue, Date startDate, Date endDate) {

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
    private ChemicalTestsDataChartDetailVO getChemicalTestsDataChartDetail(String[] cardParamValue, Date
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


}
