package com.zgiot.app.server.module.sfmonitor.controller;
 
import com.zgiot.app.server.module.sfmonitor.pojo.RelSFMonItem;
import com.zgiot.common.pojo.MetricModel; 
 
public class MonitorItemInfo { 
    private MetricModel metricModel; 
    private RelSFMonItem relSFMonItem; 
 
    public MetricModel getMetricModel() { 
        return metricModel; 
    } 
 
    public RelSFMonItem getRelSFMonItem() { 
        return relSFMonItem; 
    } 
 
    public void setMetricModel(MetricModel metricModel) { 
        this.metricModel = metricModel; 
    } 
 
    public void setRelSFMonItem(RelSFMonItem relSFMonItem) { 
        this.relSFMonItem = relSFMonItem; 
    } 
} 