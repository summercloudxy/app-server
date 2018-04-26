package com.zgiot.app.server.module.auth.mapper;

import com.zgiot.app.server.module.auth.controller.station.StationInfo;
import com.zgiot.app.server.module.auth.pojo.Station;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StationMapper {
    Station getStationByName(@Param("name") String name);

    void addStation(Station station);

    int getStationUserCount(@Param("stationId") int stationId);

    void deleteStation(@Param("stationId") int stationId);

    Station getStationById(@Param("stationId") int stationId);

    void updateStation(Station station);

    List<StationInfo> getStationInfos();

    StationInfo getStationInfoByName(@Param("name") String name);

    List<Station> getStationByUserId(@Param("userId") long userId);

    int getStationSum();


}
