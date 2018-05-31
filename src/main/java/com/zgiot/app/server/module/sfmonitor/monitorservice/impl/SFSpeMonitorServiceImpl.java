package com.zgiot.app.server.module.sfmonitor.monitorservice.impl;

import com.zgiot.app.server.common.constants.AppServerConstant;
import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.AlertRule;
import com.zgiot.app.server.module.equipments.mapper.ThingPropertiesMapper;
import com.zgiot.app.server.module.equipments.pojo.ThingProperties;
import com.zgiot.app.server.module.sfmonitor.constants.SFSpeMonitorConstant;
import com.zgiot.app.server.module.sfmonitor.constants.SFSysMonitorConstant;
import com.zgiot.app.server.module.sfmonitor.controller.SFSpeMonitorReq;
import com.zgiot.app.server.module.sfmonitor.controller.SystemMonitorDetailInfo;
import com.zgiot.app.server.module.sfmonitor.mapper.*;
import com.zgiot.app.server.module.sfmonitor.monitorservice.SFSpeMonitorService;
import com.zgiot.app.server.module.sfmonitor.pojo.*;
import com.zgiot.app.server.module.sfmonitor.util.CompareUtil;
import com.zgiot.app.server.module.thingtag.pojo.ThingTagGroup;
import com.zgiot.app.server.service.BusinessService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.impl.mapper.TMLMapper;
import com.zgiot.app.server.service.impl.mapper.ThingTagGroupMapper;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.constants.ThingTagConstants;
import com.zgiot.common.pojo.DataModelWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SFSpeMonitorServiceImpl implements SFSpeMonitorService {

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
    private RelSfSpeMonThingSubthingMapper monitorThingSubthingMapper;
    @Autowired
    private RelSfSpeMonThingMetricMapper speMonThingMetricMapper;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private ThingPropertiesMapper thingPropertiesMapper;
    @Autowired
    private TMLMapper tmlMapper;
    @Override
    public List<ThingTag> getSpeMonitorIndex() {
        ThingTagGroup thingTagGroup =new ThingTagGroup();
        thingTagGroup.setCode(SFSpeMonitorConstant.SFSPE_MONITOR_THING_TAG_GROUP_CODE);
        List<ThingTagGroup> thingTagGroupList = thingTagGroupMapper.findThingTagGroup(thingTagGroup);
        List<ThingTag> thingTagList =new ArrayList<>();
        if(thingTagGroupList != null){
            //获取专题监控thingtag根节点
            thingTagList = thingTagMapper.findRootThingTag(thingTagGroupList.get(0).getThingTagGroupId());
            for(ThingTag thingTag :thingTagList){
                //获取专题监控thingtag二级专题节点
                List<ThingTag> thingTagList2 =thingTagMapper.getThingTagByParentId(thingTag.getId());
                for(ThingTag thingTag2 :thingTagList2){
                    thingTag.getNodeList().add(thingTag2);
                }
            }
        }
        return thingTagList;
    }

    @Override
    public SystemMonitorDetailInfo getSpecialMonitorDetailHaveSub(SFSpeMonitorReq sFSpeMonitorReq) {
        SystemMonitorDetailInfo systemMonitorDetailInfo =getMonitorTermThing(sFSpeMonitorReq);
        getThingMetric(systemMonitorDetailInfo.getRelSFSysMonitorTermThingList());
        getSubThingMetric(systemMonitorDetailInfo.getRelSFSysMonitorTermThingList());
        //排序
        sortSfSpeMonitorTermThing(systemMonitorDetailInfo.getRelSFSysMonitorTermThingList());
        return systemMonitorDetailInfo;
    }

    @Override
    public SystemMonitorDetailInfo getSpecialMonitorDetailCommon(SFSpeMonitorReq sFSpeMonitorReq) {
        SystemMonitorDetailInfo systemMonitorDetailInfo = getMonitorTermThing(sFSpeMonitorReq);
        getThingMetric(systemMonitorDetailInfo.getRelSFSysMonitorTermThingList());
        //排序
        sortSfSpeMonitorTermThing(systemMonitorDetailInfo.getRelSFSysMonitorTermThingList());
        return systemMonitorDetailInfo;
    }

    @Override
    public SystemMonitorDetailInfo getSpecialMonitorDetailProtect(SFSpeMonitorReq sFSpeMonitorReq) {
        SystemMonitorDetailInfo systemMonitorDetailInfo = new SystemMonitorDetailInfo();
        List<RelSFSysMonitorTermThing> sfThingList =  new ArrayList<>();
        RelSFSysMonitorTermThing monitorThing = new RelSFSysMonitorTermThing();
        monitorThing.setMetricCode(sFSpeMonitorReq.getMetricCode());
        monitorThing.setTermId(sFSpeMonitorReq.getTermCount() == null ? SFSysMonitorConstant.TERM_ONE : sFSpeMonitorReq.getTermCount());
        if(StringUtils.isNotBlank(sFSpeMonitorReq.getThingCode())){
            monitorThing.setThingCode(sFSpeMonitorReq.getThingCode());
        }
        monitorThing.setSelectCount(sFSpeMonitorReq.getPageNum() * SFSpeMonitorConstant.pageSize);
        Long thingTagId = Long.parseLong(sFSpeMonitorReq.getThingtagId2() == null ? sFSpeMonitorReq.getThingtagId1() : sFSpeMonitorReq.getThingtagId2());
        List<ThingTag> thingTags = thingTagMapper.getThingTagByParentId(thingTagId);
        ThingTag thingTag = thingTagMapper.getThingTagById(thingTagId);
        if (thingTags != null && thingTags.size() > 0) {
            for(ThingTag tag:thingTags){
                monitorThing.setThingTagCode(tag.getCode());
                List<RelSFSysMonitorTermThing> sfThings = monitorTermThingMapper.getSFSysMonitorThing(monitorThing);
                sfThingList.addAll(sfThings);
            }
        } else {
            monitorThing.setThingTagCode(thingTag.getCode());
            sfThingList = monitorTermThingMapper.getSFSysMonitorThing(monitorThing);
        }
        getThingMetricProtect(sfThingList, monitorThing, systemMonitorDetailInfo);
        // todo 排序
        systemMonitorDetailInfo.setTermCount(monitorThing.getTermId());
//        systemMonitorDetailInfo.setRelSFSysMonitorTermThingList(sfThingList);
        return systemMonitorDetailInfo;
    }

    @Override
    public List<ThingTag> getThingTagListThree(String thingTagId) {
        //获取三级菜单
        List<ThingTag> thingTagList = thingTagMapper.getThingTagByParentId(Long.parseLong(thingTagId));
        return thingTagList;
    }

    @Override
    public List<String> getThingCodeList(SFSpeMonitorReq sFSpeMonitorReq) {
        RelSFSysMonitorTermThing monitorThing = new RelSFSysMonitorTermThing();
        List<String> thingCodeList = new ArrayList<>();
        Long thingTagId = Long.parseLong(sFSpeMonitorReq.getThingtagId2() == null ? sFSpeMonitorReq.getThingtagId1() : sFSpeMonitorReq.getThingtagId2());
        monitorThing.setTermId(sFSpeMonitorReq.getTermCount());
        // 有二级系统id按二级系统id查询thingCode
        List<ThingTag> thingTags = thingTagMapper.getThingTagByParentId(thingTagId);
        if(StringUtils.isNotEmpty(sFSpeMonitorReq.getThingtagId2()) || thingTags == null || thingTags.isEmpty()){
            ThingTag thingTag = thingTagMapper.getThingTagById(thingTagId);
            monitorThing.setThingTagCode(thingTag.getCode());
            thingCodeList = monitorTermThingMapper.getThingCodeList(monitorThing);
        }else{
            for(ThingTag tag :thingTags){
                monitorThing.setThingTagCode(tag.getCode());
                List<String> thingCodes = monitorTermThingMapper.getThingCodeList(monitorThing);
                thingCodeList.addAll(thingCodes);
            }
        }
        return thingCodeList;
    }

    @Override
    public List<RelSfSpeMonThingMetric> getMetricList(SFSpeMonitorReq sFSpeMonitorReq) {
        RelSFSysMonitorTermThing monitorThing = new RelSFSysMonitorTermThing();
        Long thingTagId = Long.parseLong(sFSpeMonitorReq.getThingtagId2() == null ? sFSpeMonitorReq.getThingtagId1() : sFSpeMonitorReq.getThingtagId2());
        monitorThing.setTermId(sFSpeMonitorReq.getTermCount());

        List<String> thingCodeList = new ArrayList<>();
        List<String> thingTagCodeList = new ArrayList<>();
        // 有二级系统id按二级系统id查询thingCode
        if(StringUtils.isNotEmpty(sFSpeMonitorReq.getThingtagId2())){
            ThingTag thingTag = thingTagMapper.getThingTagById(thingTagId);
            monitorThing.setThingTagCode(thingTag.getCode());
            thingCodeList = monitorTermThingMapper.getThingCodeList(monitorThing);
            thingTagCodeList.add(thingTag.getCode());
        }else{
            List<ThingTag> thingTags = thingTagMapper.getThingTagByParentId(thingTagId);
            for(ThingTag tag :thingTags){
                monitorThing.setThingTagCode(tag.getCode());
                List<String> thingCodes = monitorTermThingMapper.getThingCodeList(monitorThing);
                thingCodeList.addAll(thingCodes);
                thingTagCodeList.add(tag.getCode());
            }
        }

        List<RelSfSpeMonThingMetric> speMonThingMetricList = speMonThingMetricMapper.getMetricType(thingCodeList, thingTagCodeList);
        return speMonThingMetricList;
    }

    public SystemMonitorDetailInfo getMonitorTermThing(SFSpeMonitorReq sFSpeMonitorReq){
        SystemMonitorDetailInfo systemMonitorDetailInfo = new SystemMonitorDetailInfo();
        List<RelSFSysMonitorTermThing> sfThingList = new ArrayList<>();
        short termCount = 0;
        Long thingTagId = Long.parseLong(sFSpeMonitorReq.getThingtagId2() == null ? sFSpeMonitorReq.getThingtagId1() : sFSpeMonitorReq.getThingtagId2());
        List<ThingTag> thingTags = thingTagMapper.getThingTagByParentId(thingTagId);
        ThingTag thingTag = thingTagMapper.getThingTagById(thingTagId);
        RelSFSysMonitorTermThing monitorThing = new RelSFSysMonitorTermThing();
        monitorThing.setTermId(sFSpeMonitorReq.getTermCount() == null ? SFSysMonitorConstant.TERM_ONE : sFSpeMonitorReq.getTermCount());
        if(sFSpeMonitorReq.getThingCode() != null){
            monitorThing.setThingCode(sFSpeMonitorReq.getThingCode());
        }
        monitorThing.setSelectCount(sFSpeMonitorReq.getPageNum()*SFSpeMonitorConstant.pageSize);
        if (thingTags != null && thingTags.size() > 0) {
            List<RelSFSysMonitorTermThing> sfThings;
            for(ThingTag tag:thingTags){
                monitorThing.setThingTagCode(tag.getCode());
                sfThings = monitorTermThingMapper.getSFSysMonitorThing(monitorThing);
                sfThingList.addAll(sfThings);
            }
        } else {
            monitorThing.setThingTagCode(thingTag.getCode());
            sfThingList = monitorTermThingMapper.getSFSysMonitorThing(monitorThing);
        }
        getThingMetric(sfThingList);
        systemMonitorDetailInfo.setTermCount(termCount);
        systemMonitorDetailInfo.setRelSFSysMonitorTermThingList(sfThingList);
        return systemMonitorDetailInfo;
    }

    /**
     * 获取主设备数据
     */
    public void getThingMetric(List<RelSFSysMonitorTermThing> sfThingList){
        for(RelSFSysMonitorTermThing mt:sfThingList){
            //获取当前设备所有信号
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
     * 获取附属设备数据
     */
    public void getSubThingMetric(List<RelSFSysMonitorTermThing> sfThingList){
        for(RelSFSysMonitorTermThing mt:sfThingList){
            //获取附属设备所有数据
            RelSfSpeMonThingSubthingExample subThingExample = new RelSfSpeMonThingSubthingExample();
            RelSfSpeMonThingSubthingExample.Criteria criteria = subThingExample.createCriteria();
            criteria.andThingTagCodeEqualTo(mt.getThingTagCode()).andThingCodeEqualTo(mt.getThingCode());
            List<RelSfSpeMonThingSubthing> subthingList= monitorThingSubthingMapper.selectByExample(subThingExample);
            for(RelSfSpeMonThingSubthing subthing:subthingList){
                getSubThingMetricValue(subthing);
            }
            mt.setSpeMonThingSubthing(subthingList);
        }
    }

    /**
     * 获取主设备信号数据
     */
    public void getMetricValue(RelSFSysMonitorThingMetric thingMetric,RelSFSysMonitorTermThing mt){
        if(MetricCodes.TAP_OPEN.equals(thingMetric.getMetricCode())){  //开到位关到位查询
            thingMetric.setMetricValue(businessService.getTagValueByThingCode(mt.getThingCode()));
            return;
        }
        //电机电流查询
        if(SFSpeMonitorConstant.RATED_CURRENT.equals(thingMetric.getMetricCode())){
            getRatedCurrent(thingMetric);
        }
        //正常信号查询
        Optional<DataModelWrapper> data = dataService.getData(thingMetric.getThingCode(),thingMetric.getMetricCode());
        if (data.isPresent()) {
            thingMetric.setMetricValue(data.get().getValue());
        }
        //获取报警信息，判断信号是否报警
        List<AlertRule> alertRuleList = alertManager.getParamRules(thingMetric.getThingCode(),thingMetric.getMetricCode());
        if(alertRuleList != null && alertRuleList.size() >0){
            thingMetric.setAlert(judgeIsAlert(thingMetric,alertRuleList));
        }
    }

    /**
     * 获取附属设备信号数据
     */
    public void getSubThingMetricValue(RelSfSpeMonThingSubthing subThingMetric){
        if(MetricCodes.TAP_OPEN.equals(subThingMetric.getSubThingCode())){  //开到位关到位查询
            subThingMetric.setSubMetricValue(businessService.getTagValueByThingCode(subThingMetric.getThingCode()));
            return;
        }
        if(!StringUtils.isEmpty(subThingMetric.getSubMetricCode())){
            Optional<DataModelWrapper> data = dataService.getData(subThingMetric.getSubThingCode(),subThingMetric.getSubMetricCode());
            if (data.isPresent()) {
                subThingMetric.setSubMetricValue(data.get().getValue());
            }
        }
    }

    /**
     * 获取设备保护模块设备数据
     */
    public void getThingMetricProtect(List<RelSFSysMonitorTermThing> sfThingList,
            RelSFSysMonitorTermThing monitorThing, SystemMonitorDetailInfo systemMonitorDetailInfo){
        List<RelSfSpeMonThingMetric> speMonThingMetricList = new ArrayList<>();

        int realCount = 0;
        for(RelSFSysMonitorTermThing mt:sfThingList){
            //获取当前设备所有信号
            if(mt.getShowType() !=SFSysMonitorConstant.SHOW_TYPE_2){
                RelSfSpeMonThingMetricExample speThingExample = new RelSfSpeMonThingMetricExample();
                RelSfSpeMonThingMetricExample.Criteria criteria = speThingExample.createCriteria();

                criteria.andThingTagCodeEqualTo(mt.getThingTagCode()).andThingCodeEqualTo(mt.getThingCode());
                if (StringUtils.isNotBlank(monitorThing.getMetricCode())) {
                    criteria.andMetricCodeEqualTo(monitorThing.getMetricCode());
                }
                List<RelSfSpeMonThingMetric> thingMetrics = speMonThingMetricMapper.selectByExample(speThingExample);

                // 处理分页逻辑
                if (thingMetrics != null && !thingMetrics.isEmpty()) {
                    realCount += thingMetrics.size();
                } else {
                    continue;
                }

                int num = thingMetrics.size();
                if (realCount > monitorThing.getSelectCount()) {
                    num = num - (realCount - monitorThing.getSelectCount());
                }

                // 获取metricValue
                for (int i = 0;i < num;i++) {
                    getMetricValueProtect(thingMetrics.get(i));
                    thingMetrics.get(i).setThingName(tmlMapper.getThingByCode(thingMetrics.get(i).getThingCode()).getThingName());
                }

                speMonThingMetricList.addAll(thingMetrics);
                if (speMonThingMetricList.size() > monitorThing.getSelectCount()) {
                    speMonThingMetricList = speMonThingMetricList.subList(0, monitorThing.getSelectCount());
                    break;
                }
            }
        }
        systemMonitorDetailInfo.setSpeMonThingMetricList(speMonThingMetricList);
    }

    /**
     * 获取设备保护模块信号数据
     */
    public void getMetricValueProtect(RelSfSpeMonThingMetric thingMetric){
        if(StringUtils.isNotEmpty(thingMetric.getMetricCode())){
            Optional<DataModelWrapper> data = dataService.getData(thingMetric.getThingCode(),thingMetric.getMetricCode());
            if (data.isPresent()) {
                thingMetric.setMetricValue(data.get().getValue());
            }
        }
        if(StringUtils.isNotEmpty(thingMetric.getMetricCode2())){
            Optional<DataModelWrapper> data = dataService.getData(thingMetric.getThingCode(),thingMetric.getMetricCode2());
            if (data.isPresent()) {
                thingMetric.setMetricValue2(data.get().getValue());
            }
        }
    }

    /**
     * 判断信号值是否报警
     */
    public Boolean judgeIsAlert(RelSFSysMonitorThingMetric thingMetric,List<AlertRule> alertRuleList){
        Boolean isAlert =false;
        for(AlertRule alertRule : alertRuleList){
            if(thingMetric.getMetricValue() != null && Double.parseDouble(thingMetric.getMetricValue()) >alertRule.getLowerLimit()
                    && Double.parseDouble(thingMetric.getMetricValue()) <alertRule.getUpperLimit()){
                isAlert = true;
                break;
            }
        }
        return isAlert;
    }
    /**
     * 额定电流查询
     */
    public void getRatedCurrent(RelSFSysMonitorThingMetric thingMetric){
        List<String>  thingCodeList= new ArrayList<>();
        thingCodeList.add(thingMetric.getThingCode());
        List<ThingProperties> ThingPropertiesList =thingPropertiesMapper.getThingPropertiesByThingCode(thingCodeList);
        if(ThingPropertiesList != null && ThingPropertiesList.size() >0){
            for(ThingProperties thingProperties:ThingPropertiesList){
                if(MetricCodes.CURRENT.equals(thingProperties.getPropKey())){
                    thingMetric.setMetricValue(thingProperties.getPropValue());
                    break;
                }
            }
        }
    }

    /**
     * 实现:先按报警的排，都是报警的情况按thingCode升序排，
     * @param relSFSysMonitorTermThingList
     */
    private void sortSfSpeMonitorTermThing(List<RelSFSysMonitorTermThing> relSFSysMonitorTermThingList) {
        Collections.sort(relSFSysMonitorTermThingList, (o1, o2) -> {
            int num = 0;
            int a =compareAlertMetric(o1,o2);
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
     * 按报警排序
     */
    private int compareAlertMetric(RelSFSysMonitorTermThing o1,RelSFSysMonitorTermThing o2){
        // 按是否报警降序
        Boolean isAlert1 = getAlertMetric(o1.getThingMetricList());
        Boolean isAlert2 = getAlertMetric(o2.getThingMetricList());
        if (isAlert1 && !isAlert2) {
            return AppServerConstant.MINUS_ONE;
        } else if (!isAlert1 && isAlert2) {
            return AppServerConstant.ONE;
        } else{
            return 0;
        }
    }

    /**
     * 获取信号是否报警
     *
     * @param thingMetricList
     * @return
     */
    private Boolean getAlertMetric(List<RelSFSysMonitorThingMetric> thingMetricList) {
        Boolean isAlert = false;
        if (thingMetricList != null && !thingMetricList.isEmpty()) {
            for (RelSFSysMonitorThingMetric relSFSysMonitorThingMetric : thingMetricList) {
                if (relSFSysMonitorThingMetric.getAlert() != null) {
                    isAlert = relSFSysMonitorThingMetric.getAlert();
                    break;
                }
            }
        }
        return isAlert;
    }

    /**
     * 按thingCode排序
     */
    private int compareThingCode(RelSFSysMonitorTermThing o1,RelSFSysMonitorTermThing o2){
        if (com.zgiot.app.server.module.util.StringUtils.isNum(o1.getThingCode()) && com.zgiot.app.server.module.util.StringUtils.isNum(o2.getThingCode())) {
            return CompareUtil.compareThingCodeOnlyNum(o1.getThingCode(), o2.getThingCode());
        } else {
            return CompareUtil.compareThingCodeHaveLetter(o1.getThingCode(), o2.getThingCode());
        }
    }

    /**
     * 实现:先按报警的排，都是报警的情况按thingCode升序排，
     */
    private void sortRelSfSpeMonThingMetric(List<RelSfSpeMonThingMetric> speMonThingMetricList) {
        Collections.sort(speMonThingMetricList, (o1, o2) -> {
            int num = 0;
            int a =compareAlertMetricProtect(o1,o2);
            if (a != 0) {
                num = (a > 0) ? AppServerConstant.TWO : AppServerConstant.MINUS_ONE;
            } else {
                // 按设备编号升序
                a = compareThingCodeProtect(o1,o2);
                if (a != 0) {
                    num = (a > 0) ? AppServerConstant.ONE : AppServerConstant.MINUS_TWO;
                }
            }
            return num;
        });
    }

    /**
     * 按报警排序
     */
    private int compareAlertMetricProtect(RelSfSpeMonThingMetric o1,RelSfSpeMonThingMetric o2){
        // 按液位降序
        Boolean isAlert1 = Boolean.getBoolean(o1.getMetricValue());
        Boolean isAlert2 = Boolean.getBoolean(o2.getMetricValue());
        if (isAlert1 && !isAlert2) {
            return AppServerConstant.MINUS_ONE;
        } else if (!isAlert1 && isAlert2) {
            return AppServerConstant.ONE;
        } else{
            return 0;
        }
    }

    /**
     * 按thingCode排序
     */
    private int compareThingCodeProtect(RelSfSpeMonThingMetric o1,RelSfSpeMonThingMetric o2){
        if (com.zgiot.app.server.module.util.StringUtils.isNum(o1.getThingCode()) && com.zgiot.app.server.module.util.StringUtils.isNum(o2.getThingCode())) {
            return CompareUtil.compareThingCodeOnlyNum(o1.getThingCode(), o2.getThingCode());
        } else {
            return CompareUtil.compareThingCodeHaveLetter(o1.getThingCode(), o2.getThingCode());
        }
    }
}
