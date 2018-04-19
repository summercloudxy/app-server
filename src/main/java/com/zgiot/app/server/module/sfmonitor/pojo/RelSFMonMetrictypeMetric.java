package com.zgiot.app.server.module.sfmonitor.pojo;

import java.util.Date;

public class RelSFMonMetrictypeMetric {

    private Long id;
    private String metricCode;
    private String sfmonMetrictype;
    private Date createDt;
    private Date updateDt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public String getSfmonMetrictype() {
        return sfmonMetrictype;
    }

    public void setSfmonMetrictype(String sfmonMetrictype) {
        this.sfmonMetrictype = sfmonMetrictype;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }
}
