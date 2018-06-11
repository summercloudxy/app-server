package com.zgiot.app.server.module.reportforms.output.controller;

import com.github.pagehelper.PageInfo;
import com.zgiot.app.server.module.reportforms.output.job.ProductOutputPlanJob;
import com.zgiot.app.server.module.reportforms.output.pojo.*;
import com.zgiot.app.server.module.reportforms.output.service.CoalAnalysisService;
import com.zgiot.app.server.module.reportforms.output.service.ReportFormProductOutputAndStoreService;
import com.zgiot.app.server.module.reportforms.output.service.ReportFormsService;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/reportForm")
public class ReportFromsController {

    @Autowired
    private ReportFormsService reportFormsService;
    @Autowired
    private ProductOutputPlanJob productOutputPlanJob;
    @Autowired
    private ReportFormProductOutputAndStoreService reportFormProductOutputAndStoreService;
    @Autowired
    private CoalAnalysisService coalAnalysisService;


    @ApiOperation("获取报表数据")
    @GetMapping("/records")
    public ResponseEntity<String> getReportFroms(@ApiParam("当班开始时间") @RequestParam Date dutyStartTime) {
        ReportFormsBean reportFormsBean = reportFormsService.getBean(dutyStartTime);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(reportFormsBean), HttpStatus.OK);
    }


    @ApiOperation("获取当班开始时间")
    @GetMapping("/dutyStartTime")
    public ResponseEntity<String> getDutyStartTime() {
        Date now = new Date();
        Long time = reportFormsService.getDutyStartTime(now);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(time), HttpStatus.OK);
    }


    @ApiOperation("获取当班开始时间")
    @GetMapping("/coal")
    public ResponseEntity<String> getDutyStartTime(String time) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
        Date parse = simpleDateFormat.parse(time);

        List<ProductCoalStatistics> productCoalStatisticsList = coalAnalysisService.getProductCoalStatisticsListFromOtherModule(parse);
        coalAnalysisService.insertProductCoalStatisticsRecords(productCoalStatisticsList);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(productCoalStatisticsList), HttpStatus.OK);

    }


    @ApiOperation("获取产品质量统计数据")
    @PostMapping("/product/coal/statistics/{pageNum}/{pageSize}")
    public ResponseEntity<String> getCoal(@RequestBody ProductCoalStatistics productCoalStatistics, @PathVariable int pageNum, @PathVariable int pageSize) throws Exception {
        PageInfo<ProductCoalStatistics> productCoalStatisticsListInDuration = coalAnalysisService.getProductCoalStatisticsListInDuration(productCoalStatistics, pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(productCoalStatisticsListInDuration), HttpStatus.OK);
    }

}
