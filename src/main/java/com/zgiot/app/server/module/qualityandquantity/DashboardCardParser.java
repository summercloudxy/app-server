package com.zgiot.app.server.module.qualityandquantity;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.qualityandquantity.pojo.DashboardData;
import com.zgiot.app.server.module.qualityandquantity.pojo.MetricDataValue;
import com.zgiot.app.server.module.qualityandquantity.pojo.ThingMetricParam;
import com.zgiot.app.server.module.qualityandquantity.service.QualityAndQuantityDataManager;
import com.zgiot.app.server.module.qualityandquantity.CardParser;
import com.zgiot.app.server.service.ThingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DashboardCardParser implements CardParser {
    @Autowired
    private ThingService thingService;
    @Autowired
    private QualityAndQuantityDataManager dataManager;
    private String parserName = "DashboardCardParser";

    @Override
    public String getParserName() {
        return parserName;
    }

    @Override
    public DashboardData parse(String paramValueJson) {
        ThingMetricParam thingMetricParam = JSON.parseObject(paramValueJson, ThingMetricParam.class);
        String thingCode = thingMetricParam.getThingCode();
        String thingName = thingService.getThing(thingCode).getThingName();
        DashboardData dashboardData = new DashboardData();
        dashboardData.setThingCode(thingCode);
        dashboardData.setThingName(thingName);
        dashboardData.setMetricDataValues(getMetricDataValue(thingMetricParam, thingCode));
        if (thingMetricParam.isIgnoreTc()){
            dashboardData.setThingCode(null);
        }
        return dashboardData;
    }

    private List<MetricDataValue> getMetricDataValue(ThingMetricParam thingMetricParam, String thingCode) {
        List<MetricDataValue> metricDataValues = new ArrayList<>();
        List<String> metricCodes = thingMetricParam.getMetricCodes();
        int index = 0;
        for (String metricCode : metricCodes) {
            MetricDataValue valueWithRule = dataManager.getValueWithRule(thingCode, metricCode);
            valueWithRule.setIndex(index);
            index++;
            metricDataValues.add(valueWithRule);
        }
        return metricDataValues;
    }

    @Override
    public DashboardData parseTest(String paramValueJson) {
        return new DashboardData();
    }
}
