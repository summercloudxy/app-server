package com.zgiot.app.server.module.filterpress.controller;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.filterpress.FilterPressManager;
import com.zgiot.app.server.module.filterpress.pojo.FeedOverWholeParam;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xiayun on 2017/9/12.
 */
@Controller
public class FilterPressController {
    @Autowired
    private FilterPressManager filterPressManager;

    @ApiOperation("切换进料结束确认模式：弹窗确认/系统自动")
    @RequestMapping(value = "api/filterPress/feedOver/autoManuState", method = RequestMethod.POST)
    public ResponseEntity<String> setAutoManuState(Boolean state) {
        filterPressManager.autoManuConfirmChange(null, state);
        return new ResponseEntity<>(
                JSON.toJSONString(new ServerResponse(
                        "Done", SysException.EC_SUCCESS, null))
                , HttpStatus.OK);
    }

    @ApiOperation("切换进料结束判断模式：智能/手动")
    @RequestMapping(value = "api/filterPress/feedOver/intelligentManuState", method = RequestMethod.POST)
    public ResponseEntity<String> setIntelligentManuState(String deviceCode, Boolean state) {
        filterPressManager.intelligentManuChange(deviceCode, state);
        return new ResponseEntity<>(
                JSON.toJSONString(new ServerResponse(
                        "Done", SysException.EC_SUCCESS, null))
                , HttpStatus.OK);
    }

    @ApiOperation("进料结束弹窗确认")
    @RequestMapping(value = "api/filterPress/feedOver/{deviceCode}/confirm")
    public ResponseEntity<String> feedOverPopupConfirm(@PathVariable String deviceCode) {
        filterPressManager.feedOverPopupConfirm(deviceCode);
        return new ResponseEntity<>(
                JSON.toJSONString(new ServerResponse(
                        "Done", SysException.EC_SUCCESS, null))
                , HttpStatus.OK);
    }

    @ApiOperation("获取进料设置页参数值")
    @RequestMapping(value = "api/filterPress/feedOver/parameter")
    @ResponseBody
    public ResponseEntity<String> getFilterPressParameter() {
        FeedOverWholeParam feedOverWholeParam = new FeedOverWholeParam();
        feedOverWholeParam.setIntelligentManuState(filterPressManager.getIntelligentManuStateMap());
        feedOverWholeParam.setAutoManuConfirmState(filterPressManager.getAutoManuConfirmState());
        feedOverWholeParam.setElectricityMap(filterPressManager.getCurrentInfoInDuration());
        return new ResponseEntity<>(
                JSON.toJSONString(new ServerResponse(
                        "Done", SysException.EC_SUCCESS, feedOverWholeParam))
                , HttpStatus.OK);

    }

}
