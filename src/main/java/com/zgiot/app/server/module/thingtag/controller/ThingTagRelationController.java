package com.zgiot.app.server.module.thingtag.controller;

import com.zgiot.app.server.module.thingtag.pojo.ThingTagGroup;
import com.zgiot.app.server.module.thingtag.pojo.ThingTagRelation;
import com.zgiot.app.server.module.util.ValidateParamUtil;
import com.zgiot.app.server.module.util.validate.AddValidate;
import com.zgiot.app.server.module.util.validate.DeleteValidate;
import com.zgiot.app.server.module.util.validate.GetValidate;
import com.zgiot.app.server.module.util.validate.UpdateValidate;
import com.zgiot.app.server.service.ThingTagGroupService;
import com.zgiot.app.server.service.ThingTagRelationService;
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
@RequestMapping("/thingTagRelation")
public class ThingTagRelationController {

    @Autowired
    private ThingTagRelationService thingTagRelationService;
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> findThingTagRelation(
            @RequestBody @Validated(value = GetValidate.class) ThingTagRelation thingTagRelation, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        List<ThingTagRelation> thingTagRelationLists = thingTagRelationService.findThingTagRelation(thingTagRelation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingTagRelationLists), HttpStatus.OK);
    }

    @RequestMapping(value = "/{thingTagRelationId}", method = RequestMethod.GET)
    public ResponseEntity<String> getThingTagGroup(
            @PathVariable("thingTagRelationId")Integer thingTagRelationId){
        ThingTagRelation thingTagRelation = thingTagRelationService.getThingTagRelation(thingTagRelationId);
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingTagRelation), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> addThingTagRelation(
            @RequestBody @Validated(value = AddValidate.class) ThingTagRelation thingTagRelation, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        thingTagRelationService.addThingTagRelation(thingTagRelation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<String> updateThingTagRelation(
            @RequestBody @Validated(value = UpdateValidate.class) ThingTagRelation thingTagRelation, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        thingTagRelationService.updateThingTagRelation(thingTagRelation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteThingTagRelation(
            @RequestBody @Validated(value = DeleteValidate.class) ThingTagRelation thingTagRelation, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        thingTagRelationService.deleteThingTagRelation(thingTagRelation);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }
}
