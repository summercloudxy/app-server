package com.zgiot.app.server.module.qualityandquantity.pojo;

import com.zgiot.app.server.module.alert.pojo.AlertRule;

import java.util.List;

public class MetricDataValue {
    private int index;
    private String thingCode;
    private String metricCode;
    private String metricName;
    private String valueUnit;
    private String value;
    private List<AlertRule> alertRules;
    private AlertRule paramRange;

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public AlertRule getParamRange() {
        return paramRange;
    }

    public void setParamRange(AlertRule paramRange) {
        this.paramRange = paramRange;
    }

    public List<AlertRule> getAlertRules() {
        return alertRules;
    }

    public void setAlertRules(List<AlertRule> alertRules) {
        this.alertRules = alertRules;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueUnit() {
        return valueUnit;
    }

    public void setValueUnit(String valueUnit) {
        this.valueUnit = valueUnit;
    }
}
