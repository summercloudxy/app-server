package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.sfmonitor.constants.SFSysMonitorConstant;
import com.zgiot.app.server.module.sfmonitor.mapper.SFSysMonitorThingTagMapper;
import com.zgiot.app.server.module.sfmonitor.monitorservice.SFSysMonitorThingTagService;
import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;
import com.zgiot.app.server.module.thingtag.pojo.ThingTagGroup;
import com.zgiot.app.server.module.thingtag.pojo.ThingTagRelation;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.impl.mapper.ThingTagGroupMapper;
import com.zgiot.app.server.service.impl.mapper.ThingTagRelationMapper;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = GlobalConstants.API  + GlobalConstants.API_VERSION + "/sfsysmonitor")
public class SFSysMonitorController {

    @Autowired
    private SFSysMonitorThingTagMapper thingTagMapper;
    @Autowired
    private ThingTagGroupMapper thingTagGroupMapper;
    @Autowired
    private SFSysMonitorThingTagService thingTagService;
    @Autowired
    private ThingTagRelationMapper thingTagRelationMapper;
    @Autowired
    private DataService dataService;

    /**
     * 获取系统监控首页数据
     */
    @GetMapping("/getSystemMonitor")
    public ResponseEntity<String> getSystemMonitor() {
        ThingTagGroup thingTagGroup =new ThingTagGroup();
        thingTagGroup.setCode(SFSysMonitorConstant.SFSYS_MONITOR_THING_TAG_GROUP_CODE);
        List<ThingTagGroup> thingTagGroupList = thingTagGroupMapper.findThingTagGroup(thingTagGroup);
        List<ThingTag> thingTagList =new ArrayList<>();
        if(thingTagGroupList != null){
            //获取系统监控thingtag根节点
            thingTagList = thingTagMapper.findRootThingTag(thingTagGroupList.get(0).getThingTagGroupId());
            for(ThingTag thingTag :thingTagList){
                //获取系统监控thingtag二级系统节点
                List<ThingTag> thingTagList2 =thingTagMapper.getThingTagByParentId(thingTag.getId());
                for(ThingTag thingTag2 :thingTagList2){
                    //获取系统监控thingtag三级节点
                    List<ThingTag> thingTagList3 =thingTagMapper.getThingTagByParentId(thingTag2.getId());
                    if(thingTagList3 !=null && thingTagList3.size() >0){
                        getThingTagMetricCount(thingTag2,thingTagList3);
                    }else{
                        List<ThingTag> thingTags =new ArrayList<>();
                        thingTags.add(thingTag2);
                        getThingTagMetricCount(thingTag2,thingTags);
                    }
                    thingTag.getNodeList().add(thingTag2);
                }
            }
        }

        return new ResponseEntity<>(ServerResponse.buildOkJson(thingTagList), HttpStatus.OK);
    }

    /**
     * 统计二级系统节点下的设备运行情况
     */
    public void getThingTagMetricCount(ThingTag thingTag2,List<ThingTag> ThingTagList){
        ThingTagRelation thingTagRelation = new ThingTagRelation();
        int thingRunCount =0;//运行中设备数量
        int thingFaultCount =0;//故障中设备数量
        int thingStopCount =0;//待机中设备数量
        for(ThingTag thingTag:ThingTagList) {
            thingTagRelation.setThingTagCode(thingTag.getCode());
            List<ThingTagRelation> ThingTagRelationList = thingTagRelationMapper.findThingTagRelation(thingTagRelation);
            for (ThingTagRelation ttr : ThingTagRelationList) {
                Optional<DataModelWrapper> data = dataService.getData(ttr.getThingCode(),MetricCodes.STATE);
                if (data.isPresent()) {
                    if (data.get().getValue().equals(String.valueOf(GlobalConstants.STATE_RUNNING))) {
                        thingRunCount++;
                    } else if (data.get().getValue().equals(String.valueOf(GlobalConstants.STATE_FAULT))) {
                        thingFaultCount++;
                    } else {
                        thingStopCount++;
                    }
                }else {
                    thingStopCount++;
                }
            }
        }
        thingTag2.setThingRunCount(thingRunCount);
        thingTag2.setThingFaultCount(thingFaultCount);
        thingTag2.setThingStopCount(thingStopCount);
    }
}
