package com.zgiot.app.server.module.qualityandquantity.pojo;

import com.alibaba.fastjson.annotation.JSONField;

public class ProportionPieChartParam {
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "tc")
    private String thingCode;
    @JSONField(name = "tmc1")
    private String partOneThingMetricCode;
    @JSONField(name = "tmc2")
    private String partTwoThingMetricCode;
    @JSONField(name = "cmc1")
    private String partOneCoalMetricCode;
    @JSONField(name = "cmc2")
    private String partTwoCoalMetricCode;
    @JSONField(name = "rmc")
    private String ratioMetricCode;

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

    public String getPartOneThingMetricCode() {
        return partOneThingMetricCode;
    }

    public void setPartOneThingMetricCode(String partOneThingMetricCode) {
        this.partOneThingMetricCode = partOneThingMetricCode;
    }

    public String getPartTwoThingMetricCode() {
        return partTwoThingMetricCode;
    }

    public void setPartTwoThingMetricCode(String partTwoThingMetricCode) {
        this.partTwoThingMetricCode = partTwoThingMetricCode;
    }

    public String getPartOneCoalMetricCode() {
        return partOneCoalMetricCode;
    }

    public void setPartOneCoalMetricCode(String partOneCoalMetricCode) {
        this.partOneCoalMetricCode = partOneCoalMetricCode;
    }

    public String getPartTwoCoalMetricCode() {
        return partTwoCoalMetricCode;
    }

    public void setPartTwoCoalMetricCode(String partTwoCoalMetricCode) {
        this.partTwoCoalMetricCode = partTwoCoalMetricCode;
    }

    public String getRatioMetricCode() {
        return ratioMetricCode;
    }

    public void setRatioMetricCode(String ratioMetricCode) {
        this.ratioMetricCode = ratioMetricCode;
    }
}
