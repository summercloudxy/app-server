package com.zgiot.app.server.module.generic;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.MetricService;
import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.MetricModel;
import com.zgiot.common.pojo.ThingModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired
    private DataService dataService;
    @Autowired
    ThingService thingService;
    @Autowired
    MetricService metricService;

    @GetMapping("/thing/{thingCode}/metric/{metricCode}")

    public ResponseEntity<String> getData(@PathVariable String thingCode, @PathVariable String metricCode) {
        this.thingService.validateThing(thingCode);
        this.metricService.validateMetric(metricCode);

        DataModelWrapper dw = dataService.getData(thingCode, metricCode).orElse(null);
        return new ResponseEntity<>(
                JSON.toJSONString(ServerResponse.buildOK(dw))
                , HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> getDataBatch(@RequestBody Map<String, List<String>> dataReq) {
        HashMap<String, Map<String, DataModelWrapper>> result = new HashMap<>(dataReq.size());
        dataReq.forEach((thingCode, metricCodes) -> {
            HashMap<String, DataModelWrapper> valueMap = new HashMap<>(metricCodes.size());
            result.put(thingCode, valueMap);
            for (String metricCode : metricCodes) {
                valueMap.put(metricCode, dataService.getData(thingCode, metricCode).orElse(null));
            }
        });

        return new ResponseEntity<>(
                JSON.toJSONString(ServerResponse.buildOK(result))
                , HttpStatus.OK);
    }

    @GetMapping("/adhocLoadData/thing/{thingCode}/metric/{metricCode}")
    public ResponseEntity<String> adhocLoadData(@PathVariable String thingCode, @PathVariable String metricCode) {
        this.thingService.validateThing(thingCode);
        this.metricService.validateMetric(metricCode);

        DataModelWrapper dw = dataService.adhocLoadData(thingCode, metricCode);

        return new ResponseEntity<>(
                JSON.toJSONString(ServerResponse.buildOK(dw))
                , HttpStatus.OK);
    }

    @GetMapping("/stats/counters")
    public ResponseEntity<String> counterStats() {
        Map counterMap = dataService.getAccessCounterMap();
        return new ResponseEntity<>(
                JSON.toJSONString(ServerResponse.buildOK(counterMap))
                , HttpStatus.OK);
    }

}
