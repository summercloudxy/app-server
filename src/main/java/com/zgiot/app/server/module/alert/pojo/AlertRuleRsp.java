package com.zgiot.app.server.module.alert.pojo;

import java.util.List;

public class AlertRuleRsp {
    private Integer pageCount;
    private List<ThingAlertRule> thingAlertRules;

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public List<ThingAlertRule> getThingAlertRules() {
        return thingAlertRules;
    }

    public void setThingAlertRules(List<ThingAlertRule> thingAlertRules) {
        this.thingAlertRules = thingAlertRules;
    }

    public AlertRuleRsp(List<ThingAlertRule> thingAlertRules) {
        this.thingAlertRules = thingAlertRules;
    }

    public AlertRuleRsp() {
    }
}
