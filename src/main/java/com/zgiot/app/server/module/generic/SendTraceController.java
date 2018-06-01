package com.zgiot.app.server.module.generic;

import com.zgiot.app.server.service.SendTraceLogService;
import com.zgiot.app.server.service.pojo.SendTraceLog;
import com.zgiot.app.server.service.pojo.SendType;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class SendTraceController {
    @Autowired
    private SendTraceLogService sendTraceLogService;

    @GetMapping("/sendTrace/logs")
    public ResponseEntity<String> getSendTraceLog(@ModelAttribute SendTraceLog filterCondition, @RequestParam(required = false) Date startTime, @RequestParam(required = false) Date endTime) {

        List<SendTraceLog> sendTraceLogList = sendTraceLogService.getSendTraceLogList(filterCondition, startTime, endTime);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(sendTraceLogList)
                , HttpStatus.OK);
    }


    @GetMapping("/sendTrace/types")
    public ResponseEntity<String> getSendTypeList() {

        List<SendType> sendTypeList = sendTraceLogService.getSendTypeList();
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(sendTypeList)
                , HttpStatus.OK);
    }
}
