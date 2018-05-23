package com.zgiot.app.server.module.auth.service;

import com.zgiot.app.server.module.auth.controller.station.StationInfo;
import com.zgiot.app.server.module.auth.mapper.StationMapper;
import com.zgiot.app.server.module.auth.pojo.Station;
import com.zgiot.app.server.module.auth.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {
    @Autowired
    private StationMapper stationMapper;

    public Station getStationByName(String name) {
        return stationMapper.getStationByName(name);
    }

    public void addStation(Station station) {
        if (station == null) {
            return;
        }
        stationMapper.addStation(station);
    }

    public void deleteStation(int stationId) {
        stationMapper.deleteStation(stationId);
    }

    public Station getStationById(int stationId) {
        return stationMapper.getStationById(stationId);
    }

    public int getStationUserCount(int stationId) {
        return stationMapper.getStationUserCount(stationId);
    }

    public void updateStation(Station station) {
        stationMapper.updateStation(station);
    }

    public List<StationInfo> getStationInfos() {
        return stationMapper.getStationInfos();
    }

    public List<Station> getStationByUserId(long userId) {
        return stationMapper.getStationByUserId(userId);
    }

    public int getStationSum() {
        return stationMapper.getStationSum();
    }
}
