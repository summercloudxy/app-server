package com.zgiot.app.server.module.tools;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/devtools")
public class DevToolController {
    @Autowired
    private HistoryDataService historyDataService;

    @GetMapping("/test")
    public ResponseEntity<String> test() throws Exception {

        int size = 3000;
        List<DataModel> list = new ArrayList<>(size);
        long dt = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            DataModel dm = new DataModel();
            dm.setThingCode("2492");
            dm.setMetricCode("RESET");
            dm.setValue(String.valueOf(i));
            dm.setDataTimeStamp(new Date(dt + i * 1000));
            list.add(dm);
        }

        this.historyDataService.insertBatch(list);

        return new ResponseEntity<>(
                JSON.toJSONString(ServerResponse.buildOK(size))
                , HttpStatus.OK);
    }
}
