package com.zgiot.app.server.module.sfmonitor.pojo;


public class RelSFMonItem {
    private Long id;
    private Long sfMonId;
    private Float sort;
    private Short viewTypeId;
    private String thingCode;
    private String metricCode;

    public Long getId() {
        return id;
    }

    public Long getSfMonId() {
        return sfMonId;
    }

    public Float getSort() {
        return sort;
    }

    public Short getViewTypeId() {
        return viewTypeId;
    }

    public String getThingCode() {
        return thingCode;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSfMonId(Long sfMonId) {
        this.sfMonId = sfMonId;
    }

    public void setSort(Float sort) {
        this.sort = sort;
    }

    public void setViewTypeId(Short viewTypeId) {
        this.viewTypeId = viewTypeId;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }
}
