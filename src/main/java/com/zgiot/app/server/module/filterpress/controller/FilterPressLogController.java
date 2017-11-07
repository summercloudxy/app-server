package com.zgiot.app.server.module.filterpress.controller;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressHisPlateCountBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountWrapper;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressTotalPlateCountBean;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.FilterPressLogService;
import com.zgiot.common.constants.FilterPressLogConstants;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class FilterPressLogController {

    @Autowired
    private FilterPressLogService filterPressLogService;
    @Autowired
    private CmdControlService cmdControlService;

    @ApiOperation("日志查询，按照日期、设备编号、状态模式查询")
    @GetMapping(value="api/filterPress/log/queryLogByDate/{date}")
    public ResponseEntity<String> queryLogByDate(@PathVariable String date){
        List<FilterPressLogBean> filterPressLogBeanList = filterPressLogService.getLogByDate(date);
        return new ResponseEntity<>(ServerResponse.buildOkJson(filterPressLogBeanList),
                HttpStatus.OK);
    }

    @ApiOperation("压板信息查询")
    @GetMapping(value="api/filterPress/plate/queryPlateInfo")
    public ResponseEntity<String> queryPlateInfo(){
        FilterPressPlateCountWrapper filterPressPlateCountWrapper = filterPressLogService.getPlateInfos();
        return new ResponseEntity<>(ServerResponse.buildOkJson(filterPressPlateCountWrapper),
                HttpStatus.OK);
    }

    @ApiOperation("压板总数信息查询")
    @GetMapping(value="api/filterPress/plate/queryTotalPlateInfo")
    public ResponseEntity<String> queryTotalPlateInfo(){
        FilterPressTotalPlateCountBean filterPressTotalPlateCountBean = filterPressLogService.getTotalPlateInfos();
        return new ResponseEntity<>(ServerResponse.buildOkJson(filterPressTotalPlateCountBean),
                HttpStatus.OK);
    }

    @ApiOperation("上一班历史压板信息查询")
    @GetMapping(value="api/filterPress/plate/queryHisPlateInfo")
    public ResponseEntity<String> queryHisPlateInfo(){
        FilterPressHisPlateCountBean filterPressHisPlateCountBean = filterPressLogService.getHisPlateInfos();
        return new ResponseEntity<>(ServerResponse.buildOkJson(filterPressHisPlateCountBean),
                HttpStatus.OK);
    }

    @ApiOperation("人工清零")
    @PostMapping(value="api/filterPress/plate/manualReset")
    public ResponseEntity<String> manualReset( HttpServletRequest request){
        int team = filterPressLogService.getPriorShiftTeam();
        List<DataModel> dataModelList = null;
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        int position = -1;
        boolean isHolding = Boolean.FALSE;
        switch(team){
            case FilterPressLogConstants.ONE_TEAM_RESET:
                dataModelList = filterPressLogService.getDataModelAllFilterPressByMetricCode(FilterPressMetricConstants.T1_RCD);
                position = FilterPressLogConstants.T1_RCD_POSITION;
                break;
            case FilterPressLogConstants.TWO_TEAM_RESET:
                dataModelList = filterPressLogService.getDataModelAllFilterPressByMetricCode(FilterPressMetricConstants.T2_RCD);
                position = FilterPressLogConstants.T2_RCD_POSITION;
                break;
            case FilterPressLogConstants.THREE_TEAM_RESET:
                dataModelList = filterPressLogService.getDataModelAllFilterPressByMetricCode(FilterPressMetricConstants.T3_RCD);
                position = FilterPressLogConstants.T3_RCD_POSITION;
                break;
            default:
        }
        for(DataModel dataModel:dataModelList){
            cmdControlService.sendPulseCmdBoolByShort(dataModel,5000,3,requestId,position,500,isHolding);
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(null)
                , HttpStatus.OK);
    }

    @ApiOperation("对组选择")
    @PostMapping(value="api/filterPress/plate/chooseTeam")
    public ResponseEntity<String> chooseTeam( @PathVariable String team, HttpServletRequest request){
        List<DataModel> dataModelList = null;
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        int position = -1;
        int cleanPeriod = FilterPressLogConstants.TEAM_CHOOSE_CLEAN_PERIOD;
        boolean isHolding = Boolean.FALSE;
        switch(Integer.valueOf(team)){
            case FilterPressLogConstants.ONE_TEAM_RESET:
                dataModelList = filterPressLogService.getDataModelAllFilterPressByMetricCode(FilterPressMetricConstants.T1_CHOOSE);
                position = FilterPressLogConstants.T1_CHOOSE_POSITION;
                break;
            case FilterPressLogConstants.TWO_TEAM_RESET:
                dataModelList = filterPressLogService.getDataModelAllFilterPressByMetricCode(FilterPressMetricConstants.T2_CHOOSE);
                position = FilterPressLogConstants.T2_CHOOSE_POSITION;
                break;
            case FilterPressLogConstants.THREE_TEAM_RESET:
                dataModelList = filterPressLogService.getDataModelAllFilterPressByMetricCode(FilterPressMetricConstants.T2_CHOOSE);
                position = FilterPressLogConstants.T3_CHOOSE_POSITION;
                break;
            default:
        }
        for(DataModel dataModel:dataModelList){
            cmdControlService.sendPulseCmdBoolByShort(dataModel,5000,3,requestId,position,cleanPeriod,isHolding);
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(null)
                , HttpStatus.OK);
    }
}
