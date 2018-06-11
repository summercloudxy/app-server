package com.zgiot.app.server.module.sfstop.controller;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetDelay;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetDelayDTO;
import com.zgiot.app.server.module.sfstop.service.StopTypeSetDelayService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 停车设备延时
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfstopconfig")
public class StopTypeSetDelayController {

    @Autowired
    private StopTypeSetDelayService stopTypeSetDelayService;

    /**
     * 根据设备编码查询所有非检修模式的延迟时间
     *
     * @param stopTypeSetDelay
     * @return
     */
    @RequestMapping(value = "/getStopTypeSetDelayByTCNoChecked", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopTypeSetDelayByTCNoChecked(@RequestBody StopTypeSetDelay stopTypeSetDelay) {
        List<StopTypeSetDelay> stopTypeSetDelayList = stopTypeSetDelayService.getStopTypeSetDelayByTCNoChecked(stopTypeSetDelay);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopTypeSetDelayList), HttpStatus.OK);
    }

    /**
     * 根据设备编码查询所有检修模式的延迟时间
     *
     * @param stopTypeSetDelay
     * @return
     */
    @RequestMapping(value = "/getStopTypeSetDelayByTCNIsChecked", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopTypeSetDelayByTCNIsChecked(@RequestBody StopTypeSetDelay stopTypeSetDelay) {
        List<StopTypeSetDelay> stopTypeSetDelayList = stopTypeSetDelayService.getStopTypeSetDelayByTCNIsChecked(stopTypeSetDelay);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopTypeSetDelayList), HttpStatus.OK);
    }

    /**
     * 更新thingCode对应的延迟时间非检修
     *
     * @param stopTypeSetDelayDTO
     * @return
     */
    @RequestMapping(value = "/updateDelayByCodeNoChecked", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateDelayByCodeNoChecked(@RequestBody StopTypeSetDelayDTO stopTypeSetDelayDTO) {
        stopTypeSetDelayService.updateDelayByCodeNoChecked(stopTypeSetDelayDTO);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 更新thingCode对应的延迟时间检修
     *
     * @param stopTypeSetDelayDTO
     * @return
     */
    @RequestMapping(value = "/updateDelayByCodeChecked", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateDelayByCodeChecked(@RequestBody StopTypeSetDelayDTO stopTypeSetDelayDTO) {
        stopTypeSetDelayService.updateDelayByCodeChecked(stopTypeSetDelayDTO);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }


}
