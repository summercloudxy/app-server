package com.zgiot.app.server.module.tcs.controller;

import com.zgiot.app.server.module.tcs.pojo.AnalysisInfoList;
import com.zgiot.app.server.module.tcs.pojo.TcsAnalysisInfo;
import com.zgiot.app.server.module.tcs.pojo.TcsParameter;
import com.zgiot.app.server.module.tcs.service.TcsService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = GlobalConstants.API_VERSION + "/tcs")
public class TcsController {
    @Autowired
    private TcsService tcsService;

    @RequestMapping(value = "analysis/history/{system}")
    public ResponseEntity<String> getAnalysisRecordList(@PathVariable int system, @RequestParam(required = false) Date endTime, @RequestParam int page, @RequestParam int count) {
        if (endTime == null) {
            endTime = new Date();
        }
        List<AnalysisInfoList> analysisHistoryListGroupByDay = tcsService.getAnalysisHistoryListGroupByDay(system, endTime, page, count);
        return new ResponseEntity<>(ServerResponse.buildOkJson(analysisHistoryListGroupByDay), HttpStatus.OK);
    }

    @RequestMapping(value = "analysis/recent")
    public ResponseEntity<String> getRecentRecord(){
        List<TcsAnalysisInfo> tcsAnalysisInfos = new ArrayList<>();
        tcsAnalysisInfos.add(tcsService.getRecentTcsRecord(1));
        tcsAnalysisInfos.add(tcsService.getRecentTcsRecord(2));
        return new ResponseEntity<>(ServerResponse.buildOkJson(tcsAnalysisInfos), HttpStatus.OK);
    }

    @RequestMapping(value = "analysis/curve")
    public ResponseEntity<String> getRecordCurve(@RequestParam Long duration){
        Map<Integer,List<TcsAnalysisInfo>> curveMap = new HashMap<>(2);
        curveMap.put(1,tcsService.getTcsAnalysisCurve(1,duration));
        curveMap.put(2,tcsService.getTcsAnalysisCurve(2,duration));
        return new ResponseEntity<>(ServerResponse.buildOkJson(curveMap), HttpStatus.OK);
    }


    @RequestMapping(value = "parameter")
    public ResponseEntity<String> getMonitorParameter(){
        List<TcsParameter> tcsParameters = new ArrayList<>(2);
        tcsParameters.add(tcsService.getMonitorParameter(1));
        tcsParameters.add(tcsService.getMonitorParameter(2));
        return new ResponseEntity<>(ServerResponse.buildOkJson(tcsParameters), HttpStatus.OK);
    }



}
                                                  