package com.zgiot.app.server.module.filterpress.pojo;


public class FilterPressRatedStartTimeBean {
   private String dayOrNightRatedStartTimes;
   private String startTimeOffset;

    public String getDayOrNightRatedStartTimes() {
        return dayOrNightRatedStartTimes;
    }

    public String getStartTimeOffset() {
        return startTimeOffset;
    }

    public void setDayOrNightRatedStartTimes(String dayOrNightRatedStartTimes) {
        this.dayOrNightRatedStartTimes = dayOrNightRatedStartTimes;
    }

    public void setStartTimeOffset(String startTimeOffset) {
        this.startTimeOffset = startTimeOffset;
    }
}
