package com.zgiot.app.server.module.alert.pojo;

import java.util.List;

public class AlertMaskRsp {
    private Integer pageCount;
    private List<AlertMaskStatistics> alertMaskStatisticsInfo;
    private Long queryTime;

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public List<AlertMaskStatistics> getAlertMaskStatisticsInfo() {
        return alertMaskStatisticsInfo;
    }

    public void setAlertMaskStatisticsInfo(List<AlertMaskStatistics> alertMaskStatisticsInfo) {
        this.alertMaskStatisticsInfo = alertMaskStatisticsInfo;
    }

    public Long getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Long queryTime) {
        this.queryTime = queryTime;
    }
}
