package com.zgiot.app.server.module.sfmonitor.controller;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.metrictag.pojo.MetricTag;
import com.zgiot.app.server.module.util.validate.ControllerUtil;
import com.zgiot.app.server.service.impl.mapper.MetricTagMapper;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping(value = GlobalConstants.API  + GlobalConstants.API_VERSION + "/sfmonitor/signalWrapper")
public class SFMonitorSignalWrapperController {
    @Autowired
    private MetricTagMapper metricTagMapper;

    @GetMapping("")
    public ResponseEntity<String> getSignalWrapper(){
        MetricTag metricTag = new MetricTag();
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricTagMapper.findMetricTag(metricTag)), HttpStatus.OK);
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public ResponseEntity<String> addOrEditSignalWrapper(@RequestBody String bodyStr){
        ControllerUtil.validateBodyRequired(bodyStr);
        MetricTag metricTag = JSON.parseObject(bodyStr,MetricTag.class);
        String name = metricTag.getTagName();
        MetricTag tag = metricTagMapper.getMetricTagByName(name);
        boolean isExist = false;
        if(metricTag.getMetricTagId() == 0){//add
            if(tag != null){
                isExist = true;
            }else{
                metricTag.setCreateDate(new Date());
                metricTagMapper.addMetricTag(metricTag);
            }
        }else{//edit
            if(tag != null && tag.getMetricTagId() != metricTag.getMetricTagId()){
                isExist = true;
            }else{
                metricTag.setUpdateDate(new Date());
                metricTagMapper.updateMetricTag(metricTag);
            }
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(isExist), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSignalWrapper(@PathVariable int id){
        int signalCount = 0;

        metricTagMapper.deleteThingTag(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }



}
