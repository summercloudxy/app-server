package com.zgiot.app.server.module.metrictag.controller;

import com.zgiot.app.server.module.metrictag.pojo.MetricTagRelation;
import com.zgiot.app.server.module.util.ValidateParamUtil;
import com.zgiot.app.server.service.MetricTagRelationService;
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
public class MetricTagRelationController {

    @Autowired
    private MetricTagRelationService metricTagRelationService;

    @RequestMapping(value = "/metricTagRelation", method = RequestMethod.GET)
    public ResponseEntity<String> getMetricTagRelation(
            @RequestBody @Validated() MetricTagRelation metricTagRelation, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        List<MetricTagRelation> metricTagRelations = metricTagRelationService.findMetricTagRelation(metricTagRelation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricTagRelations), HttpStatus.OK);
    }

    @RequestMapping(value = "/metricTagRelation", method = RequestMethod.POST)
    public ResponseEntity<String> addMetricTagRelation(
            @RequestBody @Validated() MetricTagRelation metricTagRelation, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagRelationService.addMetricTagRelation(metricTagRelation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/metricTagRelation", method = RequestMethod.PUT)
    public ResponseEntity<String> updateMetricTagRelation(
            @RequestBody @Validated() MetricTagRelation metricTagRelation, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagRelationService.updateMetricTagRelation(metricTagRelation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/metricTagRelation", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMetricTagRelation(
            @RequestBody @Validated() MetricTagRelation metricTagRelation, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagRelationService.findMetricTagRelation(metricTagRelation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }
}
