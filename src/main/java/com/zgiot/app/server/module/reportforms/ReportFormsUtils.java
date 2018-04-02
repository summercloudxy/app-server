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
import java.math.BigDecimal;
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
        record.setDensityAndFlowInfos(densityAndFlowValues);
        countDensityAndFlowAvgValue(record);
    }




    public void countDensityAndFlowAvgValue(ReportFormsRecord record) {
        List<DensityAndFlowInfo> densityAndFlowValues = record.getDensityAndFlowInfos();
        CumulativeData densityCumulativeData = new CumulativeData();
        CumulativeData flowCumulativeData = new CumulativeData();
        for (DensityAndFlowInfo densityAndFlowValue : densityAndFlowValues) {
            cumulateValue(densityCumulativeData,densityAndFlowValue.getDensity());
            cumulateValue(flowCumulativeData,densityAndFlowValue.getFlow());
        }
        record.setAvgDensity(densityCumulativeData.getAvgValue(true,2));
        record.setAvgFlow(flowCumulativeData.getAvgValue(true,2));
    }


    /**
     * 求和
     * @param data
     * @param currentValue
     */
    public void cumulateValue(CumulativeData data, Double currentValue){
        if (currentValue!= null && !INVALID_VALUE.equals(currentValue)){
            BigDecimal currentValueDecimal = BigDecimal.valueOf(currentValue);
            BigDecimal add = data.getSumValue().add(currentValueDecimal);
            data.setSumValue(add);
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
    public List<DataModel> getTargetSampleModel(ReportFormsRecord record,MetricCodeEnum code) {
        List<DataModel> dataModels = new ArrayList<>();
        DataModel sampleThingModel = getSampleThingModel(record,code);
        DataModel targetDataModel = getTargetDataModel(record,code);
        DataModel sampleDataModel = getSampleDataModel(record,code);
        dataModels.add(sampleThingModel);
        dataModels.add(targetDataModel);
        dataModels.add(sampleDataModel);
        return dataModels;

    }


    private DataModel getSampleDataModel(ReportFormsRecord record,MetricCodeEnum code) {
        DataModel sampleDataModel = new DataModel();
        sampleDataModel.setMetricDataType(MetricDataTypeEnum.METRIC_DATA_TYPE_OK.getName());
        sampleDataModel.setThingCode(record.getSample());
        sampleDataModel.setMetricCategoryCode(code.getMetricType());
        sampleDataModel.setMetricCode(code.getDataCode());
        sampleDataModel.setDataTimeStamp(record.getTime());
        sampleDataModel.setValue(JSON.toJSONString(record));
        return sampleDataModel;
    }

    private DataModel getTargetDataModel(ReportFormsRecord record,MetricCodeEnum code) {
        DataModel targetDataModel = new DataModel();
        targetDataModel.setMetricDataType(MetricDataTypeEnum.METRIC_DATA_TYPE_OK.getName());
        String system = getSystemStr(record);
        targetDataModel.setThingCode(system + record.getTarget());
        targetDataModel.setMetricCategoryCode(code.getMetricType());
        targetDataModel.setMetricCode(code.getDataCode());
        targetDataModel.setDataTimeStamp(record.getTime());
        targetDataModel.setValue(JSON.toJSONString(record));
        return targetDataModel;
    }

    private DataModel getSampleThingModel(ReportFormsRecord record,MetricCodeEnum code) {
        DataModel sampleThingModel = new DataModel();
        sampleThingModel.setMetricDataType(MetricDataTypeEnum.METRIC_DATA_TYPE_OK.getName());
        String system = getSystemStr(record);
        sampleThingModel.setThingCode(system + record.getTarget());
        sampleThingModel.setMetricCategoryCode(code.getMetricType());
        sampleThingModel.setMetricCode(code.getSampleCode());
        sampleThingModel.setDataTimeStamp(record.getTime());
        sampleThingModel.setValue(record.getSample());
        return sampleThingModel;
    }

    private String getSystemStr(ReportFormsRecord record) {
        String system= "";
        if (record.getSystem().equals(1)) {
            system = GlobalConstants.SYSTEM_ONE;
        } else if (record.getSystem().equals(2)){
            system = GlobalConstants.SYSTEM_TWO;
        }
        return system;
    }

    public DataModel getParamModel(ReportFormsRecord record, String metricCode,
                              Double value,MetricCodeEnum codeEnum) {
        DataModel dataModel = new DataModel();
        dataModel.setMetricDataType(MetricDataTypeEnum.METRIC_DATA_TYPE_OK.getName());
        dataModel.setThingCode(record.getSample());
        dataModel.setMetricCategoryCode(codeEnum.getMetricType());
        dataModel.setMetricCode(metricCode);
        dataModel.setDataTimeStamp(record.getTime());
        dataModel.setValue(String.valueOf(value));
        return dataModel;
    }


    public List<DensityAndFlowInfo> getDensityAndFlowValues(ReportFormsRecord record) {
        List<DensityAndFlowInfo> densityAndFlowValues = new ArrayList<>();
        if (densityThingMap.containsKey(record.getSample())) {
            DensityAndFlowSourceInfo densityAndFlowInfo = densityThingMap.get(record.getSample()).get(record.getSystem());
            List<String> thingCodes = densityAndFlowInfo.getThingCodes();
            logger.debug("开始计算id为{}的煤质化验数据对应的分选密度/顶水流量，设备列表为：{}", record.getId(), thingCodes);
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
