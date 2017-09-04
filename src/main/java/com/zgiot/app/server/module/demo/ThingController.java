package com.zgiot.app.server.module.demo;

import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.pojo.ThingModel;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ThingModel getThing(@PathVariable String thingCode) {
        return thingService.getThing(thingCode);
    }

}
