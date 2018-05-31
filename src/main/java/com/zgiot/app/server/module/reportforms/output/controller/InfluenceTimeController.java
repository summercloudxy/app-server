package com.zgiot.app.server.module.reportforms.output.controller;

import com.zgiot.app.server.module.reportforms.output.pojo.InfluenceTime;
import com.zgiot.app.server.module.reportforms.output.pojo.InfluenceTimeRemarks;
import com.zgiot.app.server.module.reportforms.output.pojo.InfluenceTimeReq;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.pojo.ReportFormSystemStartRecord;
import com.zgiot.app.server.module.reportforms.output.service.InfluenceTimeService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/reportform/influenceTime/")
public class InfluenceTimeController {

    @Autowired
    private InfluenceTimeService influenceTimeService;
    @ApiOperation("更新影响生产时间")
    @PostMapping("updateTime")
    public ResponseEntity<String> updateinfluenceTime(@RequestBody InfluenceTimeReq influenceTimeReq) {
        influenceTimeService.influenceTimeService(influenceTimeReq);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(null), HttpStatus.OK);
    }
    @ApiOperation("更新审核人员")
    @PostMapping("updatePersonnel")
    public ResponseEntity<String> updatePersonnel(@RequestBody InfluenceTimeRemarks influenceTimeRemarks) {
        influenceTimeService.updatePersonnel(influenceTimeRemarks);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(null), HttpStatus.OK);
    }

    @ApiOperation("更新影响生产时间测试方法")
    @PostMapping("handle")
    public ResponseEntity<String> handle(@RequestBody List<ReportFormSystemStartRecord> list) {
        influenceTimeService.handle(list);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(null), HttpStatus.OK);
    }


}
