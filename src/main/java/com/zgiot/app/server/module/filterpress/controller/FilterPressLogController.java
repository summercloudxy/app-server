package com.zgiot.app.server.module.filterpress.controller;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountWrapper;
import com.zgiot.app.server.service.FilterPressLogService;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class FilterPressLogController {

    @Autowired
    private FilterPressLogService filterPressLogService;

    @ApiOperation("日志查询，按照日期、设备编号、状态模式查询")
    @GetMapping(value="api/filterPress/log/queryLogByDate/{date}")
    public ResponseEntity<String> queryLogByDate(@PathVariable String date){
        List<FilterPressLogBean> filterPressLogBeanList = filterPressLogService.getLogByDate(date);
        return new ResponseEntity<>(ServerResponse.buildOkJson(filterPressLogBeanList),
                HttpStatus.OK);
    }

    @ApiOperation("压板信息查询")
    @GetMapping(value="api/filterPress/plate/queryPlateInfo/{isDayShift}")
    public ResponseEntity<String> queryPlateInfo(@PathVariable boolean isDayShift){
        FilterPressPlateCountWrapper filterPressPlateCountWrapper = filterPressLogService.getPlateInfos(isDayShift);
        return new ResponseEntity<>(ServerResponse.buildOkJson(filterPressPlateCountWrapper),
                HttpStatus.OK);
    }
}
