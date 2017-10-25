package com.zgiot.app.server.module.generic;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 * @author wangwei
 */
public class HistoryDataDto {
    @JSONField(name = "sd")
    private Date startDate;

    @JSONField(name = "ed")
    private Date endDate;

    @JSONField(name = "dr")
    private Long duration;

    @JSONField(name = "tc")
    private List<String> thingCodes;

    @JSONField(name = "mc")
    private List<String> metricCodes;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public List<String> getThingCodes() {
        return thingCodes;
    }

    public void setThingCodes(List<String> thingCodes) {
        this.thingCodes = thingCodes;
    }

    public List<String> getMetricCodes() {
        return metricCodes;
    }

    public void setMetricCodes(List<String> metricCodes) {
        this.metricCodes = metricCodes;
    }
}
