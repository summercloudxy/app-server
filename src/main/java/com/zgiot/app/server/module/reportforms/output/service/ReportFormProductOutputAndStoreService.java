package com.zgiot.app.server.module.reportforms.output.service;

import com.zgiot.app.server.module.coalanalysis.mapper.CoalAnalysisMapper;
import com.zgiot.app.server.module.reportforms.input.manager.CoalAnalysisManager;
import com.zgiot.app.server.module.reportforms.output.constant.ReportFormCoalTypeConstant;
import com.zgiot.app.server.module.reportforms.output.constant.ReportFormTargetConstant;
import com.zgiot.app.server.module.reportforms.output.dao.ReportFormProductionMapper;
import com.zgiot.app.server.module.reportforms.output.pojo.*;
import com.zgiot.app.server.module.reportforms.output.utils.ReportFormDateUtil;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.common.constants.CoalAnalysisConstants;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.zgiot.app.server.module.tcs.pojo.FilterCondition.FilterConditionBuilder;

import java.util.*;

@Service
public class ReportFormProductOutputAndStoreService {
    @Autowired
    private ReportFormProductionMapper reportFormProductionMapper;
    @Autowired
    private OutputStoreAndTargetService outputStoreAndTargetService;
    @Autowired
    private ProductTransportService productTransportService;
    @Autowired
    private CoalAnalysisMapper coalAnalysisMapper;
    @Autowired
    private CoalAnalysisManager coalAnalysisManager;

    /**
     * 获取生产日报生产信息
     *
     * @param dayTime 到天，没有时分秒  eg 20180511-00:00:00
     * @return
     */
    public Map<Integer, ReportFormProductOutput> getProductOutputInfo(Date dayTime) {
        Date productStartTime = ReportFormDateUtil.getProductStartTime(dayTime);
        Map<Integer, ReportFormProductOutput> reportFormProductOutput = reportFormProductionMapper.getReportFormProductOutput(productStartTime);
        if (reportFormProductOutput == null || reportFormProductOutput.size() == 0) {
            reportFormProductOutput = getProductOutputInfoFromDispatch(dayTime);
            List<ReportFormProductOutput> reportFormProductOutputList = new ArrayList<>(reportFormProductOutput.values());
            if (!ReportFormDateUtil.isProductCurrentDuty(dayTime) && !ReportFormDateUtil.isCurrentDuty(DateUtils.addHours(dayTime, 8)) && CollectionUtils.isNotEmpty(reportFormProductOutputList)) {
                reportFormProductionMapper.insertReportFormProductOutputs(reportFormProductOutputList);
            }
        }
        return reportFormProductOutput;
    }


    public Map<Integer, ReportFormProductOutput> getProductOutputInfoFromDispatch(Date date) {
        Map<Integer, ReportFormProductOutput> productOutputRealInfoFromDispatch = getProductOutputRealInfoFromDispatch(date);
        Map<Integer, ReportFormProductOutput> productOutputPlanInfoMap = getProductOutputPlanInfo(date);
        for (Map.Entry<Integer, ReportFormProductOutput> entry : productOutputRealInfoFromDispatch.entrySet()) {
            Integer coalType = entry.getKey();
            ReportFormProductOutput value = entry.getValue();
            if (productOutputPlanInfoMap.containsKey(coalType)) {
                ReportFormProductOutput planOutput = productOutputPlanInfoMap.get(coalType);
                value.setPlanDay(planOutput.getPlanDay());
                value.setProfitLoseDay(value.getRealDay() - planOutput.getPlanDay());
                value.setPlanMonth(planOutput.getPlanMonth());
                value.setProfitLoseMonth(value.getRealMonth() - planOutput.getPlanMonth());
                value.setPlanYear(planOutput.getPlanYear());
                value.setProfitLoseYear(value.getRealYear() - planOutput.getPlanYear());
            }
            if (coalType != ReportFormCoalTypeConstant.rawCoal && productOutputRealInfoFromDispatch.containsKey(ReportFormCoalTypeConstant.rawCoal)) {
                countRatio(productOutputRealInfoFromDispatch, coalType, value);
            }
        }
        return productOutputRealInfoFromDispatch;
    }

    private void countRatio(Map<Integer, ReportFormProductOutput> productOutputRealInfoFromDispatch, Integer coalType, ReportFormProductOutput value) {
        ReportFormProductOutput rawCoalProductOutput = productOutputRealInfoFromDispatch.get(ReportFormCoalTypeConstant.rawCoal);
        if (rawCoalProductOutput.getRealDay() != null && rawCoalProductOutput.getRealDay() != 0) {
            value.setRatioDay(((double) value.getRealDay()) / rawCoalProductOutput.getRealDay());
        }
        if (rawCoalProductOutput.getRealMonth() != null && rawCoalProductOutput.getRealMonth() != 0) {
            value.setRatioMonth(((double) value.getRealMonth()) / rawCoalProductOutput.getRealMonth());
        }
        if (rawCoalProductOutput.getRealYear() != null && rawCoalProductOutput.getRealYear() != 0) {
            value.setRatioYear(((double) value.getRealYear()) / rawCoalProductOutput.getRealYear());
        }
        if (coalType == ReportFormCoalTypeConstant.medium) {
            if (value.getRatioDay() != null) {
                value.setRatioDay(value.getRatioDay() * 1000);
            }
            if (value.getRatioMonth() != null) {
                value.setRatioMonth(value.getRatioMonth() * 1000);
            }
            if (value.getRatioYear() != null) {
                value.setRatioYear(value.getRatioYear() * 1000);
            }
        }
    }


    public Map<Integer, ReportFormProductOutput> getProductOutputPlanInfo(Date dayTime) {
        return reportFormProductionMapper.getReportFormProductOutputPlan(ReportFormDateUtil.getProductStartTime(dayTime));
    }


    /**
     * 从调度台账获取生产信息
     *
     * @param date 到天
     * @return
     */
    public Map<Integer, ReportFormProductOutput> getProductOutputRealInfoFromDispatch(Date date) {
        //当天白班
        Date dayTime = DateUtils.addHours(date, 8);
        //前一天夜班
        Date nightTime = DateUtils.addHours(date, -4);
        Map<Integer, ReportFormProductOutput> reportFormProductOutputMap = new HashMap<>();
        Map<Integer, ReportFormOutputStoreRecord> dayOutputStoreRecord = outputStoreAndTargetService.getOutputStoreRecord(dayTime);
        Map<Integer, ReportFormOutputStoreRecord> nightOutputStoreRecord = outputStoreAndTargetService.getOutputStoreRecord(nightTime);
        if (dayOutputStoreRecord.containsKey(OutputStoreAndTargetService.TYPE_OUTPUT)) {
            ReportFormOutputStoreRecord reportFormOutputStoreRecord = dayOutputStoreRecord.get(OutputStoreAndTargetService.TYPE_OUTPUT);
            if (reportFormOutputStoreRecord != null) {
                getOutputCoalDayValue(reportFormProductOutputMap, reportFormOutputStoreRecord);
            }
        }
        if (nightOutputStoreRecord.containsKey(OutputStoreAndTargetService.TYPE_OUTPUT)) {
            ReportFormOutputStoreRecord reportFormOutputStoreRecord = nightOutputStoreRecord.get(OutputStoreAndTargetService.TYPE_OUTPUT);
            if (reportFormOutputStoreRecord != null) {
                getOutputCoalDayValue(reportFormProductOutputMap, reportFormOutputStoreRecord);
            }
        }
        countCumulateOutputCoalValue(date, reportFormProductOutputMap);
        getOutputMediumValue(reportFormProductOutputMap, date);
        List<ReportFormProductOutput> reportFormProductOutputList = new ArrayList<>(reportFormProductOutputMap.values());
        if (CollectionUtils.isNotEmpty(reportFormProductOutputList)) {
            reportFormProductOutputList.forEach(t -> t.setProductStartTime(ReportFormDateUtil.getProductStartTime(date)));
        }
        return reportFormProductOutputMap;
    }

    /**
     * 计算年、月累计
     *
     * @param date                       到天
     * @param reportFormProductOutputMap
     */
    private void countCumulateOutputCoalValue(Date date, Map<Integer, ReportFormProductOutput> reportFormProductOutputMap) {
        Map<Integer, ReportFormProductOutput> lastOutputMap = reportFormProductionMapper.getReportFormProductOutput(DateUtils.addDays(ReportFormDateUtil.getProductStartTime(date), -1));
        List<ReportFormProductOutput> lastReportFormProductOutputs = new ArrayList<>(lastOutputMap.values());
        //生产日报表中有前一天的记录，取前一天数据加当天数据
        if (CollectionUtils.isNotEmpty(lastReportFormProductOutputs)) {
            for (ReportFormProductOutput lastProductOutput : lastReportFormProductOutputs) {
                if (!ReportFormDateUtil.isProductMonthFirstDay(date)) {
                    countOutputMonthSum(reportFormProductOutputMap, (double) lastProductOutput.getRealMonth(), lastProductOutput.getCoalType());
                }
                if (!ReportFormDateUtil.isProductYearFirstDay(date)) {
                    countOutputYearSum(reportFormProductOutputMap, (double) lastProductOutput.getRealYear(), lastProductOutput.getCoalType());
                }
            }

        }
        //生产日报表没有前一天的记录，从调度表中查询一年的数据累加
        else {
            getHistoryCumulatedValueFromDispatch(reportFormProductOutputMap, date);
        }
    }


    /**
     * 获取介质用量
     *
     * @param reportFormProductOutputMap
     * @param date                       到天
     */
    private void getOutputMediumValue(Map<Integer, ReportFormProductOutput> reportFormProductOutputMap, Date date) {
        Date dayTime = DateUtils.addHours(date, 8);
        Date nightTime = DateUtils.addHours(date, -4);
        Map<Integer, Map<Integer, ReportFormTargetRecord>> dayTargetInfo = outputStoreAndTargetService.getTargetInfo(dayTime);
        Map<Integer, Map<Integer, ReportFormTargetRecord>> nightTargetInfo = outputStoreAndTargetService.getTargetInfo(nightTime);
        getOutputMediumDayValue(reportFormProductOutputMap, dayTargetInfo);
        getOutputMediumDayValue(reportFormProductOutputMap, nightTargetInfo);
        ReportFormProductOutput reportFormProductOutput = getReportFormProductOutputFromMap(reportFormProductOutputMap, ReportFormCoalTypeConstant.medium);
        reportFormProductOutput.setRealMonth(0);
        reportFormProductOutput.setRealYear(0);
        getOutputMediumMonthValue(reportFormProductOutputMap, dayTargetInfo);
        getOutputMediumYearValue(reportFormProductOutputMap, dayTargetInfo);
        reportFormProductOutput.setRealDay(reportFormProductOutput.getRealDay() * 6);
        reportFormProductOutput.setRealMonth(reportFormProductOutput.getRealMonth() * 6);
        reportFormProductOutput.setRealYear(reportFormProductOutput.getRealYear() * 6);

    }

    private void getOutputCoalDayValue(Map<Integer, ReportFormProductOutput> reportFormProductOutputMap, ReportFormOutputStoreRecord outputRecord) {
        countOutputDaySum(reportFormProductOutputMap, outputRecord.getWashedCoal(), ReportFormCoalTypeConstant.washedCoal);
        countOutputDaySum(reportFormProductOutputMap, outputRecord.getClenedCoal(), ReportFormCoalTypeConstant.clenedCoal);
        countOutputDaySum(reportFormProductOutputMap, outputRecord.getSlime(), ReportFormCoalTypeConstant.slime);
        countOutputDaySum(reportFormProductOutputMap, outputRecord.getWasheryRejects(), ReportFormCoalTypeConstant.washeryRejects);
        countOutputDaySum(reportFormProductOutputMap, outputRecord.getRawCoal(), ReportFormCoalTypeConstant.rawCoal);
    }

    private void getOutputCoalMonthValue(Map<Integer, ReportFormProductOutput> reportFormProductOutputMap, ReportFormOutputStoreRecord outputRecord) {
        countOutputMonthSum(reportFormProductOutputMap, outputRecord.getWashedCoal(), ReportFormCoalTypeConstant.washedCoal);
        countOutputMonthSum(reportFormProductOutputMap, outputRecord.getClenedCoal(), ReportFormCoalTypeConstant.clenedCoal);
        countOutputMonthSum(reportFormProductOutputMap, outputRecord.getSlime(), ReportFormCoalTypeConstant.slime);
        countOutputMonthSum(reportFormProductOutputMap, outputRecord.getWasheryRejects(), ReportFormCoalTypeConstant.washeryRejects);
        countOutputMonthSum(reportFormProductOutputMap, outputRecord.getRawCoal(), ReportFormCoalTypeConstant.rawCoal);
    }

    private void getOutputCoalYearValue(Map<Integer, ReportFormProductOutput> reportFormProductOutputMap, ReportFormOutputStoreRecord outputRecord) {
        countOutputYearSum(reportFormProductOutputMap, outputRecord.getWashedCoal(), ReportFormCoalTypeConstant.washedCoal);
        countOutputYearSum(reportFormProductOutputMap, outputRecord.getClenedCoal(), ReportFormCoalTypeConstant.clenedCoal);
        countOutputYearSum(reportFormProductOutputMap, outputRecord.getSlime(), ReportFormCoalTypeConstant.slime);
        countOutputYearSum(reportFormProductOutputMap, outputRecord.getWasheryRejects(), ReportFormCoalTypeConstant.washeryRejects);
        countOutputYearSum(reportFormProductOutputMap, outputRecord.getRawCoal(), ReportFormCoalTypeConstant.rawCoal);
    }

    private void getOutputMediumDayValue(Map<Integer, ReportFormProductOutput> reportFormProductOutputMap, Map<Integer, Map<Integer, ReportFormTargetRecord>> targetInfo) {
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_COAL) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_COAL).containsKey(1)) {
            countOutputDaySum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_COAL).get(1).getClassValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_COAL) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_COAL).containsKey(2)) {

            countOutputDaySum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_COAL).get(2).getClassValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN).containsKey(1)) {
            countOutputDaySum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN).get(1).getClassValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN).containsKey(2)) {
            countOutputDaySum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN).get(2).getClassValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_RE) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_RE).containsKey(1)) {
            countOutputDaySum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_RE).get(1).getClassValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_RE) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_RE).containsKey(2)) {
            countOutputDaySum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_RE).get(2).getClassValue(), ReportFormCoalTypeConstant.medium);
        }
    }

    private void getOutputMediumMonthValue(Map<Integer, ReportFormProductOutput> reportFormProductOutputMap, Map<Integer, Map<Integer, ReportFormTargetRecord>> targetInfo) {
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_COAL) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_COAL).containsKey(1)) {
            countOutputMonthSum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_COAL).get(1).getMonthValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_COAL) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_COAL).containsKey(2)) {
            countOutputMonthSum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_COAL).get(2).getMonthValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN).containsKey(1)) {
            countOutputMonthSum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN).get(1).getMonthValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN).containsKey(2)) {
            countOutputMonthSum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN).get(2).getMonthValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_RE) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_RE).containsKey(1)) {
            countOutputMonthSum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_RE).get(1).getMonthValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_RE) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_RE).containsKey(2)) {
            countOutputMonthSum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_RE).get(2).getMonthValue(), ReportFormCoalTypeConstant.medium);
        }
    }


    private void getOutputMediumYearValue(Map<Integer, ReportFormProductOutput> reportFormProductOutputMap, Map<Integer, Map<Integer, ReportFormTargetRecord>> targetInfo) {
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_COAL) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_COAL).containsKey(1)) {
            countOutputYearSum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_COAL).get(1).getYearValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_COAL) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_COAL).containsKey(2)) {
            countOutputYearSum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_COAL).get(2).getYearValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN).containsKey(1)) {
            countOutputYearSum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN).get(1).getYearValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN).containsKey(2)) {
            countOutputYearSum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_MAIN).get(2).getYearValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_RE) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_RE).containsKey(1)) {
            countOutputYearSum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_RE).get(1).getYearValue(), ReportFormCoalTypeConstant.medium);
        }
        if (targetInfo.containsKey(ReportFormTargetConstant.MEDIUM_CONSUME_RE) && targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_RE).containsKey(2)) {
            countOutputYearSum(reportFormProductOutputMap, targetInfo.get(ReportFormTargetConstant.MEDIUM_CONSUME_RE).get(2).getYearValue(), ReportFormCoalTypeConstant.medium);
        }
    }


    private void countOutputDaySum(Map<Integer, ReportFormProductOutput> reportFormProductOutputMap, Double coalValue, int coalType) {
        ReportFormProductOutput reportFormProductOutput = getReportFormProductOutputFromMap(reportFormProductOutputMap, coalType);
        if (coalValue != null) {
            reportFormProductOutput.setRealDay(reportFormProductOutput.getRealDay() + coalValue.intValue());
            reportFormProductOutput.setRealMonth(reportFormProductOutput.getRealMonth() + coalValue.intValue());
            reportFormProductOutput.setRealYear(reportFormProductOutput.getRealYear() + coalValue.intValue());
        }
    }

    private void countOutputYearSum(Map<Integer, ReportFormProductOutput> reportFormProductOutputMap, Double value, int coalType) {
        ReportFormProductOutput reportFormProductOutput = getReportFormProductOutputFromMap(reportFormProductOutputMap, coalType);
        if (value != null) {
            reportFormProductOutput.setRealYear(reportFormProductOutput.getRealYear() + value.intValue());
        }
    }

    private void countOutputMonthSum(Map<Integer, ReportFormProductOutput> reportFormProductOutputMap, Double value, int coalType) {
        ReportFormProductOutput reportFormProductOutput = getReportFormProductOutputFromMap(reportFormProductOutputMap, coalType);
        if (value != null) {
            reportFormProductOutput.setRealMonth(reportFormProductOutput.getRealMonth() + value.intValue());
        }
    }

    private ReportFormProductOutput getReportFormProductOutputFromMap(Map<Integer, ReportFormProductOutput> reportFormProductOutputMap, int coalType) {
        ReportFormProductOutput reportFormProductOutput;
        if (reportFormProductOutputMap.containsKey(coalType)) {
            reportFormProductOutput = reportFormProductOutputMap.get(coalType);
        } else {
            reportFormProductOutput = new ReportFormProductOutput();
            reportFormProductOutput.setCoalType(coalType);
            reportFormProductOutputMap.put(coalType, reportFormProductOutput);
        }
        return reportFormProductOutput;
    }

    private ReportFormProductStore getReportFormProductStoreFromMap(Map<Integer, ReportFormProductStore> reportFormProductStoreMap, int coalType) {
        ReportFormProductStore reportFormProductStore;
        if (reportFormProductStoreMap.containsKey(coalType)) {
            reportFormProductStore = reportFormProductStoreMap.get(coalType);
        } else {
            reportFormProductStore = new ReportFormProductStore();
            reportFormProductStore.setCoalType(coalType);
            reportFormProductStoreMap.put(coalType, reportFormProductStore);
        }
        return reportFormProductStore;
    }

    private void countStoreDaySum(Map<Integer, ReportFormProductStore> reportFormProductStoreMap, Double coalValue, int coalType) {
        ReportFormProductStore reportFormProductStore = getReportFormProductStoreFromMap(reportFormProductStoreMap, coalType);
        reportFormProductStore.setValue(reportFormProductStore.getValue() + coalValue.intValue());
    }

    private void getStoreCoalDayValue(Map<Integer, ReportFormProductStore> reportFormProductStoreMap, ReportFormOutputStoreRecord storeRecord) {
        countStoreDaySum(reportFormProductStoreMap, storeRecord.getWashedCoal(), ReportFormCoalTypeConstant.washedCoal);
        countStoreDaySum(reportFormProductStoreMap, storeRecord.getClenedCoal(), ReportFormCoalTypeConstant.clenedCoal);
        countStoreDaySum(reportFormProductStoreMap, storeRecord.getSlime(), ReportFormCoalTypeConstant.slime);
        countStoreDaySum(reportFormProductStoreMap, storeRecord.getWasheryRejects(), ReportFormCoalTypeConstant.washeryRejects);
        countStoreDaySum(reportFormProductStoreMap, storeRecord.getRawCoal(), ReportFormCoalTypeConstant.rawCoal);
    }


    /**
     * 从调度台账获取月、年生产累积
     *
     * @param reportFormProductOutputMap
     * @param date
     */
    private void getHistoryCumulatedValueFromDispatch(Map<Integer, ReportFormProductOutput> reportFormProductOutputMap, Date date) {
        //从去年最后一天夜班累积到昨天白班
        List<ReportFormOutputStoreRecord> outputRecordsInDuration = outputStoreAndTargetService.getOutputRecordsInDuration(ReportFormDateUtil.getNowDutyStartTime(DateUtils.truncate(date, Calendar.YEAR)), DateUtils.addHours(DateUtils.addDays(date, -1), 8));
        for (ReportFormOutputStoreRecord reportFormOutputRecord : outputRecordsInDuration) {
            getOutputCoalYearValue(reportFormProductOutputMap, reportFormOutputRecord);
            if (!reportFormOutputRecord.getDutyStartTime().before(ReportFormDateUtil.getNowDutyStartTime(DateUtils.truncate(date, Calendar.MONTH)))) {
                getOutputCoalMonthValue(reportFormProductOutputMap, reportFormOutputRecord);
            }
        }
    }


    /**
     * @param date 到某一天，时分秒均为0
     */
    public Map<Integer, ReportFormProductStore> getProductStoreInfo(Date date) {
        Map<Integer, ReportFormProductStore> reportFormProductStore = reportFormProductionMapper.getReportFormProductStore(ReportFormDateUtil.getProductStartTime(date));
        ReportFormProductStore mediumStore = null;
        if (reportFormProductStore.size() == 1 && reportFormProductStore.containsKey(ReportFormCoalTypeConstant.medium)) {
            mediumStore = reportFormProductStore.remove(ReportFormCoalTypeConstant.medium);
        }
        if (reportFormProductStore.size() == 0) {
            //获取除介质的所有数据
            reportFormProductStore = getProductStoreFromDispatch(date);
            List<ReportFormProductStore> reportFormProductStores = new ArrayList<>(reportFormProductStore.values());
            //生产调度台账白班结束后，才更新数据
            if (!ReportFormDateUtil.isProductCurrentDuty(date) && !ReportFormDateUtil.isCurrentDuty(DateUtils.addHours(date, 8)) && CollectionUtils.isNotEmpty(reportFormProductStores)) {
                reportFormProductionMapper.insertReportFormProductStores(reportFormProductStores);
            }
        }
        if (mediumStore != null) {
            reportFormProductStore.put(ReportFormCoalTypeConstant.medium, mediumStore);
        }
        return reportFormProductStore;
    }


    public Map<Integer, ReportFormProductStore> getProductStoreFromDispatch(Date date) {
        Date dayTime = DateUtils.addHours(date, 8);
        Date nightTime = DateUtils.addHours(date, -4);
        Map<Integer, ReportFormProductStore> reportFormProductOutputMap = new HashMap<>();
        Map<Integer, ReportFormOutputStoreRecord> dayOutputStoreRecord = outputStoreAndTargetService.getOutputStoreRecord(dayTime);
        Map<Integer, ReportFormOutputStoreRecord> nightOutputStoreRecord = outputStoreAndTargetService.getOutputStoreRecord(nightTime);
        if (dayOutputStoreRecord.containsKey(OutputStoreAndTargetService.TYPE_OUTPUT)) {
            ReportFormOutputStoreRecord dayOutputRecord = dayOutputStoreRecord.get(OutputStoreAndTargetService.TYPE_OUTPUT);
            if (dayOutputRecord != null) {
                getStoreCoalDayValue(reportFormProductOutputMap, dayOutputStoreRecord.get(OutputStoreAndTargetService.TYPE_OUTPUT));
            }
        }
        if (nightOutputStoreRecord.containsKey(OutputStoreAndTargetService.TYPE_OUTPUT)) {
            ReportFormOutputStoreRecord nightOutputRecord = nightOutputStoreRecord.get(OutputStoreAndTargetService.TYPE_OUTPUT);
            if (nightOutputRecord != null) {
                getStoreCoalDayValue(reportFormProductOutputMap, nightOutputStoreRecord.get(OutputStoreAndTargetService.TYPE_OUTPUT));
            }
        }
        Map<Integer, ReportFormProductStore> lastProductStore = reportFormProductionMapper.getReportFormProductStore(DateUtils.addDays(ReportFormDateUtil.getProductStartTime(date), -1));
        Map<Integer, Map<Integer, ReportFormTargetRecord>> dayTargetInfo = outputStoreAndTargetService.getTargetInfo(dayTime);
        Map<Integer, Map<Integer, ReportFormTargetRecord>> nightTargetInfo = outputStoreAndTargetService.getTargetInfo(nightTime);
        ReportFormTargetRecord daySelfUse = dayTargetInfo.get(ReportFormTargetConstant.SELF_USE_COAL_TON).get(0);
        ReportFormTargetRecord nightSelfUse = nightTargetInfo.get(ReportFormTargetConstant.SELF_USE_COAL_TON).get(0);
        Map<Integer, Map<Integer, ProductTransport>> productTransportBean = productTransportService.getProductTransportBean(date);

        for (Map.Entry<Integer, ReportFormProductStore> entry : reportFormProductOutputMap.entrySet()) {
            Integer coalType = entry.getKey();
            ReportFormProductStore productStore = entry.getValue();
            productStore.setProductStartTime(ReportFormDateUtil.getProductStartTime(date));
            storeAddLastStore(lastProductStore, coalType, productStore);
            storeExcludeTransport(productTransportBean, coalType, productStore);
            storeExcludeSelfUse(daySelfUse, nightSelfUse, coalType, productStore);
            if (coalType == ReportFormCoalTypeConstant.washeryRejects) {
                productStore.setValue(0.0);
            }

        }
        return reportFormProductOutputMap;

    }

    /**
     * 加上一班库存
     *
     * @param lastProductStore
     * @param coalType
     * @param productStore
     */
    private void storeAddLastStore(Map<Integer, ReportFormProductStore> lastProductStore, Integer coalType, ReportFormProductStore productStore) {
        if (lastProductStore.containsKey(coalType)) {
            productStore.setValue(productStore.getValue() + lastProductStore.get(coalType).getValue());
        }
    }

    /**
     * 减自用
     *
     * @param daySelfUse
     * @param nightSelfUse
     * @param coalType
     * @param productStore
     */
    private void storeExcludeSelfUse(ReportFormTargetRecord daySelfUse, ReportFormTargetRecord nightSelfUse, Integer coalType, ReportFormProductStore productStore) {
        if (coalType == ReportFormCoalTypeConstant.washedCoal) {
            productStore.setValue(productStore.getValue() - daySelfUse.getClassValue() - nightSelfUse.getClassValue());
        }
    }

    /**
     * 减运销
     *
     * @param productTransportBean
     * @param coalType
     * @param productStore
     */
    private void storeExcludeTransport(Map<Integer, Map<Integer, ProductTransport>> productTransportBean, Integer coalType, ReportFormProductStore productStore) {
        if (productTransportBean.containsKey(coalType)) {
            if (productTransportBean.get(coalType).containsKey(1) && productTransportBean.get(coalType).get(1) != null && productTransportBean.get(coalType).get(1).getDayVolume() != null) {
                productStore.setValue(productStore.getValue() - productTransportBean.get(coalType).get(1).getDayVolume());
            }
            if (productTransportBean.get(coalType).containsKey(2) && productTransportBean.get(coalType).get(2) != null && productTransportBean.get(coalType).get(2).getDayVolume() != null) {
                productStore.setValue(productStore.getValue() - productTransportBean.get(coalType).get(2).getDayVolume());
            }
        }
    }


    public Map<Integer, ReportFormProductQuality> getProductQuality(Date date) {
        Map<Integer, ReportFormProductQuality> reportFormProductQualityMap = reportFormProductionMapper.getReportFormProductQuality(date);
        if (reportFormProductQualityMap == null || reportFormProductQualityMap.size() == 0) {
            reportFormProductQualityMap = getProductQualityFromCoalAnalysis(date);
            List<ReportFormProductQuality> reportFormProductQualityList = new ArrayList<>(reportFormProductQualityMap.values());
            if (!ReportFormDateUtil.isProductCurrentDuty(date) && !ReportFormDateUtil.isCurrentDuty(DateUtils.addHours(date, 8)) && CollectionUtils.isNotEmpty(reportFormProductQualityList)) {
                reportFormProductionMapper.insertReportFormProductQualities(reportFormProductQualityList);
            }

        }
        return reportFormProductQualityMap;
    }

    public Map<Integer, ReportFormProductQuality> getProductQualityFromCoalAnalysis(Date date) {
        Date startTime = DateUtils.addHours(date, -4);
        Date endTime = DateUtils.addHours(date, 20);
        Date productStartTime = ReportFormDateUtil.getProductStartTime(date);
        FilterCondition clenedCoalFilterCondition =
                FilterConditionBuilder.newFilterCondition().startTime(startTime).endTime(endTime).addTarget(CoalAnalysisConstants.FIVE_FIVE_ONE_REFINDED).build();
        FilterCondition washedCoalFilterCondition =
                FilterConditionBuilder.newFilterCondition().startTime(startTime).endTime(endTime).addTarget(CoalAnalysisConstants.FIVE_FIVE_TWO_WASHED).build();
        FilterCondition slimeFilterCondition =
                FilterConditionBuilder.newFilterCondition().startTime(startTime).endTime(endTime).addTarget(CoalAnalysisConstants.FILTER_PRESS_SLIME).build();
        FilterCondition rawCoalFilterCondition =
                FilterConditionBuilder.newFilterCondition().startTime(startTime).endTime(endTime).addTarget(CoalAnalysisConstants.RAW_COAL).build();
        List<CoalAnalysisRecord> clenedCoalRecords = coalAnalysisMapper.getRecordsMatchCondition(clenedCoalFilterCondition);
        List<CoalAnalysisRecord> washedCoalRecords = coalAnalysisMapper.getRecordsMatchCondition(washedCoalFilterCondition);
        List<CoalAnalysisRecord> slimeRecords = coalAnalysisMapper.getRecordsMatchCondition(slimeFilterCondition);
        List<CoalAnalysisRecord> rawCoalRecords = coalAnalysisMapper.getRecordsMatchCondition(rawCoalFilterCondition);
        CoalAnalysisRecord clenedCoalAvgRecord = coalAnalysisManager.getAvgRecord(clenedCoalRecords);
        CoalAnalysisRecord washedCoalAvgRecord = coalAnalysisManager.getAvgRecord(washedCoalRecords);
        CoalAnalysisRecord slimeAvgRecord = coalAnalysisManager.getAvgRecord(slimeRecords);
        CoalAnalysisRecord rawAvgRecord = coalAnalysisManager.getAvgRecord(rawCoalRecords);
        generateClenedData(clenedCoalAvgRecord,date);
        generateWashedData(washedCoalAvgRecord,date);
        generateSlimeData(slimeAvgRecord);
        generateRawData(rawAvgRecord, clenedCoalAvgRecord, washedCoalAvgRecord, slimeAvgRecord, date);
        Map<Integer, ReportFormProductQuality> reportFormProductQualityMap = new HashMap<>();
        reportFormProductQualityMap.put(ReportFormCoalTypeConstant.clenedCoal, new ReportFormProductQuality(productStartTime, ReportFormCoalTypeConstant.clenedCoal, clenedCoalAvgRecord));
        reportFormProductQualityMap.put(ReportFormCoalTypeConstant.washedCoal, new ReportFormProductQuality(productStartTime, ReportFormCoalTypeConstant.washedCoal, washedCoalAvgRecord));
        reportFormProductQualityMap.put(ReportFormCoalTypeConstant.slime, new ReportFormProductQuality(productStartTime, ReportFormCoalTypeConstant.slime, slimeAvgRecord));
        reportFormProductQualityMap.put(ReportFormCoalTypeConstant.rawCoal, new ReportFormProductQuality(productStartTime, ReportFormCoalTypeConstant.rawCoal, rawAvgRecord));
        return reportFormProductQualityMap;
    }


    public void generateClenedData(CoalAnalysisRecord clenedCoalRecord,Date date) {
        Random random = new Random(date.getTime());
        int aad = random.nextInt(30) + 1050;
        clenedCoalRecord.setAad((double) aad / 100);
    }

    public void generateWashedData(CoalAnalysisRecord washedCoalRecord,Date date) {
        Random random = new Random(date.getTime());
        int mtRandom = random.nextInt(10) + 90;
        double mt = (double) mtRandom / 10;
        washedCoalRecord.setMt(mt);
        int qnetRandom = random.nextInt(98) + 5301;
        washedCoalRecord.setQnetar((double) qnetRandom);
        double aad = Math.abs((((qnetRandom + 70 * (mt - 5)) * 4.1816) / (1000 - 31.734)) / 0.3723);
        washedCoalRecord.setAad(aad);
    }

    public void generateSlimeData(CoalAnalysisRecord slime) {
        if (slime.getAad() == null) {
            slime.setAad(0.0);
        }
    }

    public void generateRawData(CoalAnalysisRecord rawCoalRecord, CoalAnalysisRecord clenedCoalRecord, CoalAnalysisRecord washedCoalRecord, CoalAnalysisRecord slimeRecord, Date date) {
        Map<Integer, ReportFormProductOutput> productOutputInfo = getProductOutputInfo(date);
        double aad = 0.0;
        if (productOutputInfo.containsKey(ReportFormCoalTypeConstant.rawCoal) && productOutputInfo.get(ReportFormCoalTypeConstant.rawCoal).getRealDay() != 0) {
            aad = ((productOutputInfo.containsKey(ReportFormCoalTypeConstant.clenedCoal) ? productOutputInfo.get(ReportFormCoalTypeConstant.clenedCoal).getRealDay() * clenedCoalRecord.getAad() : 0)
                    + (productOutputInfo.containsKey(ReportFormCoalTypeConstant.washedCoal) ? productOutputInfo.get(ReportFormCoalTypeConstant.washedCoal).getRealDay() * washedCoalRecord.getAad() : 0)
                    + (productOutputInfo.containsKey(ReportFormCoalTypeConstant.slime) ? productOutputInfo.get(ReportFormCoalTypeConstant.slime).getRealDay() * slimeRecord.getAad() : 0)
                    + (productOutputInfo.containsKey(ReportFormCoalTypeConstant.washeryRejects) ? (productOutputInfo.get(ReportFormCoalTypeConstant.washeryRejects).getRealDay() * 78) : 0)) /
                    productOutputInfo.get(ReportFormCoalTypeConstant.rawCoal).getRealDay();
        }
        rawCoalRecord.setAad(aad);
    }


    public void addMediumStore(ReportFormProductStore store) {
        store.setProductStartTime(ReportFormDateUtil.getProductStartTime(store.getProductStartTime()));
        Map<Integer, ReportFormProductStore> reportFormProductStore = reportFormProductionMapper.getReportFormProductStore(store.getProductStartTime());
        if (reportFormProductStore.containsKey(ReportFormCoalTypeConstant.medium)) {
            ReportFormProductStore existStore = reportFormProductStore.get(ReportFormCoalTypeConstant.medium);
            store.setId(existStore.getId());
        }
        if (store.getId() == null) {
            reportFormProductionMapper.insertReportFormProductStore(store);
        } else {
            reportFormProductionMapper.updateReportFormProductStore(store);
        }
    }


}
