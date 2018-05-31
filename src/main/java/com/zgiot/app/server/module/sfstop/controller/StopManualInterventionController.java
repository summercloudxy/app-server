package com.zgiot.app.server.module.sfstop.controller;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopManualIntervention;
import com.zgiot.app.server.module.sfstop.service.StopManualInterventionService;
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

/**
 * 停车人工干预
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfstopconfig")
public class StopManualInterventionController {

    @Autowired
    private StopManualInterventionService stopManualInterventionService;

    /**
     * 根据设备编码查询是否有人工干预
     *
     * @param stopManualIntervention
     * @return
     */
    @RequestMapping(value = "/getStopManualInterventionByTC", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStopManualInterventionByTC(@RequestBody StopManualIntervention stopManualIntervention) {
        StopManualIntervention stopManualInterventionByThingCode = stopManualInterventionService.getStopManualInterventionByThingCode(stopManualIntervention.getThingCode());
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopManualInterventionByThingCode), HttpStatus.OK);
    }

    /**
     * 根据设备编码更新人工干预状态
     *
     * @param stopManualIntervention
     * @return
     */
    @RequestMapping(value = "/updateStopManualInterventionByTC", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateStopManualInterventionByTC(@RequestBody StopManualIntervention stopManualIntervention) {
        stopManualInterventionService.updateStopManualInterventionByTC(stopManualIntervention);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }
}
