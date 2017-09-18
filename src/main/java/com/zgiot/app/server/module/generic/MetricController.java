package com.zgiot.app.server.module.generic;

import com.zgiot.app.server.service.MetricService;
import com.zgiot.common.pojo.MetricModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metric")
public class MetricController {
    @Autowired
    private MetricService metricService;

    @GetMapping("/{metricCode}")
    public ResponseEntity<String> getMetric(@PathVariable String metricCode) {
        MetricModel mm= metricService.getMetric(metricCode);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(mm)
                , HttpStatus.OK);
    }
}
