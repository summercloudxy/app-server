package com.zgiot.app.server.module.auth.controller.station;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.auth.mapper.StationMapper;
import com.zgiot.app.server.module.auth.pojo.Station;
import com.zgiot.app.server.module.auth.service.StationService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/stations")
public class StationController {
    @Autowired
    private StationService stationService;
    @Autowired
    private StationMapper stationMapper;

    @ApiOperation("增加岗位")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> addStation(@RequestBody String station) {
        Station stat = JSON.parseObject(station, Station.class);
        if (StringUtils.isBlank(station) || stat == null || stat.getName() == null) {
            ServerResponse res = new ServerResponse<>(
                    "Not valid request data. The incoming req body is: `" + station + "`", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        Boolean isExist = null;
        Station addstat = stationService.getStationByName(stat.getName());
        if (addstat == null) {
            isExist = false;
            stat.setUpdateDate(new Date());
            stationService.addStation(stat);
        } else {
            isExist = true;
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(isExist),
                HttpStatus.OK);
    }

    @ApiOperation("删除岗位")
    @RequestMapping(value = "/{stationId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteStation(@PathVariable int stationId) {
        Station station = stationService.getStationById(stationId);
        int userCount = 0;
        if (station != null) {
            userCount = stationService.getStationUserCount(stationId);
            if (userCount == 0) {
                stationService.deleteStation(stationId);
            }
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(userCount),
                HttpStatus.OK);
    }

    @ApiOperation("获取岗位信息")
    @GetMapping(value = "/{stationId}")
    public ResponseEntity<String> getStation(@PathVariable int stationId) {
        Station station = stationService.getStationById(stationId);
        if (station == null) {
            ServerResponse res = new ServerResponse<>(
                    "station is not exist`" + stationId + "`", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(station),
                HttpStatus.OK);
    }

    @ApiOperation("编辑岗位信息")
    @RequestMapping(value = "/{stationId}", method = RequestMethod.PUT)
    public ResponseEntity<String> editStation(@PathVariable int stationId, @RequestBody String station) {
        Station stat = JSON.parseObject(station, Station.class);
        if (StringUtils.isBlank(station) || stat == null || stat.getName() == null) {
            ServerResponse res = new ServerResponse<>(
                    "Not valid request data. The incoming req body is: `" + station + "`", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        boolean isExist = true;
        Station editstat = stationService.getStationById(stationId);
        if (editstat == null) {
            isExist = false;
        }
        editstat.setName(stat.getName());
        editstat.setUpdateDate(new Date());
        stationService.updateStation(editstat);
        return new ResponseEntity<>(ServerResponse.buildOkJson(isExist),
                HttpStatus.OK);
    }

    @ApiOperation("获取所有岗位信息")
    @GetMapping(value = "/pageNum/{pageNum}/pageSize/{pageSize}")
    public ResponseEntity<String> getAllStations(@PathVariable int pageNum, @PathVariable int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<StationInfo> stationInfos = stationService.getStationInfos();
        int sum = stationService.getStationSum();
        if (stationInfos == null) {
            stationInfos = Collections.emptyList();
        }
        StationReturn stationReturn = new StationReturn();
        stationReturn.setStationInfos(stationInfos);
        stationReturn.setSum(sum);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stationReturn),
                HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<String> getAllStation() {
        List<StationInfo> stationInfos = stationService.getStationInfos();
        if (stationInfos == null) {
            stationInfos = Collections.emptyList();
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(stationInfos),
                HttpStatus.OK);
    }

    @GetMapping("/station/{name}")
    public ResponseEntity<String> getStation(@PathVariable String name) {
        StationInfo stationInfo = stationMapper.getStationInfoByName(name);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stationInfo),
                HttpStatus.OK);
    }

}
