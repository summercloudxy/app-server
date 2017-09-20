package com.zgiot.app.server.module.filterpress.controller;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.filterpress.FilterPressManager;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressParam;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by xiayun on 2017/9/12.
 */
@Controller
public class FilterPressController {
    @Autowired
    private FilterPressManager filterPressManager;
    private static final String TYPE_FEED = "feed";
    private static final String TYPE_UNLOAD = "unload";

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


}
