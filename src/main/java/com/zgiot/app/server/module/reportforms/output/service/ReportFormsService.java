package com.zgiot.app.server.module.reportforms.output.service;

import com.zgiot.app.server.module.reportforms.output.pojo.*;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.service.ReportFormSystemStartService;
import com.zgiot.app.server.module.reportforms.output.utils.ReportFormDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ReportFormsService {

    @Autowired
    private ReportFormSystemStartService reportFormSystemStartService;//开车

    @Autowired
    private OutputStoreAndTargetService outputStoreAndTargetService;//其他指标,库存,生产

    @Autowired
    private CoalAnalysisService qualityService;//551,552精煤

    @Autowired
    private TransPortService transPortService;//运销统计

    @Autowired
    private InfluenceTimeService influenceTimeService;//影响时间

    @Autowired
    private DutyLogService dutyLogService;

    @Value("${cloud.service.url}")
    private String cloudServiceUrl;
    @Value("${cloud.service.path}")
    private String cloudServicePath;


    public ReportFormsBean getBean(Date date){

        //根据时间获取当班开始时间
        Date dutyStartTime = ReportFormDateUtil.getNowDutyStartTime(date);

        //TODO 获取对组信息进行封装

        ReportFormsBean reportFormsBean=new ReportFormsBean();
        //封装开车
        reportFormsBean.setStartRecordMap(reportFormSystemStartService.getSystemStartRecords(dutyStartTime));

        //封装库存以及生产
        Map<Integer, ReportFormOutputStoreRecord> outputStoreRecord = outputStoreAndTargetService.getOutputStoreRecord(dutyStartTime);
        reportFormsBean.setDutyOutputStoreRecords(outputStoreRecord);

        //指标
        reportFormsBean.setTargetInfo(outputStoreAndTargetService.getTargetInfo(dutyStartTime));


        CoalAnalysisBean coalAnalysisBean = qualityService.getCoalAnalysisBean(dutyStartTime);
        if(coalAnalysisBean!=null){
            reportFormsBean.setCoalAnalysis(coalAnalysisBean.getCoalAnalysis());
            reportFormsBean.setCoalAnalysisAvg(coalAnalysisBean.getCoalAnalysisAvg());
        }
        TransBean transPortCacheBean = transPortService.getTransPortCacheBean(dutyStartTime);
        if(transPortCacheBean!=null){
            reportFormsBean.setTransportList(transPortCacheBean.getTransportList());
            reportFormsBean.setSaleStatisticsOutwardMap(transPortCacheBean.getSaleStatisticsOutwardMap());
            reportFormsBean.setSaleStatisticsLocalityMap(transPortCacheBean.getSaleStatisticsLocalityMap());
        }
        InfluenceTimeBean influenceTimeBean = influenceTimeService.getData(date);
        if(influenceTimeBean!=null){
            reportFormsBean.setInfluenceTimeRsps(influenceTimeBean.getInfluenceTimeRsps());
            reportFormsBean.setInfluenceTimeRemarks(influenceTimeBean.getInfluenceTimeRemarks());
        }
       reportFormsBean.setSchedulingGroup(getTeamInfo(dutyStartTime));

        return reportFormsBean;
    }


    public Integer getTeamInfo(Date dutyStartTime){
        DutyLog whichTeam = dutyLogService.getWhichTeam(dutyStartTime);
        return whichTeam.getTeamId();
    }

    public Long getDutyStartTime(Date currentTime){
        return ReportFormDateUtil.getNowDutyStartTime(currentTime).getTime();
    }


}
                                                  