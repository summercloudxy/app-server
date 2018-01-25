package com.zgiot.app.server.module.coalanalysis.listener;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.module.coalanalysis.mapper.CoalAnalysisMapper;
import com.zgiot.app.server.module.coalanalysis.pojo.DensityAndFlowInfo;
import com.zgiot.app.server.module.coalanalysis.pojo.DensityAndFlowValue;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.constants.CoalAnalysisConstants;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import com.zgiot.common.pojo.DataModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CoalAnalysisListener implements DataListener {
    private final Logger logger = LoggerFactory.getLogger(CoalAnalysisListener.class);
    @Autowired
    private CoalAnalysisMapper coalAnalysisMapper;
    @Autowired
    private HistoryDataService historyDataService;
    /**
     * outerkey: target  innerkey: system
     */
    private Map<String, Map<Integer, DensityAndFlowInfo>> densityThingMap = new HashMap<>(10);
//    private Map<String, Map<Integer, DensityAndFlowInfo>> flowThingMap = new HashMap<>(10);
//    private static final String STATE_RUN = "2";

    @PostConstruct
    private void init() {
        List<DensityAndFlowInfo> densityAndFlowInfos = coalAnalysisMapper.getDensityAndFlowInfo();
        for (DensityAndFlowInfo densityAndFlowInfo : densityAndFlowInfos) {
            generateThingMap(densityAndFlowInfo, densityThingMap);
        }
    }

    private void generateThingMap(DensityAndFlowInfo densityAndFlowInfo, Map<String, Map<Integer, DensityAndFlowInfo>> paramMap) {
        if (!paramMap.containsKey(densityAndFlowInfo.getCoalTarget())) {
            paramMap.put(densityAndFlowInfo.getCoalTarget(), new HashMap<>(10));
        }
        Map<Integer, DensityAndFlowInfo> systemDensityAndFlowInfo = paramMap.get(densityAndFlowInfo.getCoalTarget());
        systemDensityAndFlowInfo.put(densityAndFlowInfo.getSystem(), densityAndFlowInfo);
    }

    @Override
    public void onDataChange(DataModel dataModel) {
        if (CoalAnalysisConstants.COAL_ANALYSIS.equals(dataModel.getThingCode())) {
            String value = dataModel.getValue();
            CoalAnalysisRecord record = JSON.parseObject(value, CoalAnalysisRecord.class);
            Integer existRecordId = coalAnalysisMapper.getExistRecordId(record);
            if (existRecordId != null) {
                coalAnalysisMapper.updateRecord(record, existRecordId);
            } else {
                coalAnalysisMapper.insertRecord(record);
                List<DensityAndFlowValue> densityAndFlowValues = getDensityAndFlowValues(dataModel, record);
                if (!CollectionUtils.isEmpty(densityAndFlowValues)) {
                    disposeDensityAndFlow(densityAndFlowValues, record);
                }
            }
        }
    }

    private List<DensityAndFlowValue> getDensityAndFlowValues(DataModel dataModel, CoalAnalysisRecord record) {
        List<DensityAndFlowValue> densityAndFlowValues = null;
        if (densityThingMap.containsKey(dataModel.getMetricCode())) {
            DensityAndFlowInfo densityAndFlowInfo = densityThingMap.get(dataModel.getMetricCode()).get(record.getSystem());
            List<String> thingCodes = densityAndFlowInfo.getThingCodes();
//            List<String> runThingCodes = new ArrayList<>();
//            for (String thingCode : thingCodes) {
//                DataModel thingState = historyDataService.findClosestHistoryDataInDuration(Lists.newArrayList(thingCode), Lists.newArrayList(MetricCodes.STATE), record.getTime(), historyDataService.QUERY_TIME_TYPE_BEFORE);
//                if (STATE_RUN.equals(thingState.getValue())) {
//                    runThingCodes.add(thingState.getThingCode());
//                }
//            }
            densityAndFlowValues = new ArrayList<>();
            for (String runThingCode : thingCodes) {
                DataModel densityData = null;
                DataModel flowData = null;
                if (!StringUtils.isBlank(densityAndFlowInfo.getDensityCode())) {
                    densityData = historyDataService.findClosestHistoryData(Lists.newArrayList(runThingCode), Lists.newArrayList(densityAndFlowInfo.getDensityCode()), record.getTime());
                }
                if (!StringUtils.isBlank(densityAndFlowInfo.getFlowCode())) {
                    flowData = historyDataService.findClosestHistoryData(Lists.newArrayList(runThingCode), Lists.newArrayList(densityAndFlowInfo.getFlowCode()), record.getTime());
                }
                if (densityData == null && flowData == null) {
                    continue;
                }
                DensityAndFlowValue densityAndFlowValue = getRunDensityAndFlowValue(densityAndFlowInfo, densityData, flowData);
                if (densityAndFlowValue == null) {
                    continue;
                }
//                if (flowData != null && !StringUtils.isBlank(flowData.getValue())) {
//                    if (densityAndFlowInfo.getRunFlowThreshold() != null && Double.valueOf(flowData.getValue()) < densityAndFlowInfo.getRunFlowThreshold()) {
//                        continue;
//                    }
//                    densityAndFlowValue.setFlow(Double.valueOf(flowData.getValue()));
//                }
                densityAndFlowValue.setThingCode(runThingCode);
                densityAndFlowValue.setAnalysisId(record.getId());
                densityAndFlowValues.add(densityAndFlowValue);
            }
        }
        return densityAndFlowValues;
    }

    private DensityAndFlowValue getRunDensityAndFlowValue(DensityAndFlowInfo densityAndFlowInfo, DataModel densityData, DataModel flowData) {
        DensityAndFlowValue densityAndFlowValue = new DensityAndFlowValue();

//                if (densityData != null && !StringUtils.isBlank(densityData.getValue())) {
//                    if (densityAndFlowInfo.getRunDensityThreshold() != null && Double.valueOf(densityData.getValue()) < densityAndFlowInfo.getRunDensityThreshold()) {
//                        continue;
//                    }
//                    densityAndFlowValue.setDensity(Double.valueOf(densityData.getValue()));
//                }
        if (!isRunState(densityAndFlowInfo, densityData, flowData)) {
            return null;
        }
        if (densityData != null && !StringUtils.isBlank(densityData.getValue())) {
            densityAndFlowValue.setDensity(Double.valueOf(densityData.getValue()));
        }
        if (flowData != null && !StringUtils.isBlank(flowData.getValue())) {
            densityAndFlowValue.setFlow(Double.valueOf(flowData.getValue()));
        }
        return densityAndFlowValue;
    }

    private boolean isRunState(DensityAndFlowInfo densityAndFlowInfo, DataModel densityData, DataModel flowData) {
        if (densityAndFlowInfo.getRunDensityThreshold() != null &&
                (densityData == null || StringUtils.isBlank(densityData.getValue()) || Double.valueOf(densityData.getValue()) < densityAndFlowInfo.getRunDensityThreshold())) {
            return false;
        }
        if (densityAndFlowInfo.getRunFlowThreshold() != null &&
                (flowData == null || StringUtils.isBlank(flowData.getValue()) || Double.valueOf(flowData.getValue()) < densityAndFlowInfo.getRunFlowThreshold())) {
            return false;
        }
        return true;
    }

    private void disposeDensityAndFlow(List<DensityAndFlowValue> densityAndFlowValues, CoalAnalysisRecord record) {
        countAvgValue(densityAndFlowValues, record);
        coalAnalysisMapper.updateRecordDensityAndFlow(record);
//        densityAndFlowValues.forEach(t -> t.setAnalysisId(record.getId()));
        coalAnalysisMapper.insertDensityAndFlowValues(densityAndFlowValues);

    }

    private void countAvgValue(List<DensityAndFlowValue> densityAndFlowValues, CoalAnalysisRecord record) {
        Double avgDensity = 0.0;
        Double avgFlow = 0.0;
        int count = 0;
        for (DensityAndFlowValue densityAndFlowValue : densityAndFlowValues) {
            if (densityAndFlowValue.getDensity() != null) {
                avgDensity += densityAndFlowValue.getDensity();
            }
            if (densityAndFlowValue.getFlow() != null) {
                avgFlow += densityAndFlowValue.getFlow();
            }
            count++;
        }
        if (count != 0) {
            avgDensity = avgDensity / count;
            avgFlow = avgFlow / count;
        }
        record.setAvgDensity(avgDensity);
        record.setAvgFlow(avgFlow);
    }

    @Override
    public void onError(Throwable error) {
        logger.error("data invalid", error);
    }
}
