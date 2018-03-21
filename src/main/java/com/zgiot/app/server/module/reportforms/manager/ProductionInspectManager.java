package com.zgiot.app.server.module.reportforms.manager;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.reportforms.ReportFormsUtils;
import com.zgiot.app.server.module.produtioninspect.mapper.ProductionInspectMapper;
import com.zgiot.app.server.module.reportforms.pojo.CumulativeData;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Component
public class ProductionInspectManager implements ReportFormsManager {
    @Autowired
    private ProductionInspectMapper productionInspectMapper;
    @Autowired
    private ReportFormsUtils reportFormsUtils;
    private final Logger logger = LoggerFactory.getLogger(ProductionInspectManager.class);

    @Override
    public Integer getExistRecordId(ReportFormsRecord record) {
        return productionInspectMapper.getExistRecordId((ProductionInspectRecord) record);
    }

    @Override
    public void updateRecord(ReportFormsRecord record) {
        logger.debug("该生产检查数据已经存在，进行更新");
        productionInspectMapper.updateRecord((ProductionInspectRecord) record);
        if (record.getTarget().contains(ReportFormsUtils.AVG_RECORD_KEYWORD)) {
            productionInspectMapper.updateRecordDensityAndFlow((ProductionInspectRecord) record);
        }
    }

    @Override
    public void insertRecord(ReportFormsRecord record) {
        logger.debug("新增一条生产检查数据");
        productionInspectMapper.insertRecord((ProductionInspectRecord) record);
        if (!record.getTarget().contains(ReportFormsUtils.AVG_RECORD_KEYWORD)) {
            List<DensityAndFlowInfo> densityAndFlowValues = reportFormsUtils.getDensityAndFlowValues(record);
            if (!CollectionUtils.isEmpty(densityAndFlowValues)) {
                densityAndFlowValues.forEach(t -> t.setInspectId(record.getId()));
                disposeProductInspectDensityAndFlow(densityAndFlowValues, (ProductionInspectRecord) record);
            }
        }
    }

    private void disposeProductInspectDensityAndFlow(List<DensityAndFlowInfo> densityAndFlowValues, ProductionInspectRecord record) {
        reportFormsUtils.countAvgValue(densityAndFlowValues, record);
        productionInspectMapper.updateRecordDensityAndFlow(record);
        productionInspectMapper.insertDensityAndFlowValues(densityAndFlowValues);
    }


    private List<DataModel> parseToParamDataModel(ReportFormsRecord record) {
        ProductionInspectRecord productionInspectRecord = (ProductionInspectRecord) record;
        boolean avgFlag = false;
        if (record.getTarget().contains(ReportFormsUtils.AVG_RECORD_KEYWORD)) {
            avgFlag = true;
        }
        List<DataModel> dataModels = new ArrayList<>();
        if (productionInspectRecord.getNegative1Point8() != null) {
            getParamModel(productionInspectRecord, avgFlag, dataModels, MetricCodes.PRODUCT_INSPECT_NEGATIVE1_POINT8_AVG, productionInspectRecord.getNegative1Point8(), MetricCodes.PRODUCT_INSPECT_NEGATIVE1_POINT8);
        }
        if (productionInspectRecord.getNegative1Point45() != null) {
            getParamModel(productionInspectRecord, avgFlag, dataModels, MetricCodes.PRODUCT_INSPECT_NEGATIVE1_POINT45_AVG, productionInspectRecord.getNegative1Point45(), MetricCodes.PRODUCT_INSPECT_NEGATIVE1_POINT45);
        }
        if (productionInspectRecord.getPositive1Point8() != null) {
            getParamModel(productionInspectRecord, avgFlag, dataModels, MetricCodes.PRODUCT_INSPECT_POSITIVE1_POINT8_AVG, productionInspectRecord.getPositive1Point8(), MetricCodes.PRODUCT_INSPECT_POSITIVE1_POINT8);
        }
        if (productionInspectRecord.getPositive1Point45() != null) {
            getParamModel(productionInspectRecord, avgFlag, dataModels, MetricCodes.PRODUCT_INSPECT_POSITIVE1_POINT45_AVG, productionInspectRecord.getPositive1Point45(), MetricCodes.PRODUCT_INSPECT_POSITIVE1_POINT45);
        }
        if (productionInspectRecord.getOnePoint45To1Point8() != null) {
            getParamModel(productionInspectRecord, avgFlag, dataModels, MetricCodes.PRODUCT_INSPECT_ONE_POINT45_TO1_POINT8_AVG, productionInspectRecord.getOnePoint45To1Point8(), MetricCodes.PRODUCT_INSPECT_ONE_POINT45_TO1_POINT8);
        }
        if (productionInspectRecord.getNegative50mm() != null) {
            getParamModel(productionInspectRecord, avgFlag, dataModels, MetricCodes.PRODUCT_INSPECT_NEGATIVE50MM_AVG, productionInspectRecord.getNegative50mm(), MetricCodes.PRODUCT_INSPECT_NEGATIVE50MM);
        }
        if (productionInspectRecord.getPositive50mm() != null) {
            getParamModel(productionInspectRecord, avgFlag, dataModels, MetricCodes.PRODUCT_INSPECT_POSITIVE50MM_AVG, productionInspectRecord.getPositive50mm(), MetricCodes.PRODUCT_INSPECT_POSITIVE50MM);
        }
        if (productionInspectRecord.getAvgDensity() != null) {
            getParamModel(productionInspectRecord, avgFlag, dataModels, MetricCodes.PRODUCT_INSPECT_DENSITY_AVG, productionInspectRecord.getAvgDensity(), MetricCodes.PRODUCT_INSPECT_DENSITY);
        }
        return dataModels;
    }

    private void getParamModel(ProductionInspectRecord productionInspectRecord, boolean avgFlag, List<DataModel> dataModels, String avgParamMetricCode, Double metricValue, String paramMetricCode) {
        if (avgFlag) {
            reportFormsUtils.getParamModel(dataModels, productionInspectRecord, avgParamMetricCode, metricValue);
        } else {
            reportFormsUtils.getParamModel(dataModels, productionInspectRecord, paramMetricCode, metricValue);
        }
    }

    @Override
    public List<DataModel> getDataForCache(ReportFormsRecord record) {
        List<DataModel> targetSampleModels = reportFormsUtils.getTargetSampleModel(record);
        List<DataModel> paramModels = parseToParamDataModel(record);
        targetSampleModels.addAll(paramModels);
        return targetSampleModels;
    }

    @Override
    public ReportFormsRecord parseDataModelToRecord(DataModel dataModel) {
        String value = dataModel.getValue();
        ProductionInspectRecord record = JSON.parseObject(value, ProductionInspectRecord.class);
        logger.debug("接收到生产检查数据：检查项目{}，检查样本{}，检查时间{}，-1.45：{}，+1.45：{}，-1.8：{}，+1.8{}，1.45-1.8：{}，+50mm:{},-50mm:{}"
                , record.getTarget(), record.getSample(), record.getTime(), record.getNegative1Point45(), record.getPositive1Point45(), record.getNegative1Point8(), record.getPositive1Point8(), record.getOnePoint45To1Point8(), record.getPositive50mm(), record.getNegative50mm());
        return record;
    }

    @Override
    public void disposeAvgRecord(ReportFormsRecord record) {
        FilterCondition filterCondition = reportFormsUtils.getDutyFilterCondition(record);
        while (!hasAllRecordBeforeAvgRecord((ProductionInspectRecord) record, filterCondition)) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                throw new SysException(e.getMessage(), SysException.EC_UNKNOWN);
            }
        }
        reportFormsUtils.getAvgDensityAndFlowOnDuty(record, getRecordsOnDuty(filterCondition));
    }


    private boolean hasAllRecordBeforeAvgRecord(ProductionInspectRecord record, FilterCondition filterCondition) {
        CumulativeData negative50mmData = new CumulativeData();
        CumulativeData positive1Point45Data = new CumulativeData();
        CumulativeData negative1Point45Data = new CumulativeData();
        CumulativeData positive1Point8Data = new CumulativeData();
        CumulativeData negative1Point8Data = new CumulativeData();
        CumulativeData onePoint45To1Point8Data = new CumulativeData();
        CumulativeData positive50mm = new CumulativeData();
        List<ProductionInspectRecord> recordsOnDuty = getRecordsOnDuty(filterCondition);
        for (ProductionInspectRecord recordOnDuty : recordsOnDuty) {
            reportFormsUtils.cumulateValue(positive1Point45Data, recordOnDuty.getPositive1Point45());
            reportFormsUtils.cumulateValue(negative1Point45Data, recordOnDuty.getNegative1Point45());
            reportFormsUtils.cumulateValue(positive1Point8Data, recordOnDuty.getPositive1Point45());
            reportFormsUtils.cumulateValue(negative1Point8Data, recordOnDuty.getNegative1Point8());
            reportFormsUtils.cumulateValue(onePoint45To1Point8Data, recordOnDuty.getOnePoint45To1Point8());
            reportFormsUtils.cumulateValue(positive50mm, recordOnDuty.getPositive50mm());
            reportFormsUtils.cumulateValue(negative50mmData, recordOnDuty.getNegative50mm());
        }
        Double avgPositive1Point45 = positive1Point45Data.getAvgValue(true, 2);
        Double avgNegative1Point45 = negative1Point45Data.getAvgValue(true, 2);
        Double avgPositive1Point8 = positive1Point8Data.getAvgValue(true, 2);
        Double avgNegative1Point8 = negative1Point8Data.getAvgValue(true, 2);
        Double avgOnePoint45To1Point8 = onePoint45To1Point8Data.getAvgValue(true, 2);
        Double avgPositive50mm = positive50mm.getAvgValue(true, 2);
        Double avgNegative50mm = negative50mmData.getAvgValue(true, 2);

        return Objects.equals(avgPositive1Point45, record.getPositive1Point45()) &&
                Objects.equals(avgNegative1Point45, record.getNegative1Point45()) &&
                Objects.equals(avgPositive1Point8, record.getPositive1Point45()) &&
                Objects.equals(avgNegative1Point8, record.getNegative1Point8()) &&
                Objects.equals(avgOnePoint45To1Point8, record.getOnePoint45To1Point8()) &&
                Objects.equals(avgPositive50mm, record.getPositive50mm()) &&
                Objects.equals(avgNegative50mm, record.getNegative50mm());
    }

    private List<ProductionInspectRecord> getRecordsOnDuty(FilterCondition filterCondition) {
        return productionInspectMapper.getRecordsMatchCondition(filterCondition);
    }
}
