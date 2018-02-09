package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.thingtag.dao.ThingTagMapper;
import com.zgiot.app.server.module.thingtag.pojo.ThingTag;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = GlobalConstants.API  + GlobalConstants.API_VERSION + "/sfmonitor/signalWrapper")
public class SFMonitorSignalWrapperController {
    @Autowired
    private ThingTagMapper thingTagMapper;

    @GetMapping("")
    public ResponseEntity<String> getSignalWrapper(){
        ThingTag thingTag = new ThingTag();
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingTagMapper.findThingTag(thingTag)), HttpStatus.OK);
    }

}
