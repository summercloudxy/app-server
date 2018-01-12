package com.zgiot.app.server.module.thingtag.controller;

import com.zgiot.app.server.module.thingtag.pojo.ThingTag;
import com.zgiot.app.server.module.util.ValidateParamUtil;
import com.zgiot.app.server.module.util.validate.AddValidate;
import com.zgiot.app.server.module.util.validate.DeleteValidate;
import com.zgiot.app.server.module.util.validate.GetValidate;
import com.zgiot.app.server.module.util.validate.UpdateValidate;
import com.zgiot.app.server.service.ThingTagService;
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
public class ThingTagController {
    @Autowired
    private ThingTagService thingTagService;
    
    @RequestMapping(value = "/thingTag", method = RequestMethod.GET)
    public ResponseEntity<String> getThingTag(
            @RequestBody @Validated(value = GetValidate.class) ThingTag thingTag, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        List<ThingTag> thingTagLists = thingTagService.getThingTag(thingTag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingTagLists), HttpStatus.OK);
    }

    @RequestMapping(value = "/thingTag", method = RequestMethod.POST)
    public ResponseEntity<String> addThingTag(
            @RequestBody @Validated(value = AddValidate.class) ThingTag thingTag, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        thingTagService.addThingTag(thingTag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/thingTag", method = RequestMethod.PUT)
    public ResponseEntity<String> updateThingTag(
            @RequestBody @Validated(value = UpdateValidate.class) ThingTag thingTag, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        thingTagService.updateThingTag(thingTag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/thingTag", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteThingTag(
            @RequestBody @Validated(value = DeleteValidate.class) ThingTag thingTag, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(ServerResponse.buildOkJson(ValidateParamUtil.getBindingResultError(bindingResult)),
                    HttpStatus.BAD_REQUEST);
        }
        thingTagService.deleteThingTag(thingTag);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }
}
