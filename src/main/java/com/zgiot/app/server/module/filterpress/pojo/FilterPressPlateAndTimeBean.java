package com.zgiot.app.server.module.filterpress.pojo;

import java.util.Date;

public class FilterPressPlateAndTimeBean {
    private int plateCount;
    private Date time;

    public int getPlateCount() {
        return plateCount;
    }

    public Date getTime() {
        return time;
    }

    public void setPlateCount(int plateCount) {
        this.plateCount = plateCount;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
