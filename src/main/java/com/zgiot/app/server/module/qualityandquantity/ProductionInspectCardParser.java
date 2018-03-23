package com.zgiot.app.server.module.qualityandquantity;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.qualityandquantity.pojo.ProductionInspectData;
import com.zgiot.app.server.module.qualityandquantity.pojo.ReportFormsParam;
import com.zgiot.app.server.module.qualityandquantity.service.QualityAndQuantityDataManager;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.ProductionInspectRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductionInspectCardParser implements CardParser {
    @Autowired
    private DataService dataService;
    @Autowired
    private QualityAndQuantityDataManager dataManager;
    private String parserName = "ProductionInspectCardParser";

    @Override
    public String getParserName() {
        return parserName;
    }

    @Override
    public ProductionInspectData parse(String paramValueJson) {
        ProductionInspectData productionInspectData = new ProductionInspectData();
        ReportFormsParam thingMetricParam = JSON.parseObject(paramValueJson, ReportFormsParam.class);

        String metricCode = thingMetricParam.getMetricCode();
        String target = thingMetricParam.getThingCode();
        Optional<DataModelWrapper> data = dataService.getData(target, metricCode);
        if (data.isPresent()) {
            String value = data.get().getValue();
            ProductionInspectRecord productionInspectRecord = JSON.parseObject(value, ProductionInspectRecord.class);
            if (productionInspectRecord != null) {
                productionInspectData = dataManager.transProductionInspectRecordToData(productionInspectRecord);
                productionInspectData.setType(thingMetricParam.getType());
                productionInspectData.setShowBit(thingMetricParam.getShowBit());
            }
        }
        return productionInspectData;
    }


    @Override
    public ProductionInspectData parseTest(String paramValueJson) {
        return new ProductionInspectData();
    }
}
