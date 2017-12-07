package com.zgiot.app.server.module.filterpress.controller;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.filterPressService.FilterPressLogService;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressHisPlateCountWrapper;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountWrapper;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressTotalPlateCountBean;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.common.constants.FilterPressLogConstants;
import com.zgiot.common.constants.FilterPressMetricConstants;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @ApiOperation("历史压板信息查询")
    @GetMapping(value="api/filterPress/plate/queryHisPlateInfo")
    public ResponseEntity<String> queryHisPlateInfo(){
        List<FilterPressHisPlateCountWrapper> filterPressHisPlateCountWrapperList = filterPressLogService.getHisPlateInfos();
        return new ResponseEntity<>(ServerResponse.buildOkJson(filterPressHisPlateCountWrapperList),
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
                dataModelList = filterPressLogService.getDataModelAllFilterPressByMetricCode(FilterPressMetricConstants.T1_CLR);
                position = FilterPressLogConstants.T1_CLR_POSITION;
                break;
            case FilterPressLogConstants.TWO_TEAM_RESET:
                dataModelList = filterPressLogService.getDataModelAllFilterPressByMetricCode(FilterPressMetricConstants.T2_CLR);
                position = FilterPressLogConstants.T2_CLR_POSITION;
                break;
            case FilterPressLogConstants.THREE_TEAM_RESET:
                dataModelList = filterPressLogService.getDataModelAllFilterPressByMetricCode(FilterPressMetricConstants.T3_CLR);
                position = FilterPressLogConstants.T3_CLR_POSITION;
                break;
            default:
        }
        if(team != 0){
            for(DataModel dataModel:dataModelList){
                try{
                    cmdControlService.sendPulseCmdBoolByShort(dataModel,5000,3,requestId,position,500,isHolding);
                }catch (Exception e){
                    throw new SysException("filterPress:" + dataModel.getThingCode() + "team reset exception", SysException.EC_UNKNOWN);
                }
            }
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(null)
                , HttpStatus.OK);
    }

    @ApiOperation("队组选择")
    @PostMapping(value="api/filterPress/plate/chooseTeam/{team}")
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
                dataModelList = filterPressLogService.getDataModelAllFilterPressByMetricCode(FilterPressMetricConstants.T3_CHOOSE);
                position = FilterPressLogConstants.T3_CHOOSE_POSITION;
                break;
            default:
        }
        for(DataModel dataModel:dataModelList){
            try{
                cmdControlService.sendPulseCmdBoolByShort(dataModel,5000,3,requestId,position,cleanPeriod,isHolding);
            }catch(Exception e){
                throw new SysException("filterPress:" + dataModel.getThingCode() + "team choose exception", SysException.EC_UNKNOWN);
            }
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(null)
                , HttpStatus.OK);
    }
}
