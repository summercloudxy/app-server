package com.zgiot.app.server.module.demo;

import com.zgiot.app.server.service.DataService;
import com.zgiot.common.restcontroller.ServerResponse;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/demo")
public class DemoRestController {

    @Autowired
    private DemoBusiness demoBusiness;

    @RequestMapping(
            value = "/thing/{thingCode}/metric/{metricCode}/latest-status",
            method = RequestMethod.GET)
    public ResponseEntity<String> calStatus(@PathVariable("thingCode")
                                            @NotNull
                                            @NotEmpty String thingCode,
                                            @PathVariable("metricCode")
                                            @NotNull
                                            @NotEmpty String metricCode) {

        String statusStr = this.demoBusiness.doCalStatus(thingCode, metricCode);

        return new ResponseEntity<String>(
                ServerResponse.buildOkJson(statusStr)
                , HttpStatus.OK);
    }

}
