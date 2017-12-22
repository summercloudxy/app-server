package com.zgiot.app.server.module.filterpress.controller;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.filterPressService.FilterPressLogService;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressHisPlateCountWrapper;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountWrapper;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressTotalPlateCountBean;
import com.zgiot.app.server.module.filterpress.pojo.ManualResetBean;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.common.constants.FilterPressLogConstants;
import com.zgiot.common.constants.FilterPressMetricConstants;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class FilterPressLogController {

    @Autowired
    private FilterPressLogService filterPressLogService;
    @Autowired
    private CmdControlService cmdControlService;

    private static final Logger logger = LoggerFactory.getLogger(FilterPressLogController.class);

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
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        boolean isHolding = Boolean.FALSE;
        List<ManualResetBean> manualResetBeans =  filterPressLogService.getResetInfo();
        DataModel dataModel = null;
        if((manualResetBeans != null) && (manualResetBeans.size() > 0)){
            for(ManualResetBean manualResetBean:manualResetBeans){
                try{
                    int position = manualResetBean.getPosition();
                    dataModel = manualResetBean.getDataModel();
                    cmdControlService.sendPulseCmdBoolByShort(dataModel,5000,3,requestId,position,500,isHolding);
                    logger.info("filterPress:" + dataModel.getThingCode() + " team:" + dataModel.getMetricCode() + "successfully reset");
                }catch (Exception e){
                    logger.info("filterPress:" + dataModel.getThingCode() + "team reset exception");
                    continue;
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
        final int pos = position;
        if(dataModelList != null && (dataModelList.size() > 0)){
            for(DataModel dataModel:dataModelList){
                try{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            cmdControlService.sendPulseCmdBoolByShort(dataModel,5000,3,requestId,pos,cleanPeriod,isHolding);
                        }
                    }).start();
                }catch(Exception e){
                    logger.info("filterPress:" + dataModel.getThingCode() + "team choose exception");
                    continue;
                }
            }
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(null)
                , HttpStatus.OK);
    }
}
