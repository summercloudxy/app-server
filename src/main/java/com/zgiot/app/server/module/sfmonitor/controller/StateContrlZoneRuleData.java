package com.zgiot.app.server.module.sfmonitor.controller;

public class StateContrlZoneRuleData {
    private String metricCode;
    private int sort;
    private int direction;
    private int rule;

    public String getMetricCode() {
        return metricCode;
    }

    public int getSort() {
        return sort;
    }

    public int getDirection() {
        return direction;
    }

    public int getRule() {
        return rule;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }
}
