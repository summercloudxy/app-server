package com.zgiot.app.server.module.generic;

import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.pojo.ThingModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/thing")
public class ThingController {
    @Autowired
    private ThingService thingService;

    @GetMapping("/{thingCode}")
    public ResponseEntity<String> getThing(@PathVariable String thingCode) {
        ThingModel tm = thingService.getThing(thingCode);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(tm)
                , HttpStatus.OK);
    }

}
