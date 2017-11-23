package com.zgiot.app.server.module.filterpress.pojo;

import java.util.List;

public class FilterPressHisPlateCountBean {
    /**
     * 白班：true，夜班：false
     */
    private boolean isDayShift;
    /**
     * 卸料总数
     */
    private int unloadTotalCount;
    /**
     * key:thingCode
     * value:unloadCount
     */
    private List<FilterPressTcAndMaxPlateCount> filterPressTcAndMaxPlateCountList;

    public boolean isDayShift() {
        return isDayShift;
    }

    public int getUnloadTotalCount() {
        return unloadTotalCount;
    }

    public List<FilterPressTcAndMaxPlateCount> getFilterPressTcAndMaxPlateCountList() {
        return filterPressTcAndMaxPlateCountList;
    }

    public void setDayShift(boolean dayShift) {
        isDayShift = dayShift;
    }

    public void setUnloadTotalCount(int unloadTotalCount) {
        this.unloadTotalCount = unloadTotalCount;
    }

    public void setFilterPressTcAndMaxPlateCountList(List<FilterPressTcAndMaxPlateCount> filterPressTcAndMaxPlateCountList) {
        this.filterPressTcAndMaxPlateCountList = filterPressTcAndMaxPlateCountList;
    }
}
