package com.zgiot.app.server.module.sfstop.controller;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetPararmeter;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetPararmeterDTO;
import com.zgiot.app.server.module.sfstop.service.StopTypeSetPararmeterService;
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
 * 停车设备参数
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfstopconfig")
public class StopTypeSetPararmeterController {

    @Autowired
    private StopTypeSetPararmeterService stopTypeSetPararmeterService;

    /**
     * 根据thingCode获取停车设备参数非检修
     *
     * @param stopTypeSetPararmeter
     * @return
     */
    @RequestMapping(value = "/getPararmeterNoChecked", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPararmeterNoChecked(@RequestBody StopTypeSetPararmeter stopTypeSetPararmeter) {
        List<StopTypeSetPararmeter> stopTypeSetPararmeterList=stopTypeSetPararmeterService.getPararmeterNoChecked(stopTypeSetPararmeter);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopTypeSetPararmeterList), HttpStatus.OK);
    }

    /**
     * 根据thingCode获取停车设备参数检修
     *
     * @param stopTypeSetPararmeter
     * @return
     */
    @RequestMapping(value = "/getPararmeterChecked", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPararmeterChecked(@RequestBody StopTypeSetPararmeter stopTypeSetPararmeter) {
        List<StopTypeSetPararmeter> stopTypeSetPararmeterList=stopTypeSetPararmeterService.getPararmeterChecked(stopTypeSetPararmeter);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopTypeSetPararmeterList), HttpStatus.OK);
    }

    /**
     * 根据thingCode更新设备参数非检修
     *
     * @param stopTypeSetPararmeterDTO
     * @return
     */
    @RequestMapping(value = "/updatePararmeterNoChecked", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePararmeterNoChecked(@RequestBody StopTypeSetPararmeterDTO stopTypeSetPararmeterDTO) {
        stopTypeSetPararmeterService.updatePararmeterNoChecked(stopTypeSetPararmeterDTO);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 根据thingCode更新设备参数检修
     *
     * @param stopTypeSetPararmeterDTO
     * @return
     */
    @RequestMapping(value = "/updatePararmeterChecked", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePararmeterChecked(@RequestBody StopTypeSetPararmeterDTO stopTypeSetPararmeterDTO) {
        stopTypeSetPararmeterService.updatePararmeterChecked(stopTypeSetPararmeterDTO);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

}
