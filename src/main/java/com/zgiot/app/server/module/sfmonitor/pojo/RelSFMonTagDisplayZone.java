package com.zgiot.app.server.module.sfmonitor.pojo;

public class RelSFMonTagDisplayZone {
    private Integer id;
    private String metricTagCode;
    private int zoneId;
    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMetricTagCode() {
        return metricTagCode;
    }

    public int getZoneId() {
        return zoneId;
    }

    public String getCode() {
        return code;
    }

    public void setMetricTagCode(String metricTagCode) {
        this.metricTagCode = metricTagCode;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
