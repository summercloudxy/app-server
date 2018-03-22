package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.common.pojo.DataModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SFMonPadParameterZoneInfo {
    private String metricName;
    private String metricCode;
    private String value;
    private String unit;
    private Map<String,List<DataModel>> hisData;
    private Date startDate;
    private Date endDate;

    public String getMetricName() {
        return metricName;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public String getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public Map<String, List<DataModel>> getHisData() {
        return hisData;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setHisData(Map<String, List<DataModel>> hisData) {
        this.hisData = hisData;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
