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
     *     //时间轴(总板数,时间)
     */
    private Map<Integer,Date> timeLineMap;

    public int getPeriod() {
        return period;
    }

    public boolean isDayShift() {
        return isDayShift;
    }

    public Integer getRatedTotalPlateCount() {
        return ratedTotalPlateCount;
    }

    public Map<Integer, Date> getTimeLineMap() {
        return timeLineMap;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setDayShift(boolean dayShift) {
        isDayShift = dayShift;
    }

    public void setRatedTotalPlateCount(Integer ratedTotalPlateCount) {
        this.ratedTotalPlateCount = ratedTotalPlateCount;
    }

    public void setTimeLineMap(Map<Integer, Date> timeLineMap) {
        this.timeLineMap = timeLineMap;
    }
}
