package com.zgiot.app.server.module.filterpress.controller;

import com.zgiot.app.server.module.filterpress.manager.FeedOverManager;
import com.zgiot.app.server.module.filterpress.pojo.FeedOverWholeParam;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressElectricity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by xiayun on 2017/9/12.
 */
@Controller
public class FilterPressController {
    @Autowired
    private FeedOverManager feedOverManager;
    private static final String RECONFIRM_CODE = "reconfirm";

    @ApiOperation("切换进料结束确认模式：弹窗确认/系统自动")
    @RequestMapping(value = "api/filterPress/feedOver/autoManuState", method = RequestMethod.POST)
    public void setAutoManuState(Integer state) {
        feedOverManager.autoManuConfirmChange(null, state);
    }

    @ApiOperation("切换进料结束判断模式：智能/手动")
    @RequestMapping(value = "api/filterPress/feedOver/intelligentManuState", method = RequestMethod.POST)
    public void setIntelligentManuState(String deviceCode, Integer state) {
        feedOverManager.intelligentManuChange(deviceCode, state);
    }

    @ApiOperation("进料结束弹窗确认")
    @RequestMapping(value = "api/filterPress/feedOver/{deviceCode}/confirm")
    public void feedOverPopupConfirm(@PathVariable String deviceCode) {
        feedOverManager.feedOverPopupConfirm(deviceCode);
    }

    @ApiOperation("获取进料设置页参数值")
    @RequestMapping(value = "api/filterPress/feedOver/parameter")
    @ResponseBody
    public FeedOverWholeParam getFilterPressParameter() {
        FeedOverWholeParam feedOverWholeParam = new FeedOverWholeParam();
        feedOverWholeParam.setIntelligentManuState(feedOverManager.getIntelligentManuStateMap());
        feedOverWholeParam.setAutoManuConfirmState(feedOverManager.getAutoManuConfirmState());
        feedOverWholeParam.setElectricityMap(feedOverManager.getCurrentInfoInDuration());
        return feedOverWholeParam;
    }


}
