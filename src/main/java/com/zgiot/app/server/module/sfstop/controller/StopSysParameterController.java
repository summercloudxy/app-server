package com.zgiot.app.server.module.sfstop.controller;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopSysParameter;
import com.zgiot.app.server.module.sfstop.service.StopSysParameterService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 停车系统参数设置
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfstopconfig")
public class StopSysParameterController {

    @Autowired
    private StopSysParameterService stopSysParameterService;

    /**
     * 获取所有系统参数
     *
     * @return
     */
    @RequestMapping(value = "/getStopSysParameterList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopSysParameterList() {
        List<StopSysParameter> stopSysParameterList = stopSysParameterService.getStopSysParameterList();
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopSysParameterList), HttpStatus.OK);
    }
}
