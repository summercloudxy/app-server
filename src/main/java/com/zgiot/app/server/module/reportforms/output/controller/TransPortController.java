package com.zgiot.app.server.module.reportforms.output.controller;

import com.zgiot.app.server.module.reportforms.output.pojo.Transport;
import com.zgiot.app.server.module.reportforms.output.pojo.TransportSaleStatistics;
import com.zgiot.app.server.module.reportforms.output.service.TransPortService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/reportForm/transPort/")
public class TransPortController {

    @Autowired
    private TransPortService transPortService;

    @ApiOperation("更新运销")
    @PostMapping("updateTransPort")
    public ResponseEntity<String> updateTransPort(@RequestBody List<Transport> transportList){
        transPortService.updateTransPort(transportList);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(null), HttpStatus.OK);
    }

    @ApiOperation("更新地销")
    @PostMapping("updateSaleStatistics")
    public ResponseEntity<String> updateSaleStatistics(@RequestBody List<TransportSaleStatistics> transportSaleStatisticsList){
        transPortService.updateSaleStatistics(transportSaleStatisticsList);
        return new ResponseEntity<String>(ServerResponse.buildOkJsonWithNonStringKey(null),HttpStatus.OK);
    }

}
