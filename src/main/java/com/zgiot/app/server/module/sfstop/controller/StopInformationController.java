package com.zgiot.app.server.module.sfstop.controller;


import com.zgiot.app.server.module.sfstop.entity.pojo.StopInformation;
import com.zgiot.app.server.module.sfstop.service.StopInformationService;
import com.zgiot.app.server.module.sfstop.util.PageListRsp;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 智能停车设备Controller
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfstopconfig")
public class StopInformationController {

    @Autowired
    private StopInformationService stopInformationService;

    /**
     * 查询所有的设备未关联包(分页)
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStopInformationPageByNoBagId/{pageNum}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopInformationPageByNoBagId(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        PageListRsp pageListRsp = stopInformationService.getStopInformationPageByNoBagId(pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageListRsp), HttpStatus.OK);
    }


    /**
     * 条件查询所有设备未关联包
     *
     * @return
     */
    @RequestMapping(value = "/getStopInformationByNoBagId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopInformationByNoBagId() {
        List<StopInformation> stopInformationList = stopInformationService.getStopInformationByNoBagId();
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopInformationList), HttpStatus.OK);
    }

    /**
     * 查询所有的设备(分页)
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStopInformationPage/{pageNum}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopInformationPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        PageListRsp pageListRsp = stopInformationService.getStopInformationPage(pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageListRsp), HttpStatus.OK);
    }

    /**
     * 查询所有设备
     *
     * @return
     */
    @RequestMapping(value = "/getStopInformationAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopInformationAll() {
        List<StopInformation> stopInformationList = stopInformationService.getStopInformationAll();
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopInformationList), HttpStatus.OK);
    }

    /**
     * 根据包id获取设备
     */
    @RequestMapping(value = "/getStopInformationByBagId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopInformationByBagId(@RequestBody StopInformation stopInformation) {
        List<StopInformation> stopInformationList = stopInformationService.getStopInformationByBagId(stopInformation.getBagId());
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopInformationList), HttpStatus.OK);
    }

    /**
     * 根据包id获取设备(分页)
     */
    @RequestMapping(value = "/getStopInformationPageByBagId/{pageNum}/{pageSize}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopInformationPageByBagId(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @RequestBody StopInformation stopInformation) {
        PageListRsp pageListRsp = stopInformationService.getStopInformationPageByBagId(pageNum,pageSize,stopInformation.getBagId());
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageListRsp), HttpStatus.OK);
    }

    /**
     * 更改设备信息
     */
    @RequestMapping(value = "/updateStopInformation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateStopInformation(@RequestBody StopInformation stopInformation) {
        stopInformationService.updateStopInformation(stopInformation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 根据thingCode查询设备信息
     */
    @RequestMapping(value = "/getStopInformationByTC", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopInformationByTC(@RequestBody StopInformation stopInformation) {
        StopInformation stopInformationByTC = stopInformationService.getStopInformationByTC(stopInformation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopInformationByTC), HttpStatus.OK);
    }

    /**
     * 根据thingCode解除设备和包的关联
     */
    @RequestMapping(value = "/relieveStopInformationAndBag", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> relieveStopInformationAndBag(@RequestParam String thingCode) {
        stopInformationService.relieveStopInformationAndBag(thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }


    /**
     * 根据thingCode和设备名称模糊查询未关联包设备信息
     */
    @RequestMapping(value = "/getInformationByTCAndNameNoBagId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getInformationByTCAndNameNoBagId(@RequestParam String string) {
        List<StopInformation> stopInformationList = stopInformationService.getInformationByTCAndNameNoBagId(string);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopInformationList), HttpStatus.OK);
    }


    /**
     * 根据thingCode和设备名称模糊查询设备信息
     */
    @RequestMapping(value = "/getInformationByTCAndName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getInformationByTCAndName(@RequestParam String string) {
        List<StopInformation> stopInformationList = stopInformationService.getInformationByTCAndName(string);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopInformationList), HttpStatus.OK);
    }

}
