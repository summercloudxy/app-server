package com.zgiot.app.server.module.metrictag.controller;

import com.zgiot.app.server.module.metrictag.pojo.MetricTag;
import com.zgiot.app.server.module.util.ValidateParamUtil;
import com.zgiot.app.server.service.MetricTagService;
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

/**
 * Created by wangfan on 2018/1/8.
 */
@Controller
public class MetricTagController{

    @Autowired
    private MetricTagService metricTagService;

    @RequestMapping(value = "/metricTag", method = RequestMethod.GET)
    public ResponseEntity<String> getMetricTag(
            @RequestBody @Validated() MetricTag metricTag, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagService.getMetricTag(metricTag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/metricTag", method = RequestMethod.POST)
    public ResponseEntity<String> addMetricTag(
            @RequestBody @Validated() MetricTag metricTag, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagService.addMetricTag(metricTag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/metricTag", method = RequestMethod.PUT)
    public ResponseEntity<String> updateMetricTag(
            @RequestBody @Validated() MetricTag metricTag, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagService.updateMetricTag(metricTag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/metricTag", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMetricTag(
            @RequestBody @Validated() MetricTag metricTag, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagService.getMetricTag(metricTag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }
}
