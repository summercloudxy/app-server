package com.zgiot.app.server.module.sfmonitor.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.metrictag.pojo.MetricTag;
import com.zgiot.app.server.module.metrictag.pojo.MetricTagRelation;
import com.zgiot.app.server.module.sfmonitor.constants.SFMonitorConstant;
import com.zgiot.app.server.module.sfmonitor.mapper.*;
import com.zgiot.app.server.module.sfmonitor.monitorservice.MonitorService;
import com.zgiot.app.server.module.sfmonitor.pojo.*;
import com.zgiot.app.server.module.util.validate.ControllerUtil;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.app.server.service.impl.mapper.*;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.MetricModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfmonitor")
public class SFMonitorSignalWrapperController {
    private static final int SFMONITOR_TYPE = 1;
    @Autowired
    private MetricTagMapper metricTagMapper;
    @Autowired
    private MetricTagRelationMapper metricTagRelationMapper;

    @Autowired
    private SFMonDisplayZoneMapper sfMonDisplayZoneMapper;

    @Autowired
    private MetricMapper metricMapper;

    @Autowired
    private SFMonStyleMapper sfMonStyleMapper;

    @Autowired
    private RelSFMonMetricTagStyleMapper relSFMonMetricTagStyleMapper;

    @Autowired
    private ThingTagMapper thingTagMapper;

    @Autowired
    private SFMonEquipMonitorInfoMapper sfMonEquipMonitorInfoMapper;

    @Autowired
    private TMLMapper tmlMapper;

    @Autowired
    private SFMonEquipMonitorConfigMapper sfMonEquipMonitorConfigMapper;

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private SFMonSignalWrapperRuleMapper sfMonSignalWrapperRuleMapper;

    @Autowired
    private DataService dataService;

    @Autowired
    private HistoryDataService historyDataService;

    @GetMapping("/signalWrapper/pageNum/{pageNum}/pageSize/{pageSize}")
    public ResponseEntity<String> getSignalWrapper(@PathVariable int pageNum, @PathVariable int pageSize) {
        MetricTag metricTag = new MetricTag();
        PageHelper.startPage(pageNum, pageSize);
        List<MetricTag> metricTagList = metricTagMapper.findMetricTag(metricTag);
        List<SignalWrapperInfo> signalWrapperInfos = new ArrayList<>();
        SignalWrapperRes signalWrapperRes = new SignalWrapperRes();
        getSignalWrapperInfo(metricTagList, signalWrapperInfos);
        if (signalWrapperInfos.size() > 0) {
            signalWrapperRes.setCount(metricTagMapper.getMetricTagCount());
        } else {
            signalWrapperRes.setCount(0);
        }
        signalWrapperRes.setSignalWrapperInfoList(signalWrapperInfos);

        return new ResponseEntity<>(ServerResponse.buildOkJson(signalWrapperRes), HttpStatus.OK);
    }

    @GetMapping(value = "/signalWrapper")
    public ResponseEntity<String> getAllSignalWrapper() {
        MetricTag metricTag = new MetricTag();
        List<MetricTag> metricTagList = metricTagMapper.findMetricTag(metricTag);
        List<SignalWrapperInfo> signalWrapperInfos = new ArrayList<>();
        getSignalWrapperInfo(metricTagList, signalWrapperInfos);
        return new ResponseEntity<>(ServerResponse.buildOkJson(signalWrapperInfos), HttpStatus.OK);
    }

    @GetMapping("/signalWrapper/fuzzyFind/{wrapperName}")
    public ResponseEntity<String> getWrapperNames(@PathVariable String wrapperName ){
        List<MetricTag> metricTags = null;
        if(wrapperName.equals(SFMonitorConstant.ALL_PARAMETER)){
            metricTags = metricTagMapper.getAllMetricTag();
        }else{
            metricTags = metricTagMapper.getMetricTag(wrapperName + SFMonitorConstant.FUZZY_QUERY_TAG);
        }
        List<String> wrapperNames = new ArrayList<>();
        for(MetricTag metricTag:metricTags){
            wrapperNames.add(metricTag.getTagName());
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(wrapperNames), HttpStatus.OK);
    }

    private void getSignalWrapperInfo(List<MetricTag> metricTagList, List<SignalWrapperInfo> signalWrapperInfos) {
        if (metricTagList.size() > 0) {
            for (MetricTag tag : metricTagList) {
                SignalWrapperInfo signalWrapperInfo = new SignalWrapperInfo();
                signalWrapperInfo.setMetricTag(tag);
                String zone = metricTagMapper.getMetricTagZone(tag.getCode());
                signalWrapperInfo.setZone(zone);
                signalWrapperInfos.add(signalWrapperInfo);
            }
        }
    }

    @RequestMapping(value = "/signalWrapper", method = RequestMethod.POST)
    public ResponseEntity<String> addOrEditSignalWrapper(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        SignalWrapperReq signalWrapperReq = JSON.parseObject(bodyStr, SignalWrapperReq.class);
        MetricTag metricTag = signalWrapperReq.getMetricTag();
        int zoneId = signalWrapperReq.getZoneId();
        String editor = signalWrapperReq.getUserName();
        String name = metricTag.getTagName();
        if (StringUtils.isBlank(name)) {
            return new ResponseEntity<>(ServerResponse.buildOkJson(false), HttpStatus.OK);
        }
        MetricTag tag = metricTagMapper.getMetricTagByName(name);
        boolean isExist = false;
        metricTag.setMetricTagGroupId(SFMONITOR_TYPE);
        if (metricTag.getId() == null) {//add
            if (tag != null) {
                isExist = true;
            } else {
                metricTag.setCreateDate(new Date());
                metricTag.setOperator(editor);
                saveSignalWrapperRule(name,null,zoneId);
                metricTagMapper.addMetricTag(metricTag);
                metricTag.setCode(String.valueOf(metricTag.getId()));
                metricTagMapper.updateMetricTag(metricTag);
                sfMonDisplayZoneMapper.addRelSFMonTagDisplayZone(String.valueOf(metricTag.getId()), zoneId);
            }
        } else {//edit
            if (tag != null && tag.getId() != metricTag.getId()) {
                isExist = true;
            } else {
                metricTag.setUpdateDate(new Date());
                MetricTag metric = metricTagMapper.getMetricTagById(metricTag.getId());
                saveSignalWrapperRule(name,metric.getTagName(),zoneId);
                metricTagMapper.updateMetricTag(metricTag);
                MetricTag metricTagTemp = metricTagMapper.getMetricTagByName(metricTag.getTagName());
                sfMonDisplayZoneMapper.deleteRelSFMonTagDisplayZone(metricTagTemp.getCode());
                sfMonDisplayZoneMapper.addRelSFMonTagDisplayZone(metricTagTemp.getCode(), zoneId);
            }
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(isExist), HttpStatus.OK);
    }

    private void saveSignalWrapperRule(String metricTagName,String oldMetricTagName,int zoneId){
        String zoneCode = sfMonDisplayZoneMapper.getZoneCode(zoneId);
        SFMonSignalWrapperRule sfMonSignalWrapperRule = new SFMonSignalWrapperRule();
        if(StringUtils.isBlank(oldMetricTagName)){//页面新增信号包
            sfMonSignalWrapperRule.setSignalWrapperName(metricTagName);
            SFMonSignalWrapperRule rule = sfMonSignalWrapperRuleMapper.getSignalWrapperRule(sfMonSignalWrapperRule);
            if(rule == null){
                sfMonSignalWrapperRuleMapper.addSignalWrapperRule(metricTagName,zoneCode);
            }
        }else{//页面编辑信号包
            sfMonSignalWrapperRule.setSignalWrapperName(oldMetricTagName);
            SFMonSignalWrapperRule rule = sfMonSignalWrapperRuleMapper.getSignalWrapperRule(sfMonSignalWrapperRule);
            if(rule != null){
                sfMonSignalWrapperRuleMapper.updateSignalWrapperRule(metricTagName,zoneCode,rule.getId());
            }
        }
    }

    @RequestMapping(value = "/signalWrapper/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSignalWrapper(@PathVariable int id) {
        Integer signalCount = metricTagRelationMapper.getMetricCount(id);
        if (signalCount == 0) {
            MetricTag metricTag = metricTagMapper.getMetricTagById(id);
            metricTagMapper.delMetricTag(id);
            metricTagMapper.delRelMetricTagDisplayzone(metricTag.getCode());
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(signalCount), HttpStatus.OK);
    }

    @GetMapping("/signalWrapper/{name}")
    public ResponseEntity<String> getSignalWrapper(@PathVariable String name) {
        List<SignalWrapperInfo> signalWrapperInfos = new ArrayList<>();
        List<MetricTag> metricTags = null;
        if(SFMonitorConstant.ALL_PARAMETER.equals(name)){
            metricTags = metricTagMapper.findMetricTag(new MetricTag());
        }else{
            metricTags = metricTagMapper.getMetricTag(name + SFMonitorConstant.FUZZY_QUERY_TAG);
        }

        for(MetricTag tag:metricTags){
            SignalWrapperInfo signalWrapperInfo = new SignalWrapperInfo();
            signalWrapperInfo.setMetricTag(tag);
            String zone = metricTagMapper.getMetricTagZone(tag.getCode());
            signalWrapperInfo.setZone(zone);
            signalWrapperInfos.add(signalWrapperInfo);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(signalWrapperInfos), HttpStatus.OK);
    }

    @GetMapping("/signalWrapper/zone")
    public ResponseEntity<String> getDisplayZone() {
        return new ResponseEntity<>(ServerResponse.buildOkJson(sfMonDisplayZoneMapper.getDisplayZone()), HttpStatus.OK);
    }

    //此处开始为监控二级页面api
    @GetMapping("/signalWrapper/metric/parameterType/{metricName}")
    public ResponseEntity<String> getParameterMetric(@PathVariable String metricName) {
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricMapper.findParameterMetric(metricName + SFMonitorConstant.FUZZY_QUERY_TAG)), HttpStatus.OK);
    }

    @GetMapping("/signalWrapper/metric/parameterType")
    public ResponseEntity<String> getAllParameterMetric() {
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricMapper.findAllParameterMetric()), HttpStatus.OK);
    }

    @RequestMapping(value = "/signalWrapper/metric", method = RequestMethod.POST)
    public ResponseEntity<String> addOrEditMetricToSignalWrapper(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        SignalWrapperRelateMetric signalWrapperRelateMetric = JSON.parseObject(bodyStr, SignalWrapperRelateMetric.class);
        String wrapperName = signalWrapperRelateMetric.getWrapperName();
        String tagCode = getTagCodeByTagName(wrapperName);
        List<String> metricNames = signalWrapperRelateMetric.getMetricNames();
        List<String> existMetricNames = new ArrayList<>();
        int wrapperId = signalWrapperRelateMetric.getId();
        if (metricNames != null && metricNames.size() > 0) {
            for (String metric : metricNames) {
                MetricTagRelation metricTagRelation = new MetricTagRelation();
                metricTagRelation.setMetricTagCode(tagCode);
                MetricModel metricModel = metricMapper.getMetric(metric);
                if (metricModel != null) {
                    metricTagRelation.setMetricCode(metricModel.getMetricCode());
                    List<MetricTagRelation> metricTagRelationList = metricTagRelationMapper.findMetricTagRelation(metricTagRelation);
                    if (metricTagRelationList.size() > 0 && (metricTagRelationList.get(0).getId() != wrapperId)) {
                        existMetricNames.add(metric);
                    } else {
                        metricTagRelation.setComment(signalWrapperRelateMetric.getComment());
                        metricTagRelation.setCreateDate(new Date());
                        metricTagRelation.setUserName(signalWrapperRelateMetric.getUserName());
                        addOrEditSignalWrapperAndMetric(wrapperId, metricTagRelation);
                    }
                }
            }
        }

        return new ResponseEntity<>(ServerResponse.buildOkJson(existMetricNames), HttpStatus.OK);
    }

    @RequestMapping(value = "/signalWrapper/findSignalWrapper", method = RequestMethod.PUT)
    public ResponseEntity<String> findSignalWrapper(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        FindSignalWrapperReq findSignalWrapperReq = JSON.parseObject(bodyStr, FindSignalWrapperReq.class);
        String wrapperName = findSignalWrapperReq.getWrapperName();
        String metricName = findSignalWrapperReq.getMetricName();
        String combineName = findSignalWrapperReq.getCombineName();
        List<FindSignalWrapperRes> findSignalWrapperRes = null;
        if (!StringUtils.isBlank(combineName)) {
            findSignalWrapperRes = metricTagRelationMapper.findSignalWrapperByMetricName(combineName + SFMonitorConstant.FUZZY_QUERY_TAG);
            findSignalWrapperRes.addAll(metricTagRelationMapper.findSignalWrapperByWrapperName(combineName + SFMonitorConstant.FUZZY_QUERY_TAG));
            return new ResponseEntity<>(ServerResponse.buildOkJson(findSignalWrapperRes), HttpStatus.OK);
        }
        if (!StringUtils.isBlank(metricName)) {
            if(metricName.equals(SFMonitorConstant.ALL_PARAMETER)){
                findSignalWrapperRes = metricTagRelationMapper.findAllSignalWrapper();
            }else{
                findSignalWrapperRes = metricTagRelationMapper.findSignalWrapperByMetricName(metricName + SFMonitorConstant.FUZZY_QUERY_TAG);
            }

            return new ResponseEntity<>(ServerResponse.buildOkJson(findSignalWrapperRes), HttpStatus.OK);
        }
        if (!StringUtils.isBlank(wrapperName)) {
            if(wrapperName.equals(SFMonitorConstant.ALL_PARAMETER)){
                findSignalWrapperRes = metricTagRelationMapper.findAllSignalWrapper();
            }else{
                findSignalWrapperRes = metricTagRelationMapper.findSignalWrapperByWrapperName(wrapperName + SFMonitorConstant.FUZZY_QUERY_TAG);
            }

            return new ResponseEntity<>(ServerResponse.buildOkJson(findSignalWrapperRes), HttpStatus.OK);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(findSignalWrapperRes), HttpStatus.OK);
    }


    @GetMapping("/signalWrapper/fuzzyFind/findSignalWrapper/{wrapperNameOrSignalName}")
    public ResponseEntity<String> findSignalWrapperByName(@PathVariable String wrapperNameOrSignalName){
        List<FindSignalWrapperRes> findSignalWrapperRes = null;
        Set<String> wrapperNameAndSignalName = new HashSet<>();
        if(wrapperNameOrSignalName.equals(SFMonitorConstant.ALL_PARAMETER)){
            findSignalWrapperRes = metricTagRelationMapper.findAllSignalWrapper();
            for(FindSignalWrapperRes signalWrapper:findSignalWrapperRes){
                wrapperNameAndSignalName.add(signalWrapper.getTagName());
                wrapperNameAndSignalName.add(signalWrapper.getMetricName());
            }
        }else{
            findSignalWrapperRes = metricTagRelationMapper.fuzzyFindSignalWrapperByWrapperName(wrapperNameOrSignalName + SFMonitorConstant.FUZZY_QUERY_TAG);
            for(FindSignalWrapperRes signalWrapper:findSignalWrapperRes){
                wrapperNameAndSignalName.add(signalWrapper.getTagName());
            }
            findSignalWrapperRes = metricTagRelationMapper.fuzzyFindSignalWrapperByMetricName(wrapperNameOrSignalName + SFMonitorConstant.FUZZY_QUERY_TAG);
            for(FindSignalWrapperRes signalWrapper:findSignalWrapperRes){
                wrapperNameAndSignalName.add(signalWrapper.getMetricName());
            }
        }

        return new ResponseEntity<>(ServerResponse.buildOkJson(new ArrayList<>(wrapperNameAndSignalName)), HttpStatus.OK);
    }

    @GetMapping(value = "/signalWrapper/findSignalWrapper/{id}")
    public ResponseEntity<String> findSignalWrapperById(@PathVariable int id) {
        SignalWrapperRelateMetric signalWrapperRelateMetric = metricTagRelationMapper.findTagById(id);
        String metricName = metricTagRelationMapper.getMetricNameByTagId(id);
        List<String> metricNames = new ArrayList<>();
        metricNames.add(metricName);
        signalWrapperRelateMetric.setMetricNames(metricNames);
        return new ResponseEntity<>(ServerResponse.buildOkJson(signalWrapperRelateMetric), HttpStatus.OK);
    }

    @RequestMapping(value = "/signalWrapper/signalWrapperItem/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSignalWrapperConfig(@PathVariable int id) {
        metricTagRelationMapper.deleteSignalWrapperConfig(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @GetMapping("/signalWrapper/findAllSignalWrapper/pageNum/{pageNum}/pageSize/{pageSize}")
    public ResponseEntity<String> findAllSignalWrapper(@PathVariable int pageNum, @PathVariable int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        SignalWrapperPage signalWrapperPage = new SignalWrapperPage();
        signalWrapperPage.setFindSignalWrapperRes(metricTagRelationMapper.findAllSignalWrapper());
        signalWrapperPage.setCount(metricTagRelationMapper.getSignalWrapperItemCount());
        return new ResponseEntity<>(ServerResponse.buildOkJson(signalWrapperPage), HttpStatus.OK);
    }

    private void addOrEditSignalWrapperAndMetric(int wrapperId, MetricTagRelation metricTagRelation) {
        if (wrapperId == 0) {//add
            metricTagRelationMapper.addMetricTagRelation(metricTagRelation);
        } else {//edit
            metricTagRelation.setId(wrapperId);
            metricTagRelationMapper.deleteSignalWrapperConfig(metricTagRelation.getId());
            metricTagRelationMapper.addMetricTagRelation(metricTagRelation);
        }
    }

    private String getTagCodeByTagName(String tagName) {
        MetricTag metricTag = metricTagMapper.getMetricTagByName(tagName);
        String tagCode = null;
        if (metricTag != null) {
            tagCode = metricTag.getCode();
        }
        return tagCode;
    }

    @GetMapping("/signalWrapper/style")
    public ResponseEntity<String> getAllStyleName() {
        return new ResponseEntity<>(ServerResponse.buildOkJson(sfMonStyleMapper.getAllStyle()), HttpStatus.OK);
    }

    @GetMapping("/signalWrapper/style/allWrapperName")
    public ResponseEntity<String> getAllSignalWrapperName() {
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricTagMapper.getAllSignalWrapper(SFMonitorConstant.MONITOR_GROUP)), HttpStatus.OK);
    }

    @RequestMapping(value = "/signalWrapper/style", method = RequestMethod.POST)
    public ResponseEntity<String> addOrEditSignalWrapperStyle(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        SignalWrapperStyleReq signalWrapperStyleReq = JSON.parseObject(bodyStr, SignalWrapperStyleReq.class);
        int id = signalWrapperStyleReq.getId();
        String styleName = signalWrapperStyleReq.getStyleName();
        List<String> signalWrapperNames = signalWrapperStyleReq.getSignalWrapperNames();
        String userName = signalWrapperStyleReq.getUserName();
        List<String> existStyleNames = new ArrayList<>();
        if (!StringUtils.isBlank(styleName) && (signalWrapperNames != null) && (signalWrapperNames.size() != 0)) {
            String styleCode = sfMonStyleMapper.getCodeByName(styleName);
            for (String name : signalWrapperNames) {
                String signalWrapperCode = getTagCodeByTagName(name);
                RelSFMonMetricTagStyle relSFMonMetricTagStyle = new RelSFMonMetricTagStyle();
                relSFMonMetricTagStyle.setMetricTagCode(signalWrapperCode);
                relSFMonMetricTagStyle.setStyleCode(styleCode);
                RelSFMonMetricTagStyle style = relSFMonMetricTagStyleMapper.getMetricTagStyle(relSFMonMetricTagStyle);
                if (style != null && (style.getId() != id)) {
                    existStyleNames.add(name);
                } else {
                    relSFMonMetricTagStyle.setEditor(userName);
                    relSFMonMetricTagStyle.setCreateDate(new Date());
                    relSFMonMetricTagStyle.setComment(signalWrapperStyleReq.getComment());
                    String code = addOrEditSignalWrapperStyle(id, relSFMonMetricTagStyle);
                    if(!StringUtils.isBlank(code)){
                        existStyleNames.add(name);
                    }
                }
            }
        }

        return new ResponseEntity<>(ServerResponse.buildOkJson(existStyleNames), HttpStatus.OK);
    }

    private String addOrEditSignalWrapperStyle(int wrapperAndStyleRelId, RelSFMonMetricTagStyle relSFMonMetricTagStyle) {
        String code = null;
        if (wrapperAndStyleRelId == 0) {//add
            String wrapperCode = relSFMonMetricTagStyleMapper.getWrapperCode(relSFMonMetricTagStyle.getMetricTagCode());
            if(StringUtils.isBlank(wrapperCode)){
                relSFMonMetricTagStyleMapper.addStyle(relSFMonMetricTagStyle);
            }else{
                code = relSFMonMetricTagStyle.getMetricTagCode();
            }
        } else {//edit
            relSFMonMetricTagStyle.setId(wrapperAndStyleRelId);
            relSFMonMetricTagStyleMapper.updateStyle(relSFMonMetricTagStyle);
        }
        return code;
    }

    @RequestMapping(value = "/signalWrapper/style/findAllSignalWrapperStyle", method = RequestMethod.PUT)
    public ResponseEntity<String> findAllSignalWrapperStyleByCondition(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        FindSignalWrapperReq findSignalWrapperReq = JSON.parseObject(bodyStr, FindSignalWrapperReq.class);
        String wrapperName = findSignalWrapperReq.getWrapperName();
        String styleName = findSignalWrapperReq.getMetricName();
        String combineName = findSignalWrapperReq.getCombineName();
        List<FindSignalWrapperRes> findSignalWrapperRes = null;
        if (!StringUtils.isBlank(combineName)) {
            findSignalWrapperRes = relSFMonMetricTagStyleMapper.getSiganlWrapperStyleByStyleName(combineName + SFMonitorConstant.FUZZY_QUERY_TAG);
            findSignalWrapperRes.addAll(relSFMonMetricTagStyleMapper.getSiganlWrapperStyleByWrapperName(combineName + SFMonitorConstant.FUZZY_QUERY_TAG));
            return new ResponseEntity<>(ServerResponse.buildOkJson(findSignalWrapperRes), HttpStatus.OK);
        }
        if (!StringUtils.isBlank(styleName)) {
            if(styleName.equals(SFMonitorConstant.ALL_PARAMETER)){
                findSignalWrapperRes = relSFMonMetricTagStyleMapper.getAllSignalWrapperStyle();
            }else{
                findSignalWrapperRes = relSFMonMetricTagStyleMapper.getSiganlWrapperStyleByStyleName(styleName + SFMonitorConstant.FUZZY_QUERY_TAG);
            }
            return new ResponseEntity<>(ServerResponse.buildOkJson(findSignalWrapperRes), HttpStatus.OK);
        }
        if (!StringUtils.isBlank(wrapperName)) {
            if(wrapperName.equals(SFMonitorConstant.ALL_PARAMETER)){
                findSignalWrapperRes = relSFMonMetricTagStyleMapper.getAllSignalWrapperStyle();
            }else{
                findSignalWrapperRes = relSFMonMetricTagStyleMapper.getSiganlWrapperStyleByWrapperName(wrapperName + SFMonitorConstant.FUZZY_QUERY_TAG);
            }

            return new ResponseEntity<>(ServerResponse.buildOkJson(findSignalWrapperRes), HttpStatus.OK);
        }

        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }


    @GetMapping("/signalWrapper/style/fuzzyFind/findAllSignalWrapperStyle/{wrapperNameOrStyleName}")
    public ResponseEntity<String> findAllSignalWrapperStyleByName(@PathVariable String wrapperNameOrStyleName){
        List<FindSignalWrapperRes> findSignalWrapperRes = null;
        Set<String> wrapperNameAndStyleNames = new HashSet<>();
        if(wrapperNameOrStyleName.equals(SFMonitorConstant.ALL_PARAMETER)){
            findSignalWrapperRes = relSFMonMetricTagStyleMapper.getAllSignalWrapperStyle();
            for(FindSignalWrapperRes signalWrapper:findSignalWrapperRes){
                wrapperNameAndStyleNames.add(signalWrapper.getTagName());
                wrapperNameAndStyleNames.add(signalWrapper.getMetricName());
            }
        }else{
            findSignalWrapperRes = relSFMonMetricTagStyleMapper.fuzzyGetSiganlWrapperStyleByWrapperName(wrapperNameOrStyleName + SFMonitorConstant.FUZZY_QUERY_TAG);
            for(FindSignalWrapperRes signalWrapper:findSignalWrapperRes){
                wrapperNameAndStyleNames.add(signalWrapper.getTagName());
            }
            findSignalWrapperRes = relSFMonMetricTagStyleMapper.fuzzyGetSiganlWrapperStyleByStyleName(wrapperNameOrStyleName + SFMonitorConstant.FUZZY_QUERY_TAG);
            for(FindSignalWrapperRes signalWrapper:findSignalWrapperRes){
                wrapperNameAndStyleNames.add(signalWrapper.getMetricName());
            }
        }

        return new ResponseEntity<>(ServerResponse.buildOkJson(new ArrayList<>(wrapperNameAndStyleNames)), HttpStatus.OK);
    }

    @GetMapping(value = "/signalWrapper/style/findAllSignalWrapperStyle/pageNum/{pageNum}/pageSize/{pageSize}")
    public ResponseEntity<String> getAllSignalWrapperStyle(@PathVariable int pageNum, @PathVariable int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FindSignalWrapperRes> findSignalWrapperRes = relSFMonMetricTagStyleMapper.getAllSignalWrapperStyle();
        SignalWrapperStylePage signalWrapperStylePage = new SignalWrapperStylePage();
        signalWrapperStylePage.setFindSignalWrapperRes(findSignalWrapperRes);
        signalWrapperStylePage.setCount(relSFMonMetricTagStyleMapper.getSignalWrapperStyleCount());
        return new ResponseEntity<>(ServerResponse.buildOkJson(signalWrapperStylePage), HttpStatus.OK);
    }

    @GetMapping(value = "/signalWrapper/style/findSignalWrapperStyle/{id}")
    public ResponseEntity<String> findSignalWrapperStyleById(@PathVariable int id) {
        return new ResponseEntity<>(ServerResponse.buildOkJson(relSFMonMetricTagStyleMapper.getSignalWrapperStyleById(id)), HttpStatus.OK);
    }

    @RequestMapping(value = "/signalWrapper/style/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSignalWrapperStyle(@PathVariable int id) {
        relSFMonMetricTagStyleMapper.deleteStyle(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @GetMapping("/equipmentConfig/thing/{thingCode}")
    public ResponseEntity<String> getEquipmentInfo(@PathVariable String thingCode) {
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingTagMapper.getEquipmentByCode(thingCode + SFMonitorConstant.FUZZY_QUERY_TAG, SFMonitorConstant.EQUIPMENT)), HttpStatus.OK);
    }

    @RequestMapping(value = "/equipmentConfig/addEquipment", method = RequestMethod.POST)
    public ResponseEntity<String> addEquipmentConfig(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        SFMonEquipMonitorInfo sfMonEquipMonitorInfo = JSON.parseObject(bodyStr, SFMonEquipMonitorInfo.class);
        sfMonEquipMonitorInfo.setConfigProgress(SFMonitorConstant.NOT_CONFIG);
        sfMonEquipMonitorInfo.setCreateDate(new Date());
        String thingCode = sfMonEquipMonitorInfo.getThingCode();
        SFMonEquipMonitorInfo equipmentInfo = sfMonEquipMonitorInfoMapper.getEquiupmentInfo(thingCode);
        boolean isExist = false;
        if(equipmentInfo != null){
            isExist = true;
        }else{
            sfMonEquipMonitorInfoMapper.addEquipmentConfig(sfMonEquipMonitorInfo);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(isExist), HttpStatus.OK);
    }

    @GetMapping("/equipmentConfig/findAllEquipment/pageNum/{pageNum}/pageSize/{pageSize}")
    public ResponseEntity<String> findAllEquipment(@PathVariable int pageNum, @PathVariable int pageSize){
        EquipmentMonitorPageRes equipmentMonitorPageRes = new EquipmentMonitorPageRes();
        PageHelper.startPage(pageNum, pageSize);
        List<SFMonEquipMonitorInfo> sfMonEquipMonitorInfos = sfMonEquipMonitorInfoMapper.getEquipmentInfoPage();
        equipmentMonitorPageRes.setSfMonEquipMonitorInfos(sfMonEquipMonitorInfos);
        equipmentMonitorPageRes.setCount(sfMonEquipMonitorInfoMapper.getEquipmentMonitorCount());
        return new ResponseEntity<>(ServerResponse.buildOkJson(equipmentMonitorPageRes), HttpStatus.OK);
    }

    @RequestMapping(value = "/equipmentConfig/metrics/thing/{thingCode}/metricName/{metricName}")
    public ResponseEntity<String> getMetricNamesByThingCode(@PathVariable String thingCode,@PathVariable String metricName) {
        if(SFMonitorConstant.ALL_PARAMETER.equals(metricName)){
            return new ResponseEntity<>(ServerResponse.buildOkJson(relSFMonMetricTagStyleMapper.getAllMetricNamesByThingCode(thingCode)), HttpStatus.OK);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(relSFMonMetricTagStyleMapper.getMetricNamesByThingCode(thingCode,metricName + SFMonitorConstant.FUZZY_QUERY_TAG)), HttpStatus.OK);
    }

    @RequestMapping(value="/equipmentConfig/editEquipmentMonitorInfo",method=RequestMethod.POST)
    public ResponseEntity<String> editEquipmentMonitorInfo(@RequestBody String bodyStr){
        ControllerUtil.validateBodyRequired(bodyStr);
        EquipmentRelateToSignalWrapperReq equipmentRelateToSignalWrapperReq = JSON.parseObject(bodyStr,EquipmentRelateToSignalWrapperReq.class);
        if(equipmentRelateToSignalWrapperReq !=  null){
            monitorService.editEquipmentMonitorInfo(equipmentRelateToSignalWrapperReq);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @GetMapping("/equipmentConfig/getEquipmentMonitorInfo/{id}")
    public ResponseEntity<String> getEquipmentMonitorInfo(@PathVariable int id){
        EquipmentRelateToSignalWrapperReq equipmentRelateToSignalWrapperReq = getEquipmentMonitorData(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(equipmentRelateToSignalWrapperReq), HttpStatus.OK);
    }

    @RequestMapping(value = "/equipmentConfig/deleteEquipmentMonitorInfo/{id}",method=RequestMethod.DELETE)
    public ResponseEntity<String> deleteEquipmentMonitorInfo(@PathVariable int id){
        monitorService.deleteEquipmentConfig(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/equipmentConfig/findEquipmentMonitorInfo",method = RequestMethod.PUT)
    public ResponseEntity<String> findEquipmentMonitorInfo(@RequestBody String bodyStr){
        ControllerUtil.validateBodyRequired(bodyStr);
        EquipmentMonFindReq equipmentMonFindReq = JSON.parseObject(bodyStr,EquipmentMonFindReq.class);
        String configProgress = equipmentMonFindReq.getConfigProgress();
        String thingCode = equipmentMonFindReq.getThingCode();
        SFMonEquipMonitorInfo sfMonEquipMonitorInfo = new SFMonEquipMonitorInfo();
        if(!StringUtils.isBlank(configProgress)){
            sfMonEquipMonitorInfo.setConfigProgress(configProgress);
            return new ResponseEntity<>(ServerResponse.buildOkJson(sfMonEquipMonitorInfoMapper.getEquipmentMonitorInfo(sfMonEquipMonitorInfo)), HttpStatus.OK);
        }
        if(!StringUtils.isBlank(thingCode)){
            sfMonEquipMonitorInfo.setThingCode(thingCode + SFMonitorConstant.FUZZY_QUERY_TAG);
            return new ResponseEntity<>(ServerResponse.buildOkJson(sfMonEquipMonitorInfoMapper.getEquipmentMonitorInfo(sfMonEquipMonitorInfo)), HttpStatus.OK);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }


    @GetMapping("/equipmentConfig/fuzzyFind/thing/{thingCode}")
    public ResponseEntity<String> findEquipmentMonitorInfoByThingCode(@PathVariable String thingCode){
        SFMonEquipMonitorInfo sfMonEquipMonitorInfo = new SFMonEquipMonitorInfo();
        Set<String> thingCodes = new HashSet<>();
        if(thingCode.equals(SFMonitorConstant.ALL_PARAMETER)){
            List<SFMonEquipMonitorInfo> sfMonEquipMonitorInfos = sfMonEquipMonitorInfoMapper.getEquipmentMonitorInfo(sfMonEquipMonitorInfo);
            for(SFMonEquipMonitorInfo data:sfMonEquipMonitorInfos){
                thingCodes.add(data.getThingCode());
                thingCodes.add(data.getThingName());
            }
        }else{
            sfMonEquipMonitorInfo.setThingCode(thingCode + SFMonitorConstant.FUZZY_QUERY_TAG);
            List<SFMonEquipMonitorInfo> sfMonEquipMonitorInfos = sfMonEquipMonitorInfoMapper.getEquipmentMonitorInfo(sfMonEquipMonitorInfo);
            for(SFMonEquipMonitorInfo data:sfMonEquipMonitorInfos){
                thingCodes.add(data.getThingCode());
            }
            thingCodes.addAll(sfMonEquipMonitorInfoMapper.getEquipmentNames(thingCode + SFMonitorConstant.FUZZY_QUERY_TAG));
        }

        return new ResponseEntity<>(ServerResponse.buildOkJson(new ArrayList<>(thingCodes)), HttpStatus.OK);
    }

    @RequestMapping(value = "/monitorData",method = RequestMethod.PUT)
    public ResponseEntity<String> getMonitorPadData(@RequestBody String bodyStr){
        ControllerUtil.validateBodyRequired(bodyStr);
        SFMonPadReq sfMonPadReq = JSON.parseObject(bodyStr,SFMonPadReq.class);
        String thingCode = sfMonPadReq.getThingCode();
        SFMonPadRes sfMonPadRes = new SFMonPadRes();
        List<SFMonPadJumpZoneInfo> sfMonPadJumpZoneInfoList = getJumpZoneData(thingCode);
        List<SFMonPadParameterZoneInfo> sfMonPadParameterZoneInfos = getParameterZoneData(sfMonPadReq);
        SFMonPadStateControlInfo sfMonPadStateControlInfo = getStateControlZoneData(thingCode,SFMonitorConstant.STATE_AREA);
        List<SFMonPadAuxiliaryZoneInfo> sfMonPadAuxiliaryZoneInfos = getAuxiliaryZoneData(thingCode);
        sfMonPadRes.setSfMonPadJumpZoneInfoList(sfMonPadJumpZoneInfoList);
        sfMonPadRes.setSfMonPadParameterZoneInfos(sfMonPadParameterZoneInfos);
        sfMonPadRes.setSfMonPadAuxiliaryZoneInfos(sfMonPadAuxiliaryZoneInfos);
        sfMonPadRes.setSfMonPadStateControlInfo(sfMonPadStateControlInfo);
        return new ResponseEntity<>(ServerResponse.buildOkJson(sfMonPadRes), HttpStatus.OK);
    }

    private List<SFMonPadAuxiliaryZoneInfo> getAuxiliaryZoneData(String thingCode){
        List<SFMonPadAuxiliaryZoneInfo> sfMonPadAuxiliaryZoneInfos = new ArrayList<>();
        List<String> thingCodeList = tmlMapper.findRelateThing(thingCode + SFMonitorConstant.FUZZY_QUERY_TAG);
        List<Integer> directions = new ArrayList<>();
        directions.add(SFMonitorConstant.DIRECTION);
        directions.add(SFMonitorConstant.DIRECTION_LEFT);
        directions.add(SFMonitorConstant.DIRECTION_RIGHT);
        for(String code:thingCodeList){
            List<SFMonPadAuxiliaryZoneInfo> sfMonPadAuxiliaryZoneInfoList = sfMonEquipMonitorConfigMapper.getAuxiliaryZoneData(code,SFMonitorConstant.AUXILIARY_AREA,directions);
            if(sfMonPadAuxiliaryZoneInfoList.size() > 0){
                sfMonPadAuxiliaryZoneInfos.addAll(sfMonPadAuxiliaryZoneInfoList);
            }
        }
        List<SFMonPadAuxiliaryZoneInfo> sfMonPadAuxiliaryZoneInfoList = new ArrayList<>();
        int i = 0;
        while(i < sfMonPadAuxiliaryZoneInfos.size()){
            List<SFMonPadAuxiliaryZoneMetricInfo> sfMonPadAuxiliaryZoneMetricInfoList = new ArrayList<>();
            SFMonPadAuxiliaryZoneInfo data = sfMonPadAuxiliaryZoneInfos.get(i);
            SFMonPadAuxiliaryZoneInfo compositionInfo = new SFMonPadAuxiliaryZoneInfo();
            SFMonPadAuxiliaryZoneInfo remoteValve = new SFMonPadAuxiliaryZoneInfo();
            List<SFMonPadAuxiliaryZoneMetricInfo> remoteValveInfoList = new ArrayList<>();
            if(data.getRule() == SFMonitorConstant.SINGLE_PARTION
                    || (data.getRule() == 0)){
                sfMonPadAuxiliaryZoneInfoList.add(data);
                sfMonPadAuxiliaryZoneInfos.remove(i);
                continue;
            }
            specialRuleOperate(sfMonPadAuxiliaryZoneInfos,data,sfMonPadAuxiliaryZoneMetricInfoList,remoteValveInfoList,sfMonPadAuxiliaryZoneInfoList);

            if((sfMonPadAuxiliaryZoneMetricInfoList != null) && (sfMonPadAuxiliaryZoneMetricInfoList.size() > 0)){
                removeEquipmentData(sfMonPadAuxiliaryZoneMetricInfoList);
                compositionInfo.setSfMonPadAuxiliaryZoneMetricInfoList(sfMonPadAuxiliaryZoneMetricInfoList);
                compositionInfo.setWrapperName(data.getWrapperName());
                compositionInfo.setStyleName(data.getStyleName());
                sfMonPadAuxiliaryZoneInfoList.add(compositionInfo);
                i--;
            }
            if((remoteValveInfoList != null) && (remoteValveInfoList.size() > 0)){
                removeEquipmentData(remoteValveInfoList);
                remoteValve.setSfMonPadAuxiliaryZoneMetricInfoList(remoteValveInfoList);
                remoteValve.setWrapperName(data.getWrapperName());
                remoteValve.setStyleName(data.getStyleName());
                sfMonPadAuxiliaryZoneInfoList.add(remoteValve);
                i--;
            }
            i++;
            if(sfMonPadAuxiliaryZoneInfos.size() == 0){
                break;
            }
        }
        removeRepeatDataAndSetValue(sfMonPadAuxiliaryZoneInfoList);
        return sfMonPadAuxiliaryZoneInfoList;
    }

    private void removeEquipmentData(List<SFMonPadAuxiliaryZoneMetricInfo> sfMonPadAuxiliaryZoneMetricInfoList){
        int i = 0;
        while(i < sfMonPadAuxiliaryZoneMetricInfoList.size()){
            if(sfMonPadAuxiliaryZoneMetricInfoList.get(i).getThingCode().split("\\.").length < 2){
                sfMonPadAuxiliaryZoneMetricInfoList.remove(i);
            }else{
                i++;
            }
        }
    }


    private void specialRuleOperate(List<SFMonPadAuxiliaryZoneInfo> sfMonPadAuxiliaryZoneInfos,SFMonPadAuxiliaryZoneInfo sfMonPadAuxiliaryZoneInfo,
                                    List<SFMonPadAuxiliaryZoneMetricInfo> sfMonPadAuxiliaryZoneMetricInfoList,
                                    List<SFMonPadAuxiliaryZoneMetricInfo> remoteValveInfoList,
                                    List<SFMonPadAuxiliaryZoneInfo> sfMonPadAuxiliaryZoneInfoList){
        int i = 0;
        while(i < sfMonPadAuxiliaryZoneInfos.size()){
            if((sfMonPadAuxiliaryZoneInfo.getRule() == SFMonitorConstant.COMPOSITION_ALL_PARTION)
                    && sfMonPadAuxiliaryZoneInfo.getWrapperName().equals(sfMonPadAuxiliaryZoneInfos.get(i).getWrapperName())){
                if(sfMonPadAuxiliaryZoneInfo.getSfMonPadAuxiliaryZoneMetricInfoList().get(0).getThingCode().split("\\.").length > 1){
                    sfMonPadAuxiliaryZoneMetricInfoList.addAll(sfMonPadAuxiliaryZoneInfos.get(i).getSfMonPadAuxiliaryZoneMetricInfoList());
                    sfMonPadAuxiliaryZoneInfos.remove(i);
                    continue;
                }else{
                    sfMonPadAuxiliaryZoneInfoList.add(sfMonPadAuxiliaryZoneInfo);
                    break;
                }
            }else if((sfMonPadAuxiliaryZoneInfo.getRule() == SFMonitorConstant.REMOTE_VALVE)
                    && (sfMonPadAuxiliaryZoneInfo.getWrapperName().equals(sfMonPadAuxiliaryZoneInfos.get(i).getWrapperName()))){//远程阀门
                if(sfMonPadAuxiliaryZoneInfo.getSfMonPadAuxiliaryZoneMetricInfoList().get(0).getThingCode().split("\\.").length > 1){
                    remoteValveInfoList.addAll(sfMonPadAuxiliaryZoneInfos.get(i).getSfMonPadAuxiliaryZoneMetricInfoList());
                    sfMonPadAuxiliaryZoneInfos.remove(i);
                    continue;
                }else{
                    sfMonPadAuxiliaryZoneInfoList.add(sfMonPadAuxiliaryZoneInfo);
                    break;
                }
            }
            i++;
        }
    }

    private void removeRepeatDataAndSetValue(List<SFMonPadAuxiliaryZoneInfo> sfMonPadAuxiliaryZoneInfoList){
        int i = 0;
        while(i < sfMonPadAuxiliaryZoneInfoList.size()){
            if(sfMonPadAuxiliaryZoneInfoList.get(i).getSfMonPadAuxiliaryZoneMetricInfoList().size() == 0){
                sfMonPadAuxiliaryZoneInfoList.remove(i);
                continue;
            }
            List<SFMonPadAuxiliaryZoneMetricInfo> sfMonPadAuxiliaryZoneMetricInfos = sfMonPadAuxiliaryZoneInfoList.get(i).getSfMonPadAuxiliaryZoneMetricInfoList();
            for(SFMonPadAuxiliaryZoneMetricInfo sfMonPadAuxiliaryZoneMetricInfo:sfMonPadAuxiliaryZoneMetricInfos){
                MetricModel metric = metricMapper.getMetric(sfMonPadAuxiliaryZoneMetricInfo.getMetricName());
                if(metric != null){
                    Optional<DataModelWrapper> wrapper = dataService.getData(sfMonPadAuxiliaryZoneMetricInfo.getThingCode(),metric.getMetricCode());
                    if((wrapper.isPresent()) && (!wrapper.get().isEmpty()) && (!StringUtils.isBlank(wrapper.get().getValue()))){
                        String value = wrapper.get().getValue();
                        sfMonPadAuxiliaryZoneMetricInfo.setValue(value);
                    }
                }
            }
            i++;
        }
    }

    private SFMonPadStateControlInfo getStateControlZoneData(String thingCode,String zoneCode){
        SFMonPadStateControlInfo sfMonPadStateControlInfo = new SFMonPadStateControlInfo();
        List<SFMonPadStateControlMetricInfo> sfMonPadStateControlMetricInfos = sfMonEquipMonitorConfigMapper.getStateControlZoneData(thingCode,zoneCode,SFMonitorConstant.STATE_CONTRL,SFMonitorConstant.DIRECTION);
        sfMonPadStateControlInfo.setPowerEquipment(true);
        if((sfMonPadStateControlMetricInfos != null) && (sfMonPadStateControlMetricInfos.size() == 1
        && sfMonPadStateControlMetricInfos.get(0).getMetricName().equals(SFMonitorConstant.EQUIPMENT_STATE))){
            sfMonPadStateControlInfo.setPowerEquipment(false);
        }
        for(SFMonPadStateControlMetricInfo sfMonPadStateControlMetricInfo:sfMonPadStateControlMetricInfos){
            MetricModel metric = metricMapper.getMetric(sfMonPadStateControlMetricInfo.getMetricName());
            if(metric != null){
                Optional<DataModelWrapper> wrapper = dataService.getData(thingCode,metric.getMetricCode());
                if((wrapper.isPresent()) && (!wrapper.get().isEmpty()) && (!StringUtils.isBlank(wrapper.get().getValue()))){
                    String value = wrapper.get().getValue();
                    sfMonPadStateControlMetricInfo.setValue(value);
                }
            }
        }
        sfMonPadStateControlInfo.setSfMonPadStateControlMetricInfos(sfMonPadStateControlMetricInfos);
        return sfMonPadStateControlInfo;
    }
    private List<SFMonPadParameterZoneInfo> getParameterZoneData(SFMonPadReq sfMonPadReq){
        String thingCode = sfMonPadReq.getThingCode();
        long interval = sfMonPadReq.getInterval();
        Date endDate = new Date();
        Date startDate = new Date(endDate.getTime() - interval * SFMonitorConstant.SECOND_TO_MILLSECOND);
        int segment = sfMonPadReq.getSegment();
        List<SFMonPadParameterZoneInfo> sfMonPadParameterZoneInfos = sfMonEquipMonitorConfigMapper.getParameterData(thingCode,SFMonitorConstant.SELECTED_PARAMETER);
        for(SFMonPadParameterZoneInfo sfMonPadParameterZoneInfo:sfMonPadParameterZoneInfos){
            Optional<DataModelWrapper> wrapper = dataService.getData(thingCode,sfMonPadParameterZoneInfo.getMetricCode());
            if((wrapper.isPresent()) && (!wrapper.get().isEmpty()) && (!StringUtils.isBlank(wrapper.get().getValue()))){
                String value = wrapper.get().getValue();
                sfMonPadParameterZoneInfo.setValue(value);
            }
            List<String> thingCodes = new ArrayList<>();
            thingCodes.add(thingCode);
            Map<String,List<DataModel>> hisData = historyDataService.findMultiThingsHistoryDataOfMetricBySegment(thingCodes,sfMonPadParameterZoneInfo.getMetricCode(),startDate,endDate,segment,true);
            sfMonPadParameterZoneInfo.setHisData(hisData);
            sfMonPadParameterZoneInfo.setStartDate(startDate);
            sfMonPadParameterZoneInfo.setEndDate(endDate);
        }

        return sfMonPadParameterZoneInfos;
    }

    private List<SFMonPadJumpZoneInfo> getJumpZoneData(String thingCode){
        List<SFMonPadJumpZoneInfo> sfMonPadJumpZoneInfos = new ArrayList<>();
        List<SFMonPadJumpZoneEquipmentInfo> jumpZoneData = sfMonEquipMonitorConfigMapper.getJumpZoneData(thingCode,SFMonitorConstant.KEY_CHANNEL);
        SFMonPadJumpZoneInfo sfMonPadChannelZoneInfo = new SFMonPadJumpZoneInfo();
        sfMonPadChannelZoneInfo.setZoneCode(SFMonitorConstant.KEY_CHANNEL);
        sfMonPadChannelZoneInfo.setSfMonPadJumpZoneEquipmentInfos(jumpZoneData);
        sfMonPadJumpZoneInfos.add(sfMonPadChannelZoneInfo);

        jumpZoneData = sfMonEquipMonitorConfigMapper.getJumpZoneData(thingCode,SFMonitorConstant.FROM);
        SFMonPadJumpZoneInfo sfMonPadFromZoneInfo = new SFMonPadJumpZoneInfo();
        sfMonPadFromZoneInfo.setZoneCode(SFMonitorConstant.FROM);
        sfMonPadFromZoneInfo.setSfMonPadJumpZoneEquipmentInfos(jumpZoneData);
        sfMonPadJumpZoneInfos.add(sfMonPadFromZoneInfo);
        jumpZoneData = sfMonEquipMonitorConfigMapper.getJumpZoneData(thingCode,SFMonitorConstant.TO);
        SFMonPadJumpZoneInfo sfMonPadToZoneInfo = new SFMonPadJumpZoneInfo();
        sfMonPadToZoneInfo.setZoneCode(SFMonitorConstant.TO);
        sfMonPadToZoneInfo.setSfMonPadJumpZoneEquipmentInfos(jumpZoneData);
        sfMonPadJumpZoneInfos.add(sfMonPadToZoneInfo);
        jumpZoneData = sfMonEquipMonitorConfigMapper.getJumpZoneData(thingCode,SFMonitorConstant.SIMILAR);
        SFMonPadJumpZoneInfo sfMonPadSimilarZoneInfo = new SFMonPadJumpZoneInfo();
        sfMonPadSimilarZoneInfo.setZoneCode(SFMonitorConstant.SIMILAR);
        sfMonPadSimilarZoneInfo.setSfMonPadJumpZoneEquipmentInfos(jumpZoneData);
        sfMonPadJumpZoneInfos.add(sfMonPadSimilarZoneInfo);
        createJumpZoneData(sfMonPadJumpZoneInfos);
        return sfMonPadJumpZoneInfos;
    }

    private void createJumpZoneData(List<SFMonPadJumpZoneInfo> sfMonPadJumpZoneInfos){
        List<SFMonPadJumpZoneEquipmentInfo> sfMonPadJumpZoneEquipmentInfos = new ArrayList<>();
        for(SFMonPadJumpZoneInfo sfMonPadJumpZoneInfo:sfMonPadJumpZoneInfos){
            List<SFMonPadJumpZoneEquipmentInfo> sfMonPadJumpZoneEquipmentInfoList = sfMonPadJumpZoneInfo.getSfMonPadJumpZoneEquipmentInfos();
            sfMonPadJumpZoneEquipmentInfos.addAll(sfMonPadJumpZoneEquipmentInfoList);
            for(SFMonPadJumpZoneEquipmentInfo sfMonPadJumpZoneEquipmentInfo:sfMonPadJumpZoneEquipmentInfoList){
                String thingCode = sfMonPadJumpZoneEquipmentInfo.getThingCode();
                Optional<DataModelWrapper> wrapper = dataService.getData(thingCode,SFMonitorConstant.STATE);
                if((wrapper.isPresent()) && (!wrapper.get().isEmpty()) && (!StringUtils.isBlank(wrapper.get().getValue()))){
                    String state = wrapper.get().getValue();
                    sfMonPadJumpZoneEquipmentInfo.setState(state);
                }
            }
        }
    }

    private EquipmentRelateToSignalWrapperReq getEquipmentMonitorData(int id) {
        EquipmentRelateToSignalWrapperReq equipmentRelateToSignalWrapperReq = new EquipmentRelateToSignalWrapperReq();
        List<String> keychannelData = sfMonEquipMonitorConfigMapper.getJumpOrParameterData(id,SFMonitorConstant.KEY_CHANNEL);
        List<String> fromData = sfMonEquipMonitorConfigMapper.getJumpOrParameterData(id,SFMonitorConstant.FROM);
        List<String> toData = sfMonEquipMonitorConfigMapper.getJumpOrParameterData(id,SFMonitorConstant.TO);
        List<String> similarData = sfMonEquipMonitorConfigMapper.getJumpOrParameterData(id,SFMonitorConstant.SIMILAR);
        List<String> parameterAreaData = sfMonEquipMonitorConfigMapper.getJumpOrParameterData(id,SFMonitorConstant.SELECTED_PARAMETER);
        SFMonEquipMonitorInfo sfMonEquipMonitorInfo = sfMonEquipMonitorInfoMapper.getEquiupmentInfoById(id);
        String thingCode = sfMonEquipMonitorInfo.getThingCode();
        String thingName = sfMonEquipMonitorInfo.getThingName();
        List<String> relateThings = tmlMapper.findRelateThing(thingCode + SFMonitorConstant.FUZZY_QUERY_TAG);
        List<EquipmentRelateToSignalWrapper> equipmentRelateToSignalWrapperList = new ArrayList<>();
        for(String code:relateThings){
            equipmentRelateToSignalWrapperList.addAll(sfMonEquipMonitorConfigMapper.getEquipmentRelateToSignalWrapperByThingCode(code,SFMonitorConstant.AUXILIARY_AREA));
        }

        StateControlAreaInfo stateControlAreaInfo = sfMonEquipMonitorConfigMapper.getParameterAreaData(thingCode,SFMonitorConstant.STATE_AREA_FIND);
        equipmentRelateToSignalWrapperReq.setKeyChannels(keychannelData);
        equipmentRelateToSignalWrapperReq.setFromEquipments(fromData);
        equipmentRelateToSignalWrapperReq.setToEquipments(toData);
        equipmentRelateToSignalWrapperReq.setSimilarEquipments(similarData);
        equipmentRelateToSignalWrapperReq.setSelectedparameters(parameterAreaData);
        equipmentRelateToSignalWrapperReq.setThingCode(thingCode);
        equipmentRelateToSignalWrapperReq.setThingName(thingName);
        if(equipmentRelateToSignalWrapperList.size() == 0){
            equipmentRelateToSignalWrapperReq.setEquipmentRelateToSignalWrappers(getAllAuxiliaryAreaSignalWrapperByThingCode(relateThings));
        }else{
            equipmentRelateToSignalWrapperReq.setEquipmentRelateToSignalWrappers(equipmentRelateToSignalWrapperList);
        }
        if(stateControlAreaInfo == null){
            equipmentRelateToSignalWrapperReq.setStateControlAreaInfo(getAllStateAreaSignalWrapperByThingCode());
        }else{
            equipmentRelateToSignalWrapperReq.setStateControlAreaInfo(stateControlAreaInfo);
        }
        return equipmentRelateToSignalWrapperReq;
    }

    private  List<EquipmentRelateToSignalWrapper> getAllAuxiliaryAreaSignalWrapperByThingCode(List<String> thingCodes) {
        List<EquipmentRelateToSignalWrapper> equipmentRelateToSignalWrapperList = new ArrayList<>();
        for(String thingCode:thingCodes){
            List<MetricModel> metricModels = tmlMapper.findMetric(thingCode);
            Map<String, Boolean> map = getWrapperMatchRule(SFMonitorConstant.AUXILIARY_AREA);
            List<EquipmentRelateToSignalWrapper> equipmentRelateToSignalWrappers = sfMonEquipMonitorConfigMapper.getSignalWrapperData(SFMonitorConstant.AUXILIARY_AREA);
            equipmentRelateToSignalWrapperList.addAll(getEquipmentRelateToWrapperData(metricModels,map,equipmentRelateToSignalWrappers,SFMonitorConstant.AUXILIARY_AREA,thingCode));
        }

        return equipmentRelateToSignalWrapperList;
    }

    private StateControlAreaInfo getAllStateAreaSignalWrapperByThingCode(){
        StateControlAreaInfo stateControlAreaInfo = new StateControlAreaInfo();
        String wrapperName = sfMonEquipMonitorConfigMapper.getStateControlWrapperName(SFMonitorConstant.STATE_AREA);
        stateControlAreaInfo.setWrapperName(wrapperName);
        return stateControlAreaInfo;
    }

    /**
     * 获取设备状态控制区信号包及信号
     * @param thingCode
     * @return
     */
    private EquipmentRelateToSignalWrapper getStateControlWrapperData(String thingCode){
        EquipmentRelateToSignalWrapper equipmentRelateToSignalWrapper = null;
        List<MetricModel> metricModels = tmlMapper.findMetric(thingCode);
        Map<String, Boolean> map = monitorService.getWrapperMatchRule(SFMonitorConstant.STATE_AREA);
        List<EquipmentRelateToSignalWrapper> equipmentRelateToSignalWrappers = sfMonEquipMonitorConfigMapper.getSignalWrapperData(SFMonitorConstant.STATE_AREA);
        List<EquipmentRelateToSignalWrapper> equipmentRelateToSignalWrapperList = getEquipmentRelateToWrapperData(metricModels,map,equipmentRelateToSignalWrappers,SFMonitorConstant.STATE_AREA,null);
        if(equipmentRelateToSignalWrapperList.size() > 0){
            equipmentRelateToSignalWrapper = equipmentRelateToSignalWrapperList.get(0);
        }
        return equipmentRelateToSignalWrapper;
    }

    private Map<String, Boolean> getWrapperMatchRule(String zoneCode) {
        Map<String, Boolean> map = new HashMap<>();
        List<SignalWrapperMatchRule> signalWrapperMatchRules = sfMonEquipMonitorConfigMapper.getWrapperMatchRule(zoneCode);
        for (SignalWrapperMatchRule rule : signalWrapperMatchRules) {
            map.put(rule.getSignalWrapperName(), rule.isAllMatch());
        }
        return map;
    }


    /**
     * 从全集信号包及信号数据中过滤出设备对应的包及信号数据
     *
     * @param metricModels
     * @param map
     * @param equipmentRelateToSignalWrappers
     * @return
     */
    private List<EquipmentRelateToSignalWrapper> getEquipmentRelateToWrapperData(List<MetricModel> metricModels, Map<String, Boolean> map, List<EquipmentRelateToSignalWrapper> equipmentRelateToSignalWrappers,String zone,String thingCode) {
        List<EquipmentRelateToSignalWrapper> equipmentRelateToSignalWrapperList = new ArrayList<>();
        for (EquipmentRelateToSignalWrapper data : equipmentRelateToSignalWrappers) {
            String wrapperName = data.getWarpperName();
            List<SignalWrapperMetric> signalWrapperMetrics = data.getSignalWrapperMetrics();
            boolean isAllMatch = map.containsKey(wrapperName) ? map.get(wrapperName) : false;
            EquipmentRelateToSignalWrapper equipmentRelateToSignalWrapper = allOrPartialMatch(metricModels, signalWrapperMetrics, isAllMatch);
            if (equipmentRelateToSignalWrapper != null) {
                equipmentRelateToSignalWrapper.setWarpperName(wrapperName);
                equipmentRelateToSignalWrapper.setZone(zone);
                equipmentRelateToSignalWrapper.setSelected(false);
                if(!StringUtils.isBlank(thingCode)){
                    equipmentRelateToSignalWrapper.setThingCode(thingCode);
                }
                equipmentRelateToSignalWrapperList.add(equipmentRelateToSignalWrapper);
            }
        }
        return equipmentRelateToSignalWrapperList;
    }

    private EquipmentRelateToSignalWrapper allOrPartialMatch(List<MetricModel> metricModels, List<SignalWrapperMetric> signalWrapperMetrics, boolean isAllMatch) {
        EquipmentRelateToSignalWrapper equipmentRelateToSignalWrapper = null;
        int size = signalWrapperMetrics.size();
        List<String> signalWrapperMetricNames = new ArrayList<>();
        List<SignalWrapperMetric> signalWrapperMetricList = new ArrayList<>();
        for (SignalWrapperMetric signalWrapperMetric : signalWrapperMetrics) {
            signalWrapperMetricNames.add(signalWrapperMetric.getMetricName());
        }
        for (MetricModel metric : metricModels) {
            if (signalWrapperMetricNames.contains(metric.getMetricName())) {
                SignalWrapperMetric signalWrapperMetric = new SignalWrapperMetric();
                signalWrapperMetric.setMetricName(metric.getMetricName());
                if (metric.getMetricType1Code().equals(SFMonitorConstant.PARAMETER_SET) || metric.getMetricType1Code().equals(SFMonitorConstant.STATE_SET)) {
                    signalWrapperMetric.setModel(SFMonitorConstant.FIND);
                } else {
                    signalWrapperMetric.setModel(SFMonitorConstant.NO_SHOW);
                }
                signalWrapperMetricList.add(signalWrapperMetric);
            }
        }
        //设备metric和信号包中的所有metric必须全匹配才能显示，设备所有metric=信号包metric或信号包metric是设备metric的子集
        //设备metric和信号包中的所有metric只要有一个匹配就显示
        if ((isAllMatch && (signalWrapperMetricList.size() == size))
                || ((!isAllMatch) && (signalWrapperMetricList.size() > 0))) {
            equipmentRelateToSignalWrapper = new EquipmentRelateToSignalWrapper();
            equipmentRelateToSignalWrapper.setSignalWrapperMetrics(signalWrapperMetricList);
        }

        return equipmentRelateToSignalWrapper;
    }

}
