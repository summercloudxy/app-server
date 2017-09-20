package com.zgiot.app.server.module.generic;

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
    public ResponseEntity<String> sendCmd(@RequestBody DataModel dataModel, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        int count = cmdControlService.sendCmd(dataModel, requestId);

        return new ResponseEntity<>(
                ServerResponse.buildOkJson(count)
                , HttpStatus.OK);
    }

    @PostMapping("/pulse")
    public ResponseEntity<String> sendPulseCmd(@RequestParam DataModel dataModel, @RequestParam(required = false) Integer retryPeriod,
                                @RequestParam(required = false) Integer retryCount, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        int count = cmdControlService.sendPulseCmd(dataModel, retryPeriod, retryCount, requestId);

        return new ResponseEntity<>(
                ServerResponse.buildOkJson(count)
                , HttpStatus.OK);
    }
}
