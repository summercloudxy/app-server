package com.zgiot.app.server.module.filterpress.pojo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FilterPressTotalPlateCountBean {
    /**
     * 一期 二期
     */
    private int period;
    /**
     * 白班：true，夜班：false
     */
    private boolean isDayShift;

    /**
     * 理论总压板数
     */
    private Integer ratedTotalPlateCount;

    /**
     * 时间
     */
    private Date startTime;

    /**
     *     //时间轴(总板数,时间)
     */
    private Map<String,Date> timeLineMap;

    public int getPeriod() {
        return period;
    }

    public boolean getIsDayShift() {
        return isDayShift;
    }

    public Integer getRatedTotalPlateCount() {
        return ratedTotalPlateCount;
    }

    public Map<String, Date> getTimeLineMap() {
        return timeLineMap;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setIsDayShift(boolean isDayShift) {
        isDayShift = isDayShift;
    }

    public void setRatedTotalPlateCount(Integer ratedTotalPlateCount) {
        this.ratedTotalPlateCount = ratedTotalPlateCount;
    }

    public void setTimeLineMap(Map<String, Date> timeLineMap) {
        this.timeLineMap = timeLineMap;
    }
}
