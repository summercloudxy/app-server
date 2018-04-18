package com.zgiot.app.server.module.auth.controller.station;

import java.util.Date;

public class StationInfo {
    private String stationId;
    private String stationName;
    private int stationUserCount;
    private Date updateDate;

    public String getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public int getStationUserCount() {
        return stationUserCount;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void setStationUserCount(int stationUserCount) {
        this.stationUserCount = stationUserCount;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
