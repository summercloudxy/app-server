package com.zgiot.app.server.module.qualityandquantity.service;

import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.AlertRule;
import com.zgiot.app.server.module.qualityandquantity.pojo.CoalAnalysisData;
import com.zgiot.app.server.module.qualityandquantity.pojo.MetricDataValue;
import com.zgiot.app.server.module.qualityandquantity.pojo.ProductionInspectData;
import com.zgiot.app.server.module.reportforms.ReportFormsUtils;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.MetricService;
import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.MetricModel;
import com.zgiot.common.pojo.ProductionInspectRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QualityAndQuantityDataManager {
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private ThingService thingService;
    @Autowired
    private DataService dataService;
    @Autowired
    private MetricService metricService;
    private Map<String,AlertRule> paramThresholdMap = new HashMap<>();

    private CoalAnalysisData getCoalAnalysisDataFromRecord(CoalAnalysisRecord coalAnalysisRecord) {
        boolean isAvg = false;
        if (coalAnalysisRecord.getTarget().contains(ReportFormsUtils.AVG_RECORD_KEYWORD)){
            isAvg = true;
        }
        CoalAnalysisData coalAnalysisData = new CoalAnalysisData(coalAnalysisRecord);
        String thingCode = coalAnalysisRecord.getSample();
        coalAnalysisData.setThingName(thingService.getThing(thingCode).getThingName());
        setCoalParamData(coalAnalysisData, coalAnalysisRecord,isAvg);
        return coalAnalysisData;
    }


    public List<CoalAnalysisData> transCoalAnalysisRecordToData(List<CoalAnalysisRecord> records) {
        List<CoalAnalysisData> coalAnalysisDataList = new ArrayList<>(records.size());
        for (CoalAnalysisRecord record : records) {
            CoalAnalysisData data = getCoalAnalysisDataFromRecord(record);
            coalAnalysisDataList.add(data);
        }
        return coalAnalysisDataList;
    }

    public CoalAnalysisData transCoalAnalysisRecordToData(CoalAnalysisRecord record) {
        CoalAnalysisData data = getCoalAnalysisDataFromRecord(record);
        paramThresholdMap.clear();
        return data;
    }

    private void setCoalParamData(CoalAnalysisData data, CoalAnalysisRecord record, boolean isAvg) {
        String thingCode = record.getSample();
        if (record.getMt() != null) {
            MetricDataValue mt = addRuleForValue(record.getMt(), thingCode, MetricCodes.ASSAY_MT, MetricCodes.ASSAY_MT_AVG,isAvg);
            data.setMt(mt);
        }
        if (record.getAad() != null) {
            MetricDataValue aad = addRuleForValue(record.getAad(), thingCode, MetricCodes.ASSAY_AAD, MetricCodes.ASSAY_AAD_AVG,isAvg);
            data.setAad(aad);
        }
        if (record.getStad() != null) {
            MetricDataValue stad = addRuleForValue(record.getStad(), thingCode, MetricCodes.ASSAY_STAD, MetricCodes.ASSAY_STAD_AVG,isAvg);
            data.setStad(stad);
        }
        if (record.getQnetar() != null) {
            MetricDataValue qnetar = addRuleForValue(record.getQnetar(), thingCode, MetricCodes.ASSAY_QNETAR, MetricCodes.ASSAY_QNETAR_AVG,isAvg);
            data.setQnetar(qnetar);
        }
        if (record.getAvgFlow() != null) {
            MetricDataValue avgFlow = addRuleForValue(record.getAvgFlow(), thingCode, MetricCodes.ASSAY_FLOW, MetricCodes.ASSAY_FLOW,isAvg);
            data.setAvgFlow(avgFlow);
        }
        if (record.getAvgDensity() != null) {
            MetricDataValue avgDensity = addRuleForValue(record.getAvgDensity(), thingCode, MetricCodes.ASSAY_DENSITY,MetricCodes.ASSAY_FLOW,isAvg);
            data.setAvgDensity(avgDensity);
        }

    }

    private ProductionInspectData getProductionInspectDateFromRecord(ProductionInspectRecord productionInspectRecord) {
        boolean isAvg = false;
        if (productionInspectRecord.getTarget().contains(ReportFormsUtils.AVG_RECORD_KEYWORD)){
            isAvg = true;
        }
        ProductionInspectData productionInspectData = new ProductionInspectData(productionInspectRecord);
        String thingCode = productionInspectRecord.getSample();
        productionInspectData.setThingName(thingService.getThing(thingCode).getThingName());
        setProductionParamData(productionInspectData, productionInspectRecord,isAvg);
        return productionInspectData;
    }


    public List<ProductionInspectData> transProductionInspectRecordToData(List<ProductionInspectRecord> productionInspectRecords){
        List<ProductionInspectData> productionInspectDataList = new ArrayList<>(productionInspectRecords.size());
        for (ProductionInspectRecord record:productionInspectRecords){
            ProductionInspectData data = getProductionInspectDateFromRecord(record);
            productionInspectDataList.add(data);
        }
        paramThresholdMap.clear();
        return productionInspectDataList;
    }

    public ProductionInspectData transProductionInspectRecordToData(ProductionInspectRecord record){
         ProductionInspectData data = getProductionInspectDateFromRecord(record);
        paramThresholdMap.clear();
        return data;
    }



    private void setProductionParamData(ProductionInspectData data, ProductionInspectRecord record,boolean isAvg) {
        String thingCode = record.getSample();
        if (record.getNegative1Point8() != null) {
            MetricDataValue negative1Point8 = addRuleForValue(record.getNegative1Point8(), thingCode, MetricCodes.PRODUCT_INSPECT_NEGATIVE1_POINT8,MetricCodes.PRODUCT_INSPECT_NEGATIVE1_POINT8_AVG,isAvg );
            data.setNegative1Point8(negative1Point8);
        }
        if (record.getPositive1Point8() != null) {
            MetricDataValue positive1Point8 = addRuleForValue(record.getPositive1Point8(), thingCode, MetricCodes.PRODUCT_INSPECT_POSITIVE1_POINT8,MetricCodes.PRODUCT_INSPECT_POSITIVE1_POINT8_AVG,isAvg);
            data.setPositive1Point8(positive1Point8);
        }
        if (record.getNegative1Point45() != null) {
            MetricDataValue negative1Point45 = addRuleForValue(record.getNegative1Point45(), thingCode, MetricCodes.PRODUCT_INSPECT_NEGATIVE1_POINT45, MetricCodes.PRODUCT_INSPECT_NEGATIVE1_POINT45_AVG,isAvg);
            data.setNegative1Point45(negative1Point45);
        }
        if (record.getPositive1Point45() != null) {
            MetricDataValue positive1Point45 = addRuleForValue(record.getPositive1Point45(), thingCode, MetricCodes.PRODUCT_INSPECT_POSITIVE1_POINT45, MetricCodes.PRODUCT_INSPECT_POSITIVE1_POINT45_AVG,isAvg);
            data.setPositive1Point45(positive1Point45);
        }
        if (record.getOnePoint45To1Point8() != null) {
            MetricDataValue onePoint45To1Point8 = addRuleForValue(record.getOnePoint45To1Point8(), thingCode, MetricCodes.PRODUCT_INSPECT_ONE_POINT45_TO1_POINT8, MetricCodes.PRODUCT_INSPECT_ONE_POINT45_TO1_POINT8_AVG,isAvg);
            data.setOnePoint45To1Point8(onePoint45To1Point8);
        }
        if (record.getNegative50mm() != null) {
            MetricDataValue negative50mm = addRuleForValue(record.getNegative50mm(), thingCode, MetricCodes.PRODUCT_INSPECT_NEGATIVE50MM, MetricCodes.PRODUCT_INSPECT_NEGATIVE50MM_AVG,isAvg);
            data.setNegative50mm(negative50mm);
        }
        if (record.getPositive50mm() != null) {
            MetricDataValue positive50mm = addRuleForValue(record.getPositive50mm(), thingCode, MetricCodes.PRODUCT_INSPECT_POSITIVE50MM, MetricCodes.PRODUCT_INSPECT_POSITIVE50MM_AVG,isAvg);
            data.setPositive50mm(positive50mm);
        }
        if (record.getAvgDensity() != null) {
            MetricDataValue avgDensity = addRuleForValue(record.getAvgDensity(), thingCode, MetricCodes.PRODUCT_INSPECT_DENSITY, MetricCodes.PRODUCT_INSPECT_DENSITY_AVG,isAvg);
            data.setAvgDensity(avgDensity);
        }

    }

    private MetricDataValue addRuleForValue(Double value, String thingCode, String metricCode, String avgMetricCode, boolean isAvg) {
        String valueStr = Double.toString(value);
        MetricDataValue dataValue = new MetricDataValue();
        dataValue.setValue(valueStr);
        String paramCode;
        if (isAvg) {
            paramCode = avgMetricCode;
        } else {
            paramCode = metricCode;
        }
        dataValue.setAlertRules(alertManager.getParamRules(thingCode, paramCode));
        String paramThresholdMapKey =thingCode+"-"+paramCode;
        AlertRule paramThreshold;
        if (paramThresholdMap.containsKey(paramThresholdMapKey)){
            paramThreshold = paramThresholdMap.get(paramThresholdMapKey);
        }else {
            paramThreshold = alertManager.getParamThreshold(thingCode, paramCode);
        }
        dataValue.setParamRange(paramThreshold);
        MetricModel metric = metricService.getMetric(paramCode);
        if (metric != null) {
            dataValue.setValueUnit(metric.getValueUnit());
            dataValue.setMetricName(metric.getMetricName());
        }
        return dataValue;
    }

    public MetricDataValue getValueWithRule(String thingCode, String metricCode) {
        MetricDataValue metricDataValue = new MetricDataValue();
        Optional<DataModelWrapper> data = dataService.getData(thingCode, metricCode);
        if (data.isPresent()) {
            String value = data.get().getValue();
            metricDataValue.setValue(value);
        }
        metricDataValue.setAlertRules(alertManager.getParamRules(thingCode, metricCode));
        metricDataValue.setParamRange(alertManager.getParamThreshold(thingCode, metricCode));
        MetricModel metric = metricService.getMetric(metricCode);
        if (metric != null) {
            metricDataValue.setValueUnit(metric.getValueUnit());
            metricDataValue.setMetricName(metric.getMetricName());
        }
        return metricDataValue;
    }

}
