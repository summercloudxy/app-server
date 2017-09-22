package com.zgiot.app.server.module.generic;

import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.constants.FilterPressConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/thing")
public class ThingController {
    @Autowired
    private ThingService thingService;

    @GetMapping("/{thingCode}")
    public ResponseEntity<String> getThing(@PathVariable String thingCode) {
        ConcurrentHashMap<String,ConcurrentHashMap<String,String>> conMap = new ConcurrentHashMap<>();
        if(org.apache.commons.lang.StringUtils.isNotBlank(thingCode)){
            conMap = thingService.getThingProperties(thingCode, FilterPressConstants.PROP_TYPE,FilterPressConstants.DISP_TYPE );
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(conMap)
                , HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<String> getThing(){
        List<ConcurrentHashMap<String,ConcurrentHashMap<String,String>>> things = new ArrayList<>();
        things = thingService.getThings(FilterPressConstants.PROP_TYPE,FilterPressConstants.DISP_TYPE );
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(things)
                , HttpStatus.OK);
    }

}
