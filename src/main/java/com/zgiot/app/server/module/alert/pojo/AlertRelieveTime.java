package com.zgiot.app.server.module.alert.pojo;

/**
 * Created by Administrator on 2018\4\25 0025.
 */
public class AlertRelieveTime {
    private Long id;
    private String thingCode;
    private String metricCode;
    private Long delayTime;
    private Boolean enable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
