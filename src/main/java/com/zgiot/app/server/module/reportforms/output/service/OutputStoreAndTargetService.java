package com.zgiot.app.server.module.reportforms.output.service;

import com.zgiot.app.server.module.filterpress.dao.FilterPressLogMapper;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateStatistic;
import com.zgiot.app.server.module.reportforms.output.constant.ReportFormConstant;
import com.zgiot.app.server.module.reportforms.output.constant.ReportFormTargetConstant;
import com.zgiot.app.server.module.reportforms.output.dao.ReportFormOutputStoreMapper;
import com.zgiot.app.server.module.reportforms.output.dao.ReportFormTargetMapper;
import com.zgiot.app.server.module.reportforms.output.pojo.*;
import com.zgiot.app.server.module.reportforms.output.utils.ReportFormDateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OutputStoreAndTargetService {
    @Autowired
    private ReportFormOutputStoreMapper outputStoreMapper;
    @Autowired
    private ReportFormTargetMapper targetMapper;
    @Autowired
    private TransPortService transPortService;
    @Autowired
    private FilterPressLogMapper filterPressLogMapper;
    /**
     * 当班生产库存情况缓存
     * key：1.生产  2.库存
     */
    private Map<Integer, ReportFormOutputStoreRecord> dutyOutputStoreRecords = new HashMap<>(2);
    public static final Integer TYPE_OUTPUT = 1;
    public static final Integer TYPE_STORE = 2;

    private Map<Integer, FeedbackTargetRelation> feedbackTargetMap = new HashMap<>();


    public void init() {
        Date currentDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());
        dutyOutputStoreRecords.put(TYPE_OUTPUT, outputStoreMapper.getOutputStoreRecord(TYPE_OUTPUT, currentDutyStartTime));
        ReportFormOutputStoreRecord currentStoreRecord = outputStoreMapper.getOutputStoreRecord(TYPE_STORE, currentDutyStartTime);
        if (currentStoreRecord == null) {
            ReportFormOutputStoreRecord lastStoreRecord = getLastStoreRecord(currentDutyStartTime);
            currentStoreRecord = copyStoreValueFromLastRecord(currentDutyStartTime, lastStoreRecord);
            outputStoreMapper.insertRecord(currentStoreRecord);
        }
        dutyOutputStoreRecords.put(TYPE_STORE, outputStoreMapper.getOutputStoreRecord(TYPE_STORE, currentDutyStartTime));
        feedbackTargetMap = targetMapper.getFeedbackTargetRelationMap();
    }

    /**
     * 新增 or 更新一条生产情况记录
     *
     * @param outputRecord
     */
    public void addOutputRecord(ReportFormOutputStoreRecord outputRecord) {

        ReportFormOutputStoreRecord lastStoreRecord = getLastStoreRecord(outputRecord.getDutyStartTime());
        ReportFormOutputStoreRecord currentStoreRecord = getCurrentStoreRecord(outputRecord.getDutyStartTime(), lastStoreRecord);
        //防止获取自用数据时 重复添加库存数据
        insertOrUpdateRecord(currentStoreRecord);
        TransBean transPortCacheBean = transPortService.getTransPortCacheBean(outputRecord.getDutyStartTime());
        Map<Integer, Map<Integer, ReportFormTargetRecord>> otherTargetInfo = getOtherTargetInfo(outputRecord.getDutyStartTime());
        Double rawCoal = 0.0;
        if (outputRecord.getClenedCoal() != null) {
            rawCoal += outputRecord.getClenedCoal();
            Double currentClenedCoalStoreValue = lastStoreRecord.getClenedCoal();
            currentClenedCoalStoreValue += outputRecord.getClenedCoal();
            currentClenedCoalStoreValue = getStoreValueWithoutSale(transPortCacheBean, currentClenedCoalStoreValue, ReportFormConstant.COAL_TYPE_GAS_REFINDED_NUM);
            currentStoreRecord.setClenedCoal(currentClenedCoalStoreValue);
        }
        if (outputRecord.getWashedCoal() != null) {
            rawCoal += outputRecord.getWashedCoal();
            Double currentWashedCoalStoreValue = lastStoreRecord.getWashedCoal();
            currentWashedCoalStoreValue += outputRecord.getWashedCoal();
            currentWashedCoalStoreValue = getStoreValueWithoutSale(transPortCacheBean, currentWashedCoalStoreValue, ReportFormConstant.COAL_TYPE_WASHED_NUM);
            currentWashedCoalStoreValue = getStoreValueWithoutSelfUse(currentWashedCoalStoreValue, otherTargetInfo);
            currentStoreRecord.setWashedCoal(currentWashedCoalStoreValue);
        }
        if (outputRecord.getSlime() != null) {
            rawCoal += outputRecord.getSlime();
            Double currentSlimeStoreValue = lastStoreRecord.getSlime();
            currentSlimeStoreValue += outputRecord.getSlime();
            currentSlimeStoreValue = getStoreValueWithoutSale(transPortCacheBean, currentSlimeStoreValue, ReportFormConstant.COAL_TYPE_SLIME_NUM);
            currentStoreRecord.setSlime(currentSlimeStoreValue);
        }
        if (outputRecord.getWasheryRejects() != null) {
            rawCoal += outputRecord.getWasheryRejects();
        }
        outputRecord.setRawCoal(rawCoal);
        insertOrUpdateRecord(outputRecord);
        insertOrUpdateRecord(currentStoreRecord);
        if (ReportFormDateUtil.isCurrentDuty(outputRecord.getDutyStartTime())) {
            dutyOutputStoreRecords.put(TYPE_OUTPUT, outputRecord);
            dutyOutputStoreRecords.put(TYPE_STORE, currentStoreRecord);
        }
    }

    @Scheduled(cron = "0 0 8,20 * * ? ")
    public void checkDutyEndTime() {
        Date dutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());

        ReportFormOutputStoreRecord currentStoreRecord = outputStoreMapper.getOutputStoreRecord(TYPE_STORE, dutyStartTime);
        if(currentStoreRecord==null){
            ReportFormOutputStoreRecord lastStoreRecord = dutyOutputStoreRecords.get(TYPE_STORE);
            currentStoreRecord = copyStoreValueFromLastRecord(dutyStartTime, lastStoreRecord);
            outputStoreMapper.insertRecord(currentStoreRecord);
        }

        dutyOutputStoreRecords.put(TYPE_STORE, currentStoreRecord);
        dutyOutputStoreRecords.put(TYPE_OUTPUT, null);

    }

    public void addStoreRecord(ReportFormOutputStoreRecord storeRecord) {
        if (storeRecord.getId() != null) {
            outputStoreMapper.updateRecord(storeRecord);
        } else {
            ReportFormOutputStoreRecord lastStoreRecord = getLastStoreRecord(storeRecord.getDutyStartTime());
            ReportFormOutputStoreRecord currentStoreRecord = copyStoreValueFromLastRecord(storeRecord.getDutyStartTime(), lastStoreRecord);
            currentStoreRecord.setRawCoal8(storeRecord.getRawCoal8());
            currentStoreRecord.setRawCoal13(storeRecord.getRawCoal13());
            outputStoreMapper.insertRecord(currentStoreRecord);
        }
        if (ReportFormDateUtil.isCurrentDuty(storeRecord.getDutyStartTime())) {
            dutyOutputStoreRecords.put(TYPE_STORE, storeRecord);
        }

    }


    public void insertOrUpdateRecord(ReportFormOutputStoreRecord record) {
        if (record.getId() != null) {
            outputStoreMapper.updateRecord(record);
        } else {
            outputStoreMapper.insertRecord(record);
        }
    }


    /**
     * 获取一个班次的生产库存情况
     *
     * @param dutyStartTime
     * @return
     */
    public Map<Integer, ReportFormOutputStoreRecord> getOutputStoreRecord(Date dutyStartTime) {
        Map<Integer, ReportFormOutputStoreRecord> outputStoreRecord;
        if (ReportFormDateUtil.isCurrentDuty(dutyStartTime)) {
            outputStoreRecord = dutyOutputStoreRecords;
        } else {
            outputStoreRecord = new HashMap<>();
            outputStoreRecord.put(TYPE_OUTPUT, outputStoreMapper.getOutputStoreRecord(TYPE_OUTPUT, dutyStartTime));
            outputStoreRecord.put(TYPE_STORE, outputStoreMapper.getOutputStoreRecord(TYPE_STORE, dutyStartTime));
        }
        return outputStoreRecord;
    }

    /**
     * 库存减去销售
     *
     * @param transPortCacheBean
     * @param currentStoreValue
     * @param coalType
     * @return
     */
    public Double getStoreValueWithoutSale(TransBean transPortCacheBean, Double currentStoreValue, Integer coalType) {
        Map<Integer, TransportSaleStatistics> saleStatisticsLocalityMap = transPortCacheBean.getSaleStatisticsLocalityMap();
        Map<Integer, TransportSaleStatistics> saleStatisticsOutwardMap = transPortCacheBean.getSaleStatisticsOutwardMap();
        if (saleStatisticsLocalityMap != null && saleStatisticsLocalityMap.containsKey(coalType)) {
            TransportSaleStatistics localitySaleStatistics = saleStatisticsLocalityMap.get(coalType);
            Double localitySaleValue = localitySaleStatistics.getCoalVolunm();
            currentStoreValue -= localitySaleValue;
        }
        if (saleStatisticsOutwardMap != null && saleStatisticsOutwardMap.containsKey(coalType)) {
            TransportSaleStatistics outwardSaleStatistics = saleStatisticsOutwardMap.get(coalType);
            Double outwardSaleValue = outwardSaleStatistics.getCoalVolunm();
            currentStoreValue -= outwardSaleValue;
        }
        return currentStoreValue;
    }


    public Double getStoreValueWithoutSelfUse(Double currentStoreValue, Map<Integer, Map<Integer, ReportFormTargetRecord>> otherTargetRecordMap) {
        if (otherTargetRecordMap.containsKey(ReportFormTargetConstant.SELF_USE_COAL_TON) && otherTargetRecordMap.get(ReportFormTargetConstant.SELF_USE_COAL_TON).containsKey(0)) {
            ReportFormTargetRecord reportFormTargetRecord = otherTargetRecordMap.get(ReportFormTargetConstant.SELF_USE_COAL_TON).get(0);
            currentStoreValue -= reportFormTargetRecord.getClassValue();
        }
        return currentStoreValue;
    }

    public ReportFormOutputStoreRecord getLastStoreRecord(Date currentDutyStartTime) {
        ReportFormOutputStoreRecord lastStoreRecord = outputStoreMapper.getOutputStoreRecord(TYPE_STORE, ReportFormDateUtil.getbeforeDutyStartTime(currentDutyStartTime));
        if (lastStoreRecord == null) {
            lastStoreRecord = new ReportFormOutputStoreRecord();
            lastStoreRecord.setRawCoal(0.0);
            lastStoreRecord.setClenedCoal(0.0);
            lastStoreRecord.setWashedCoal(0.0);
            lastStoreRecord.setSlime(0.0);
            lastStoreRecord.setWasheryRejects(0.0);
            lastStoreRecord.setLocalWashedCoal(0.0);
            lastStoreRecord.setType(TYPE_STORE);
        }
        return lastStoreRecord;
    }

    public ReportFormOutputStoreRecord getCurrentStoreRecord(Date currentDutyStartTime, ReportFormOutputStoreRecord lastStoreRecord) {
        ReportFormOutputStoreRecord currentStoreRecord = outputStoreMapper.getOutputStoreRecord(TYPE_STORE, currentDutyStartTime);
        if (currentStoreRecord == null) {
            currentStoreRecord = copyStoreValueFromLastRecord(currentDutyStartTime, lastStoreRecord);
        }
        return currentStoreRecord;
    }

    /**
     * 复制上一班的库存值到当前班次
     *
     * @param currentDutyStartTime
     * @param lastStoreRecord
     * @return
     */
    public ReportFormOutputStoreRecord copyStoreValueFromLastRecord(Date currentDutyStartTime, ReportFormOutputStoreRecord lastStoreRecord) {
        ReportFormOutputStoreRecord currentStoreRecord = new ReportFormOutputStoreRecord(lastStoreRecord);
        currentStoreRecord.setId(null);
        currentStoreRecord.setDutyStartTime(currentDutyStartTime);
        currentStoreRecord.setRawCoal8(null);
        currentStoreRecord.setRawCoal13(null);
        return currentStoreRecord;
    }


    /**
     * 获取指标信息
     *
     * @param dutyStartTime
     * @return
     */
    public Map<Integer, Map<Integer, ReportFormTargetRecord>> getTargetInfo(Date dutyStartTime) {
        Map<Integer, Map<Integer, ReportFormTargetRecord>> otherTargetInfo = getOtherTargetInfo(dutyStartTime);
        otherTargetInfo.put(ReportFormTargetConstant.FILTER_PRESS_SLIME, getFilterPressTargetInfo(dutyStartTime));
        return otherTargetInfo;
    }

    public Map<Integer, ReportFormTargetRecord> getFilterPressTargetInfo(Date dutyStartTime) {
        Map<Integer, ReportFormTargetRecord> reportFormTargetRecords = new HashMap<>();
        ReportFormTargetRecord filterPressTargetRecordTermOne = getFilterPressTargetRecord(dutyStartTime, 1);
        ReportFormTargetRecord filterPressTargetRecordTermTwo = getFilterPressTargetRecord(dutyStartTime, 2);
        reportFormTargetRecords.put(1, filterPressTargetRecordTermOne);
        reportFormTargetRecords.put(2, filterPressTargetRecordTermTwo);
        return reportFormTargetRecords;

    }

    public ReportFormTargetRecord getFilterPressTargetRecord(Date dutyStartTime, Integer term) {
        ReportFormTargetRecord targetRecord;
        //当前班需要实时取数据
        if (ReportFormDateUtil.isCurrentDuty(dutyStartTime)) {
            Date lastDutyStartTime = ReportFormDateUtil.getbeforeDutyStartTime(dutyStartTime);
            ReportFormTargetRecord lastTargetRecord = targetMapper.getTargetRecord(lastDutyStartTime, term, ReportFormTargetConstant.FILTER_PRESS_SLIME);
            //如果报表模块没有记录统计数据，需要从压滤模块计算
            if (lastTargetRecord == null) {
                lastTargetRecord = getRecordFromFilterPressModule(lastDutyStartTime, term);
                targetMapper.insertTargetRecord(lastTargetRecord);
            }
            targetRecord = new ReportFormTargetRecord();
            targetRecord.setTerm(term);
            targetRecord.setTargetType(ReportFormTargetConstant.FILTER_PRESS_SLIME);
            targetRecord.setDutyStartTime(dutyStartTime);
            //实时取数据，计算
            int currentClassPlate = filterPressLogMapper.queryTotalPlateCount(dutyStartTime, new Date(), term);
            targetRecord.setMonthValue(currentClassPlate + lastTargetRecord.getMonthValue());
            targetRecord.setYearValue(currentClassPlate + lastTargetRecord.getYearValue());
            targetRecord.setClassValue((double) currentClassPlate);
        } else {
            targetRecord = targetMapper.getTargetRecord(dutyStartTime, term, ReportFormTargetConstant.FILTER_PRESS_SLIME);
            if (targetRecord == null) {
                targetRecord = getRecordFromFilterPressModule(dutyStartTime, term);
                targetMapper.insertTargetRecord(targetRecord);
            }
        }
        return targetRecord;
    }


    /**
     * 从压滤模块获取统计信息
     *
     * @param dutyStartTime
     * @param term
     * @return
     */
    public ReportFormTargetRecord getRecordFromFilterPressModule(Date dutyStartTime, Integer term) {
        ReportFormTargetRecord reportFormTargetRecord = new ReportFormTargetRecord();
        reportFormTargetRecord.setTerm(term);
        reportFormTargetRecord.setTargetType(ReportFormTargetConstant.FILTER_PRESS_SLIME);
        reportFormTargetRecord.setDutyStartTime(dutyStartTime);
        Date firstDayOfYear = DateUtils.truncate(dutyStartTime, Calendar.YEAR);
        int yearSumPlate = getSumPlate(firstDayOfYear, dutyStartTime, term);
        reportFormTargetRecord.setYearValue((double) yearSumPlate);
        Date firstDayOfMonth = DateUtils.truncate(dutyStartTime, Calendar.MONTH);
        int monthSumPlate = getSumPlate(firstDayOfMonth, dutyStartTime, term);
        reportFormTargetRecord.setMonthValue((double) monthSumPlate);
        FilterPressPlateStatistic designatedClassPlateStatistic =
                filterPressLogMapper.getDesignatedClassPlateStatistic(DateUtils.truncate(dutyStartTime, Calendar.DAY_OF_MONTH), ReportFormDateUtil.isDayShift(dutyStartTime), term);
        if (designatedClassPlateStatistic != null) {
            reportFormTargetRecord.setClassValue((double) designatedClassPlateStatistic.getTotalPlateCount());
        } else {
            reportFormTargetRecord.setClassValue(0.0);
        }
        return reportFormTargetRecord;
    }


    public int getSumPlate(Date startTime, Date endTime, Integer term) {
        Date date = DateUtils.truncate(endTime, Calendar.DAY_OF_MONTH);
        List<FilterPressPlateStatistic> plateStatisticList = filterPressLogMapper.getPlateStatisticInDuration(startTime, endTime, term);
        if (CollectionUtils.isEmpty(plateStatisticList)) {
            return 0;
        }
        //统计到某一天的白班，把夜班数据去掉
        if (ReportFormDateUtil.isDayShift(endTime)) {
            Iterator<FilterPressPlateStatistic> iterator = plateStatisticList.iterator();
            while (iterator.hasNext()) {
                FilterPressPlateStatistic next = iterator.next();
                if (date.equals(next.getDateTime()) &&
                        !next.getIsDayShift()) {
                    iterator.remove();
                    break;
                }
            }
        }
        return countSumPlate(plateStatisticList);
    }

    private int countSumPlate(List<FilterPressPlateStatistic> plateStatisticList) {
        int sumPlate = 0;
        for (FilterPressPlateStatistic classStatistics : plateStatisticList) {
            sumPlate += classStatistics.getTotalPlateCount();
        }
        return sumPlate;
    }


    public Map<Integer, Map<Integer, ReportFormTargetRecord>> getOtherTargetInfo(Date dutyStartTime) {
        Map<Integer, Map<Integer, ReportFormTargetRecord>> otherTargetRecordMap;
        //当前班需要实时取数据
        if (ReportFormDateUtil.isCurrentDuty(dutyStartTime)) {
            otherTargetRecordMap = new HashMap<>();
            Date lastDutyStartTime = ReportFormDateUtil.getbeforeDutyStartTime(dutyStartTime);
            Map<Integer, Map<Integer, ReportFormTargetRecord>> lastOtherTargetRecordMap = getOtherTargetRecordMap(lastDutyStartTime);
            //从任务模块取数据
            List<TaskFeedbackInfo> currentDutyTaskFeedbackInfo = targetMapper.getTaskFeedbackInfoList(dutyStartTime, DateUtils.addHours(dutyStartTime, 12));
            for (TaskFeedbackInfo taskFeedbackInfo : currentDutyTaskFeedbackInfo) {
                FeedbackTargetRelation feedbackTargetRelation = feedbackTargetMap.get(taskFeedbackInfo.getFeedbackInfoId());
                Integer targetTypeId = feedbackTargetRelation.getTargetTypeId();
                Integer term = feedbackTargetRelation.getTerm();
                ReportFormTargetRecord lastReportFormTargetRecord = lastOtherTargetRecordMap.get(targetTypeId).get(term);
                Double classValue = taskFeedbackInfo.getFeedbackValue();
                ReportFormTargetRecord currentTargetRecord = getTargetRecordWithYearAndMonthValue(dutyStartTime, lastReportFormTargetRecord, classValue);
                Map<Integer, ReportFormTargetRecord> reportFormTargetTermRecordMap = getReportFormTargetTermRecordMap(otherTargetRecordMap, targetTypeId);
                reportFormTargetTermRecordMap.put(term, currentTargetRecord);
                //根据水表数据计算中水数据
                if (ReportFormTargetConstant.WATER_METER.equals(targetTypeId)) {
                    ReportFormTargetRecord lastReportFormWaterTargetRecord = lastOtherTargetRecordMap.get(13).get(term);
                    Double waterClassValue = 0.0;
                    if (classValue != 0.0) {
                        if (lastReportFormTargetRecord.getClassValue() != 0.0) {
                            waterClassValue = classValue - lastReportFormTargetRecord.getClassValue();
                        } else {
                            //上一个班没有水表读数，再往前取数据
                            TaskFeedbackInfo lastTaskFeedbackInfoBeforeDate = targetMapper.getLastTaskFeedbackInfoBeforeDate(dutyStartTime, feedbackTargetRelation.getFeedbackInfoId());
                            if (lastTaskFeedbackInfoBeforeDate != null && lastTaskFeedbackInfoBeforeDate.getFeedbackValue() != null) {
                                waterClassValue = classValue - lastTaskFeedbackInfoBeforeDate.getFeedbackValue();
                            }
                        }
                    }
                    ReportFormTargetRecord currentWaterTargetRecord = getTargetRecordWithYearAndMonthValue(dutyStartTime, lastReportFormWaterTargetRecord, waterClassValue);
                    Map<Integer, ReportFormTargetRecord> reportFormWaterTargetTermRecordMap = getReportFormTargetTermRecordMap(otherTargetRecordMap, ReportFormTargetConstant.WATER_CONSUME);
                    reportFormWaterTargetTermRecordMap.put(term, currentWaterTargetRecord);
                }
            }
            //填充没有查询到的指标数据
            checkNonExistTargetRecord(dutyStartTime,otherTargetRecordMap);


        } else {
            otherTargetRecordMap = getOtherTargetRecordMap(dutyStartTime);
        }
        checkWaterConsume(dutyStartTime,otherTargetRecordMap);
        return otherTargetRecordMap;
    }

    private ReportFormTargetRecord getTargetRecordWithYearAndMonthValue(Date dutyStartTime, ReportFormTargetRecord lastReportFromTargetRecord, Double classValue) {
        ReportFormTargetRecord reportFormTargetRecord = new ReportFormTargetRecord();
        reportFormTargetRecord.setTargetType(lastReportFromTargetRecord.getTargetType());
        reportFormTargetRecord.setTerm(lastReportFromTargetRecord.getTerm());
        reportFormTargetRecord.setDutyStartTime(dutyStartTime);
        reportFormTargetRecord.setClassValue(classValue);
        if (ReportFormDateUtil.isYearFirstDay(dutyStartTime)) {
            reportFormTargetRecord.setYearValue(classValue);
        } else {
            reportFormTargetRecord.setYearValue(lastReportFromTargetRecord.getYearValue() + classValue);
        }
        if (ReportFormDateUtil.isMonthFirstDay(dutyStartTime)) {
            reportFormTargetRecord.setMonthValue(classValue);
        } else {
            reportFormTargetRecord.setMonthValue(lastReportFromTargetRecord.getMonthValue() + classValue);
        }
        return reportFormTargetRecord;
    }


    private Map<Integer, Map<Integer, ReportFormTargetRecord>> getOtherTargetRecordMap(Date dutyStartTime) {
        //从报表模块获取指标数据
        List<ReportFormTargetRecord> targetRecordList = targetMapper.getTargetRecordList(dutyStartTime, null, null);
        Map<Integer, Map<Integer, ReportFormTargetRecord>> targetRecordMap;
        //获取不到时从任务模块获取指标数据
        if (CollectionUtils.isEmpty(targetRecordList)) {
            targetRecordList = new ArrayList<>();
            targetRecordMap = getTargetRecordFromTaskModule(dutyStartTime);
            for (Map.Entry<Integer, Map<Integer, ReportFormTargetRecord>> entry : targetRecordMap.entrySet()) {
                Map<Integer, ReportFormTargetRecord> termTargetRecordMap = entry.getValue();
                for (Map.Entry<Integer, ReportFormTargetRecord> innerEntry : termTargetRecordMap.entrySet()) {
                    ReportFormTargetRecord value = innerEntry.getValue();
                    targetRecordList.add(value);
                }
            }
            targetMapper.insertTargetRecordList(targetRecordList);
            //自用需要更新库存
            updateWashedCoalStoreWhenSelfUse(dutyStartTime, targetRecordMap);
        } else {
            targetRecordMap = new HashMap<>();
            for (ReportFormTargetRecord reportFormTargetRecord : targetRecordList) {
                Map<Integer, ReportFormTargetRecord> termTargetRecordMap;
                termTargetRecordMap = getReportFormTargetTermRecordMap(targetRecordMap, reportFormTargetRecord.getTargetType());
                termTargetRecordMap.put(reportFormTargetRecord.getTerm(), reportFormTargetRecord);
            }
        }
        return targetRecordMap;
    }

    private void checkWaterConsume(Date dutyStartTime,Map<Integer, Map<Integer, ReportFormTargetRecord>> targetRecordMap) {
        if (!targetRecordMap.containsKey(ReportFormTargetConstant.WATER_CONSUME)){
            ReportFormTargetRecord waterConsumeRecordTermOne = createWaterConsumeRecord(dutyStartTime, 1);
            ReportFormTargetRecord waterConsumeRecordTermTwo = createWaterConsumeRecord(dutyStartTime, 2);
            Map<Integer,ReportFormTargetRecord> termRecordMap = new HashMap<>();
            termRecordMap.put(1,waterConsumeRecordTermOne);
            termRecordMap.put(2,waterConsumeRecordTermTwo);
            targetRecordMap.put(ReportFormTargetConstant.WATER_CONSUME,termRecordMap);
        }
    }

    private ReportFormTargetRecord createWaterConsumeRecord(Date dutyStartTime,int term) {
        ReportFormTargetRecord reportFormTargetRecordTermOne = new ReportFormTargetRecord();
        reportFormTargetRecordTermOne.setDutyStartTime(dutyStartTime);
        reportFormTargetRecordTermOne.setTerm(term);
        reportFormTargetRecordTermOne.setTargetType(ReportFormTargetConstant.WATER_CONSUME);
        return reportFormTargetRecordTermOne;
    }

    /**
     * 有自用数据时，更新库存的洗混煤
     *
     * @param dutyStartTime
     * @param targetRecordMap
     */
    private void updateWashedCoalStoreWhenSelfUse(Date dutyStartTime, Map<Integer, Map<Integer, ReportFormTargetRecord>> targetRecordMap) {
        if (targetRecordMap.containsKey(ReportFormTargetConstant.SELF_USE_COAL_TON) && targetRecordMap.get(ReportFormTargetConstant.SELF_USE_COAL_TON).containsKey(0)) {
            ReportFormTargetRecord selfUseRecord = targetRecordMap.get(ReportFormTargetConstant.SELF_USE_COAL_TON).get(0);
            ReportFormOutputStoreRecord lastStoreRecord = getLastStoreRecord(dutyStartTime);
            ReportFormOutputStoreRecord currentOutputRecord = getOutputStoreRecord(dutyStartTime).get(TYPE_OUTPUT);
            ReportFormOutputStoreRecord currentStoreRecord = getOutputStoreRecord(dutyStartTime).get(TYPE_STORE);
            if (currentStoreRecord == null) {
                currentStoreRecord = getCurrentStoreRecord(dutyStartTime, lastStoreRecord);
            }
            TransBean transPortCacheBean = transPortService.getTransPortCacheBean(dutyStartTime);
            Double currentWashedCoalStoreValue = lastStoreRecord.getWashedCoal();
            if (currentOutputRecord != null) {
                currentWashedCoalStoreValue += currentOutputRecord.getWashedCoal();
            }
            currentWashedCoalStoreValue = getStoreValueWithoutSale(transPortCacheBean, currentWashedCoalStoreValue, TYPE_STORE);
            currentWashedCoalStoreValue -= selfUseRecord.getClassValue();
            currentStoreRecord.setWashedCoal(currentWashedCoalStoreValue);
            insertOrUpdateRecord(currentStoreRecord);
        }
    }


    /**
     * 从任务模块获取某一个班的指标数据
     *
     * @param dutyStartTime
     * @return
     */
    private Map<Integer, Map<Integer, ReportFormTargetRecord>> getTargetRecordFromTaskModule(Date dutyStartTime) {
        Date firstDayOfYear = DateUtils.addHours(DateUtils.truncate(dutyStartTime, Calendar.YEAR),-4);

        List<TaskFeedbackInfo> taskFeedbackInfoList = targetMapper.getTaskFeedbackInfoList(firstDayOfYear, DateUtils.addHours(dutyStartTime, 12));
        //outer key :targetTypeId  inner key : term
        Map<Integer, Map<Integer, ReportFormTargetRecord>> reportFormTargetRecordMap = new HashMap<>();
        for (TaskFeedbackInfo taskFeedbackInfo : taskFeedbackInfoList) {
            Integer feedbackInfoId = taskFeedbackInfo.getFeedbackInfoId();
            if (feedbackTargetMap.containsKey(feedbackInfoId)) {
                getTargetRecordFromFeedbackInfo(dutyStartTime, reportFormTargetRecordMap, taskFeedbackInfo);
            }
        }
        checkNonExistTargetRecord(dutyStartTime, reportFormTargetRecordMap);
        checkWaterConsume(dutyStartTime,reportFormTargetRecordMap);
        return reportFormTargetRecordMap;
    }


    /**
     * 从任务反馈信息中获取指标数据
     *
     * @param dutyStartTime
     * @param reportFormTargetRecordMap
     * @param taskFeedbackInfo
     */
    private void getTargetRecordFromFeedbackInfo(Date dutyStartTime, Map<Integer, Map<Integer, ReportFormTargetRecord>> reportFormTargetRecordMap, TaskFeedbackInfo taskFeedbackInfo) {
        Date firstDayOfMonth = DateUtils.addHours(DateUtils.truncate(dutyStartTime, Calendar.MONTH),-4);
        Integer feedbackInfoId = taskFeedbackInfo.getFeedbackInfoId();
        FeedbackTargetRelation feedbackTargetRelation = feedbackTargetMap.get(feedbackInfoId);
        Integer targetTypeId = feedbackTargetRelation.getTargetTypeId();
        Map<Integer, ReportFormTargetRecord> reportFormTargetTermRecordMap;
        reportFormTargetTermRecordMap = getReportFormTargetTermRecordMap(reportFormTargetRecordMap, targetTypeId);
        Integer term = feedbackTargetRelation.getTerm();
        ReportFormTargetRecord reportFormTargetRecord;
        if (reportFormTargetTermRecordMap.containsKey(term)) {
            reportFormTargetRecord = reportFormTargetTermRecordMap.get(term);
        } else {
            reportFormTargetRecord = generateNewTargetRecord(dutyStartTime, feedbackTargetRelation);
            reportFormTargetTermRecordMap.put(term, reportFormTargetRecord);
        }
        Double feedbackValue = taskFeedbackInfo.getFeedbackValue();
        Date feedbackTime = taskFeedbackInfo.getFeedbackTime();
        if (dutyStartTime.before(feedbackTime)) {
            reportFormTargetRecord.setClassValue(feedbackValue);
        }
        //水表读数数据不算累积
        if (!ReportFormTargetConstant.WATER_METER.equals(targetTypeId)) {
            reportFormTargetRecord.setYearValue(reportFormTargetRecord.getYearValue() + feedbackValue);
            if (firstDayOfMonth.before(feedbackTime)) {
                reportFormTargetRecord.setMonthValue(reportFormTargetRecord.getMonthValue() + feedbackValue);
            }
        } else {
            countWaterConsumeFromMeter(reportFormTargetRecordMap, feedbackTargetRelation, reportFormTargetRecord);
        }
    }

    private void countWaterConsumeFromMeter(Map<Integer, Map<Integer, ReportFormTargetRecord>> reportFormTargetRecordMap, FeedbackTargetRelation feedbackTargetRelation, ReportFormTargetRecord reportFormTargetRecord) {
        Map<Integer, ReportFormTargetRecord> reportFormTargetTermRecordMap;
        ReportFormTargetRecord reportFormWaterTargetRecord = getWaterTargetRecord(feedbackTargetRelation, reportFormTargetRecord);
        reportFormTargetTermRecordMap = getReportFormTargetTermRecordMap(reportFormTargetRecordMap, ReportFormTargetConstant.WATER_CONSUME);
        reportFormTargetTermRecordMap.put(feedbackTargetRelation.getTerm(), reportFormWaterTargetRecord);
    }


    private Map<Integer, ReportFormTargetRecord> getReportFormTargetTermRecordMap(Map<Integer, Map<Integer, ReportFormTargetRecord>> reportFormTargetRecordMap, Integer targetTypeId) {
        Map<Integer, ReportFormTargetRecord> reportFormTargetTermRecordMap;
        if (reportFormTargetRecordMap.containsKey(targetTypeId)) {
            reportFormTargetTermRecordMap = reportFormTargetRecordMap.get(targetTypeId);
        } else {
            reportFormTargetTermRecordMap = new HashMap<>();
            reportFormTargetRecordMap.put(targetTypeId, reportFormTargetTermRecordMap);
        }
        return reportFormTargetTermRecordMap;
    }


    /**
     * 根据水表读数记录生成中水记录
     *
     * @param feedbackTargetRelation 水表读数relation
     * @param reportFormTargetRecord 水表读数记录
     * @return
     */
    private ReportFormTargetRecord getWaterTargetRecord(FeedbackTargetRelation feedbackTargetRelation, ReportFormTargetRecord reportFormTargetRecord) {
        Date dutyStartTime = reportFormTargetRecord.getDutyStartTime();
        ReportFormTargetRecord reportFormWaterTargetRecord = generateNewTargetRecord(dutyStartTime, feedbackTargetRelation);
        reportFormWaterTargetRecord.setTargetType(ReportFormTargetConstant.WATER_CONSUME);
        //把当前水表读数设置为班累积
        reportFormWaterTargetRecord.setClassValue(reportFormTargetRecord.getClassValue());
        //调用方法减去上一班累积
        getWaterValueFromTaskModule(reportFormWaterTargetRecord, feedbackTargetRelation);
        return reportFormWaterTargetRecord;

    }

    /**
     * 获取不到的指标数据，生成为0的数据填充
     *
     * @param dutyStartTime
     * @param reportFormTargetRecordMap
     */
    private void checkNonExistTargetRecord(Date dutyStartTime, Map<Integer, Map<Integer, ReportFormTargetRecord>> reportFormTargetRecordMap) {
        Collection<FeedbackTargetRelation> feedbackTargetRelationList = feedbackTargetMap.values();
        for (FeedbackTargetRelation feedbackTargetRelation : feedbackTargetRelationList) {
            Map<Integer, ReportFormTargetRecord> reportFormTargetTermRecordMap;
            reportFormTargetTermRecordMap = getReportFormTargetTermRecordMap(reportFormTargetRecordMap, feedbackTargetRelation.getTargetTypeId());
            if (!reportFormTargetTermRecordMap.containsKey(feedbackTargetRelation.getTerm())) {
                ReportFormTargetRecord reportFormTargetRecord = generateNewTargetRecord(dutyStartTime, feedbackTargetRelation);
                reportFormTargetTermRecordMap.put(feedbackTargetRelation.getTerm(), reportFormTargetRecord);
            }
        }
    }

    /**
     * 生成班、月、年值都为0的指标数据
     *
     * @param dutyStartTime
     * @param feedbackTargetRelation
     * @return
     */
    private ReportFormTargetRecord generateNewTargetRecord(Date dutyStartTime, FeedbackTargetRelation feedbackTargetRelation) {
        ReportFormTargetRecord reportFormTargetRecord;
        reportFormTargetRecord = new ReportFormTargetRecord();
        reportFormTargetRecord.setDutyStartTime(dutyStartTime);
        reportFormTargetRecord.setTargetType(feedbackTargetRelation.getTargetTypeId());
        reportFormTargetRecord.setTerm(feedbackTargetRelation.getTerm());
        return reportFormTargetRecord;
    }

    private void getWaterValueFromTaskModule(ReportFormTargetRecord reportFormTargetRecord, FeedbackTargetRelation feedbackTargetRelation) {
        Date dutyStartTime = reportFormTargetRecord.getDutyStartTime();
        Double currentWaterMeter = reportFormTargetRecord.getClassValue();
        Date firstDayOfYear = DateUtils.addHours(DateUtils.truncate(dutyStartTime, Calendar.YEAR),-4);
        Date firstDayOfMonth = DateUtils.addHours(DateUtils.truncate(dutyStartTime, Calendar.MONTH),-4);
        TaskFeedbackInfo lastYearWaterMeterValue = targetMapper.getLastTaskFeedbackInfoBeforeDate(firstDayOfYear, feedbackTargetRelation.getFeedbackInfoId());
        TaskFeedbackInfo lastMonthWaterMeterValue = targetMapper.getLastTaskFeedbackInfoBeforeDate(firstDayOfMonth, feedbackTargetRelation.getFeedbackInfoId());
        TaskFeedbackInfo lastClassWaterMeterValue = targetMapper.getLastTaskFeedbackInfoBeforeDate(dutyStartTime, feedbackTargetRelation.getFeedbackInfoId());
        TaskFeedbackInfo firstClassWaterMeterInfoBeforeDate = targetMapper.getFirstTaskFeedbackInfoBeforeDate(dutyStartTime, feedbackTargetRelation.getFeedbackInfoId());
        if (lastClassWaterMeterValue != null && lastClassWaterMeterValue.getFeedbackValue() != null && currentWaterMeter != 0.0) {
            reportFormTargetRecord.setClassValue(currentWaterMeter - lastClassWaterMeterValue.getFeedbackValue());
        } else {
            reportFormTargetRecord.setClassValue(0.0);
        }
        //当前班没有水表读数，取上一个记录去计算年累计和月累计
        if (currentWaterMeter == 0.0 && lastClassWaterMeterValue != null && lastClassWaterMeterValue.getFeedbackValue() != null) {
            currentWaterMeter = lastClassWaterMeterValue.getFeedbackValue();
        }
        if (lastMonthWaterMeterValue != null && lastMonthWaterMeterValue.getFeedbackValue() != null && currentWaterMeter != 0.0) {
            reportFormTargetRecord.setMonthValue(currentWaterMeter - lastMonthWaterMeterValue.getFeedbackValue());
        }
        //历史数据不足一个月时
        else if
                (firstClassWaterMeterInfoBeforeDate != null && firstClassWaterMeterInfoBeforeDate.getFeedbackValue() != null && currentWaterMeter != 0.0) {
            reportFormTargetRecord.setMonthValue(currentWaterMeter - firstClassWaterMeterInfoBeforeDate.getFeedbackValue());
        }
        if (lastYearWaterMeterValue != null && lastYearWaterMeterValue.getFeedbackValue() != null && currentWaterMeter != 0.0) {
            reportFormTargetRecord.setYearValue(currentWaterMeter - lastYearWaterMeterValue.getFeedbackValue());
        } else if
                (firstClassWaterMeterInfoBeforeDate != null && firstClassWaterMeterInfoBeforeDate.getFeedbackValue() != null && currentWaterMeter != 0.0) {
            reportFormTargetRecord.setYearValue(currentWaterMeter - firstClassWaterMeterInfoBeforeDate.getFeedbackValue());
        }
    }


    public List<ReportFormOutputStoreRecord> getOutputRecordsInDuration(Date startTime,Date endTime){
       return outputStoreMapper.getOutputStoreRecordsInDuration(TYPE_OUTPUT,startTime,endTime);
    }
}
