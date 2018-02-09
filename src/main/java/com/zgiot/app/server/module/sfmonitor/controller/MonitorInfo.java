package com.zgiot.app.server.module.sfmonitor.controller;
 
import com.zgiot.app.server.module.sfmonitor.pojo.RelSFMonItem;
import com.zgiot.app.server.module.sfmonitor.pojo.SFMonitor;
 
import java.util.List; 
 
public class MonitorInfo { 
    private SFMonitor sfMonitor; 
    private List<RelSFMonItem> relSFMonItems; 
 
    public SFMonitor getSfMonitor() { 
        return sfMonitor; 
    } 
 
    public List<RelSFMonItem> getRelSFMonItems() { 
        return relSFMonItems; 
    } 
 
    public void setSfMonitor(SFMonitor sfMonitor) { 
        this.sfMonitor = sfMonitor; 
    } 
 
    public void setRelSFMonItems(List<RelSFMonItem> relSFMonItems) { 
        this.relSFMonItems = relSFMonItems; 
    } 
} 