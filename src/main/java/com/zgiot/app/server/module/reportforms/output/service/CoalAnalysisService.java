package com.zgiot.app.server.module.reportforms.output.service;

import com.zgiot.app.server.module.coalanalysis.mapper.CoalAnalysisMapper;
import com.zgiot.app.server.module.reportforms.output.pojo.CoalAnalysisBean;
import com.zgiot.app.server.module.reportforms.output.utils.ReportFormDateUtil;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.common.constants.CoalAnalysisConstants;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CoalAnalysisService {

    private CoalAnalysisBean coalAnalysisBean=new CoalAnalysisBean();

    public static final String AVG_RECORD_KEYWORD = "平均";

    private static final Integer ONE_SYSTEM=1;
    private static final Integer TWO_SYSTEM=2;

    private static final String ONETERMCLENED="oneTermClened";
    private static final String TWOTERMCLENED="twoTermClened";
    private static final String ONETERMWASHED="oneTermWashed";
    private static final String TWOTERMWASHED="twoTermWashed";

    @Autowired
    private CoalAnalysisMapper coalAnalysisMapper;


    public CoalAnalysisBean getCoalAnalysisBean(Date dutyStartTime) {
        Date nowDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(dutyStartTime);
        Date dutyEndStartTime = ReportFormDateUtil.getDutyEndStartTime(dutyStartTime);
        coalAnalysisBean.setDutyStartTime(nowDutyStartTime);
        Map<String, List<CoalAnalysisRecord>> coalAnalysis = coalAnalysisBean.getCoalAnalysis();
        //获取1551精煤
        FilterCondition offoClened=new FilterCondition();
        offoClened.setStartTime(nowDutyStartTime);
        offoClened.setEndTime(dutyEndStartTime);
        offoClened.setSystem(ONE_SYSTEM);
        offoClened.setTargetList(Collections.singletonList(CoalAnalysisConstants.FIVE_FIVE_ONE_REFINDED));
        List<CoalAnalysisRecord> offoRefindedList= coalAnalysisMapper.getCoalAnalysisList(offoClened);
        coalAnalysis.put(ONETERMCLENED,offoRefindedList);

        //获取2551精煤
        FilterCondition tffoClened=new FilterCondition();
        tffoClened.setStartTime(nowDutyStartTime);
        tffoClened.setEndTime(dutyEndStartTime);
        tffoClened.setSystem(TWO_SYSTEM);
        tffoClened.setTargetList(Collections.singletonList(CoalAnalysisConstants.FIVE_FIVE_ONE_REFINDED));
        List<CoalAnalysisRecord> tffoRefindedList= coalAnalysisMapper.getCoalAnalysisList(tffoClened);
        coalAnalysis.put(TWOTERMCLENED,tffoRefindedList);

        //获取1552洗混煤
        FilterCondition offtWashed=new FilterCondition();
        offtWashed.setStartTime(nowDutyStartTime);
        offtWashed.setEndTime(dutyEndStartTime);
        offtWashed.setSystem(ONE_SYSTEM);
        offtWashed.setTargetList(Collections.singletonList(CoalAnalysisConstants.FIVE_FIVE_TWO_WASHED));
        List<CoalAnalysisRecord> offtWashedList= coalAnalysisMapper.getCoalAnalysisList(offtWashed);
        coalAnalysis.put(ONETERMWASHED,offtWashedList);

        //获取2552混洗煤
        FilterCondition tfftWashed=new FilterCondition();
        tfftWashed.setStartTime(nowDutyStartTime);
        tfftWashed.setEndTime(dutyEndStartTime);
        tfftWashed.setSystem(TWO_SYSTEM);
        tfftWashed.setTargetList(Collections.singletonList(CoalAnalysisConstants.FIVE_FIVE_TWO_WASHED));
        List<CoalAnalysisRecord> tfftWashedList= coalAnalysisMapper.getCoalAnalysisList(tfftWashed);
        coalAnalysis.put(TWOTERMWASHED,tfftWashedList);


        Map<String, CoalAnalysisRecord> coalAnalysisAvg = coalAnalysisBean.getCoalAnalysisAvg();
        //获取1551平均值
        FilterCondition offoClenedAvg=new FilterCondition();
        offoClenedAvg.setStartTime(nowDutyStartTime);
        offoClenedAvg.setSystem(ONE_SYSTEM);
        offoClenedAvg.setTargetList(Collections.singletonList(CoalAnalysisConstants.FIVE_FIVE_ONE_REFINDED+AVG_RECORD_KEYWORD));
        CoalAnalysisRecord avgOffoRefinded = coalAnalysisMapper.getCoalAnalysisAvg(offoClenedAvg);
        coalAnalysisAvg.put(ONETERMCLENED,avgOffoRefinded);


        //获取2551平均值
        FilterCondition tffoClenedAvg=new FilterCondition();
        tffoClenedAvg.setStartTime(nowDutyStartTime);
        tffoClenedAvg.setSystem(TWO_SYSTEM);
        tffoClenedAvg.setTargetList(Collections.singletonList(CoalAnalysisConstants.FIVE_FIVE_ONE_REFINDED+AVG_RECORD_KEYWORD));
        CoalAnalysisRecord avgTffoRefindedAvg = coalAnalysisMapper.getCoalAnalysisAvg(tffoClenedAvg);
        coalAnalysisAvg.put(TWOTERMCLENED,avgTffoRefindedAvg);

        //获取1552平均值
        FilterCondition offtWashedAvg=new FilterCondition();
        offtWashedAvg.setStartTime(nowDutyStartTime);
        offtWashedAvg.setSystem(TWO_SYSTEM);
        offtWashedAvg.setTargetList(Collections.singletonList(CoalAnalysisConstants.FIVE_FIVE_TWO_WASHED+AVG_RECORD_KEYWORD));
        CoalAnalysisRecord avgOfftWashedAvg = coalAnalysisMapper.getCoalAnalysisAvg(offtWashedAvg);
        coalAnalysisAvg.put(ONETERMWASHED,avgOfftWashedAvg);


        //获取2552平均值
        FilterCondition tfftWashedAvg=new FilterCondition();
        tfftWashedAvg.setStartTime(nowDutyStartTime);
        tfftWashedAvg.setSystem(TWO_SYSTEM);
        tfftWashedAvg.setTargetList(Collections.singletonList(CoalAnalysisConstants.FIVE_FIVE_TWO_WASHED+AVG_RECORD_KEYWORD));
        CoalAnalysisRecord avgTfftWashedAvg = coalAnalysisMapper.getCoalAnalysisAvg(tfftWashedAvg);
        coalAnalysisAvg.put(TWOTERMWASHED,avgTfftWashedAvg);

        return coalAnalysisBean;
    }



}
