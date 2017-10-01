package com.zgiot.app.server.module.generic;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cmd")
public class CmdController {
    @Autowired
    private CmdControlService cmdControlService;

    @PostMapping("")
    public ResponseEntity<String> sendCmd(@RequestBody String reqBody , HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        DataModel dataModel = JSON.parseObject(reqBody.trim(), DataModel.class);
        int count = cmdControlService.sendCmd(dataModel, requestId);

        return new ResponseEntity<>(
                ServerResponse.buildOkJson(count)
                , HttpStatus.OK);
    }

    @RequestMapping(value = "/pulse",method = RequestMethod.POST)
    public ResponseEntity<String> sendPulseCmd(@RequestBody String data, @RequestParam(required = false,defaultValue = "5000") Integer retryPeriod,
                                @RequestParam(required = false,defaultValue = "3") Integer retryCount, HttpServletRequest request) {
        DataModel dataModel = JSON.parseObject(data,DataModel.class);
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        int count = cmdControlService.sendPulseCmd(dataModel, retryPeriod, retryCount, requestId);

        return new ResponseEntity<>(
                ServerResponse.buildOkJson(count)
                , HttpStatus.OK);
    }
}
