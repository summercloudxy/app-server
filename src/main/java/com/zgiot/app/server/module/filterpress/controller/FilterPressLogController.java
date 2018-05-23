package com.zgiot.app.server.module.filterpress.controller;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.FilterPressLogUtil;
import com.zgiot.app.server.module.filterpress.dao.FilterPressLogMapper;
import com.zgiot.app.server.module.filterpress.filterPressService.FilterPressLogService;
import com.zgiot.app.server.module.filterpress.pojo.*;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class FilterPressLogController {

    @Autowired
    private FilterPressLogService filterPressLogService;
    @Autowired
    private CmdControlService cmdControlService;
    @Autowired
    private FilterPressLogMapper filterPressLogMapper;

    private static final int TERM1 = 1;
    private static final int TERM2 = 2;

    private static final Logger logger = LoggerFactory.getLogger(FilterPressLogController.class);

    @ApiOperation("日志查询，按照日期、设备编号、状态模式查询")
    @GetMapping(value = "api/filterPress/log/queryLogByDate/{date}/term/{term}")
    public ResponseEntity<String> queryLogByDate(@PathVariable String date, @PathVariable int term) {
        List<FilterPressLogBean> filterPressLogBeanList = filterPressLogService.getLogByDate(date, term);
        return new ResponseEntity<>(ServerResponse.buildOkJson(filterPressLogBeanList),
                HttpStatus.OK);
    }

    @ApiOperation("压板信息查询")
    @GetMapping(value = "api/filterPress/plate/queryPlateInfo/term/{term}")
    public ResponseEntity<String> queryPlateInfo(@PathVariable int term) {
        FilterPressPlateCountWrapper filterPressPlateCountWrapper = filterPressLogService.getPlateInfos(term);
        return new ResponseEntity<>(ServerResponse.buildOkJson(filterPressPlateCountWrapper),
                HttpStatus.OK);
    }

    @ApiOperation("压板总数信息查询")
    @GetMapping(value = "api/filterPress/plate/queryTotalPlateInfo/term/{term}")
    public ResponseEntity<String> queryTotalPlateInfo(@PathVariable int term) {
        FilterPressTotalPlateCountBean filterPressTotalPlateCountBean = filterPressLogService.getTotalPlateInfos(term);
        return new ResponseEntity<>(ServerResponse.buildOkJson(filterPressTotalPlateCountBean),
                HttpStatus.OK);
    }

    @ApiOperation("历史压板信息查询")
    @GetMapping(value = "api/filterPress/plate/queryHisPlateInfo/term/{term}")
    public ResponseEntity<String> queryHisPlateInfo(@PathVariable int term) {
        List<FilterPressHisPlateCountWrapper> filterPressHisPlateCountWrapperList = filterPressLogService.getHisPlateInfos(term);
        return new ResponseEntity<>(ServerResponse.buildOkJson(filterPressHisPlateCountWrapperList),
                HttpStatus.OK);
    }

    @ApiOperation("人工清零")
    @PostMapping(value = "api/filterPress/plate/manualReset/term/{term}")
    public ResponseEntity<String> manualReset(HttpServletRequest request, @PathVariable int term) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        boolean isHolding = Boolean.FALSE;
        List<ManualResetBean> manualResetBeans = filterPressLogService.getResetInfo(term);
        DataModel dataModel = null;
        if ((manualResetBeans != null) && (manualResetBeans.size() > 0)) {
            for (ManualResetBean manualResetBean : manualResetBeans) {
                try {
                    int position = manualResetBean.getPosition();
                    dataModel = manualResetBean.getDataModel();
                    cmdControlService.sendPulseCmdBoolByShort(dataModel, 5000, 3, requestId, position, 500, isHolding);
                    logger.info("filterPress:" + dataModel.getThingCode() + " team:" + dataModel.getMetricCode() + "successfully reset");
                } catch (Exception e) {
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
    @PostMapping(value = "api/filterPress/plate/chooseTeam/{team}/term/{term}")
    public ResponseEntity<String> chooseTeam(@PathVariable String team, @PathVariable int term, HttpServletRequest request) {
        List<DataModel> dataModelList = null;
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        int position = -1;
        int cleanPeriod = FilterPressLogConstants.TEAM_CHOOSE_CLEAN_PERIOD;
        boolean isHolding = Boolean.FALSE;
        switch (Integer.valueOf(team)) {
            case FilterPressLogConstants.ONE_TEAM_RESET:
                dataModelList = filterPressLogService.getDataModelAllFilterPressByMetricCode(FilterPressMetricConstants.T1_CHOOSE, term);
                position = FilterPressLogConstants.T1_CHOOSE_POSITION;
                break;
            case FilterPressLogConstants.TWO_TEAM_RESET:
                dataModelList = filterPressLogService.getDataModelAllFilterPressByMetricCode(FilterPressMetricConstants.T2_CHOOSE, term);
                position = FilterPressLogConstants.T2_CHOOSE_POSITION;
                break;
            case FilterPressLogConstants.THREE_TEAM_RESET:
                dataModelList = filterPressLogService.getDataModelAllFilterPressByMetricCode(FilterPressMetricConstants.T3_CHOOSE, term);
                position = FilterPressLogConstants.T3_CHOOSE_POSITION;
                break;
            default:
        }
        final int pos = position;
        if (dataModelList != null && (dataModelList.size() > 0)) {
            for (DataModel dataModel : dataModelList) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            cmdControlService.sendPulseCmdBoolByShort(dataModel, 5000, 3, requestId, pos, cleanPeriod, isHolding);
                        }
                    }).start();
                } catch (Exception e) {
                    logger.info("filterPress:" + dataModel.getThingCode() + "team choose exception");
                    continue;
                }
            }
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(null)
                , HttpStatus.OK);
    }

    @ApiOperation("查询压板统计信息")
    @GetMapping("/api/filterPress/plateStaistics/date/{currentDate}/term/{term}/isDayShift/{isDayShift}")
    public ResponseEntity<String> getPlateStaistics(@PathVariable String currentDate, @PathVariable int term, @PathVariable boolean isDayShift) {
        String startTime = currentDate + FilterPressLogConstants.NIGHT_SHIFT_ZERO_LINE;
        String endTime = currentDate + FilterPressLogConstants.NIGHT_SHIFT_MIDDLE_LINE;
        List<FilterPressPlateStatistic> filterPressPlateStatistics = filterPressLogMapper.getPlateStatistic(isDayShift,startTime,endTime,term);
        if (filterPressPlateStatistics.size() > 0) {
            return new ResponseEntity<>(
                    ServerResponse.buildOkJson(filterPressPlateStatistics.get(0))
                    , HttpStatus.OK);
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(null)
                , HttpStatus.OK);
    }

    @ApiOperation("获取进料平均时长")
    @GetMapping("/api/filterPress/getFeedAverageDuration/thingCode/{thingCode}")
    public ResponseEntity<String> getFeedAverageDuration(@PathVariable String thingCode){
        int term = 0;
        if(thingCode.startsWith(String.valueOf(TERM1))){
            term = TERM1;
        }else if(thingCode.startsWith(String.valueOf(TERM2))){
            term = TERM2;
        }
        boolean isDayShift = FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE, FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        String currentDay = simpleDateFormat.format(date);
        String startTime = currentDay + FilterPressLogConstants.NIGHT_SHIFT_ZERO_LINE;
        String endTime = currentDay + FilterPressLogConstants.NIGHT_SHIFT_MIDDLE_LINE;
        List<FilterPressLogBean> filterPressLogBeans = filterPressLogMapper.queryLog(startTime,endTime,term,isDayShift);
        int num = filterPressLogBeans.size();
        long totalFeedDuration = 0;
        for(FilterPressLogBean filterPressLogBean:filterPressLogBeans){
            totalFeedDuration += filterPressLogBean.getFeedDuration();
        }
        long averageFeedDuration = 0;
        if (num != 0) {
            averageFeedDuration = (totalFeedDuration / num) / 1000;
        }

        return new ResponseEntity<>(
                ServerResponse.buildOkJson(averageFeedDuration)
                , HttpStatus.OK);
    }
}
