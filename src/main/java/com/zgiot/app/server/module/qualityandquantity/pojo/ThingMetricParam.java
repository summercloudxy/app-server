package com.zgiot.app.server.module.qualityandquantity.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class ThingMetricParam {
    @JSONField(name = "tc")
    private String thingCode;
    @JSONField(name = "mcs")
    private List<String> metricCodes;
    @JSONField(name = "ignoretc")
    private boolean ignoreTc;
    @JSONField(name = "title")
    private String title;

    public List<String> getMetricCodes() {
        return metricCodes;
    }

    public void setMetricCodes(List<String> metricCodes) {
        this.metricCodes = metricCodes;
    }

    public String getThingCode() {

        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public boolean isIgnoreTc() {
        return ignoreTc;
    }

    public void setIgnoreTc(boolean ignoreTc) {
        this.ignoreTc = ignoreTc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
