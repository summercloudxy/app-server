package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.sfmonitor.constants.SFSysMonitorConstant;
import com.zgiot.app.server.module.sfmonitor.mapper.RelSFSysMonitorTermThingMapper;
import com.zgiot.app.server.module.sfmonitor.mapper.RelSFSysMonitorThingMetricMapper;
import com.zgiot.app.server.module.sfmonitor.mapper.SFSysMonitorThingTagMapper;
import com.zgiot.app.server.module.sfmonitor.monitorservice.SFSysMonitorThingTagService;
import com.zgiot.app.server.module.sfmonitor.pojo.RelSFSysMonitorThingMetric;
import com.zgiot.app.server.module.sfmonitor.pojo.SFSysMonitorThing;
import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;
import com.zgiot.app.server.module.thingtag.pojo.ThingTagGroup;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.impl.mapper.ThingTagGroupMapper;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private RelSFSysMonitorTermThingMapper monitorTermThingMapper;
    @Autowired
    private DataService dataService;
    @Autowired
    private RelSFSysMonitorTermThingMapper termThingMapper;
    @Autowired
    private RelSFSysMonitorThingMetricMapper monitorThingMetricMapper;
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
        SFSysMonitorThing monitorThing = new SFSysMonitorThing();
        int thingRunCount =0;//运行中设备数量
        int thingFaultCount =0;//故障中设备数量
        int thingStopCount =0;//待机中设备数量
        for(ThingTag thingTag:ThingTagList) {
            monitorThing.setThingTagCode(thingTag.getCode());
            List<SFSysMonitorThing> monitorThingList = monitorTermThingMapper.getSFSysMonitorThing(monitorThing);
            for (SFSysMonitorThing mt : monitorThingList) {
                Optional<DataModelWrapper> data = dataService.getData(mt.getThingCode(),MetricCodes.STATE);
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
    /**
     * 获取系统监控系统详情页数据
     */
    @GetMapping("/getSystemMonitorDetail")
    public ResponseEntity<String> getSystemMonitorDetail(@RequestParam String thingTagId1,@RequestParam String thingTagId2,@RequestParam String term) {
        SystemMonitorDetailInfo systemMonitorDetailInfo =new SystemMonitorDetailInfo();
        List<SFSysMonitorThing> sfThingList= new ArrayList<>();
        short termCount =0;
        if(StringUtils.isEmpty(thingTagId2)){
            ThingTag thingTag = thingTagMapper.getThingTagById(Long.parseLong(thingTagId1));
            List<ThingTag> thingTags =thingTagMapper.getThingTagByParentId(Long.parseLong(thingTagId1));
            SFSysMonitorThing monitorThing = new SFSysMonitorThing();
            if(thingTags !=null && thingTags.size()>0){
                systemMonitorDetailInfo.setThingTag(thingTags);
                //获取一共有多少期
               termCount = termThingMapper.getTermCountByThingTagCode(thingTags.get(0).getCode());
               monitorThing.setThingTagCode(thingTags.get(0).getCode());
               if(termCount > 0){
                   monitorThing.setTermId(term ==null?SFSysMonitorConstant.TERM_ONE:Short.valueOf(term));
               }
               sfThingList = monitorTermThingMapper.getSFSysMonitorThing(monitorThing);
               for(SFSysMonitorThing mt:sfThingList){
                   List<RelSFSysMonitorThingMetric> thingMetrics = monitorThingMetricMapper.getThingMetricByThingCode(mt.getThingCode());
                   for(RelSFSysMonitorThingMetric thingMetric:thingMetrics){
                       String[] metrics =thingMetric.getMetricCode().split(",");
                       if(metrics.length ==1 &&thingMetric.getIsShowMetric() ==1){
                           Optional<DataModelWrapper> data = dataService.getData(thingMetric.getThingCode(),thingMetric.getMetricCode());
                           if (data.isPresent()) {
                               thingMetric.setMetricValue(data.get().getValue());
                           }
                       }else if(metrics.length >1){//目前只有液位需要存两个code组合判断液位值

                       }
                   }
               }
            }else{
                termCount = termThingMapper.getTermCountByThingTagCode(thingTag.getCode());
                monitorThing.setThingTagCode(thingTags.get(0).getCode());
                if(termCount > 0){
                    monitorThing.setTermId(term ==null?SFSysMonitorConstant.TERM_ONE:Short.valueOf(term));
                }
            }
        }
        systemMonitorDetailInfo.setTermCount(termCount);
        systemMonitorDetailInfo.setSfThingList(sfThingList);
        return new ResponseEntity<>(ServerResponse.buildOkJson(systemMonitorDetailInfo), HttpStatus.OK);
    }


}
