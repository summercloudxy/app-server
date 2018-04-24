package com.zgiot.app.server.module.filterpress.controller;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.filterpress.FilterPressManager;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressParam;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.common.annotation.KepServerMapping;
import com.zgiot.common.constants.FilterPressMetricConstants;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by xiayun on 2017/9/12.
 */
@Controller
public class FilterPressController {
    @Autowired
    private FilterPressManager filterPressManager;
    private static final String TYPE_FEED = "feed";
    private static final String TYPE_UNLOAD = "unload";
    private static final String CLEAN_PERIOD_VALUE = "1";
    private static final String CLEAN_PERIOD_ZERO = "0";
    private static final String IS_HOLDING_OK = "1";
    private static final String IS_HOLDING_NOT = "0";
    private static final String POSITION = "position";
    private static final String CLEAN_PERIOD = "cleanPeriod";
    private static final String IS_HOLDING = "isHolding";
    private static final int TERM1 = 1;
    private static final int TERM2 = 2;

    @Autowired
    private CmdControlService cmdControlService;

    @ApiOperation("切换进料/卸料结束确认模式：弹窗确认/系统自动")
    @PostMapping(value = "api/filterPress/param/autoManuConfirmState")
    public ResponseEntity<String> setAutoManuState(Boolean state, String type,int term) {
        if (TYPE_FEED.equals(type)) {
            filterPressManager.feedAutoManuConfirmChange(null, state,term);
        } else if (TYPE_UNLOAD.equals(type)) {
            filterPressManager.unloadAutoManuConfirmChange(null, state,term);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }

    @ApiOperation("切换进料/卸料结束判断模式：智能/手动")
    @PostMapping(value = "api/filterPress/param/intelligentManuState")
    public ResponseEntity<String> setIntelligentManuState(String thingCode, Boolean state, String type,int term) {
        if (TYPE_FEED.equals(type)) {
            filterPressManager.feedIntelligentManuChange(thingCode, state,term);
        } else if (TYPE_UNLOAD.equals(type)) {
            filterPressManager.unloadIntelligentManuChange(thingCode, state,term);
            filterPressManager.removeQueue(thingCode,state,term);

        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }

    @ApiOperation("进料结束弹窗确认")
    @PostMapping(value = "api/filterPress/feedOver/{thingCode}/confirm/term/{term}")
    public ResponseEntity<String> feedOverPopupConfirm(@PathVariable String thingCode,@PathVariable int term) {
        filterPressManager.confirmFeedOver(thingCode,term);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }

    @ApiOperation("卸料结束弹窗确认")
    @RequestMapping(value = "api/filterPress/unload/{thingCode}/confirm/term/{term}", method = RequestMethod.POST)
    public ResponseEntity<String> unloadPopupConfirm(@PathVariable String thingCode,@PathVariable int term) {
        filterPressManager.confirmUnload(thingCode,term);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }

    @ApiOperation("给app端返回弹窗时未点击确定卸料的thingCodes")
    @RequestMapping(value = "api/filterPress/unload/unConfirmUnload/term/{term}", method = RequestMethod.GET)
    public ResponseEntity<String> unConfirmUnload(@PathVariable int term) {
        String thingCode = null;
        if(term == TERM1){
            thingCode = filterPressManager.getFirstUnConfirmedUnloadTerm1();
        }else if(term == TERM2){
            thingCode = filterPressManager.getFirstUnConfirmedUnload();
        }
        Set<String> thingCodeSet = new ConcurrentSkipListSet<>();
        if(!StringUtils.isBlank(thingCode)){
            thingCodeSet.add(thingCode);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingCodeSet),
                HttpStatus.OK);
    }

    @ApiOperation("获取进料/卸料设置页参数值")
    @GetMapping(value = "api/filterPress/parameter")
    @ResponseBody
    public ResponseEntity<String> getFilterPressParameter(@RequestParam String type,@RequestParam int term) {
        FilterPressParam filterPressParam = new FilterPressParam();
        if (TYPE_FEED.equals(type)) {
            filterPressParam.setIntelligentManuState(filterPressManager.getFeedIntelligentManuStateMap(term));
            filterPressParam.setAutoManuConfirmState(filterPressManager.getFeedAutoManuConfirmState(term));
            filterPressParam.setElectricityMap(filterPressManager.getCurrentInfoInDuration());
        } else if (TYPE_UNLOAD.equals(type)) {
            filterPressParam.setIntelligentManuState(filterPressManager.getUnloadIntelligentManuStateMap(term));
            filterPressParam.setAutoManuConfirmState(filterPressManager.getUnloadAutoManuConfirmState(term));
            filterPressParam.setMaxUnloadParallel(filterPressManager.getMaxUnloadParallel(term));
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(filterPressParam),
                HttpStatus.OK);

    }

    @ApiOperation("设置同时卸料台数")
    @PostMapping(value = "api/filterPress/parameter/maxUnload")
    @ResponseBody
    public ResponseEntity<String> setMaxUnloadParallel(@RequestParam int num,@RequestParam int term){
        filterPressManager.updateMaxUnloadParallel(num,term);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }

    @ApiOperation("获取卸料次序")
    @GetMapping(value = "api/filterPress/unload/sequence/term/{term}")
    public ResponseEntity<String> getUnloadSequence(@PathVariable int term){
        Map<String, Integer> unloadSequence = new HashMap<>();
        Set<String> unloadQueue = filterPressManager.getUnloadSequence(term);
        int i = 0;
        for(String thingCode:unloadQueue){
            if(!StringUtils.isBlank(thingCode)){
                unloadSequence.put(thingCode,i + 1);
                i++;
            }

        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(unloadSequence),
                HttpStatus.OK);
    }

    @ApiOperation("给压滤机内部plc发送长/短脉冲信号及电平信号")
    @RequestMapping(value = "/filterpress/cmd/send",method = RequestMethod.POST)
    public ResponseEntity<String> sendFilterPressPulseCmd(@RequestBody String data, @RequestParam(required = false,defaultValue = "5000") Integer retryPeriod,
                                               @RequestParam(required = false,defaultValue = "3") Integer retryCount, HttpServletRequest request) {
        DataModel dataModel = JSON.parseObject(data,DataModel.class);
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        int position = -1;
        int cleanPeriod = -1;
        boolean isHolding = false;
        if(!StringUtils.isBlank(dataModel.getMetricCode())){
            Map<String,String> map = getMapByMetricCode(dataModel.getMetricCode());
            if((!map.isEmpty()) && (map.size() > 0)){
                for(Map.Entry<String,String> entry:map.entrySet()){
                    if(POSITION.equals(entry.getKey())){
                        position = Integer.valueOf(entry.getValue()) + 1;
                    }else if(IS_HOLDING.equals(entry.getKey())){
                        isHolding = (entry.getValue().equals(IS_HOLDING_OK));
                    }
                }
            }
        }
        int count = cmdControlService.sendPulseCmdBoolByShort(dataModel,retryPeriod,retryCount,requestId,position,500,isHolding);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(count)
                , HttpStatus.OK);
    }

    @ApiOperation("清除卸料队列")
    @RequestMapping(value = "/filterpress/clearUnloadQueue",method = RequestMethod.POST)
    public ResponseEntity<String> clearUnloadQueue(@RequestParam int term){
        filterPressManager.clearAllUnloadQueue(term);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(null)
                , HttpStatus.OK);
    }

    private Map<String,String> getMapByMetricCode(String metricCode){
        Class<FilterPressMetricConstants> clz = FilterPressMetricConstants.class;
        Field[] fields = clz.getDeclaredFields();
        Map<String,String> resultMap = new HashMap<>();
        String index = null;
        for(Field field : fields){
            boolean fieldHasAnno = field.isAnnotationPresent(KepServerMapping.class);
            if(fieldHasAnno && metricCode.equals(field.getName())){
                KepServerMapping fieldAnno = field.getAnnotation(KepServerMapping.class);
                String position = fieldAnno.position();
                if(position.indexOf('.') != -1 && position.split("\\.").length > 1){
                    index = position.split("\\.")[1];
                }
                resultMap.put(POSITION,index);

                if(FilterPressMetricConstants.T1_CHOOSE.equals(metricCode)
                        || FilterPressMetricConstants.T2_CHOOSE.equals(metricCode)
                        ||FilterPressMetricConstants.T3_CHOOSE.equals(metricCode)){
                    resultMap.put(CLEAN_PERIOD,CLEAN_PERIOD_VALUE);
                }else{
                    resultMap.put(CLEAN_PERIOD,CLEAN_PERIOD_ZERO);
                }

                addData(metricCode,resultMap);
            }
        }
        return resultMap;
    }

    private void addData(String metricCode, Map<String,String> resultMap){
        if(FilterPressMetricConstants.SYS_ALARM.equals(metricCode)
                ||FilterPressMetricConstants.CONTROL.equals(metricCode)
                ||FilterPressMetricConstants.GATE_ALARM.equals(metricCode)
                ||FilterPressMetricConstants.SCR_BLK.equals(metricCode)
                ||FilterPressMetricConstants.STOP.equals(metricCode)
                ||FilterPressMetricConstants.R_AUTO.equals(metricCode)){
            resultMap.put(IS_HOLDING,IS_HOLDING_OK);
        }else{
            resultMap.put(IS_HOLDING,IS_HOLDING_NOT);
        }
    }
}
