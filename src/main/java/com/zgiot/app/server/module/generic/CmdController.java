package com.zgiot.app.server.module.generic;

import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.pojo.DataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cmd")
public class CmdController {
    @Autowired
    private CmdControlService cmdControlService;

    @PostMapping("")
    public Integer sendCmd(@RequestBody DataModel dataModel, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        return cmdControlService.sendCmd(dataModel, requestId);
    }
}
