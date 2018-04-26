package com.zgiot.app.server.module.auth.controller.station;

import java.util.List;

public class StationReturn {
    private List<StationInfo> stationInfos;
    private int sum;

    public List<StationInfo> getStationInfos() {
        return stationInfos;
    }

    public int getSum() {
        return sum;
    }

    public void setStationInfos(List<StationInfo> stationInfos) {
        this.stationInfos = stationInfos;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
