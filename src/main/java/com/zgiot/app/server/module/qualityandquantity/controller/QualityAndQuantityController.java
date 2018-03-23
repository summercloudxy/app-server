package com.zgiot.app.server.module.qualityandquantity.controller;

import com.zgiot.app.server.module.qualityandquantity.pojo.*;
import com.zgiot.app.server.module.qualityandquantity.service.QualityAndQuantityService;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = GlobalConstants.API_VERSION + "/qaq")
public class QualityAndQuantityController {
    @Autowired
    private QualityAndQuantityService qualityAndQuantityService;


    @RequestMapping(value = "areainfo")
    public ResponseEntity<String> getAreaInfos(@RequestParam List<Integer> areaIds) {
        List<AreaInfo> areaInfos = qualityAndQuantityService.getAreaInfos(areaIds);
        return new ResponseEntity<>(ServerResponse.buildOkJson(areaInfos), HttpStatus.OK);
    }


    @RequestMapping(value = "coalcumulate")
    public ResponseEntity<String> getCoalCumulatedValueInDuty(@RequestParam String thingCode,@RequestParam Date startTime,@RequestParam Date endTime){
        List<DutyInfoInOneDay> maxValueOnDuty = qualityAndQuantityService.getMaxValueOnDuty(thingCode, MetricCodes.CT_C, startTime, endTime);
        return new ResponseEntity<>(ServerResponse.buildOkJson(maxValueOnDuty), HttpStatus.OK);
    }


    @RequestMapping(value = "coalanalysis/history")
    public ResponseEntity<String> getCoalAnalysisHistory(@RequestBody FilterCondition filterCondition){
        List<CoalAnalysisData> coalAnalysisHistoryData = qualityAndQuantityService.getCoalAnalysisHistoryData(filterCondition);
        return new ResponseEntity<>(ServerResponse.buildOkJson(coalAnalysisHistoryData), HttpStatus.OK);
    }

    @RequestMapping(value = "productioninspect/history")
    public ResponseEntity<String> getProductionInspectHistory(@RequestBody FilterCondition filterCondition){
        List<ProductionInspectData> productionInspectData = qualityAndQuantityService.getProductionInspectData(filterCondition);
        return new ResponseEntity<>(ServerResponse.buildOkJson(productionInspectData), HttpStatus.OK);
    }




}
