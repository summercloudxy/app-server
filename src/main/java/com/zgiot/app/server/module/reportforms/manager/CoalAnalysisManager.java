package com.zgiot.app.server.module.reportforms.manager;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.reportforms.ReportFormsUtils;
import com.zgiot.app.server.module.coalanalysis.mapper.CoalAnalysisMapper;
import com.zgiot.app.server.module.reportforms.pojo.CumulativeData;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Transactional
public class CoalAnalysisManager implements ReportFormsManager {
    @Autowired
    private CoalAnalysisMapper coalAnalysisMapper;
    @Autowired
    private ReportFormsUtils reportFormsUtils;
    private final Logger logger = LoggerFactory.getLogger(CoalAnalysisMapper.class);

    @Override
    public Integer getExistRecordId(ReportFormsRecord record) {
        return coalAnalysisMapper.getExistRecordId((CoalAnalysisRecord) record);
    }

    @Override
    public void updateRecord(ReportFormsRecord record) {
        logger.debug("该化验数据已经存在，进行更新");
        coalAnalysisMapper.updateRecordWithOutDensityAndFlow((CoalAnalysisRecord) record);
    }


    @Override
    public void updateAvgRecord(ReportFormsRecord record) {
        logger.debug("该化验数据已经存在，进行更新");
        coalAnalysisMapper.updateRecordWithOutDensityAndFlow((CoalAnalysisRecord) record);
            coalAnalysisMapper.updateRecordDensityAndFlow((CoalAnalysisRecord) record);
    }

    @Override
    public void insertRecord(ReportFormsRecord record) {
        logger.debug("新增一条煤质化验数据");
        coalAnalysisMapper.insertRecord((CoalAnalysisRecord) record);
        List<DensityAndFlowInfo> densityAndFlowValues = reportFormsUtils.getDensityAndFlowValues(record);
        if (!CollectionUtils.isEmpty(densityAndFlowValues)) {
            densityAndFlowValues.forEach(t -> t.setAnalysisId(record.getId()));
            disposeCoalAnalysisDensityAndFlow(densityAndFlowValues, (CoalAnalysisRecord) record);
        }

    }


    @Override
    public void insertAvgRecord(ReportFormsRecord record) {
        logger.debug("新增一条煤质化验平均数据");
        coalAnalysisMapper.insertRecord((CoalAnalysisRecord) record);
    }

    private void disposeCoalAnalysisDensityAndFlow(List<DensityAndFlowInfo> densityAndFlowValues, CoalAnalysisRecord record) {
        reportFormsUtils.countAvgValue(densityAndFlowValues, record);
        coalAnalysisMapper.updateRecordDensityAndFlow(record);
        coalAnalysisMapper.insertDensityAndFlowValues(densityAndFlowValues);
    }

    @Override
    public List<DataModel> getDataForCache(ReportFormsRecord record) {
        List<DataModel> targetSampleModels = reportFormsUtils.getTargetSampleModel(record);
        List<DataModel> paramModels = parseToParamDataModel(record);
        targetSampleModels.addAll(paramModels);
        return targetSampleModels;

    }

    /**
     * 在缓存中添加 以化验设备为thing，分别以灰分、水分、硫份为metric，具体化验值为value的data
     *
     * @param record
     * @return
     */
    private List<DataModel> parseToParamDataModel(ReportFormsRecord record) {
        CoalAnalysisRecord coalAnalysisRecord = (CoalAnalysisRecord) record;
        boolean avgFlag = false;
        if (record.getTarget().contains(ReportFormsUtils.AVG_RECORD_KEYWORD)) {
            avgFlag = true;
        }
        List<DataModel> dataModels = new ArrayList<>();
        if (coalAnalysisRecord.getAad() != null) {
            getParamModel(avgFlag, dataModels, coalAnalysisRecord, MetricCodes.ASSAY_AAD_AVG, MetricCodes.ASSAY_AAD, coalAnalysisRecord.getAad());
        }
        if (coalAnalysisRecord.getMt() != null) {
            getParamModel(avgFlag, dataModels, coalAnalysisRecord, MetricCodes.ASSAY_MT_AVG, MetricCodes.ASSAY_MT, coalAnalysisRecord.getMt());
        }
        if (coalAnalysisRecord.getStad() != null) {
            getParamModel(avgFlag, dataModels, coalAnalysisRecord, MetricCodes.ASSAY_STAD_AVG, MetricCodes.ASSAY_STAD, coalAnalysisRecord.getStad());
        }
        if (coalAnalysisRecord.getQnetar() != null) {
            getParamModel(avgFlag, dataModels, coalAnalysisRecord, MetricCodes.ASSAY_QNETAR_AVG, MetricCodes.ASSAY_QNETAR, coalAnalysisRecord.getQnetar());
        }
        if (coalAnalysisRecord.getAvgDensity() != null) {
            getParamModel(avgFlag, dataModels, coalAnalysisRecord, MetricCodes.ASSAY_DENSITY_AVG, MetricCodes.ASSAY_DENSITY, coalAnalysisRecord.getAvgDensity());
        }
        if (coalAnalysisRecord.getAvgFlow() != null) {
            getParamModel(avgFlag, dataModels, coalAnalysisRecord, MetricCodes.ASSAY_FLOW_AVG, MetricCodes.ASSAY_FLOW, coalAnalysisRecord.getAvgFlow());
        }
        return dataModels;
    }

    private void getParamModel(boolean avgFlag, List<DataModel> dataModels, CoalAnalysisRecord coalAnalysisRecord, String avgParamMetricCode, String paramMetricCode, Double metricValue) {
        if (avgFlag) {
            reportFormsUtils.getParamModel(dataModels, coalAnalysisRecord, avgParamMetricCode, metricValue);
        } else {
            reportFormsUtils.getParamModel(dataModels, coalAnalysisRecord, paramMetricCode, metricValue);
        }
    }

    @Override
    public ReportFormsRecord parseDataModelToRecord(DataModel dataModel) {
        String value = dataModel.getValue();
        CoalAnalysisRecord record = JSON.parseObject(value, CoalAnalysisRecord.class);
        logger.debug("接收到煤质化验数据：化验项目{}，化验样本{}，化验时间{}，灰分{}，水份{}，硫份{}，发热量{}", record.getTarget(), record.getSample(), record.getTime(), record.getAad(), record.getMt(), record.getStad(), record.getQnetar());
        return record;
    }

    @Override
    public boolean hasAllRecordsBeforeAvgRecord(ReportFormsRecord record) {
        FilterCondition filterCondition = reportFormsUtils.getDutyFilterCondition(record);
        List<CoalAnalysisRecord> recordsOnDuty = getRecordsOnDuty(filterCondition);
        //确认平均记录之前的所有记录已经读取到，再取所有记录计算平均密度
        if (!hasAllRecordBeforeAvgRecord((CoalAnalysisRecord) record, recordsOnDuty)) {
            return false;
        }
        reportFormsUtils.getAvgDensityAndFlowOnDuty(record, getRecordsOnDuty(filterCondition));
        return true;
    }


    private boolean hasAllRecordBeforeAvgRecord(CoalAnalysisRecord record, List<CoalAnalysisRecord> recordsOnDuty) {
        CumulativeData aadCumulativeData = new CumulativeData();
        CumulativeData mtCumulativeData = new CumulativeData();
        CumulativeData stadCumulativeData = new CumulativeData();
        CumulativeData qnetarCumulativeData = new CumulativeData();
        for (CoalAnalysisRecord recordOnDuty : recordsOnDuty) {
            reportFormsUtils.cumulateValue(aadCumulativeData, recordOnDuty.getAad());
            reportFormsUtils.cumulateValue(mtCumulativeData, recordOnDuty.getMt());
            reportFormsUtils.cumulateValue(stadCumulativeData, recordOnDuty.getStad());
            reportFormsUtils.cumulateValue(qnetarCumulativeData, recordOnDuty.getQnetar());
        }
        Double avgAad = aadCumulativeData.getAvgValue(true, 2);
        Double avgMt = mtCumulativeData.getAvgValue(true, 1);
        Double avgStad = stadCumulativeData.getAvgValue(true, 2);
        return Objects.equals(avgAad, record.getAad()) &&
                Objects.equals(avgMt, record.getMt()) &&
                Objects.equals(avgStad, record.getStad()) ;

    }


    private List<CoalAnalysisRecord> getRecordsOnDuty(FilterCondition filterCondition) {
        return coalAnalysisMapper.getRecordsMatchCondition(filterCondition);
    }

}
