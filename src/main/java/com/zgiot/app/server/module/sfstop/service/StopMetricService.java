package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopMetric;
import com.zgiot.app.server.module.sfstop.util.PageListRsp;

import java.util.List;

public interface StopMetricService {

    StopMetric getStopMetricByCode(String metricCode);

    List<StopMetric> getStopMetricList();

    PageListRsp getStopInformationPage(Integer pageNum, Integer pageSize);

    List<StopMetric> getStopMetricListByTCAndString(String string);
}
