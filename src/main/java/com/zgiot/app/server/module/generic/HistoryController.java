package com.zgiot.app.server.module.generic;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.HistoryModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangwei
 */
@RestController
@RequestMapping(value = "/history")
public class HistoryController {

    @Autowired
    private HistoryDataService historyDataService;

    @PostMapping("")
    public ResponseEntity<String> insertBatch(@RequestBody String requestData) {
        //check requestData
        if (StringUtils.isBlank(requestData)) {
            ServerResponse res = new ServerResponse("Blank request.", SysException.EC_UNKOWN, 0);
            String resJSON = JSON.toJSONString(res);
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        List<DataModel> modelList = JSON.parseArray(requestData, DataModel.class);
        //check modelList
        if (modelList == null || modelList.isEmpty()) {
            ServerResponse res = new ServerResponse("Empty list.The incoming req body is: `" + requestData + "`", SysException.EC_UNKOWN, 0);
            String resJSON = JSON.toJSONString(res);
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        int count = historyDataService.insertBatch(modelList);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(count)
                , HttpStatus.OK);
    }


    @GetMapping("")
    public ResponseEntity<String> list(@RequestBody String requestData) {
        HistoryModel param = null;
        boolean valid = true;

        if (StringUtils.isBlank(requestData)) {
            //check requestData
            valid = false;
        } else {
            param = JSON.parseObject(requestData, HistoryModel.class);
            //check parse result
            if (param.getDuration() == null && param.getEndDate() == null && param.getStartDate() == null
                    && param.getThingCodes() == null && param.getMetricCodes() == null) {
                valid = false;
            }
        }

        if (!valid) {
            ServerResponse res = new ServerResponse<>(
                    "Not valid request data. The incoming req body is: `" + requestData + "`", SysException.EC_UNKOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }

        List<DataModel> modelList;
        if (param.getStartDate() == null) {
            //has not startDate, must have endDate
            if (param.getEndDate() == null) {
                throw new IllegalArgumentException("end date required");
            } else {
                modelList = historyDataService.findHistoryData(param.getThingCodes(), param.getMetricCodes(), param.getEndDate());
            }
        } else {
            //has startDate, check endDate first, then duration
            if (param.getEndDate() != null) {
                modelList = historyDataService.findHistoryDataList(param.getThingCodes(), param.getMetricCodes(),
                        param.getStartDate(), param.getEndDate());
            } else if (param.getDuration() != null) {
                modelList = historyDataService.findHistoryData(param.getThingCodes(), param.getMetricCodes(),
                        param.getStartDate(), param.getDuration());
            } else {
                throw new IllegalArgumentException("end date or duration required");
            }
        }

        return new ResponseEntity<>(ServerResponse.buildOkJson(modelList), HttpStatus.OK);
    }
}
