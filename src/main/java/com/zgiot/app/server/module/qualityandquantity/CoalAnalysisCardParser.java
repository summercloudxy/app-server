package com.zgiot.app.server.module.qualityandquantity;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.qualityandquantity.pojo.CoalAnalysisData;
import com.zgiot.app.server.module.qualityandquantity.pojo.ReportFormsParam;
import com.zgiot.app.server.module.qualityandquantity.service.QualityAndQuantityDataManager;
import com.zgiot.app.server.module.reportforms.ReportFormsUtils;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import com.zgiot.common.pojo.DataModelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CoalAnalysisCardParser implements CardParser {
    @Autowired
    private DataService dataService;
    @Autowired
    private QualityAndQuantityDataManager dataManager;
    private String parserName = "CoalAnalysisCardParser";

    @Override
    public String getParserName() {
        return parserName;
    }

    @Override
    public CoalAnalysisData parse(String paramValueJson) {
        CoalAnalysisData coalAnalysisData = new CoalAnalysisData();
        ReportFormsParam thingMetricParam = JSON.parseObject(paramValueJson, ReportFormsParam.class);

        String metricCode = thingMetricParam.getMetricCode();
        String target = thingMetricParam.getThingCode();
        Optional<DataModelWrapper> data = dataService.getData(target, metricCode);
        if (data.isPresent()) {
            String value = data.get().getValue();
            CoalAnalysisRecord coalAnalysisRecord = JSON.parseObject(value, CoalAnalysisRecord.class);
            if (coalAnalysisRecord != null) {
                coalAnalysisData = dataManager.transCoalAnalysisRecordToData(coalAnalysisRecord);
                coalAnalysisData.setType(thingMetricParam.getType());
                coalAnalysisData.setShowBit(thingMetricParam.getShowBit());
                if (thingMetricParam.isAvg()){
                    coalAnalysisData.setThingName(coalAnalysisData.getThingCode()+ ReportFormsUtils.AVG_RECORD_KEYWORD);
                    coalAnalysisData.setThingCode(null);
                }
            }
        }

        return coalAnalysisData;
    }

    @Override
    public CoalAnalysisData parseTest(String paramValueJson) {
        return new CoalAnalysisData();
    }
}
