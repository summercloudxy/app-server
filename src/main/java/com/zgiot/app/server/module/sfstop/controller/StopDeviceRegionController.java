package com.zgiot.app.server.module.sfstop.controller;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceRegion;
import com.zgiot.app.server.module.sfstop.service.StopDeviceAreaService;
import com.zgiot.app.server.module.sfstop.service.StopDeviceRegionService;
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
 * 智能停车大区
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfstopconfig")
public class StopDeviceRegionController {

    @Autowired
    private StopDeviceRegionService stopDeviceRegionService;
    @Autowired
    private StopDeviceAreaService stopDeviceAreaService;

    /**
     * 查询所有的设备大区(分页)
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询所有设备大区(分页)")
    @RequestMapping(value = "/getStopDeviceRegionPage/{pageNum}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopDeviceRegionPageTurn(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        PageListRsp pageListRsp = stopDeviceRegionService.getStopDeviceRegionPageTurn(pageNum, pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageListRsp), HttpStatus.OK);
    }

    /**
     * 查询所有的设备大区
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStopDeviceRegion", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopDeviceRegion() {
        List<StopDeviceRegion> stopDeviceRegionList = stopDeviceRegionService.getStopDeviceRegion();
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopDeviceRegionList), HttpStatus.OK);
    }

    /**
     * 添加设备大区
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addStopDeviceRegion", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addStopDeviceRegion(@RequestBody StopDeviceRegion stopDeviceRegion) {
        String serverResponse = stopDeviceRegionService.validateStopDeviceRegionName(stopDeviceRegion);
        if (serverResponse != null) {
            return new ResponseEntity<>(serverResponse, HttpStatus.OK);
        }
        stopDeviceRegionService.addStopDeviceRegion(stopDeviceRegion);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 更新设备大区
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateStopDeviceRegion", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateStopDeviceRegion(@RequestBody StopDeviceRegion stopDeviceRegion) {
        String serverResponse = stopDeviceRegionService.validateStopDeviceRegionName(stopDeviceRegion);
        if (serverResponse != null) {
            return new ResponseEntity<>(serverResponse, HttpStatus.OK);
        }
        stopDeviceRegionService.updateStopDeviceRegion(stopDeviceRegion);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 删除设备大区
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteStopDeviceRegion", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteStopDeviceRegion(@RequestBody StopDeviceRegion stopDeviceRegion) {
        String serverResponse = stopDeviceAreaService.checkRegionHaveArea(stopDeviceRegion);
        if (serverResponse != null) {
            return new ResponseEntity<>(serverResponse, HttpStatus.OK);
        }
        stopDeviceRegionService.deleteStopDeviceRegion(stopDeviceRegion);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

}
