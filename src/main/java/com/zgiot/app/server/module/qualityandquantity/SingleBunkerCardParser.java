package com.zgiot.app.server.module.qualityandquantity;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.qualityandquantity.pojo.BiaxialCircularDiagramData;
import com.zgiot.app.server.module.qualityandquantity.pojo.MetricDataValue;
import com.zgiot.app.server.module.qualityandquantity.pojo.UniaxialCircularDiagramParam;
import com.zgiot.app.server.module.qualityandquantity.service.QualityAndQuantityDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SingleBunkerCardParser implements CardParser {
    @Autowired
    private QualityAndQuantityDataManager dataManager;
    private String parserName = "SingleBunkerCardParser";

    @Override
    public String getParserName() {
        return parserName;
    }

    @Override
    public BiaxialCircularDiagramData parse(String paramValueJson) {
        UniaxialCircularDiagramParam param = JSON.parseObject(paramValueJson,UniaxialCircularDiagramParam.class);
        BiaxialCircularDiagramData data = new BiaxialCircularDiagramData();
        data.setTitle(param.getTitle());
        MetricDataValue value = dataManager.getValueWithRule(param.getThingCode(), param.getThingCode());
        data.setValueOne(value);
        return data;
    }

    @Override
    public BiaxialCircularDiagramData parseTest(String paramValueJson) {
        return new BiaxialCircularDiagramData();
    }
}
