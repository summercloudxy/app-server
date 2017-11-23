package com.zgiot.app.server.module.filterpress.pojo;

import java.util.List;

public class FilterPressPlateCountWrapper {
    /**
     * 一期 二期
     */
    private int period;
    /**
     * 白班：true，夜班：false
     */
    private boolean isDayShift;

    /**
     * 理论压板数
     */
    private Integer ratedPlateCount;

    /**
     * 设备时间轴Map
     */
    private List<FilterPressPlateCountBean> filterPressPlateCountBeanList;

    public int getPeriod() {
        return period;
    }

    public boolean getIsDayShift() {
        return isDayShift;
    }

    public Integer getRatedPlateCount() {
        return ratedPlateCount;
    }

    public List<FilterPressPlateCountBean> getFilterPressPlateCountBeanList() {
        return filterPressPlateCountBeanList;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setIsDayShift(boolean isDayShift) {
        this.isDayShift = isDayShift;
    }

    public void setRatedPlateCount(Integer ratedPlateCount) {
        this.ratedPlateCount = ratedPlateCount;
    }

    public void setFilterPressPlateCountBeanList(List<FilterPressPlateCountBean> filterPressPlateCountBeanList) {
        this.filterPressPlateCountBeanList = filterPressPlateCountBeanList;
    }
}
