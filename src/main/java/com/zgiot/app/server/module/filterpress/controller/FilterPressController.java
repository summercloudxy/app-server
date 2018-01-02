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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

    @Autowired
    private CmdControlService cmdControlService;

    @ApiOperation("切换进料/卸料结束确认模式：弹窗确认/系统自动")
    @PostMapping(value = "api/filterPress/param/autoManuConfirmState")
    public ResponseEntity<String> setAutoManuState(Boolean state, String type) {
        if (TYPE_FEED.equals(type)) {
            filterPressManager.feedAutoManuConfirmChange(null, state);
        } else if (TYPE_UNLOAD.equals(type)) {
            filterPressManager.unloadAutoManuConfirmChange(null, state);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }

    @ApiOperation("切换进料/卸料结束判断模式：智能/手动")
    @PostMapping(value = "api/filterPress/param/intelligentManuState")
    public ResponseEntity<String> setIntelligentManuState(String thingCode, Boolean state, String type) {
        if (TYPE_FEED.equals(type)) {
            filterPressManager.feedIntelligentManuChange(thingCode, state);
        } else if (TYPE_UNLOAD.equals(type)) {
            filterPressManager.unloadIntelligentManuChange(thingCode, state);
            filterPressManager.removeQueue(thingCode,state);

        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }

    @ApiOperation("进料结束弹窗确认")
    @PostMapping(value = "api/filterPress/feedOver/{thingCode}/confirm")
    public ResponseEntity<String> feedOverPopupConfirm(@PathVariable String thingCode) {
        filterPressManager.confirmFeedOver(thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }

    @ApiOperation("卸料结束弹窗确认")
    @RequestMapping(value = "api/filterPress/unload/{thingCode}/confirm", method = RequestMethod.POST)
    public ResponseEntity<String> unloadPopupConfirm(@PathVariable String thingCode) {
        filterPressManager.confirmUnload(thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }

    @ApiOperation("给app端返回弹窗时未点击确定卸料的thingCodes")
    @RequestMapping(value = "api/filterPress/unload/unConfirmUnload", method = RequestMethod.GET)
    public ResponseEntity<String> unConfirmUnload() {
        String thingCode = filterPressManager.getFirstUnConfirmedUnload();
        Set<String> thingCodeSet = new ConcurrentSkipListSet<>();
        thingCodeSet.add(thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingCodeSet),
                HttpStatus.OK);
    }

    @ApiOperation("获取进料/卸料设置页参数值")
    @GetMapping(value = "api/filterPress/parameter")
    @ResponseBody
    public ResponseEntity<String> getFilterPressParameter(@RequestParam String type) {
        FilterPressParam filterPressParam = new FilterPressParam();
        if (TYPE_FEED.equals(type)) {
            filterPressParam.setIntelligentManuState(filterPressManager.getFeedIntelligentManuStateMap());
            filterPressParam.setAutoManuConfirmState(filterPressManager.getFeedAutoManuConfirmState());
            filterPressParam.setElectricityMap(filterPressManager.getCurrentInfoInDuration());
        } else if (TYPE_UNLOAD.equals(type)) {
            filterPressParam.setIntelligentManuState(filterPressManager.getUnloadIntelligentManuStateMap());
            filterPressParam.setAutoManuConfirmState(filterPressManager.getUnloadAutoManuConfirmState());
            filterPressParam.setMaxUnloadParallel(filterPressManager.getMaxUnloadParallel());
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(filterPressParam),
                HttpStatus.OK);

    }

    @ApiOperation("设置同时卸料台数")
    @PostMapping(value = "api/filterPress/parameter/maxUnload")
    @ResponseBody
    public ResponseEntity<String> setMaxUnloadParallel(@RequestParam int num){
        filterPressManager.updateMaxUnloadParallel(num);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }

    @ApiOperation("获取卸料次序")
    @GetMapping(value = "api/filterPress/unload/sequence")
    public ResponseEntity<String> getUnloadSequence(){
        return new ResponseEntity<>(ServerResponse.buildOkJson(filterPressManager.getUnloadSequence()),
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
                for(String key:map.keySet()){
                    if(POSITION.equals(key)){
                        position = Integer.valueOf(map.get(key)) + 1;
                    }else if(CLEAN_PERIOD.equals(key)){
                        cleanPeriod = Integer.valueOf(map.get(key));
                    }else if(IS_HOLDING.equals(key)){
                        isHolding = (map.get(key).equals(IS_HOLDING_OK));
                    }
                }
            }
        }
        int count = cmdControlService.sendPulseCmdBoolByShort(dataModel,retryPeriod,retryCount,requestId,position,500,isHolding);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(count)
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
                position = null;
                resultMap.put(POSITION,index);

                if(FilterPressMetricConstants.T1_CHOOSE.equals(metricCode)
                        || FilterPressMetricConstants.T2_CHOOSE.equals(metricCode)
                        ||FilterPressMetricConstants.T3_CHOOSE.equals(metricCode)){
                    resultMap.put(CLEAN_PERIOD,CLEAN_PERIOD_VALUE);
                }else{
                    resultMap.put(CLEAN_PERIOD,CLEAN_PERIOD_ZERO);
                }

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
        return resultMap;
    }
}
