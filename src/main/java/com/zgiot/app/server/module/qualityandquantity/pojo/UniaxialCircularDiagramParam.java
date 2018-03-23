package com.zgiot.app.server.module.qualityandquantity.pojo;

import com.alibaba.fastjson.annotation.JSONField;

public class UniaxialCircularDiagramParam {
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "tc")
    private String thingCode;
    @JSONField(name = "mc")
    private String metricCode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }
}
