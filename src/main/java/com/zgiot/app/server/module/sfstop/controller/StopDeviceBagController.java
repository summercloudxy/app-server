package com.zgiot.app.server.module.sfstop.controller;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceBag;
import com.zgiot.app.server.module.sfstop.service.StopDeviceBagService;
import com.zgiot.app.server.module.sfstop.service.StopInformationService;
import com.zgiot.app.server.module.sfstop.util.PageListRsp;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 停车包
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfstopconfig")
public class StopDeviceBagController {

    @Autowired
    private StopDeviceBagService stopDeviceBagService;

    @Autowired
    private StopInformationService stopInformationService;

    /**
     * 根据区域id查询停车包分页
     *
     * @param pageNum
     * @param pageSize
     * @param stopDeviceBag
     * @return
     */
    @RequestMapping(value = "/getStopDeviceBagByAreaPage/{pageNum}/{pageSize}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopDeviceBagByAreaPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @RequestBody StopDeviceBag stopDeviceBag) {
        PageListRsp pageListRsp = stopDeviceBagService.getStopDeviceBagByAreaPage(pageNum,pageSize,stopDeviceBag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageListRsp), HttpStatus.OK);
    }

    /**
     * 根据区域id查询停车包
     *
     * @param stopDeviceBag
     * @return
     */
    @RequestMapping(value = "/getStopDeviceBagByArea", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopDeviceBagByArea(@RequestBody StopDeviceBag stopDeviceBag) {
        List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByArea(stopDeviceBag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopDeviceBags), HttpStatus.OK);
    }

    /**
     * 根据停车线id查询停车包
     *
     * @param pageNum
     * @param pageSize
     * @param stopDeviceBag
     * @return
     */
    @RequestMapping(value = "/getStopDeviceBagByLinePage/{pageNum}/{pageSize}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopDeviceBagByLinePage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @RequestBody StopDeviceBag stopDeviceBag) {
        PageListRsp pageListRsp= stopDeviceBagService.getStopDeviceBagByLinePage(pageNum,pageSize,stopDeviceBag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageListRsp), HttpStatus.OK);
    }

    /**
     * 根据停车线id查询停车包
     *
     * @param stopDeviceBag
     * @return
     */
    @RequestMapping(value = "/getStopDeviceBagByLine", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopDeviceBagByLine(@RequestBody StopDeviceBag stopDeviceBag) {
        List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByLine(stopDeviceBag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopDeviceBags), HttpStatus.OK);
    }

    /**
     * 新增停车包
     *
     * @param stopDeviceBag
     * @return
     */
    @RequestMapping(value = "/addStopDeviceBag", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addStopDeviceBag(@RequestBody StopDeviceBag stopDeviceBag) {
        String serverResponse = stopDeviceBagService.checkAreaHaveBagName(stopDeviceBag);
        if(serverResponse!=null){
            return new ResponseEntity<>(serverResponse, HttpStatus.OK);
        }
        stopDeviceBagService.addStopDeviceBag(stopDeviceBag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 编辑停车包
     *
     * @param stopDeviceBag
     * @return
     */
    @RequestMapping(value = "/updateStopDeviceBag", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateStopDeviceBag(@RequestBody StopDeviceBag stopDeviceBag) {
        String serverResponse = stopDeviceBagService.checkAreaHaveBagName(stopDeviceBag);
        if(serverResponse!=null){
            return new ResponseEntity<>(serverResponse, HttpStatus.OK);
        }
        stopDeviceBagService.updateStopDeviceBag(stopDeviceBag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 物理删除停车包
     *
     * @param stopDeviceBag
     * @return
     */
    @RequestMapping(value = "/deleteStopDeviceBag", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteStopDeviceBag(@RequestBody StopDeviceBag stopDeviceBag) {
        String serverResponse=stopInformationService.checkBagHaveInformation(stopDeviceBag);
        if(serverResponse!=null){
            return new ResponseEntity<>(serverResponse, HttpStatus.OK);
        }
        stopDeviceBagService.deleteStopDeviceBag(stopDeviceBag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @ApiOperation("根据包id查询设备数量")
    @RequestMapping(value = "/getInformationCountById", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getInformationCountById(@RequestParam Long bagId) {
        StopDeviceBag stopDeviceBag=stopDeviceBagService.getInformationCountById(bagId);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopDeviceBag), HttpStatus.OK);
    }

}
