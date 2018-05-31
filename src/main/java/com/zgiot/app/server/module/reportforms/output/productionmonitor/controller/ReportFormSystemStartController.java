package com.zgiot.app.server.module.reportforms.output.productionmonitor.controller;


import com.zgiot.app.server.module.reportforms.output.productionmonitor.pojo.ReportFormSystemStartRecord;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.service.ReportFormSystemStartService;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/reportform/systemstart")
public class ReportFormSystemStartController {
    @Autowired
    private ReportFormSystemStartService reportFormSystemStartService;

    @ApiOperation("设置开车停车状态")
    @PostMapping(value = "/state/update")
    public ResponseEntity<String> updateDescriptionAndReason(@RequestBody List<ReportFormSystemStartRecord> records) {
        reportFormSystemStartService.updateDescriptionAndReason(records);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(null), HttpStatus.OK);
    }

}
