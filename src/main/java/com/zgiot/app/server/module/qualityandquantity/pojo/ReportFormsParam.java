package com.zgiot.app.server.module.qualityandquantity.pojo;

import com.alibaba.fastjson.annotation.JSONField;

public class ReportFormsParam {
    @JSONField(name = "tc")
    private String thingCode;
    @JSONField(name = "mc")
    private String metricCode;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "showbit")
    private int showBit;
    @JSONField(name = "ignoretc")
    private boolean ignoreTc;
    /**
     * 班平均数据
     */
    @JSONField(name = "isavg")
    private boolean isAvg;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getShowBit() {
        return showBit;
    }

    public void setShowBit(int showBit) {
        this.showBit = showBit;
    }

    public boolean isIgnoreTc() {
        return ignoreTc;
    }

    public void setIgnoreTc(boolean ignoreTc) {
        this.ignoreTc = ignoreTc;
    }

    public boolean isAvg() {
        return isAvg;
    }

    public void setAvg(boolean avg) {
        isAvg = avg;
    }
}
