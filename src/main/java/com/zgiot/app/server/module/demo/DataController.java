package com.zgiot.app.server.module.demo;

import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired
    private DataService dataService;

    @GetMapping("/thing/{thingCode}/metric/{metricCode}")
    public DataModelWrapper getData(@PathVariable String thingCode, @PathVariable String metricCode) {
        return dataService.getData(thingCode, metricCode);
    }

    @PostMapping
    public Map<String, Map<String, DataModelWrapper>> getDataBatch(@RequestBody Map<String, List<String>> dataReq) {
        HashMap<String, Map<String, DataModelWrapper>> result = new HashMap<>(dataReq.size());
        dataReq.forEach((thingCode, metricCodes) -> {
            HashMap<String, DataModelWrapper> valueMap = new HashMap<>(metricCodes.size());
            result.put(thingCode, valueMap);
            for (String metricCode : metricCodes) {
                valueMap.put(metricCode, dataService.getData(thingCode, metricCode));
            }
        });
        return result;
    }
}
