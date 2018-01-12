package com.zgiot.app.server.module.thingtag.controller;

import com.zgiot.app.server.module.thingtag.pojo.ThingTagGroup;
import com.zgiot.app.server.module.util.ValidateParamUtil;
import com.zgiot.app.server.module.util.validate.AddValidate;
import com.zgiot.app.server.module.util.validate.DeleteValidate;
import com.zgiot.app.server.module.util.validate.GetValidate;
import com.zgiot.app.server.module.util.validate.UpdateValidate;
import com.zgiot.app.server.service.ThingTagGroupService;
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
@RequestMapping("/thingTagGroupGroup")
public class ThingTagGroupController {

    @Autowired
    private ThingTagGroupService thingTagGroupService;

    @RequestMapping(value = "/thingTagGroup", method = RequestMethod.GET)
    public ResponseEntity<String> getThingTagGroup(
            @RequestBody @Validated(value = GetValidate.class) ThingTagGroup thingTagGroup, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        List<ThingTagGroup> thingTagGroupLists = thingTagGroupService.getThingTagGroup(thingTagGroup);
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingTagGroupLists), HttpStatus.OK);
    }

    @RequestMapping(value = "/thingTagGroup", method = RequestMethod.POST)
    public ResponseEntity<String> addThingTagGroup(
            @RequestBody @Validated(value = AddValidate.class) ThingTagGroup thingTagGroup, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        thingTagGroupService.addThingTagGroup(thingTagGroup);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/thingTagGroup", method = RequestMethod.PUT)
    public ResponseEntity<String> updateThingTagGroup(
            @RequestBody @Validated(value = UpdateValidate.class) ThingTagGroup thingTagGroup, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        thingTagGroupService.updateThingTagGroup(thingTagGroup);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/thingTagGroup", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteThingTagGroup(
            @RequestBody @Validated(value = DeleteValidate.class) ThingTagGroup thingTagGroup, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        thingTagGroupService.deleteThingTagGroup(thingTagGroup);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }
}
