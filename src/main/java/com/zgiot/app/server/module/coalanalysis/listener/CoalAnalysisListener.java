package com.zgiot.app.server.module.coalanalysis.listener;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.dataprocessor.impl.CacheUpdater;
import com.zgiot.app.server.module.coalanalysis.mapper.CoalAnalysisMapper;
import com.zgiot.app.server.module.coalanalysis.pojo.DensityAndFlowInfo;
import com.zgiot.app.server.module.coalanalysis.pojo.DensityAndFlowValue;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.constants.CoalAnalysisConstants;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.enums.MetricDataTypeEnum;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.MetricModel;
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
    @Autowired
    private CacheUpdater cacheUpdater;
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
            logger.debug("接收到煤质化验数据：化验项目{}，化验样本{}，化验时间{}，灰分{}，水份{}，硫份{}，发热量{}", record.getTarget(), record.getSample(), record.getTime(), record.getAad(), record.getMt(), record.getStad(), record.getQnetar());
            Integer existRecordId = coalAnalysisMapper.getExistRecordId(record);
            if (existRecordId != null) {
                updateRecord(record, existRecordId);
            } else {
                insertRecord(dataModel, record);
            }
            putParamDataToCache(record);

        }
    }

    private void updateRecord(CoalAnalysisRecord record, Integer existRecordId) {
        logger.debug("该化验数据已经存在，进行更新");
        coalAnalysisMapper.updateRecord(record, existRecordId);
    }

    private void insertRecord(DataModel dataModel, CoalAnalysisRecord record) {
        logger.debug("新增一条化验数据");
        coalAnalysisMapper.insertRecord(record);
        List<DensityAndFlowValue> densityAndFlowValues = getDensityAndFlowValues(dataModel, record);
        if (!CollectionUtils.isEmpty(densityAndFlowValues)) {
            disposeDensityAndFlow(densityAndFlowValues, record);
        }
    }

    private void putParamDataToCache(CoalAnalysisRecord record) {
        List<DataModel> paramDataModel = parseToParamDataModel(record);
        for (DataModel dataModel : paramDataModel) {
            cacheUpdater.onDataChange(dataModel);
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
            logger.debug("开始计算id为{}的煤质化验数据对应的分选密度/顶水流量，设备列表为：{}", record.getId(), thingCodes);
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
                    logger.debug("获取不到设备{}上的分选密度/顶水流量数据", runThingCode);
                    continue;
                }
                DensityAndFlowValue densityAndFlowValue = getRunDensityAndFlowValue(densityAndFlowInfo, densityData, flowData);
                if (densityAndFlowValue == null) {
                    logger.debug("根据计算，设备{}未处于运行状态，不采集其分选密度/顶水流量数据", runThingCode);
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

    private List<DataModel> parseToParamDataModel(CoalAnalysisRecord record) {
        List<DataModel> dataModels = new ArrayList<>();
        getTargetModel(dataModels,record);
        if (record.getAad() != null) {
            getParamModel(dataModels, record, MetricCodes.ASSAY_AAD, record.getAad());
        }
        if (record.getMt() != null) {
            getParamModel(dataModels, record, MetricCodes.ASSAY_MT, record.getMt());
        }
        if (record.getStad() != null) {
            getParamModel(dataModels, record, MetricCodes.ASSAY_STAD, record.getStad());
        }
        if (record.getQnetar() != null) {
            getParamModel(dataModels, record, MetricCodes.ASSAY_QNETAR, record.getQnetar());
        }
        if (record.getAvgDensity() != null) {
            getParamModel(dataModels, record, MetricCodes.ASSAY_DENSITY, record.getAvgDensity());
        }
        if (record.getAvgFlow() != null) {
            getParamModel(dataModels, record, MetricCodes.ASSAY_FLOW, record.getAvgFlow());
        }
        return dataModels;
    }

    private void getTargetModel(List<DataModel> dataModels, CoalAnalysisRecord coalAnalysisRecord){
        DataModel dataModel = new DataModel();
        dataModel.setMetricDataType(MetricDataTypeEnum.METRIC_DATA_TYPE_OK.getName());
        String system ;
        if (coalAnalysisRecord.getSystem().equals(1)){
            system = GlobalConstants.SYSTEM_ONE;
        }else {
            system = GlobalConstants.SYSTEM_TWO;
        }
        dataModel.setThingCode(system+coalAnalysisRecord.getTarget());
        dataModel.setMetricCategoryCode(MetricModel.CATEGORY_ASSAY);
        dataModel.setMetricCode(MetricCodes.ASSAY_SAMPLE);
        dataModel.setDataTimeStamp(coalAnalysisRecord.getTime());
        dataModel.setValue(coalAnalysisRecord.getTarget());
        dataModels.add(dataModel);
    }

    private void getParamModel(List<DataModel> dataModels, CoalAnalysisRecord coalAnalysisRecord, String metricCode,
                               Double value) {
        DataModel dataModel = new DataModel();
        dataModel.setMetricDataType(MetricDataTypeEnum.METRIC_DATA_TYPE_OK.getName());
        dataModel.setThingCode(coalAnalysisRecord.getSample());
        dataModel.setMetricCategoryCode(MetricModel.CATEGORY_ASSAY);
        dataModel.setMetricCode(metricCode);
        dataModel.setDataTimeStamp(coalAnalysisRecord.getTime());
        dataModel.setValue(String.valueOf(value));
        dataModels.add(dataModel);
    }

    @Override
    public void onError(Throwable error) {
        logger.error("data invalid", error);
    }
}
