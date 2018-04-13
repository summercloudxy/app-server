package com.zgiot.app.server.module.qualityandquantity;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.qualityandquantity.pojo.*;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModelWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;

@Component
public class ProportionPieChartParser implements CardParser {
    @Autowired
    private DataService dataService;
    private String parserName = "ProportionPieChartParser";
    private static final Double INVALID_VALUE = 0.0;
    private static final String VALUE_ONE = "1";

    private static final String VALUE_ZERO = "0";
    private static final DecimalFormat df = new DecimalFormat("######0.00");

    @Override
    public String getParserName() {
        return parserName;
    }

    @Override
    public ProportionPieChartData parse(String paramValueJson) {
        ProportionPieChartData proportionPieChartData = new ProportionPieChartData();
        ProportionPieChartParam param = JSON.parseObject(paramValueJson, ProportionPieChartParam.class);
        MetricDataValue partOneCoalValue = getCoalValue(param.getThingCode(), param.getPartOneThingMetricCode(), param.getPartOneCoalMetricCode());
        MetricDataValue partTwoCoalValue = getCoalValue(param.getThingCode(), param.getPartTwoThingMetricCode(), param.getPartTwoCoalMetricCode());
        proportionPieChartData.setTitle(param.getTitle());
        proportionPieChartData.setRatioThingCode(param.getThingCode());
        proportionPieChartData.setRatioMetricCode(param.getRatioMetricCode());
        if (partOneCoalValue != null) {
            proportionPieChartData.setPartOneThingCode(partOneCoalValue.getThingCode());
            proportionPieChartData.setPartOneValue(partOneCoalValue.getValue());
        }
        if (partTwoCoalValue != null) {
            proportionPieChartData.setPartTwoThingCode(partTwoCoalValue.getThingCode());
            proportionPieChartData.setPartTwoValue(partTwoCoalValue.getValue());
        }
        countRatio(proportionPieChartData, partOneCoalValue, partTwoCoalValue);
        return proportionPieChartData;

    }

    /**
     * @param thingCode
     * @param thingMetricCode
     * @return
     */
    private MetricDataValue getCoalValue(String thingCode, String thingMetricCode, String coalMetricCode) {
        MetricDataValue metricDataValue = null;
        //根据该thing、metric查到的值是一个设备号
        Optional<DataModelWrapper> coalThingCodeData = dataService.getData(thingCode, thingMetricCode);
        if (coalThingCodeData.isPresent()) {
            String coalThingCode = coalThingCodeData.get().getValue();

            Optional<DataModelWrapper> coalData = dataService.getData(thingCode, coalMetricCode);
            if (coalData.isPresent()) {
                String coalValueStr = coalData.get().getValue();
                if (StringUtils.isNotEmpty(coalValueStr)) {
                    if (Double.valueOf(coalValueStr) < INVALID_VALUE) {
                        return null;
                    }
                    metricDataValue = new MetricDataValue();
                    metricDataValue.setThingCode(coalThingCode);
                    metricDataValue.setValue(coalValueStr);
                }
            }
        }
        return metricDataValue;
    }

    private void countRatio(ProportionPieChartData proportionPieChartData, MetricDataValue partOneData, MetricDataValue partTwoData) {
        if (isInvalidValue(partOneData, partTwoData)) return;
        if (partOneData == null || Double.parseDouble(partOneData.getValue()) <= 0) {
            proportionPieChartData.setRatioTwo(VALUE_ONE);
        } else if (partTwoData == null || Double.parseDouble(partTwoData.getValue()) <= 0) {
            proportionPieChartData.setRatioOne(VALUE_ONE);
        } else {
            double partOneValue = Double.parseDouble(partOneData.getValue());
            double partTwoValue = Double.parseDouble(partTwoData.getValue());
            if (partOneValue < 100 && partTwoValue >= 100) {
                proportionPieChartData.setRatioTwo(VALUE_ONE);
            } else if (partOneValue >= 100 && partTwoValue < 100) {
                proportionPieChartData.setRatioOne(VALUE_ONE);
            } else if (partOneValue < partTwoValue) {
                BigDecimal bigDecimal = BigDecimal.valueOf(partTwoValue / partOneValue);
                String ratio = getFormatValue(bigDecimal);
                proportionPieChartData.setRatioOne(VALUE_ONE);
                proportionPieChartData.setRatioTwo(ratio);
            } else {
                BigDecimal bigDecimal = BigDecimal.valueOf(partOneValue / partTwoValue);
                String ratio = getFormatValue(bigDecimal);
                proportionPieChartData.setRatioTwo(VALUE_ONE);
                proportionPieChartData.setRatioOne(ratio);
            }

        }
    }

    public String getFormatValue(BigDecimal bigDecimal){
        String ratio  ;
        if(new BigDecimal(bigDecimal.intValue()).compareTo(bigDecimal)==0){
            ratio = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        }else{

            ratio = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
        }
        return ratio;
    }

    private boolean isInvalidValue(MetricDataValue partOneData, MetricDataValue partTwoData) {
        if (partOneData == null && partTwoData == null) {
            return true;
        }
        if (partOneData != null && partTwoData != null && Double.parseDouble(partOneData.getValue()) < 100 && Double.parseDouble(partTwoData.getValue()) < 100) {
            return true;
        }
        return false;
    }

    @Override
    public ProportionPieChartData parseTest(String paramValueJson) {
        return new ProportionPieChartData();
    }
}
