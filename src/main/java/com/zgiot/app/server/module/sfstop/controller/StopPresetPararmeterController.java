package com.zgiot.app.server.module.sfstop.controller;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopPresetPararmeter;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopPresetPararmeterDTO;
import com.zgiot.app.server.module.sfstop.service.StopPresetPararmeterService;
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
 * 停车预设参数
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfstopconfig")
public class StopPresetPararmeterController {

    @Autowired
    private StopPresetPararmeterService stopPresetPararmeterService;

    /**
     * 根据设备编码查询所有预设参数
     *
     * @param stopPresetPararmeter
     * @return
     */
    @RequestMapping(value = "/getStopPresetPararmeterByTC", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopPresetPararmeterByTC(@RequestBody StopPresetPararmeter stopPresetPararmeter) {
        List<StopPresetPararmeter> stopPresetPararmeterList=stopPresetPararmeterService.getStopPresetPararmeterByTC(stopPresetPararmeter);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopPresetPararmeterList), HttpStatus.OK);
    }

    /**
     * 根据设备编码编辑预设参数
     *
     * @param stopPresetPararmeterDTO
     * @return
     */
    @RequestMapping(value = "/updateStopPresetPararmeterByTC", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateStopPresetPararmeterByTC(@RequestBody StopPresetPararmeterDTO stopPresetPararmeterDTO) {
        stopPresetPararmeterService.updateStopPresetPararmeterByTC(stopPresetPararmeterDTO);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

}
