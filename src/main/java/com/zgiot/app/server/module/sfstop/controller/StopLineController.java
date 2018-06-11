package com.zgiot.app.server.module.sfstop.controller;


import com.zgiot.app.server.module.sfstop.entity.pojo.StopLine;
import com.zgiot.app.server.module.sfstop.service.StopDeviceBagService;
import com.zgiot.app.server.module.sfstop.service.StopLineService;
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
 * 停车线
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfstopconfig")
public class StopLineController {

    @Autowired
    private StopLineService stopLineService;

    @Autowired
    private StopDeviceBagService stopDeviceBagService;

    /**
     * 根据区域id查询所有的停车线
     *
     * @param stopLine
     * @return
     */
    @RequestMapping(value = "/getStopLine", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopLine(@RequestBody StopLine stopLine) {
        List<StopLine> stopLineList=stopLineService.getStopLine(stopLine);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopLineList), HttpStatus.OK);
    }

    /**
     * 添加停车线
     *
     * @param stopLine
     * @return
     */
    @RequestMapping(value = "/addStopLine", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addStopLine(@RequestBody StopLine stopLine) {
        String serverResponse=stopLineService.validateStopLineName(stopLine);
        if(serverResponse!=null){
            return new ResponseEntity<>(serverResponse, HttpStatus.OK);
        }
        stopLineService.addStopLine(stopLine);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 编辑停车线
     *
     * @param stopLine
     * @return
     */
    @RequestMapping(value = "/updateStopLine", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateStopLine(@RequestBody StopLine stopLine) {
        String serverResponse = stopLineService.validateStopLineName(stopLine);
        if(serverResponse!=null){
            return new ResponseEntity<>(serverResponse, HttpStatus.OK);
        }
        stopLineService.updateStopLine(stopLine);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 物理删除停车线
     *
     * @param stopLine
     * @return
     */
    @RequestMapping(value = "/deleteStopLine", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteStopLine(@RequestBody StopLine stopLine) {
        String serverResponse=stopDeviceBagService.checkLineHaveBag(stopLine.getId());
        if(serverResponse!=null){
            return new ResponseEntity<>(serverResponse, HttpStatus.OK);
        }
        stopLineService.deleteStopLine(stopLine);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }
}
