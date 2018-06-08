package com.zgiot.app.server.module.sfstop.controller;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceArea;
import com.zgiot.app.server.module.sfstop.service.StopDeviceAreaService;
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
 * 智能停车区域
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfstopconfig")
public class StopDeviceAreaController {

    @Autowired
    private StopDeviceAreaService stopDeviceAreaService;

    /**
     * 根据大区id查询所有的设备区域(分页)
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStopDeviceAreaPage/{pageNum}/{pageSize}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopDeviceAreaPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @RequestBody StopDeviceArea stopDeviceArea) {
        PageListRsp pageListRsp=stopDeviceAreaService.getStopDeviceAreaPage(pageNum,pageSize,stopDeviceArea.getRegionId());
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageListRsp), HttpStatus.OK);
    }

    /**
     * 根据大区id查询所有的设备区域
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStopDeviceArea", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopDeviceArea(@RequestBody StopDeviceArea stopDeviceArea) {
        List<StopDeviceArea> stopDeviceAreaList=stopDeviceAreaService.getStopDeviceArea(stopDeviceArea.getRegionId());
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopDeviceAreaList), HttpStatus.OK);
    }

    /**
     * 保存设备区域
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addStopDeviceArea", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addStopDeviceArea(@RequestBody StopDeviceArea stopDeviceArea) {
        String serverResponse=stopDeviceAreaService.validateStopDeviceAreaName(stopDeviceArea);
        if(serverResponse!=null){
            return new ResponseEntity<>(serverResponse, HttpStatus.OK);
        }
        stopDeviceAreaService.addStopDeviceArea(stopDeviceArea);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 更新设备区域
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateStopDeviceArea", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateStopDeviceArea(@RequestBody StopDeviceArea stopDeviceArea) {
        String serverResponse=stopDeviceAreaService.validateStopDeviceAreaName(stopDeviceArea);
        if(serverResponse!=null){
            return new ResponseEntity<>(serverResponse, HttpStatus.OK);
        }
        stopDeviceAreaService.updateStopDeviceArea(stopDeviceArea);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 删除设备区域
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteStopDeviceArea", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteStopDeviceArea(@RequestBody StopDeviceArea stopDeviceArea) {
        String serverResponse=stopDeviceAreaService.checkAreaDel(stopDeviceArea);
        if(serverResponse!=null){
            return new ResponseEntity<>(serverResponse, HttpStatus.OK);
        }
        stopDeviceAreaService.deleteStopDeviceArea(stopDeviceArea);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }


}
