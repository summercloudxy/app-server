package com.zgiot.app.server.service;

import com.zgiot.common.pojo.MetricModel;

import java.util.Map;

public interface MetricService {
    MetricModel getMetric(String metricCode);

    Map<String, MetricModel> getMetricMap();
}
