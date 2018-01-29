package com.zgiot.app.server.module.metrictag.controller;

import com.zgiot.app.server.module.metrictag.pojo.*;
import com.zgiot.app.server.module.thingtag.pojo.ThingTag;
import com.zgiot.app.server.module.util.ValidateParamUtil;
import com.zgiot.app.server.module.util.validate.AddValidate;
import com.zgiot.app.server.module.util.validate.DeleteValidate;
import com.zgiot.app.server.module.util.validate.GetValidate;
import com.zgiot.app.server.module.util.validate.UpdateValidate;
import com.zgiot.app.server.service.MetricTagGroupService;
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
@RequestMapping("/metricTagGroup")
public class MetricTagGroupController {

    @Autowired
    private MetricTagGroupService metricTagGroupService;

    @Autowired
    private MetricTagService metricTagService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getMetricTagGroup(
            @RequestBody @Validated(value = GetValidate.class) MetricTagGroup metricTagGroup, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        List<MetricTagGroup> metricTagGroups = metricTagGroupService.findMetricTagGroup(metricTagGroup);
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricTagGroups), HttpStatus.OK);
    }

    @RequestMapping(value = "/{metricTagGroupId}/{metricTagGroupCode}", method = RequestMethod.GET)
    public ResponseEntity<String> getMetricTag(
            @PathVariable("metricTagGroupId") Integer metricTagGroupId, @PathVariable("metricTagGroupCode") String metricTagGroupCode){
        MetricTagGroup metricTagGroup = metricTagGroupService.getMetricTagGroup(metricTagGroupId, metricTagGroupCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricTagGroup), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> addMetricTagGroup(
            @RequestBody @Validated(value = AddValidate.class) MetricTagGroup metricTagGroup, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagGroupService.addMetricTagGroup(metricTagGroup);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<String> updateMetricTagGroup(
            @RequestBody @Validated(value = UpdateValidate.class) MetricTagGroup metricTagGroup, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagGroupService.updateMetricTagGroup(metricTagGroup);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMetricTagGroup(
            @RequestBody @Validated(value = DeleteValidate.class) MetricTagGroup metricTagGroup, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        MetricTag metricTag = new MetricTag();
        metricTag.setMetricTagGroupId(metricTagGroup.getMetricTagGroupId());
        List<MetricTag> thingTags = metricTagService.findMetricTag(metricTag);
        if(thingTags.isEmpty()){
            return new ResponseEntity<>(ServerResponse.buildOkJson("此tagGroup下存在标签"),
                    HttpStatus.BAD_REQUEST);
        }
        metricTagGroupService.deleteMetricTagGroup(metricTagGroup);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }
}
