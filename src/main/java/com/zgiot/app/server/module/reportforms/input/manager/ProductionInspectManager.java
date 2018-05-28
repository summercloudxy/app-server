package com.zgiot.app.server.module.reportforms.input.manager;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.reportforms.input.MetricCodeEnum;
import com.zgiot.app.server.module.reportforms.input.ReportFormsUtils;
import com.zgiot.app.server.module.produtioninspect.mapper.ProductionInspectMapper;
import com.zgiot.app.server.module.reportforms.input.pojo.CumulativeData;
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
    public void updateRecordWithOutDensityAndFlow(ReportFormsRecord record) {
        logger.debug("生产检查数据项目-{},样本-{},时间-{}，该生产检查数据已经存在，对应数据库id为{}，进行更新", record.getTarget(), record.getSample(), record.getTime(), record.getId());
        productionInspectMapper.updateRecordWithOutDensityAndFlow((ProductionInspectRecord) record);
    }

    public void updateRecordDensityAndFlow(ReportFormsRecord record) {
        logger.debug("生产检查数据项目-{},样本-{},时间-{}，该生产检查数据已经存在，对应数据库id为{}，更新其分选密度和顶水流量", record.getTarget(), record.getSample(), record.getTime(), record.getId());
        productionInspectMapper.updateRecordDensityAndFlow((ProductionInspectRecord) record);
    }


    @Override
    public void insertRecord(ReportFormsRecord record) {
        logger.debug("生产检查数据项目-{},样本-{},时间-{}，新增一条生产检查数据", record.getTarget(), record.getSample(), record.getTime());
        productionInspectMapper.insertRecord((ProductionInspectRecord) record);
    }

    public void insertDetailDensityAndFlow(ReportFormsRecord record) {
        productionInspectMapper.updateRecordDensityAndFlow((ProductionInspectRecord) record);
        productionInspectMapper.insertDetailDensityAndFlowValues(record.getDensityAndFlowInfos(), record.getId());
    }


    private List<DataModel> parseToParamDataModel(ReportFormsRecord record, boolean avgFlag) {
        ProductionInspectRecord productionInspectRecord = (ProductionInspectRecord) record;
        List<DataModel> dataModels = new ArrayList<>();
        if (productionInspectRecord.getNegative1Point8() != null) {
            dataModels.add(getParamModel(productionInspectRecord, avgFlag, MetricCodes.PRODUCT_INSPECT_NEGATIVE1_POINT8_AVG, productionInspectRecord.getNegative1Point8(), MetricCodes.PRODUCT_INSPECT_NEGATIVE1_POINT8));
        }
        if (productionInspectRecord.getNegative1Point45() != null) {
            dataModels.add(getParamModel(productionInspectRecord, avgFlag, MetricCodes.PRODUCT_INSPECT_NEGATIVE1_POINT45_AVG, productionInspectRecord.getNegative1Point45(), MetricCodes.PRODUCT_INSPECT_NEGATIVE1_POINT45));
        }
        if (productionInspectRecord.getPositive1Point8() != null) {
            dataModels.add(getParamModel(productionInspectRecord, avgFlag, MetricCodes.PRODUCT_INSPECT_POSITIVE1_POINT8_AVG, productionInspectRecord.getPositive1Point8(), MetricCodes.PRODUCT_INSPECT_POSITIVE1_POINT8));
        }
        if (productionInspectRecord.getPositive1Point45() != null) {
            dataModels.add(getParamModel(productionInspectRecord, avgFlag, MetricCodes.PRODUCT_INSPECT_POSITIVE1_POINT45_AVG, productionInspectRecord.getPositive1Point45(), MetricCodes.PRODUCT_INSPECT_POSITIVE1_POINT45));
        }
        if (productionInspectRecord.getOnePoint45To1Point8() != null) {
            dataModels.add(getParamModel(productionInspectRecord, avgFlag, MetricCodes.PRODUCT_INSPECT_ONE_POINT45_TO1_POINT8_AVG, productionInspectRecord.getOnePoint45To1Point8(), MetricCodes.PRODUCT_INSPECT_ONE_POINT45_TO1_POINT8));
        }
        if (productionInspectRecord.getNegative50mm() != null) {
            dataModels.add(getParamModel(productionInspectRecord, avgFlag, MetricCodes.PRODUCT_INSPECT_NEGATIVE50MM_AVG, productionInspectRecord.getNegative50mm(), MetricCodes.PRODUCT_INSPECT_NEGATIVE50MM));
        }
        if (productionInspectRecord.getPositive50mm() != null) {
            dataModels.add(getParamModel(productionInspectRecord, avgFlag, MetricCodes.PRODUCT_INSPECT_POSITIVE50MM_AVG, productionInspectRecord.getPositive50mm(), MetricCodes.PRODUCT_INSPECT_POSITIVE50MM));
        }
        if (productionInspectRecord.getAvgDensity() != null) {
            dataModels.add(getParamModel(productionInspectRecord, avgFlag, MetricCodes.PRODUCT_INSPECT_DENSITY_AVG, productionInspectRecord.getAvgDensity(), MetricCodes.PRODUCT_INSPECT_DENSITY));
        }
        return dataModels;
    }

    private DataModel getParamModel(ProductionInspectRecord productionInspectRecord, boolean avgFlag, String avgParamMetricCode, Double metricValue, String paramMetricCode) {
        DataModel dataModel;
        if (avgFlag) {
            dataModel = reportFormsUtils.getParamModel(productionInspectRecord, avgParamMetricCode, metricValue, MetricCodeEnum.PRODUCTION_INSPECT);
        } else {
            dataModel = reportFormsUtils.getParamModel(productionInspectRecord, paramMetricCode, metricValue, MetricCodeEnum.PRODUCTION_INSPECT);
        }
        return dataModel;
    }

    @Override
    public List<DataModel> getDataForCache(ReportFormsRecord record, boolean avgFlag) {
        List<DataModel> targetSampleModels = reportFormsUtils.getTargetSampleModel(record, MetricCodeEnum.PRODUCTION_INSPECT);
        List<DataModel> paramModels = parseToParamDataModel(record, avgFlag);
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
    public boolean hasAllRecordsBeforeAvgRecord(ReportFormsRecord record) {
        FilterCondition filterCondition = reportFormsUtils.getDutyFilterCondition(record);
        List<ProductionInspectRecord> recordsOnDuty = getRecordsOnDuty(filterCondition);
        if (!hasAllRecordBeforeAvgRecord((ProductionInspectRecord) record, recordsOnDuty)) {
            logger.debug("平均报表记录:项目-{},样本-{},时间-{}，班次内的记录并未全部读取到，先不存储该平均报表记录"
                    , record.getTarget(), record.getSample(), record.getTime());
            return false;
        }
        reportFormsUtils.getAvgDensityAndFlowOnDuty(record, getRecordsOnDuty(filterCondition));
        return true;
    }


    private boolean hasAllRecordBeforeAvgRecord(ProductionInspectRecord record, List<ProductionInspectRecord> recordsOnDuty) {
        CumulativeData negative50mmData = new CumulativeData();
        CumulativeData positive1Point45Data = new CumulativeData();
        CumulativeData negative1Point45Data = new CumulativeData();
        CumulativeData positive1Point8Data = new CumulativeData();
        CumulativeData negative1Point8Data = new CumulativeData();
        CumulativeData onePoint45To1Point8Data = new CumulativeData();
        CumulativeData positive50mm = new CumulativeData();
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
        logger.debug("计算平均报表记录班次内的所有记录是否都已经读取到，平均报表数据为:项目-{},样本-{},时间-{},`+1.45`-{},`-1.45`-{},`+1.8`-{}，`-1.8`-{},`1.45-1.8`-{},`+50`-{},`-50`-{}，计算已存入数据库中的当前班次的平均值为,`+1.45`-{},`-1.45`-{},`+1.8`-{}，`-1.8`-{},`1.45-1.8`-{},`+50`-{},`-50`-{}"
                , record.getTarget(), record.getSample(), record.getTime(), record.getPositive1Point45(), record.getNegative1Point45(), record.getPositive1Point8(), record.getNegative1Point8(), record.getOnePoint45To1Point8(), record.getPositive50mm(), record.getNegative50mm(), avgPositive1Point45, avgNegative1Point45, avgPositive1Point8, avgNegative1Point8, avgOnePoint45To1Point8, avgPositive50mm, avgNegative50mm);

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

    @Override
    public ReportFormsRecord getExistRecord(ReportFormsRecord record) {
        return productionInspectMapper.getExistRecord((ProductionInspectRecord) record);
    }

    @Override
    public ReportFormsRecord getRecentRecord(ReportFormsRecord record) {
        return productionInspectMapper.getRecentRecord((ProductionInspectRecord) record);
    }

    @Override
    public ReportFormsRecord getLastRecordOnDuty(ReportFormsRecord record, Date dutyEndTime) {
        return productionInspectMapper.getLastRecordOnDuty((ProductionInspectRecord) record, dutyEndTime);
    }
}
