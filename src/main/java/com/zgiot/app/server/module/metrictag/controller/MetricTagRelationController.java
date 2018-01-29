package com.zgiot.app.server.module.metrictag.controller;

import com.zgiot.app.server.module.metrictag.pojo.MetricTagRelation;
import com.zgiot.app.server.module.util.ValidateParamUtil;
import com.zgiot.app.server.module.util.validate.AddValidate;
import com.zgiot.app.server.module.util.validate.DeleteValidate;
import com.zgiot.app.server.module.util.validate.GetValidate;
import com.zgiot.app.server.module.util.validate.UpdateValidate;
import com.zgiot.app.server.service.MetricTagRelationService;
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
@RequestMapping("/metricTagRelation")
public class MetricTagRelationController {

    @Autowired
    private MetricTagRelationService metricTagRelationService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> findMetricTagRelation(
            @RequestBody @Validated(value = GetValidate.class) MetricTagRelation metricTagRelation, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        List<MetricTagRelation> metricTagRelations = metricTagRelationService.findMetricTagRelation(metricTagRelation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricTagRelations), HttpStatus.OK);
    }

    @RequestMapping(value = "/{metricTagRelationId}", method = RequestMethod.GET)
    public ResponseEntity<String> getMetricTag(
            @PathVariable("metricTagRelationId") Integer metricTagRelationId){
        MetricTagRelation metricTagRelation = metricTagRelationService.getMetricTagRelation(metricTagRelationId);
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricTagRelation), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> addMetricTagRelation(
            @RequestBody @Validated(value = AddValidate.class) MetricTagRelation metricTagRelation, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagRelationService.addMetricTagRelation(metricTagRelation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<String> updateMetricTagRelation(
            @RequestBody @Validated(value = UpdateValidate.class) MetricTagRelation metricTagRelation, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagRelationService.updateMetricTagRelation(metricTagRelation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMetricTagRelation(
            @RequestBody @Validated(value = DeleteValidate.class) MetricTagRelation metricTagRelation, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagRelationService.deleteMetricTagRelation(metricTagRelation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }
}
