package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.mapper.TMLMapper;
import com.zgiot.app.server.service.MetricService;
import com.zgiot.common.pojo.MetricModel;
import com.zgiot.common.pojo.ThingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MetricServiceImpl implements MetricService {
    private final ConcurrentHashMap<String, MetricModel> metricCache = new ConcurrentHashMap<>(5000);
    @Autowired
    private TMLMapper tmlMapper;

    @Override
    public MetricModel getMetric(String metricCode) {
        return metricCache.get(metricCode);
    }

    @Override
    public Map<String, MetricModel> getMetricMap() {
        return metricCache;
    }

    @PostConstruct
    private void initCache() {
        List<MetricModel> allMetrics = tmlMapper.findAllMetrics();
        for (MetricModel metric : allMetrics) {
            metricCache.put(metric.getMetricCode(), metric);
        }
    }
}
