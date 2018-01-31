package com.zgiot.app.server.module.metrictag.controller;

import com.zgiot.app.server.module.metrictag.pojo.MetricTag;
import com.zgiot.app.server.module.util.ValidateParamUtil;
import com.zgiot.app.server.module.util.validate.DeleteValidate;
import com.zgiot.app.server.service.MetricTagService;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Controller
@RequestMapping("/metricTag")
public class MetricTagController{

    @Autowired
    private MetricTagService metricTagService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getMetricTag(
            @RequestBody @Validated() MetricTag metricTag, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        List<MetricTag> metricTags = metricTagService.findMetricTag(metricTag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricTags), HttpStatus.OK);
    }

    @RequestMapping(value = "/{metricTagId}/{metricTagCode}", method = RequestMethod.GET)
    public ResponseEntity<String> getMetricTag(
            @PathVariable("metricTagId") Integer metricTagId, @PathVariable("metricTagCode") String metricTagCode){
        MetricTag metricTag = metricTagService.getMetricTag(metricTagId, metricTagCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricTag), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> addMetricTag(
            @RequestBody @Validated() MetricTag metricTag, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagService.addMetricTag(metricTag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<String> updateMetricTag(
            @RequestBody @Validated(value = DeleteValidate.class) MetricTag metricTag, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagService.updateMetricTag(metricTag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMetricTag(
            @RequestBody @Validated() MetricTag metricTag, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagService.deleteMetricTag(metricTag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }
}
