package com.zgiot.app.server.module.generic;

import com.alibaba.fastjson.JSON;
import com.sun.javafx.binding.StringFormatter;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.SysexMessage;
import java.util.List;

@RestController
@RequestMapping(value = "/cmd",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
        , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CmdController {
    @Autowired
    private CmdControlService cmdControlService;

    @PostMapping(value = "")
    public ResponseEntity<String> sendCmd(@RequestBody String reqBody, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        DataModel dataModel = JSON.parseObject(reqBody.trim(), DataModel.class);

        if (StringUtils.isBlank(reqBody) || dataModel.getThingCode() == null
                || dataModel.getMetricCode() == null) {
            ServerResponse res = new ServerResponse<>(
                    "Not valid request data. The incoming req body is: `" + reqBody + "`", SysException.EC_UNKOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }

        /* do send */
        CmdControlService.CmdSendResponseData res = cmdControlService.sendCmd(dataModel, requestId);

        return new ResponseEntity<>(ServerResponse.buildJson(
                res.getErrorMessage()
                , SysException.EC_SUCCESS, res.getOkCount())
                , HttpStatus.OK);

    }

    @PostMapping("/pulse")
    public ResponseEntity<String> sendPulseCmd(@RequestParam DataModel dataModel, @RequestParam(required = false) Integer retryPeriod,
                                               @RequestParam(required = false) Integer retryCount, HttpServletRequest request) {
        // todo fix body like last method
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        int count = cmdControlService.sendPulseCmd(dataModel, retryPeriod, retryCount, requestId);

        return new ResponseEntity<>(
                ServerResponse.buildOkJson(count)
                , HttpStatus.OK);
    }
}