package com.zgiot.app.server.module.reportforms.output.controller;

import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormsBean;
import com.zgiot.app.server.module.reportforms.output.service.ReportFormsService;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/reportform")
public class ReportFromsController {

    @Autowired
    private ReportFormsService reportFormsService;

    @ApiOperation("获取报表数据")
    @GetMapping("/records/api")
    public ResponseEntity<ServerResponse<ReportFormsBean>> getReportFromsTest(@ApiParam("当班开始时间")@RequestParam Date dutyStartTime){
        ReportFormsBean reportFormsBean = reportFormsService.getBean(dutyStartTime);
        return new ResponseEntity<ServerResponse<ReportFormsBean>>(new ServerResponse(
                "OK", SysException.EC_SUCCESS, reportFormsBean
        ), HttpStatus.OK);
    }

    @ApiOperation("获取报表数据")
    @GetMapping("/records")
    public ResponseEntity<String> getReportFroms(@ApiParam("当班开始时间")@RequestParam Date dutyStartTime){
        ReportFormsBean reportFormsBean = reportFormsService.getBean(dutyStartTime);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(reportFormsBean), HttpStatus.OK);
    }

    @ApiOperation("获取报表数据")
    @GetMapping("/test")
    public ResponseEntity<String> test(@ApiParam("当班开始时间")@RequestParam Date dutyStartTime){
       Integer aa = reportFormsService.getTeamInfo(dutyStartTime);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(aa), HttpStatus.OK);
    }

    @ApiOperation("获取当班开始时间")
    @GetMapping("/dutyStartTime")
    public ResponseEntity<String> getDutyStartTime(){
        Date now = new Date();
        Long time = reportFormsService.getDutyStartTime(now);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(time), HttpStatus.OK);
    }

}
