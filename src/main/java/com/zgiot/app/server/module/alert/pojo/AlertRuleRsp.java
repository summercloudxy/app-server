package com.zgiot.app.server.module.alert.pojo;

import java.util.List;

public class AlertRuleRsp {
    private String thingCode;
    private List<AlertRule> alertRules;

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public List<AlertRule> getAlertRules() {
        return alertRules;
    }

    public void setAlertRules(List<AlertRule> alertRules) {
        this.alertRules = alertRules;
    }
}
