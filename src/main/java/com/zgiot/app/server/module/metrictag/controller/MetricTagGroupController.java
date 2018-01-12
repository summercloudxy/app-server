package com.zgiot.app.server.module.metrictag.controller;

import com.zgiot.app.server.module.metrictag.pojo.*;
import com.zgiot.app.server.module.util.ValidateParamUtil;
import com.zgiot.app.server.service.MetricTagGroupService;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Controller
@RequestMapping("/metricTagGroupGroup")
public class MetricTagGroupController {
    @Autowired
    private MetricTagGroupService metricTagGroupService;

    @RequestMapping(value = "/metricTagGroup", method = RequestMethod.GET)
    public ResponseEntity<String> getMetricTagGroup(
            @RequestBody @Validated() MetricTagGroup metricTagGroup, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        List<MetricTagGroup> metricTagGroups = metricTagGroupService.getMetricTagGroup(metricTagGroup);
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricTagGroups), HttpStatus.OK);
    }

    @RequestMapping(value = "/metricTagGroup", method = RequestMethod.POST)
    public ResponseEntity<String> addMetricTagGroup(
            @RequestBody @Validated() MetricTagGroup metricTagGroup, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagGroupService.addMetricTagGroup(metricTagGroup);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/metricTagGroup", method = RequestMethod.PUT)
    public ResponseEntity<String> updateMetricTagGroup(
            @RequestBody @Validated() MetricTagGroup metricTagGroup, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagGroupService.updateMetricTagGroup(metricTagGroup);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/metricTagGroup", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMetricTagGroup(
            @RequestBody @Validated() MetricTagGroup metricTagGroup, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagGroupService.getMetricTagGroup(metricTagGroup);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }
}
