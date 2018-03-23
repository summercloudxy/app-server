package com.zgiot.app.server.module.qualityandquantity.pojo;

import java.util.List;

public class DashboardData {
    private String thingCode;
    private String thingName;
    private List<MetricDataValue> metricDataValues;

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public List<MetricDataValue> getMetricDataValues() {
        return metricDataValues;
    }

    public void setMetricDataValues(List<MetricDataValue> metricDataValues) {
        this.metricDataValues = metricDataValues;
    }
}
