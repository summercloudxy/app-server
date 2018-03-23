package com.zgiot.app.server.module.qualityandquantity;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.qualityandquantity.pojo.BiaxialCircularDiagramData;
import com.zgiot.app.server.module.qualityandquantity.pojo.BiaxialCircularDiagramParam;
import com.zgiot.app.server.module.qualityandquantity.pojo.MetricDataValue;
import com.zgiot.app.server.module.qualityandquantity.service.QualityAndQuantityDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DoubleBunkerCardParser implements CardParser {
    @Autowired
    private QualityAndQuantityDataManager dataManager;
    private String parserName = "DoubleBunkerCardParser";

    @Override
    public String getParserName() {
        return parserName;
    }

    @Override
    public BiaxialCircularDiagramData parse(String paramValueJson) {
        BiaxialCircularDiagramParam biaxialCircularDiagramParam = JSON.parseObject(paramValueJson, BiaxialCircularDiagramParam.class);
        BiaxialCircularDiagramData biaxialCircularDiagramData = new BiaxialCircularDiagramData();
        biaxialCircularDiagramData.setTitle(biaxialCircularDiagramParam.getTitle());
        MetricDataValue outerValue = dataManager.getValueWithRule(biaxialCircularDiagramParam.getThingCode(), biaxialCircularDiagramParam.getOuterCurveMetricCode());
        MetricDataValue innerValue = dataManager.getValueWithRule(biaxialCircularDiagramParam.getThingCode(), biaxialCircularDiagramParam.getInnerCurveMetricCode());
        MetricDataValue extraValue = dataManager.getValueWithRule(biaxialCircularDiagramParam.getThingCode(), biaxialCircularDiagramParam.getExtraMetricCode());
        biaxialCircularDiagramData.setValueThree(extraValue);
        biaxialCircularDiagramData.setValueTwo(innerValue);
        biaxialCircularDiagramData.setValueOne(outerValue);
        return biaxialCircularDiagramData;
    }

    @Override
    public BiaxialCircularDiagramData parseTest(String paramValueJson) {
        return new BiaxialCircularDiagramData();
    }
}
