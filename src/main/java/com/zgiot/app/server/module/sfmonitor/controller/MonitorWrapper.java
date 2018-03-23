package com.zgiot.app.server.module.sfmonitor.controller;
 
import com.zgiot.app.server.module.sfmonitor.pojo.SFMonitor;
 
import java.util.List; 
 
public class MonitorWrapper { 
    private SFMonitor sfMonitor; 
    private List<MonitorItemInfo> monitorItemInfos; 
    private int equipmentCount; 
 
    public SFMonitor getSfMonitor() { 
        return sfMonitor; 
    } 
 
    public List<MonitorItemInfo> getMonitorItemInfos() { 
        return monitorItemInfos; 
    } 
 
    public int getEquipmentCount() { 
        return equipmentCount; 
    } 
 
    public void setSfMonitor(SFMonitor sfMonitor) { 
        this.sfMonitor = sfMonitor; 
    } 
 
    public void setMonitorItemInfos(List<MonitorItemInfo> monitorItemInfos) { 
        this.monitorItemInfos = monitorItemInfos; 
    } 
 
    public void setEquipmentCount(int equipmentCount) { 
        this.equipmentCount = equipmentCount; 
    } 
} 