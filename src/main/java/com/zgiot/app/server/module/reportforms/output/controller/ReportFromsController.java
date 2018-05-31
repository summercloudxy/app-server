package com.zgiot.app.server.module.reportforms.output.controller;

import com.zgiot.app.server.module.reportforms.output.job.ProductOutputPlanJob;
import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormProductOutput;
import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormProductQuality;
import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormProductStore;
import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormsBean;
import com.zgiot.app.server.module.reportforms.output.service.ReportFormProductOutputAndStoreService;
import com.zgiot.app.server.module.reportforms.output.service.ReportFormsService;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value = "/reportform")
public class ReportFromsController {

    @Autowired
    private ReportFormsService reportFormsService;
    @Autowired
    private ProductOutputPlanJob productOutputPlanJob;
    @Autowired
    private ReportFormProductOutputAndStoreService reportFormProductOutputAndStoreService;


    @ApiOperation("获取报表数据")
    @GetMapping("/records")
    public ResponseEntity<String> getReportFroms(@ApiParam("当班开始时间")@RequestParam Date dutyStartTime){
        ReportFormsBean reportFormsBean = reportFormsService.getBean(dutyStartTime);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(reportFormsBean), HttpStatus.OK);
    }


    @ApiOperation("获取当班开始时间")
    @GetMapping("/dutyStartTime")
    public ResponseEntity<String> getDutyStartTime(){
        Date now = new Date();
        Long time = reportFormsService.getDutyStartTime(now);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(time), HttpStatus.OK);
    }


    @ApiOperation("获取当班开始时间")
    @GetMapping("/testjob")
    public ResponseEntity<String> testjob(String time) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
        Date parse = simpleDateFormat.parse(time);
        productOutputPlanJob.createMonthPlan(parse);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(parse), HttpStatus.OK);
    }


    @ApiOperation("获取当班开始时间")
    @GetMapping("/testoutput")
    public ResponseEntity<String> testoutput(String time) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
        Date parse = simpleDateFormat.parse(time);
        Map<Integer, ReportFormProductOutput> productOutputInfo = reportFormProductOutputAndStoreService.getProductOutputInfo(parse);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(productOutputInfo), HttpStatus.OK);
    }


    @ApiOperation("获取当班开始时间")
    @GetMapping("/testoutputde")
    public ResponseEntity<String> testoutputdetail(String time) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
        Date parse = simpleDateFormat.parse(time);
        Map<Integer, ReportFormProductOutput> productOutputInfo = reportFormProductOutputAndStoreService.getProductOutputInfo(parse);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(productOutputInfo), HttpStatus.OK);
    }

    @ApiOperation("获取当班开始时间")
    @GetMapping("/testqua")
    public ResponseEntity<String> testqua(String time) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
        Date parse = simpleDateFormat.parse(time);
        Map<Integer, ReportFormProductQuality> productQuality = reportFormProductOutputAndStoreService.getProductQuality(parse);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(productQuality), HttpStatus.OK);
    }

    @ApiOperation("获取当班开始时间")
    @GetMapping("/teststore")
    public ResponseEntity<String> teststore(String time) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
        Date parse = simpleDateFormat.parse(time);
        Map<Integer, ReportFormProductStore> productStoreInfo = reportFormProductOutputAndStoreService.getProductStoreInfo(parse);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(productStoreInfo), HttpStatus.OK);
    }



}
