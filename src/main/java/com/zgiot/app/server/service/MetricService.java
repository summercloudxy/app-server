package com.zgiot.app.server.service;

import com.zgiot.common.pojo.MetricModel;

public interface MetricService {
    MetricModel getMetric(String metricCode);
}
