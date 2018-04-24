package com.zgiot.app.server.module.reportforms.manager;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.reportforms.MetricCodeEnum;
import com.zgiot.app.server.module.reportforms.ReportFormsUtils;
import com.zgiot.app.server.module.coalanalysis.mapper.CoalAnalysisMapper;
import com.zgiot.app.server.module.reportforms.pojo.CumulativeData;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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
    public void updateRecordWithOutDensityAndFlow(ReportFormsRecord record) {
        logger.debug("化验数据项目-{},样本-{},时间-{}，该化验数据已经存在，对应数据库id为{}，进行更新",record.getTarget(),record.getSample(),record.getTime(),record.getId());
        coalAnalysisMapper.updateRecordWithOutDensityAndFlow((CoalAnalysisRecord) record);
    }


    @Override
    public void updateRecordDensityAndFlow(ReportFormsRecord record) {
        logger.debug("化验数据项目-{},样本-{},时间-{}，该化验数据已经存在，对应数据库id为{}，更新其密度和顶水流量",record.getTarget(),record.getSample(),record.getTime(),record.getId());
        coalAnalysisMapper.updateRecordDensityAndFlow((CoalAnalysisRecord) record);
    }


    @Override
    public void insertRecord(ReportFormsRecord record) {
        logger.debug("化验数据项目-{},样本-{},时间-{}，该化验数据不存在,新增一条煤质化验数据",record.getTarget(),record.getSample(),record.getTime());
        coalAnalysisMapper.insertRecord((CoalAnalysisRecord) record);
    }

    /**
     * 记录每条记录对应的多个设备的密度和流量
     *
     * @param record
     */
    public void insertDetailDensityAndFlow(ReportFormsRecord record) {
        coalAnalysisMapper.updateRecordDensityAndFlow((CoalAnalysisRecord) record);
        coalAnalysisMapper.insertDetailDensityAndFlowValues(record.getDensityAndFlowInfos(), record.getId());
    }


    @Override
    public List<DataModel> getDataForCache(ReportFormsRecord record, boolean avgFlag) {
        List<DataModel> targetSampleModels = reportFormsUtils.getTargetSampleModel(record, MetricCodeEnum.COAL_ANALYSIS);
        List<DataModel> paramModels = parseToParamDataModel(record, avgFlag);
        targetSampleModels.addAll(paramModels);
        return targetSampleModels;

    }

    /**
     * 在缓存中添加 以化验设备为thing，分别以灰分、水分、硫份为metric，具体化验值为value的data
     *
     * @param record
     * @return
     */
    private List<DataModel> parseToParamDataModel(ReportFormsRecord record, boolean avgFlag) {
        CoalAnalysisRecord coalAnalysisRecord = (CoalAnalysisRecord) record;
        List<DataModel> dataModels = new ArrayList<>();
        if (coalAnalysisRecord.getAad() != null) {
            dataModels.add(getParamModel(avgFlag, coalAnalysisRecord, MetricCodes.ASSAY_AAD_AVG, MetricCodes.ASSAY_AAD, coalAnalysisRecord.getAad()));
        }
        if (coalAnalysisRecord.getMt() != null) {
            dataModels.add(getParamModel(avgFlag, coalAnalysisRecord, MetricCodes.ASSAY_MT_AVG, MetricCodes.ASSAY_MT, coalAnalysisRecord.getMt()));
        }
        if (coalAnalysisRecord.getStad() != null) {
            dataModels.add(getParamModel(avgFlag, coalAnalysisRecord, MetricCodes.ASSAY_STAD_AVG, MetricCodes.ASSAY_STAD, coalAnalysisRecord.getStad()));
        }
        if (coalAnalysisRecord.getQnetar() != null) {
            dataModels.add(getParamModel(avgFlag, coalAnalysisRecord, MetricCodes.ASSAY_QNETAR_AVG, MetricCodes.ASSAY_QNETAR, coalAnalysisRecord.getQnetar()));
        }
        if (coalAnalysisRecord.getAvgDensity() != null) {
            dataModels.add(getParamModel(avgFlag, coalAnalysisRecord, MetricCodes.ASSAY_DENSITY_AVG, MetricCodes.ASSAY_DENSITY, coalAnalysisRecord.getAvgDensity()));
        }
        if (coalAnalysisRecord.getAvgFlow() != null) {
            dataModels.add(getParamModel(avgFlag, coalAnalysisRecord, MetricCodes.ASSAY_FLOW_AVG, MetricCodes.ASSAY_FLOW, coalAnalysisRecord.getAvgFlow()));
        }
        return dataModels;
    }

    private DataModel getParamModel(boolean avgFlag, CoalAnalysisRecord coalAnalysisRecord, String avgParamMetricCode, String paramMetricCode, Double metricValue) {
        DataModel paramModel;
        if (avgFlag) {
            paramModel = reportFormsUtils.getParamModel(coalAnalysisRecord, avgParamMetricCode, metricValue, MetricCodeEnum.COAL_ANALYSIS);
        } else {
            paramModel = reportFormsUtils.getParamModel(coalAnalysisRecord, paramMetricCode, metricValue, MetricCodeEnum.COAL_ANALYSIS);
        }
        return paramModel;
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
            logger.debug("平均报表记录:项目-{},样本-{},时间-{}，班次内的记录并未全部读取到，先不存储该平均报表记录"
                    , record.getTarget(), record.getSample(), record.getTime());
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
        logger.debug("计算平均报表记录班次内的所有记录是否都已经读取到，平均报表数据为:项目-{},样本-{},时间-{},aad-{},mt-{},stad-{}，计算已存入数据库中的当前班次的平均值为aad-{}，mt-{}，stad-{}"
                , record.getTarget(), record.getSample(), record.getTime(), record.getAad(), record.getMt(), record.getStad(), avgAad, avgMt, avgStad);
        return Objects.equals(avgAad, record.getAad()) &&
                Objects.equals(avgMt, record.getMt()) &&
                Objects.equals(avgStad, record.getStad());

    }


    private List<CoalAnalysisRecord> getRecordsOnDuty(FilterCondition filterCondition) {
        return coalAnalysisMapper.getRecordsMatchCondition(filterCondition);
    }

    @Override
    public ReportFormsRecord getExistRecord(ReportFormsRecord record) {
        return coalAnalysisMapper.getExistRecord((CoalAnalysisRecord) record);
    }

    @Override
    public ReportFormsRecord getRecentRecord(ReportFormsRecord record) {
        return coalAnalysisMapper.getRecentRecord((CoalAnalysisRecord) record);
    }

    @Override
    public ReportFormsRecord getLastRecordOnDuty(ReportFormsRecord record, Date dutyEndTime) {
        return coalAnalysisMapper.getLastRecordOnDuty((CoalAnalysisRecord) record, dutyEndTime);
    }
}
