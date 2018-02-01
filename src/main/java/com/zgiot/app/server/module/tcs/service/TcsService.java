package com.zgiot.app.server.module.tcs.service;

import com.google.common.collect.Lists;
import com.zgiot.app.server.module.tcs.mapper.TcsMapper;
import com.zgiot.app.server.module.tcs.pojo.AnalysisInfoList;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.app.server.module.tcs.pojo.TcsAnalysisInfo;
import com.zgiot.app.server.module.tcs.pojo.TcsParameter;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.constants.CoalAnalysisConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.zgiot.app.server.module.tcs.pojo.FilterCondition.FilterConditionBuilder.newFilterCondition;

@Service
public class TcsService {
    @Autowired
    private TcsMapper tcsMapper;
    @Autowired
    private DataService dataService;
    @Autowired
    private HistoryDataService historyDataService;
    private static final int SORT_TYPE_ASC = 0;
    private static final int SORT_TYPE_DESC = 1;
    private static final String RAW_COAL_301 = "301";
    private static final String RAW_COAL_302 = "302";
    private static final double RUN_FLOW_THRESHOLD = 10.0;
    private static final double PRE_COAL_SCALE = 0.7;
    private static final String ONE_DECIMAL_FORMAT = "#####0.0";
    private static final String INT_FORMAT = "#";
    private static final String[] FEED_BARREL_CODE = {"857-1.FT-1", "857-2.FT-1", "857-3.FT-1", "858-1.FT-1", "858-2.FT-1", "858-3.FT-1","859-1.FT-1", "859-2.FT-1", "859-3.FT-1"};


    /**
     * 获取煤质化验历史数据，一天为一条记录
     * @param system
     * @param endTime
     * @param page
     * @param count
     * @return
     */
    public List<AnalysisInfoList> getAnalysisHistoryListGroupByDay(Integer system, Date endTime, Integer page, Integer count) {
        FilterCondition filterCondition = newFilterCondition().endTime(endTime).page(page).count(count).sortType(SORT_TYPE_DESC).
                system(system).addTarget(CoalAnalysisConstants.TCS_CONCENTRATE).build();
        List<CoalAnalysisRecord> recordsMatchCondition = tcsMapper.getRecordsMatchCondition(filterCondition);
        if (recordsMatchCondition.isEmpty()) {
            return null;
        }
        CoalAnalysisRecord firstRecord = recordsMatchCondition.stream().min(Comparator.comparing(CoalAnalysisRecord::getTime)).get();
        Date startDate = DateUtils.truncate(firstRecord.getTime(), Calendar.DAY_OF_MONTH);
        filterCondition = newFilterCondition().startTime(startDate).endTime(endTime).system(system).sortType(SORT_TYPE_DESC).addTarget(CoalAnalysisConstants.TCS_CONCENTRATE).build();
        List<CoalAnalysisRecord> concentrateRecords = tcsMapper.getRecordsMatchCondition(filterCondition);
        filterCondition.setTargetList(Lists.newArrayList(CoalAnalysisConstants.TCS_TAIL));
        List<CoalAnalysisRecord> tailRecords = tcsMapper.getRecordsMatchCondition(filterCondition);
        filterCondition.setTargetList(Lists.newArrayList(CoalAnalysisConstants.RAW_COAL));
        filterCondition.setSample(system + RAW_COAL_301);
        List<CoalAnalysisRecord> rawCoal01Record = tcsMapper.getRecordsMatchCondition(filterCondition);
        filterCondition.setSample(system + RAW_COAL_302);
        List<CoalAnalysisRecord> rawCoal02Record = tcsMapper.getRecordsMatchCondition(filterCondition);
        Map<Date, List<TcsAnalysisInfo>> concentrateAndTailDateMap = getConcentrateAndTailDateMap(concentrateRecords, tailRecords);
        Map<Date, List<TcsAnalysisInfo>> raw01And02DateMap = getRaw01And02DateMap(rawCoal01Record, rawCoal02Record);
        return getAnalysisInfoListGroupByDate(concentrateAndTailDateMap, raw01And02DateMap);
    }


    /**
     * 按天为单位，组装精矿和尾矿记录
     * @param concentrateRecords
     * @param tailRecords
     * @return
     */
    private Map<Date, List<TcsAnalysisInfo>> getConcentrateAndTailDateMap(List<CoalAnalysisRecord> concentrateRecords, List<CoalAnalysisRecord> tailRecords) {
        Map<Date, CoalAnalysisRecord> concentrateRecordMap = concentrateRecords.stream().collect(Collectors.toMap(CoalAnalysisRecord::getTime, t -> t));
        Map<Date, CoalAnalysisRecord> tailRecordMap = tailRecords.stream().collect(Collectors.toMap(CoalAnalysisRecord::getTime, t -> t));
        Set<Date> dateTimeSet = new TreeSet<>(Comparator.reverseOrder());
        dateTimeSet.addAll(concentrateRecordMap.keySet());
        dateTimeSet.addAll(tailRecordMap.keySet());
        Map<Date, List<TcsAnalysisInfo>> recordGroupByDate = new TreeMap<>();
        for (Date dateTime : dateTimeSet) {
            TcsAnalysisInfo tcsAnalysisInfo = new TcsAnalysisInfo();
            tcsAnalysisInfo.setConcentrateRecord(concentrateRecordMap.get(dateTime));
            tcsAnalysisInfo.setTailRecord(tailRecordMap.get(dateTime));
            Date date = DateUtils.truncate(dateTime, Calendar.DAY_OF_MONTH);
            if (!recordGroupByDate.containsKey(date)) {
                recordGroupByDate.put(date, new ArrayList<>());
            }
            List<TcsAnalysisInfo> tcsAnalysisInfos = recordGroupByDate.get(date);
            tcsAnalysisInfos.add(tcsAnalysisInfo);
        }
        return recordGroupByDate;
    }

    /**
     * 按天为单位，组装原煤01和02的记录
     * @param rawCoal01
     * @param rawCoal02
     * @return
     */
    private Map<Date, List<TcsAnalysisInfo>> getRaw01And02DateMap(List<CoalAnalysisRecord> rawCoal01, List<CoalAnalysisRecord> rawCoal02) {
        Map<Date, CoalAnalysisRecord> rawCoal01RecordMap = rawCoal01.stream().collect(Collectors.toMap(CoalAnalysisRecord::getTime, t -> t));
        Map<Date, CoalAnalysisRecord> rawCoal02RecordMap = rawCoal02.stream().collect(Collectors.toMap(CoalAnalysisRecord::getTime, t -> t));
        Set<Date> dateTimeSet = new TreeSet<>(Comparator.reverseOrder());
        dateTimeSet.addAll(rawCoal01RecordMap.keySet());
        dateTimeSet.addAll(rawCoal02RecordMap.keySet());
        Map<Date, List<TcsAnalysisInfo>> recordGroupByDate = new TreeMap<>();
        for (Date dateTime : dateTimeSet) {
            TcsAnalysisInfo tcsAnalysisInfo = new TcsAnalysisInfo();
            tcsAnalysisInfo.setRawCoal01(rawCoal01RecordMap.get(dateTime));
            tcsAnalysisInfo.setRawCoal02(rawCoal02RecordMap.get(dateTime));
            Date date = DateUtils.truncate(dateTime, Calendar.DAY_OF_MONTH);
            if (!recordGroupByDate.containsKey(date)) {
                recordGroupByDate.put(date, new ArrayList<>());
            }
            List<TcsAnalysisInfo> tcsAnalysisInfos = recordGroupByDate.get(date);
            tcsAnalysisInfos.add(tcsAnalysisInfo);
        }
        return recordGroupByDate;
    }

    /**
     * 组装精矿尾矿和原煤记录
     * @param concentrateAndTailDateMap
     * @param raw01And02DateMap
     * @return
     */
    private List<AnalysisInfoList> getAnalysisInfoListGroupByDate(Map<Date, List<TcsAnalysisInfo>> concentrateAndTailDateMap, Map<Date, List<TcsAnalysisInfo>> raw01And02DateMap) {
        Set<Date> dateSet = new TreeSet<>(Comparator.reverseOrder());
        dateSet.addAll(concentrateAndTailDateMap.keySet());
        dateSet.addAll(raw01And02DateMap.keySet());
        List<AnalysisInfoList> analysisInfoLists = new ArrayList<>();
        for (Date date : dateSet) {
            AnalysisInfoList analysisInfoList = new AnalysisInfoList();
            analysisInfoList.setDate(date);
            if (concentrateAndTailDateMap.containsKey(date) && !raw01And02DateMap.containsKey(date)) {
                analysisInfoList.setTcsAnalysisInfos(concentrateAndTailDateMap.get(date));
                analysisInfoLists.add(analysisInfoList);
                continue;
            }
            if (!concentrateAndTailDateMap.containsKey(date) && raw01And02DateMap.containsKey(date)) {
                analysisInfoList.setTcsAnalysisInfos(raw01And02DateMap.get(date));
                analysisInfoLists.add(analysisInfoList);
                continue;
            }
            List<TcsAnalysisInfo> tcsAnalysisInfos = constructConcentrateAndTailAndRawRecord(concentrateAndTailDateMap, raw01And02DateMap, date);
            analysisInfoList.setTcsAnalysisInfos(tcsAnalysisInfos);
            analysisInfoLists.add(analysisInfoList);
        }
        return analysisInfoLists;
    }

    private List<TcsAnalysisInfo> constructConcentrateAndTailAndRawRecord(Map<Date, List<TcsAnalysisInfo>> concentrateAndTailDateMap, Map<Date, List<TcsAnalysisInfo>> raw01And02DateMap, Date date) {
        List<TcsAnalysisInfo> analysisInfos = new ArrayList<>();
        List<TcsAnalysisInfo> concentrateAndTailRecords = concentrateAndTailDateMap.get(date);
        List<TcsAnalysisInfo> raw01And02Records = raw01And02DateMap.get(date);
        int count = 0;
        for (; count < concentrateAndTailRecords.size(); count++) {
            TcsAnalysisInfo concentrateAndTailRecord = concentrateAndTailRecords.get(count);
            if (count < raw01And02Records.size()) {
                TcsAnalysisInfo raw01And02Record = raw01And02Records.get(count);
                concentrateAndTailRecord.setRawCoal01(raw01And02Record.getRawCoal01());
                concentrateAndTailRecord.setRawCoal02(raw01And02Record.getRawCoal02());
            }
            analysisInfos.add(concentrateAndTailRecord);
        }
        for (; count < raw01And02Records.size(); count++) {
            analysisInfos.add(raw01And02Records.get(count));
        }
        return analysisInfos;
    }


    /**
     * 获取最新一条化验记录
     * @param system
     * @return
     */
    public TcsAnalysisInfo getRecentTcsRecord(int system) {
        TcsAnalysisInfo tcsAnalysisInfo = new TcsAnalysisInfo();
        FilterCondition filterCondition = newFilterCondition().system(system).addTarget(CoalAnalysisConstants.TCS_CONCENTRATE).page(0).count(1).sortType(SORT_TYPE_DESC).build();
        CoalAnalysisRecord concentrateRecord = getRecentRecord(filterCondition);
        filterCondition.setTargetList(Lists.newArrayList(CoalAnalysisConstants.TCS_TAIL));
        CoalAnalysisRecord tailRecord = getRecentRecord(filterCondition);
        filterCondition.setTargetList(Lists.newArrayList(CoalAnalysisConstants.RAW_COAL));
        filterCondition.setSample(system + RAW_COAL_301);
        CoalAnalysisRecord raw01Record = getRecentRecord(filterCondition);
        filterCondition.setSample(system + RAW_COAL_302);
        CoalAnalysisRecord raw02Record = getRecentRecord(filterCondition);
        disposeRecentConcentrateAndTailRecord(tcsAnalysisInfo, concentrateRecord, tailRecord);
        disposeRecentRaw01and02Record(tcsAnalysisInfo, raw01Record, raw02Record);
        return tcsAnalysisInfo;
    }

    private void disposeRecentRaw01and02Record(TcsAnalysisInfo tcsAnalysisInfo, CoalAnalysisRecord raw01Record, CoalAnalysisRecord raw02Record) {
        if (raw01Record != null && raw02Record != null) {
            if (raw01Record.getTime().before(raw02Record.getTime())) {
                tcsAnalysisInfo.setRawCoal02(raw02Record);
            } else if (raw01Record.getTime().after(raw02Record.getTime())) {
                tcsAnalysisInfo.setRawCoal01(raw01Record);
            } else {
                tcsAnalysisInfo.setRawCoal01(raw01Record);
                tcsAnalysisInfo.setRawCoal02(raw02Record);
            }
        } else {
            tcsAnalysisInfo.setRawCoal01(raw01Record);
            tcsAnalysisInfo.setRawCoal02(raw02Record);
        }
    }

    private void disposeRecentConcentrateAndTailRecord(TcsAnalysisInfo tcsAnalysisInfo, CoalAnalysisRecord concentrateRecord, CoalAnalysisRecord tailRecord) {
        if (concentrateRecord != null && tailRecord != null) {
            if (concentrateRecord.getTime().before(tailRecord.getTime())) {
                tcsAnalysisInfo.setTailRecord(tailRecord);
            } else if (concentrateRecord.getTime().after(tailRecord.getTime())) {
                tcsAnalysisInfo.setConcentrateRecord(concentrateRecord);
            } else {
                tcsAnalysisInfo.setConcentrateRecord(concentrateRecord);
                tcsAnalysisInfo.setTailRecord(tailRecord);
            }
        } else {
            tcsAnalysisInfo.setConcentrateRecord(concentrateRecord);
            tcsAnalysisInfo.setTailRecord(tailRecord);
        }
    }

    private CoalAnalysisRecord getRecentRecord(FilterCondition filterCondition) {
        List<CoalAnalysisRecord> concentrateRecords = tcsMapper.getRecordsMatchCondition(filterCondition);
        CoalAnalysisRecord recentRecord = null;
        if (concentrateRecords.size() > 0) {
            recentRecord = concentrateRecords.get(0);
            recentRecord.setDensityAndFlowInfos(tcsMapper.getDensityAndFlowInfo(recentRecord.getId()));
        }
        return recentRecord;
    }


    /**
     * 获取一段时间内的化验数据
     * @param system
     * @param duration
     * @return
     */
    public List<TcsAnalysisInfo> getTcsAnalysisCurve(int system, long duration) {
        FilterCondition filterCondition = newFilterCondition().endTime(new Date()).duration(duration).sortType(SORT_TYPE_DESC).
                system(system).addTarget(CoalAnalysisConstants.TCS_CONCENTRATE).build();
        List<CoalAnalysisRecord> concentrateRecords = tcsMapper.getRecordsMatchCondition(filterCondition);
        filterCondition.setTargetList(Lists.newArrayList(CoalAnalysisConstants.TCS_TAIL));
        List<CoalAnalysisRecord> tailRecords = tcsMapper.getRecordsMatchCondition(filterCondition);
        Map<Date, List<TcsAnalysisInfo>> concentrateAndTailDateMap = getConcentrateAndTailDateMap(concentrateRecords, tailRecords);
        List<TcsAnalysisInfo> tcsAnalysisInfos = new ArrayList<>();
        for (Map.Entry<Date, List<TcsAnalysisInfo>> entry : concentrateAndTailDateMap.entrySet()) {
            tcsAnalysisInfos.addAll(entry.getValue());
        }
        Collections.reverse(tcsAnalysisInfos);
        return tcsAnalysisInfos;
    }

    public TcsParameter getMonitorParameter(int system) {
        //取带煤量的设备
        String[] thingCodesForCoal = {system + RAW_COAL_301, system + RAW_COAL_302};
        //取入料流量的设备
        Double currentCoal = 0.0;
        Double classCoal = 0.0;
        Double monthCoal = 0.0;
        Double ash = 0.0;
        Double preCoal = 0.0;
        for (String thingCode : thingCodesForCoal) {
            currentCoal = getSumValue(currentCoal, MetricCodes.COAL_CAP, thingCode);
            classCoal = getSumValue(classCoal, MetricCodes.CT_C, thingCode);
            monthCoal = getSumValue(monthCoal, MetricCodes.CT_M, thingCode);
            ash = getSumAsh(ash, thingCode);
            preCoal = getSumPreCoal(preCoal, thingCode);
        }
        if (currentCoal>0) {
            ash = ash / currentCoal;
        }
        preCoal = preCoal * PRE_COAL_SCALE;
        Double singleCoal = getSingleCoal(system, preCoal);
        return formatParam(currentCoal, classCoal, monthCoal, ash, preCoal, singleCoal);
    }

    private TcsParameter formatParam(Double currentCoal, Double classCoal, Double monthCoal, Double ash, Double preCoal, Double singleCoal) {
        DecimalFormat intFormat = new DecimalFormat(INT_FORMAT);
        String currentCoalStr = intFormat.format(currentCoal);
        String classCoalStr = intFormat.format(classCoal);
        String monthCoalStr = intFormat.format(monthCoal);
        String preCoalStr = intFormat.format(preCoal);
        String singleCoalStr = intFormat.format(singleCoal);
        DecimalFormat oneDecimalFormat = new DecimalFormat(ONE_DECIMAL_FORMAT);
        String ashStr = oneDecimalFormat.format(ash);
        return new TcsParameter(currentCoalStr, classCoalStr, monthCoalStr, ashStr, preCoalStr, singleCoalStr);
    }

    private Double getSingleCoal(int system, Double preCoal) {
        //运行状态桶的个数
        Integer currentCount = 0;
        Double singleCoal = 0.0;

        //入料流量大于10，则认为该桶处于运行状态
        for (String thingCode : FEED_BARREL_CODE) {
            Optional<DataModelWrapper> flowData = dataService.getData(thingCode, MetricCodes.FL);
            if (flowData.isPresent()) {
                Double flow = Double.valueOf(flowData.get().getValue());
                if (flow > RUN_FLOW_THRESHOLD) {
                    currentCount++;
                }
            }
        }
        if (currentCount != 0) {
            singleCoal = preCoal / currentCount;
        }
        return singleCoal;
    }

    private Double getSumValue(Double value, String metricCode, String thingCode) {
        Optional<DataModelWrapper> data = dataService.getData(thingCode, metricCode);
        if (data.isPresent()) {
            value += Double.valueOf(data.get().getValue());
        }
        return value;
    }

    private Double getSumAsh(Double ash, String thingCode) {
        Optional<DataModelWrapper> coalCapData = dataService.getData(thingCode, MetricCodes.COAL_CAP);
        Optional<DataModelWrapper> ashData = dataService.getData(thingCode, MetricCodes.ASH);
        if (coalCapData.isPresent() && ashData.isPresent()) {
            ash += Double.valueOf(coalCapData.get().getValue()) * Double.valueOf(ashData.get().getValue());
        }
        return ash;
    }

    private Double getSumPreCoal(Double preCoal, String thingCode) {
        DataModel closestHistoryData = historyDataService.findClosestHistoryData(Lists.newArrayList(thingCode), Lists.newArrayList(MetricCodes.COAL_CAP), new Date(System.currentTimeMillis() - 65000));
        if (closestHistoryData != null) {
            preCoal += Double.valueOf(closestHistoryData.getValue());
        }
        return preCoal;
    }


}


                                                  