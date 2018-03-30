package com.zgiot.app.server.module.qualityandquantity.pojo;

import java.util.List;

public class DashboardData {
    private String thingCode;
    private String thingName;
    private boolean ignoreTc;
    private List<MetricDataValue> metricDataValues;
    private String module;
    private int system;

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

    public boolean isIgnoreTc() {
        return ignoreTc;
    }

    public void setIgnoreTc(boolean ignoreTc) {
        this.ignoreTc = ignoreTc;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }
}
