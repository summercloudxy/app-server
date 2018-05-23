package com.zgiot.app.server.module.reportforms.output.controller;

import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormOutputStoreRecord;
import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormTargetRecord;
import com.zgiot.app.server.module.reportforms.output.service.OutputStoreAndTargetService;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value = "/reportform/")
public class OutputStoreAndTargetController {
    @Autowired
    private OutputStoreAndTargetService outputStoreAndTargetService;

    @ApiOperation("设置生产、库存数据")
    @PostMapping(value = "outputstore")
    public ResponseEntity<String> addOutPutStoreInfo(@RequestBody ReportFormOutputStoreRecord outputStoreRecord){
        if (outputStoreRecord.getType()==1){
            outputStoreAndTargetService.addOutputRecord(outputStoreRecord);
        }else {
            outputStoreAndTargetService.addStoreRecord(outputStoreRecord);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(outputStoreRecord), HttpStatus.OK);
    }


    @GetMapping(value = "outputstore")
    public ResponseEntity<String> getOutPutStoreInfo(@Param("dutyStartTime")Date dutyStartTime){
        Map<Integer, ReportFormOutputStoreRecord> outputStoreRecord = outputStoreAndTargetService.getOutputStoreRecord(dutyStartTime);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(outputStoreRecord), HttpStatus.OK);
    }


    @GetMapping(value = "target")
    public ResponseEntity<String> getTargetInfo(@Param("dutyStartTime")Date dutyStartTime){
        Map<Integer, Map<Integer,ReportFormTargetRecord>> targetInfoMap = outputStoreAndTargetService.getTargetInfo(dutyStartTime);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(targetInfoMap), HttpStatus.OK);
    }


}
