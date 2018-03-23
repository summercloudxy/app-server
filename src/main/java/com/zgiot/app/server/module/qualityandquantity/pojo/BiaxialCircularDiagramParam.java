package com.zgiot.app.server.module.qualityandquantity.pojo;

import com.alibaba.fastjson.annotation.JSONField;

public class BiaxialCircularDiagramParam {
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "tc")
    private String thingCode;
    @JSONField(name = "mc1")
    private String outerCurveMetricCode;
    @JSONField(name = "mc2")
    private String innerCurveMetricCode;
    @JSONField(name = "mc3")
    private String extraMetricCode;

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

    public String getOuterCurveMetricCode() {
        return outerCurveMetricCode;
    }

    public void setOuterCurveMetricCode(String outerCurveMetricCode) {
        this.outerCurveMetricCode = outerCurveMetricCode;
    }

    public String getInnerCurveMetricCode() {
        return innerCurveMetricCode;
    }

    public void setInnerCurveMetricCode(String innerCurveMetricCode) {
        this.innerCurveMetricCode = innerCurveMetricCode;
    }

    public String getExtraMetricCode() {
        return extraMetricCode;
    }

    public void setExtraMetricCode(String extraMetricCode) {
        this.extraMetricCode = extraMetricCode;
    }
}
