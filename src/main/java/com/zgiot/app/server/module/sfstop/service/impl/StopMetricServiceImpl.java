package com.zgiot.app.server.module.sfstop.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopInformation;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopMetric;
import com.zgiot.app.server.module.sfstop.mapper.StopMetricMapper;
import com.zgiot.app.server.module.sfstop.service.StopInformationService;
import com.zgiot.app.server.module.sfstop.service.StopMetricService;
import com.zgiot.app.server.module.sfstop.util.PageListRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StopMetricServiceImpl implements StopMetricService {

    @Autowired
    private StopMetricMapper stopMetricMapper;

    @Autowired
    private StopInformationService stopInformationService;

    @Override
    public StopMetric getStopMetricByCode(String metricCode) {
        StopMetric metric=stopMetricMapper.getMetricByCode(metricCode);
        return metric;
    }

    @Override
    public List<StopMetric> getStopMetricList() {
        List<StopMetric> metricList=stopMetricMapper.getMetricList();
        return metricList;
    }

    @Override
    public PageListRsp getStopInformationPage(Integer pageNum, Integer pageSize) {
        Page page=PageHelper.startPage(pageNum,pageSize);
        List<StopMetric> metricList = stopMetricMapper.getMetricList();
        PageListRsp pageListRsp=new PageListRsp();
        pageListRsp.setPageNum(page.getPageNum());
        pageListRsp.setPageSize(page.getPageSize());
        pageListRsp.setSize(page.getPages());
        pageListRsp.setTotal(page.getTotal());
        pageListRsp.setList(metricList);
        return pageListRsp;
    }

    @Override
    public List<StopMetric> getStopMetricListByTCAndString(String string) {
        List<StopInformation> informationList = stopInformationService.getInformationByTCAndName(string);
        List<StopMetric> stopMetrics=new ArrayList<>();
        if(informationList!=null && informationList.size()>0){
            for (StopInformation stopInformation:informationList) {
                List<StopMetric> stopMetricsList = stopMetricMapper.getMetricByThingCode(stopInformation.getThingCode());
                if(stopMetricsList!=null && stopMetricsList.size()>0){
                    for (StopMetric stopMetric:stopMetricsList) {
                        stopMetrics.add(stopMetric);
                    }
                }
            }
        }
        return stopMetrics;
    }
}
