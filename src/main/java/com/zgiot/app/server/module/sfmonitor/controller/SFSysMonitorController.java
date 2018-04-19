package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.sfmonitor.constants.SFSysMonitorConstant;
import com.zgiot.app.server.module.sfmonitor.mapper.RelSFSysMonitorTermThingMapper;
import com.zgiot.app.server.module.sfmonitor.mapper.RelSFSysMonitorThingMetricMapper;
import com.zgiot.app.server.module.sfmonitor.mapper.SFSysMonitorThingTagMapper;
import com.zgiot.app.server.module.sfmonitor.monitorservice.SFSysMonitorThingTagService;
import com.zgiot.app.server.module.sfmonitor.pojo.RelSFSysMonitorThingMetric;
import com.zgiot.app.server.module.sfmonitor.pojo.RelSFSysMonitorTermThing;
import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;
import com.zgiot.app.server.module.thingtag.pojo.ThingTagGroup;
import com.zgiot.app.server.service.BusinessService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.impl.mapper.ThingTagGroupMapper;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private BusinessService businessService;
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
        RelSFSysMonitorTermThing monitorThing = new RelSFSysMonitorTermThing();
        int thingRunCount =0;//运行中设备数量
        int thingFaultCount =0;//故障中设备数量
        int thingStopCount =0;//待机中设备数量
        for(ThingTag thingTag:ThingTagList) {
            monitorThing.setThingTagCode(thingTag.getCode());
            List<RelSFSysMonitorTermThing> monitorThingList = monitorTermThingMapper.getSFSysMonitorThing(monitorThing);
            for (RelSFSysMonitorTermThing mt : monitorThingList) {
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
    public ResponseEntity<String> getSystemMonitorDetail(@RequestParam String thingTagId1,@RequestParam(required = false)  String thingTagId2,@RequestParam(required = false) String term) {
        SystemMonitorDetailInfo systemMonitorDetailInfo = new SystemMonitorDetailInfo();
        List<RelSFSysMonitorTermThing> sfThingList;
        short termCount = 0;
        Long thingTagId = Long.parseLong(thingTagId2 == null ? thingTagId1 : thingTagId2);
        ThingTag thingTag = thingTagMapper.getThingTagById(thingTagId);
        List<ThingTag> thingTags = thingTagMapper.getThingTagByParentId(thingTagId);
        RelSFSysMonitorTermThing monitorThing = new RelSFSysMonitorTermThing();
        if (thingTags != null && thingTags.size() > 0) {
            systemMonitorDetailInfo.setThingTagList(thingTags);
            //获取一共有多少期
            termCount = termThingMapper.getTermCountByThingTagCode(thingTags.get(0).getCode());
            monitorThing.setThingTagCode(thingTags.get(0).getCode());
            if (termCount > 0) {
                monitorThing.setTermId(term == null ? SFSysMonitorConstant.TERM_ONE : Short.valueOf(term));
            }
            sfThingList = monitorTermThingMapper.getSFSysMonitorThing(monitorThing);
            getThingMetric(sfThingList);
        } else {
            termCount = termThingMapper.getTermCountByThingTagCode(thingTag.getCode());
            monitorThing.setThingTagCode(thingTag.getCode());
            if (termCount > 0) {
                monitorThing.setTermId(term == null ? SFSysMonitorConstant.TERM_ONE : Short.valueOf(term));
            }
            sfThingList = monitorTermThingMapper.getSFSysMonitorThing(monitorThing);
            getThingMetric(sfThingList);
        }
        systemMonitorDetailInfo.setTermCount(termCount);
        systemMonitorDetailInfo.setRelSFSysMonitorTermThingList(sfThingList);
        return new ResponseEntity<>(ServerResponse.buildOkJson(systemMonitorDetailInfo), HttpStatus.OK);
    }

    public void getThingMetric(List<RelSFSysMonitorTermThing> sfThingList){
        for(RelSFSysMonitorTermThing mt:sfThingList){
            if(mt.getShowType() !=SFSysMonitorConstant.SHOW_TYPE_2){
                List<RelSFSysMonitorThingMetric> thingMetrics = monitorThingMetricMapper.getThingMetricByThingCode(mt.getThingCode());
                for(RelSFSysMonitorThingMetric thingMetric:thingMetrics){
                    getMetricValue(thingMetric,mt);
                }
                mt.setThingMetricList(thingMetrics);
            }
        }
    }

    public void getMetricValue(RelSFSysMonitorThingMetric thingMetric,RelSFSysMonitorTermThing mt){
        if(thingMetric.getIsShowMetric() ==1){
            Optional<DataModelWrapper> data = dataService.getData(thingMetric.getThingCode(),thingMetric.getMetricCode());
            if (data.isPresent()) {
                thingMetric.setMetricValue(data.get().getValue());
            }
        }else if(MetricCodes.CURRENT_LEVEL_M.equals(thingMetric.getMetricCode())){  //液位查询
            thingMetric.setMetricValue(businessService.getLevelByThingCode(mt.getThingCode()));
        }else if(SFSysMonitorConstant.THING_CODE_QUIT_SYS_RAW.equals(mt.getThingCode())){   //配煤比查询
            Map<String, String> coalRatioMap = businessService.getRowCoalCapPercent();
            String coalRatioOne =GlobalConstants.SYSTEM_ONE+coalRatioMap.get(GlobalConstants.SYSTEM_ONE);
            String coalRatioTwo =GlobalConstants.SYSTEM_TWO+coalRatioMap.get(GlobalConstants.SYSTEM_TWO);
            thingMetric.setMetricValue(coalRatioOne+","+coalRatioTwo);
        }
    }

}
