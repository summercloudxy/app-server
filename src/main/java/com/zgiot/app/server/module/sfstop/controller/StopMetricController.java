package com.zgiot.app.server.module.sfstop.controller;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopMetric;
import com.zgiot.app.server.module.sfstop.service.StopMetricService;
import com.zgiot.app.server.module.sfstop.util.PageListRsp;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfstopconfig")
public class StopMetricController {


    @Autowired
    private StopMetricService stopMetricService;

    /**
     * 查询所有的PLC信号(分页)
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStopMetricPage/{pageNum}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopMetricPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        PageListRsp pageListRsp = stopMetricService.getStopInformationPage(pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageListRsp), HttpStatus.OK);
    }

    /**
     * 查询所有的PLC信号
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStopMetricList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopMetricList() {
        List<StopMetric> stopMetricList = stopMetricService.getStopMetricList();
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopMetricList), HttpStatus.OK);
    }

    /**
     * 根据设备编码模糊查询所有PLC信号
     *
     * @return
     */
    @RequestMapping(value = "/getStopMetricByTCAndString", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopMetricByTCAndString(@RequestParam String string) {
        List<StopMetric> stopMetricList = stopMetricService.getStopMetricListByTCAndString(string);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopMetricList), HttpStatus.OK);
    }


}
