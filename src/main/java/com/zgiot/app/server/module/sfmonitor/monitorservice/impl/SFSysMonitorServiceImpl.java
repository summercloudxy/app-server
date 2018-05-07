package com.zgiot.app.server.module.sfmonitor.monitorservice.impl;

import com.zgiot.app.server.common.constants.AppServerConstant;
import com.zgiot.app.server.module.sfmonitor.constants.SFSysMonitorConstant;
import com.zgiot.app.server.module.sfmonitor.controller.SystemMonitorDetailInfo;
import com.zgiot.app.server.module.sfmonitor.mapper.RelSFSysMonitorTermThingMapper;
import com.zgiot.app.server.module.sfmonitor.mapper.RelSFSysMonitorThingMetricMapper;
import com.zgiot.app.server.module.sfmonitor.mapper.SFSysMonitorThingTagMapper;
import com.zgiot.app.server.module.sfmonitor.pojo.RelSFSysMonitorTermThing;
import com.zgiot.app.server.module.sfmonitor.pojo.RelSFSysMonitorThingMetric;
import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;
import com.zgiot.app.server.module.sfmonitor.monitorservice.SFSysMonitorService;
import com.zgiot.app.server.module.sfmonitor.util.CompareUtil;
import com.zgiot.app.server.module.thingtag.pojo.ThingTagGroup;
import com.zgiot.app.server.module.util.StringUtils;
import com.zgiot.app.server.service.BusinessService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.impl.mapper.ThingTagGroupMapper;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SFSysMonitorServiceImpl implements SFSysMonitorService {
    @Autowired
    private SFSysMonitorThingTagMapper thingTagMapper;
    @Autowired
    private ThingTagGroupMapper thingTagGroupMapper;
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

    @Override
    public List<ThingTag> getSysMonitorIndex() {
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
        return thingTagList;
    }

    @Override
    public SystemMonitorDetailInfo getSystemMonitorDetail(String thingTagId1,String thingTagId2,String term) {
        SystemMonitorDetailInfo systemMonitorDetailInfo = new SystemMonitorDetailInfo();
        List<RelSFSysMonitorTermThing> sfThingList;
        short termCount = 0;
        Long thingTagId = Long.parseLong(thingTagId2 == null ? thingTagId1 : thingTagId2);
        List<ThingTag> thingTags = thingTagMapper.getThingTagByParentId(thingTagId);
        ThingTag thingTag = thingTagMapper.getThingTagById(thingTagId);
        RelSFSysMonitorTermThing monitorThing = new RelSFSysMonitorTermThing();
        if (thingTags != null && thingTags.size() > 0) {
            monitorThing.setThingTagCode(thingTags.get(0).getCode());
        } else {
            monitorThing.setThingTagCode(thingTag.getCode());
        }
        //获取一共有多少期
        termCount = termThingMapper.getTermCountByThingTagCode(monitorThing.getThingTagCode());
        if (termCount > 0) {
            monitorThing.setTermId(term == null ? SFSysMonitorConstant.TERM_ONE : Short.valueOf(term));
        }
        sfThingList = monitorTermThingMapper.getSFSysMonitorThing(monitorThing);
        getThingMetric(sfThingList);
        if(SFSysMonitorConstant.THING_TAG_CODE_SEWAGE.equals(monitorThing.getThingTagCode())){
            sortSfSysMonitorTermThingList(sfThingList);
        }
        //获取三级菜单
        List<ThingTag> thingTagList = thingTagMapper.getThingTagByParentId(Long.parseLong(thingTagId1));
        if (thingTagList != null && thingTagList.size() > 0) {
            systemMonitorDetailInfo.setThingTagList(thingTagList);
        }
        systemMonitorDetailInfo.setTermCount(termCount);
        systemMonitorDetailInfo.setRelSFSysMonitorTermThingList(sfThingList);
        return systemMonitorDetailInfo;
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
                Map countMap = countMetricState(mt);
                thingRunCount =thingRunCount+(Integer)countMap.get(GlobalConstants.STATE_RUNNING);
                thingFaultCount =thingFaultCount+(Integer)countMap.get(GlobalConstants.STATE_FAULT);
                thingStopCount =thingStopCount+(Integer)countMap.get(GlobalConstants.STATE_STOPPED);
            }
        }
        thingTag2.setThingRunCount(thingRunCount);
        thingTag2.setThingFaultCount(thingFaultCount);
        thingTag2.setThingStopCount(thingStopCount);
    }

    /**
     * 根据设备状态统计数量
     */
    public Map countMetricState(RelSFSysMonitorTermThing mt){
        int thingRunCount =0;//运行中设备数量
        int thingFaultCount =0;//故障中设备数量
        int thingStopCount =0;//待机中设备数量
        Map<String,String> thingMap =new HashMap<>();
        Map<Short,Integer> resultMap =new HashMap<>();
        //thingMap包含的设备已经计数
        if(!org.apache.commons.lang.StringUtils.isEmpty(mt.getThingCode()) && !thingMap.containsKey(mt.getThingCode())){
            thingMap.put(mt.getThingCode(),mt.getThingCode());
            Optional<DataModelWrapper> data = dataService.getData(mt.getThingCode(),MetricCodes.STATE);
            if (data.isPresent()) {
                if (data.get().getValue().equals(String.valueOf(GlobalConstants.STATE_RUNNING))) {
                    thingRunCount++;
                } else if (data.get().getValue().equals(String.valueOf(GlobalConstants.STATE_FAULT))|| data.get().getValue().equals(String.valueOf(GlobalConstants.STATE_BREAK))) {
                    thingFaultCount++;
                } else {
                    thingStopCount++;
                }
            }else {
                thingStopCount++;
            }
        }
        resultMap.put(GlobalConstants.STATE_RUNNING,thingRunCount);
        resultMap.put(GlobalConstants.STATE_FAULT,thingFaultCount);
        resultMap.put(GlobalConstants.STATE_STOPPED,thingStopCount);
        return resultMap;
    }

    /**
     * 获取设备数据
     */
    public void getThingMetric(List<RelSFSysMonitorTermThing> sfThingList){
        for(RelSFSysMonitorTermThing mt:sfThingList){
            if(mt.getShowType() !=SFSysMonitorConstant.SHOW_TYPE_2){
                List<RelSFSysMonitorThingMetric> thingMetrics = monitorThingMetricMapper.getThingMetricByThingCode(mt.getThingCode(),mt.getThingTagCode());
                for(RelSFSysMonitorThingMetric thingMetric:thingMetrics){
                    getMetricValue(thingMetric,mt);
                }
                mt.setThingMetricList(thingMetrics);
            }
        }
    }

    /**
     * 获取设备信号数据
     */
    public void getMetricValue(RelSFSysMonitorThingMetric thingMetric,RelSFSysMonitorTermThing mt){
        if(MetricCodes.CURRENT_LEVEL_M.equals(thingMetric.getMetricCode())){  //液位查询
            thingMetric.setMetricValue(businessService.getLevelByThingCode(mt.getThingCode()));
            return;
        }
        if(SFSysMonitorConstant.THING_CODE_QUIT_SYS_RAW.equals(mt.getThingCode())){   //配煤比查询
            Map<String, String> coalRatioMap = businessService.getRowCoalCapPercent();
            String coalRatioOne =GlobalConstants.SYSTEM_ONE+coalRatioMap.get(GlobalConstants.SYSTEM_ONE);
            String coalRatioTwo =GlobalConstants.SYSTEM_TWO+coalRatioMap.get(GlobalConstants.SYSTEM_TWO);
            thingMetric.setMetricValue(coalRatioOne+","+coalRatioTwo);
            return;
        }
        if(thingMetric.getIsShowMetric() ==1){
            Optional<DataModelWrapper> data = dataService.getData(thingMetric.getThingCode(),thingMetric.getMetricCode());
            if (data.isPresent()) {
                thingMetric.setMetricValue(data.get().getValue());
            }
        }
    }

    /**
     * 获取液位值
     *
     * @param thingMetricList
     * @return
     */
    private String getLiquidLevel(List<RelSFSysMonitorThingMetric> thingMetricList) {
        String liquidLevel = "";
        if (thingMetricList != null && !thingMetricList.isEmpty()) {
            for (RelSFSysMonitorThingMetric relSFSysMonitorThingMetric : thingMetricList) {
                if (MetricCodes.CURRENT_LEVEL_M.equals(relSFSysMonitorThingMetric.getMetricCode()) && (relSFSysMonitorThingMetric.getMetricValue() != null)) {
                    liquidLevel = relSFSysMonitorThingMetric.getMetricValue();
                    break;
                }
            }
        }
        return liquidLevel;
    }

    /**
     * 污水系统页根据液位和设备编码排序
     * 实现:先按液位高的排，液位都是高的情况按thingCode升序排，
     * @param relSFSysMonitorTermThingList
     */
    private void sortSfSysMonitorTermThingList(List<RelSFSysMonitorTermThing> relSFSysMonitorTermThingList) {
        Collections.sort(relSFSysMonitorTermThingList, (o1, o2) -> {
            int num = 0;
            int a =compareLevel(o1,o2);
            if (a != 0) {
                num = (a > 0) ? AppServerConstant.TWO : AppServerConstant.MINUS_ONE;
            } else {
                // 按设备编号升序
                a = compareThingCode(o1,o2);
                if (a != 0) {
                    num = (a > 0) ? AppServerConstant.ONE : AppServerConstant.MINUS_TWO;
                }
            }
            return num;
        });
    }

    /**
     * 按液位排序
     */
    private int compareLevel(RelSFSysMonitorTermThing o1,RelSFSysMonitorTermThing o2){
        // 按液位降序
        String level1 = getLiquidLevel(o1.getThingMetricList());
        String level2 = getLiquidLevel(o2.getThingMetricList());
        String level = AppServerConstant.LEVEL_HIGH;
        if (level1.contains(level) && !level2.contains(level)) {
            return AppServerConstant.MINUS_ONE;
        } else if (!level1.contains(level) && level2.contains(level)) {
            return AppServerConstant.ONE;
        } else{
            return 0;
        }
    }

    /**
     * 按thingCode排序
     */
    private int compareThingCode(RelSFSysMonitorTermThing o1,RelSFSysMonitorTermThing o2){
        if (StringUtils.isNum(o1.getThingCode()) && StringUtils.isNum(o2.getThingCode())) {
            return CompareUtil.compareThingCodeOnlyNum(o1.getThingCode(), o2.getThingCode());
        } else {
            return CompareUtil.compareThingCodeHaveLetter(o1.getThingCode(), o2.getThingCode());
        }
    }
}
