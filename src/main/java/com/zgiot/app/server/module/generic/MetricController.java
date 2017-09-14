package com.zgiot.app.server.module.generic;

import com.zgiot.app.server.service.MetricService;
import com.zgiot.common.pojo.MetricModel;
import org.springframework.beans.factory.annotation.Autowired;
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
    public MetricModel getMetric(@PathVariable String metricCode) {
        return metricService.getMetric(metricCode);
    }
}
