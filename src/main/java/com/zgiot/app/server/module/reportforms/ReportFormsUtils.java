package com.zgiot.app.server.module.reportforms;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.coalanalysis.mapper.CoalAnalysisMapper;
import com.zgiot.app.server.module.reportforms.pojo.CumulativeData;
import com.zgiot.app.server.module.reportforms.pojo.DensityAndFlowSourceInfo;
import com.zgiot.app.server.module.qualityandquantity.pojo.DutyInfo;
import com.zgiot.app.server.module.qualityandquantity.service.QualityAndQuantityDateUtils;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.enums.MetricDataTypeEnum;
import com.zgiot.common.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.zgiot.app.server.module.tcs.pojo.FilterCondition.FilterConditionBuilder.newFilterCondition;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ReportFormsUtils {
    @Autowired
    private CoalAnalysisMapper coalAnalysisMapper;
    @Autowired
    private HistoryDataService historyDataService;
    @Autowired
    private QualityAndQuantityDateUtils dateUtils;
    public static final Double INVALID_VALUE = 0.0;
    public static final String AVG_RECORD_KEYWORD = "平均";
    private final Logger logger = LoggerFactory.getLogger(ReportFormsUtils.class);
    /**
     * outerkey: thingcode  innerkey: system
     */
    private Map<String, Map<Integer, DensityAndFlowSourceInfo>> densityThingMap = new HashMap<>(10);

    @PostConstruct
    private void init() {
        List<DensityAndFlowSourceInfo> densityAndFlowInfos = coalAnalysisMapper.getDensityAndFlowSourceInfo();
        for (DensityAndFlowSourceInfo densityAndFlowInfo : densityAndFlowInfos) {
            generateThingMap(densityAndFlowInfo, densityThingMap);
        }
    }

    private void generateThingMap(DensityAndFlowSourceInfo densityAndFlowInfo, Map<String, Map<Integer, DensityAndFlowSourceInfo>> paramMap) {
        if (!paramMap.containsKey(densityAndFlowInfo.getCoalSample())) {
            paramMap.put(densityAndFlowInfo.getCoalSample(), new HashMap<>(10));
        }
        Map<Integer, DensityAndFlowSourceInfo> systemDensityAndFlowInfo = paramMap.get(densityAndFlowInfo.getCoalSample());
        systemDensityAndFlowInfo.put(densityAndFlowInfo.getSystem(), densityAndFlowInfo);
    }



    public FilterCondition getDutyFilterCondition(ReportFormsRecord record) {
        DutyInfo dutyInfo = dateUtils.getCurrentDutyInfo(record.getTime());
        return newFilterCondition().addTarget(StringUtils.chomp(record.getTarget(), ReportFormsUtils.AVG_RECORD_KEYWORD)).system(record.getSystem()).startTime(dutyInfo.getDutyStartTime()).endTime(dutyInfo.getDutyEndTime()).build();
    }

    public void getAvgDensityAndFlowOnDuty(ReportFormsRecord record, List<? extends ReportFormsRecord> recordHistory) {
        List<DensityAndFlowInfo> densityAndFlowValues = recordHistory.stream().map(t -> {
            DensityAndFlowInfo densityAndFlowValue = new DensityAndFlowInfo();
            densityAndFlowValue.setDensity(t.getAvgDensity());
            densityAndFlowValue.setFlow(t.getAvgFlow());
            return densityAndFlowValue;
        }).collect(Collectors.toList());
        countAvgValue(densityAndFlowValues, record);
    }




    public void countAvgValue(List<DensityAndFlowInfo> densityAndFlowValues, ReportFormsRecord record) {
        CumulativeData densityCumulativeData = new CumulativeData();
        CumulativeData flowCumulativeData = new CumulativeData();
        for (DensityAndFlowInfo densityAndFlowValue : densityAndFlowValues) {
            cumulateValue(densityCumulativeData,densityAndFlowValue.getDensity());
            cumulateValue(flowCumulativeData,densityAndFlowValue.getFlow());
        }
        record.setAvgDensity(densityCumulativeData.getAvgValue(true));
        record.setAvgFlow(flowCumulativeData.getAvgValue(true));
    }


    public void cumulateValue(CumulativeData data, Double currentValue){
        if (currentValue!= null && !INVALID_VALUE.equals(currentValue)){
            data.setSumValue(data.getSumValue()+currentValue);
            data.setCount(data.getCount()+1);
        }
    }



    /**
     * 在缓存中添加一条 以化验项目为thing，ASSAY_SAMPLE/PRODUCT_INSPECT_SAMPLE为metric，该化验项目最新一条化验数据的化验设备为value的data
     * 以化验项目为thing，ASSAY_DATA/PRODUCT_INSPECT_DATA为metric，该化验项目最新一条化验数据为value的data
     * 以化验设备为thing，ASSAY_DATA/PRODUCT_INSPECT_DATA为metric，化验数据为value的data
     *
     * @param record
     */
    public List<DataModel> getTargetSampleModel(ReportFormsRecord record) {
        List<DataModel> dataModels = new ArrayList<>();
        DataModel sampleThingModel = getSampleThingModel(record);
        DataModel targetDataModel = getTargetDataModel(record);
        DataModel sampleDataModel = getSampleDataModel(record);
        dataModels.add(sampleThingModel);
        dataModels.add(targetDataModel);
        dataModels.add(sampleDataModel);
        return dataModels;

    }


    private DataModel getSampleDataModel(ReportFormsRecord record) {
        DataModel sampleDataModel = new DataModel();
        sampleDataModel.setMetricDataType(MetricDataTypeEnum.METRIC_DATA_TYPE_OK.getName());
        sampleDataModel.setThingCode(record.getSample());
        if (record instanceof CoalAnalysisRecord) {
            sampleDataModel.setMetricCategoryCode(MetricModel.CATEGORY_ASSAY);
            sampleDataModel.setMetricCode(MetricCodes.ASSAY_DATA);
        } else if (record instanceof ProductionInspectRecord) {
            sampleDataModel.setMetricCategoryCode(MetricModel.CATEGORY_PRODUCTION_INSPECT);
            sampleDataModel.setMetricCode(MetricCodes.PRODUCT_INSPECT_DATA);
        }
        sampleDataModel.setDataTimeStamp(record.getTime());
        sampleDataModel.setValue(JSON.toJSONString(record));
        return sampleDataModel;
    }

    private DataModel getTargetDataModel(ReportFormsRecord record) {
        DataModel targetDataModel = new DataModel();
        targetDataModel.setMetricDataType(MetricDataTypeEnum.METRIC_DATA_TYPE_OK.getName());
        String system = getSystemStr(record);
        targetDataModel.setThingCode(system + record.getTarget());
        if (record instanceof CoalAnalysisRecord) {
            targetDataModel.setMetricCategoryCode(MetricModel.CATEGORY_ASSAY);
            targetDataModel.setMetricCode(MetricCodes.ASSAY_DATA);
        } else if (record instanceof ProductionInspectRecord) {
            targetDataModel.setMetricCategoryCode(MetricModel.CATEGORY_PRODUCTION_INSPECT);
            targetDataModel.setMetricCode(MetricCodes.PRODUCT_INSPECT_DATA);
        }
        targetDataModel.setDataTimeStamp(record.getTime());
        targetDataModel.setValue(JSON.toJSONString(record));
        return targetDataModel;
    }

    private DataModel getSampleThingModel(ReportFormsRecord record) {
        DataModel sampleThingModel = new DataModel();
        sampleThingModel.setMetricDataType(MetricDataTypeEnum.METRIC_DATA_TYPE_OK.getName());
        String system = getSystemStr(record);
        sampleThingModel.setThingCode(system + record.getTarget());
        if (record instanceof CoalAnalysisRecord) {
            sampleThingModel.setMetricCategoryCode(MetricModel.CATEGORY_ASSAY);
            sampleThingModel.setMetricCode(MetricCodes.ASSAY_SAMPLE);
        } else if (record instanceof ProductionInspectRecord) {
            sampleThingModel.setMetricCategoryCode(MetricModel.CATEGORY_PRODUCTION_INSPECT);
            sampleThingModel.setMetricCode(MetricCodes.PRODUCT_INSPECT_SAMPLE);
        }
        sampleThingModel.setDataTimeStamp(record.getTime());
        sampleThingModel.setValue(record.getSample());
        return sampleThingModel;
    }

    private String getSystemStr(ReportFormsRecord record) {
        String system;
        if (record.getSystem().equals(1)) {
            system = GlobalConstants.SYSTEM_ONE;
        } else {
            system = GlobalConstants.SYSTEM_TWO;
        }
        return system;
    }

    public void getParamModel(List<DataModel> dataModels, ReportFormsRecord record, String metricCode,
                              Double value) {
        DataModel dataModel = new DataModel();
        dataModel.setMetricDataType(MetricDataTypeEnum.METRIC_DATA_TYPE_OK.getName());
        dataModel.setThingCode(record.getSample());
        if (record instanceof CoalAnalysisRecord) {
            dataModel.setMetricCategoryCode(MetricModel.CATEGORY_ASSAY);
        } else if (record instanceof ProductionInspectRecord) {
            dataModel.setMetricCategoryCode(MetricModel.CATEGORY_PRODUCTION_INSPECT);
        }
        dataModel.setMetricCode(metricCode);
        dataModel.setDataTimeStamp(record.getTime());
        dataModel.setValue(String.valueOf(value));
        dataModels.add(dataModel);
    }


    public List<DensityAndFlowInfo> getDensityAndFlowValues(ReportFormsRecord record) {
        List<DensityAndFlowInfo> densityAndFlowValues = null;
        if (densityThingMap.containsKey(record.getSample())) {
            DensityAndFlowSourceInfo densityAndFlowInfo = densityThingMap.get(record.getSample()).get(record.getSystem());
            List<String> thingCodes = densityAndFlowInfo.getThingCodes();
            logger.debug("开始计算id为{}的煤质化验数据对应的分选密度/顶水流量，设备列表为：{}", record.getId(), thingCodes);
            densityAndFlowValues = new ArrayList<>();
            for (String runThingCode : thingCodes) {
                DensityAndFlowInfo densityAndFlowValue = getDensityAndFlowInfoInThing(record, densityAndFlowInfo, runThingCode);
                if (densityAndFlowValue != null) {
                    densityAndFlowValues.add(densityAndFlowValue);
                }
            }
        }
        return densityAndFlowValues;
    }

    private DensityAndFlowInfo getDensityAndFlowInfoInThing(ReportFormsRecord record, DensityAndFlowSourceInfo densityAndFlowInfo, String runThingCode) {
        Double densityData = null;
        Double flowData = null;
        if (!StringUtils.isBlank(densityAndFlowInfo.getDensityCode())) {
            densityData = historyDataService.findAvgValueDataInDuration(runThingCode, densityAndFlowInfo.getDensityCode(), DateUtils.addMinutes(record.getTime(), -10), record.getTime());
        }
        if (!StringUtils.isBlank(densityAndFlowInfo.getFlowCode())) {
            flowData = historyDataService.findAvgValueDataInDuration(runThingCode, densityAndFlowInfo.getFlowCode(), DateUtils.addMinutes(record.getTime(), -10), record.getTime());
        }
        if (densityData == null && flowData == null) {
            logger.debug("获取不到设备{}上的分选密度/顶水流量数据", runThingCode);
            return null;
        }
        DensityAndFlowInfo densityAndFlowValue = getRunDensityAndFlowValue(densityAndFlowInfo, densityData, flowData);
        if (densityAndFlowValue == null) {
            logger.debug("根据计算，设备{}未处于运行状态，不采集其分选密度/顶水流量数据", runThingCode);
            return null;
        }
        densityAndFlowValue.setThingCode(runThingCode);
        return densityAndFlowValue;
    }

    private DensityAndFlowInfo getRunDensityAndFlowValue(DensityAndFlowSourceInfo densityAndFlowSourceInfo, Double densityData, Double flowData) {
        DensityAndFlowInfo densityAndFlowValue = new DensityAndFlowInfo();
        if (!isRunState(densityAndFlowSourceInfo, densityData, flowData)) {
            return null;
        }
        densityAndFlowValue.setDensity(densityData);
        densityAndFlowValue.setFlow(flowData);
        return densityAndFlowValue;
    }

    private boolean isRunState(DensityAndFlowSourceInfo densityAndFlowSourceInfo, Double densityData, Double flowData) {
        if (densityAndFlowSourceInfo.getRunDensityThreshold() != null &&
                (densityData == null || densityData < densityAndFlowSourceInfo.getRunDensityThreshold())) {
            return false;
        }
        if (densityAndFlowSourceInfo.getRunFlowThreshold() != null &&
                (flowData == null || flowData < densityAndFlowSourceInfo.getRunFlowThreshold())) {
            return false;
        }
        return true;
    }

}
