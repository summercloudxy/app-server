package com.zgiot.app.server.module.sfmonitor.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.metrictag.pojo.MetricTag;
import com.zgiot.app.server.module.sfmonitor.mapper.SFMonDisplayZoneMapper;
import com.zgiot.app.server.module.util.validate.ControllerUtil;
import com.zgiot.app.server.service.impl.mapper.MetricTagMapper;
import com.zgiot.app.server.service.impl.mapper.MetricTagRelationMapper;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = GlobalConstants.API  + GlobalConstants.API_VERSION + "/sfmonitor/signalWrapper")
public class SFMonitorSignalWrapperController {
    @Autowired
    private MetricTagMapper metricTagMapper;
    @Autowired
    private MetricTagRelationMapper metricTagRelationMapper;

    @Autowired
    private SFMonDisplayZoneMapper sfMonDisplayZoneMapper;

    @GetMapping("/pageNum/{pageNum}/pageSize/{pageSize}")
    public ResponseEntity<String> getSignalWrapper(@PathVariable int pageNum,@PathVariable int pageSize){
        MetricTag metricTag = new MetricTag();
        PageHelper.startPage(pageNum,pageSize);
        List<MetricTag> metricTagList = metricTagMapper.findMetricTag(metricTag);
        List<SignalWrapperInfo> signalWrapperInfos = new ArrayList<>();
        SignalWrapperRes signalWrapperRes = new SignalWrapperRes();
        if(metricTagList.size() > 0){
            for(MetricTag tag:metricTagList){
                SignalWrapperInfo signalWrapperInfo = new SignalWrapperInfo();
                signalWrapperInfo.setMetricTag(tag);
                String zone = metricTagMapper.getMetricTagZone(tag.getCode());
                signalWrapperInfo.setZone(zone);
                signalWrapperInfos.add(signalWrapperInfo);
            }
            signalWrapperRes.setCount(metricTagMapper.getMetricTagCount());
        }else{
            signalWrapperRes.setCount(0);
        }
        signalWrapperRes.setSignalWrapperInfoList(signalWrapperInfos);

        return new ResponseEntity<>(ServerResponse.buildOkJson(signalWrapperRes), HttpStatus.OK);
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public ResponseEntity<String> addOrEditSignalWrapper(@RequestBody String bodyStr){
        ControllerUtil.validateBodyRequired(bodyStr);
        SignalWrapperReq signalWrapperReq = JSON.parseObject(bodyStr,SignalWrapperReq.class);
        MetricTag metricTag = signalWrapperReq.getMetricTag();
        int zoneId = signalWrapperReq.getZoneId();
        String name = metricTag.getTagName();
        MetricTag tag = metricTagMapper.getMetricTagByName(name);
        boolean isExist = false;
        if(metricTag.getId() == null){//add
            if(tag != null){
                isExist = true;
            }else{
                metricTag.setCreateDate(new Date());
                metricTag.setOperator("default");
                metricTagMapper.addMetricTag(metricTag);
                metricTag.setCode(String.valueOf(metricTag.getId()));
                metricTagMapper.updateMetricTag(metricTag);
//                MetricTag metricTagTemp = metricTagMapper.getMetricTagByName(metricTag.getTagName());
                sfMonDisplayZoneMapper.addRelSFMonTagDisplayZone(String.valueOf(metricTag.getId()),zoneId);
            }
        }else{//edit
            if(tag != null && tag.getId() != metricTag.getId()){
                isExist = true;
            }else{
                metricTag.setUpdateDate(new Date());
                metricTagMapper.updateMetricTag(metricTag);
                MetricTag metricTagTemp = metricTagMapper.getMetricTagByName(metricTag.getTagName());
                sfMonDisplayZoneMapper.deleteRelSFMonTagDisplayZone(metricTagTemp.getCode());
                sfMonDisplayZoneMapper.addRelSFMonTagDisplayZone(metricTagTemp.getCode(),zoneId);
            }
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(isExist), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSignalWrapper(@PathVariable int id){
        Integer signalCount = metricTagRelationMapper.getMetricCount(id);
        if(signalCount == 0){
            MetricTag metricTag = metricTagMapper.getMetricTagById(id);
            metricTagMapper.delMetricTag(id);
            metricTagMapper.delRelMetricTagDisplayzone(metricTag.getCode());
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(signalCount), HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<String> getSignalWrapper(@PathVariable String name){
        SignalWrapperInfo signalWrapperInfo = new SignalWrapperInfo();
        MetricTag metricTag = metricTagMapper.getMetricTagByName(name);
        signalWrapperInfo.setMetricTag(metricTag);
        String zone = metricTagMapper.getMetricTagZone(metricTag.getCode());
        signalWrapperInfo.setZone(zone);
        return new ResponseEntity<>(ServerResponse.buildOkJson(signalWrapperInfo), HttpStatus.OK);
    }

    @GetMapping("/zone")
    public ResponseEntity<String> getDisplayZone(){
        return new ResponseEntity<>(ServerResponse.buildOkJson(sfMonDisplayZoneMapper.getDisplayZone()), HttpStatus.OK);
    }
}
