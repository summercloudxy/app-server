package com.zgiot.app.server.module.sfmonitor.monitorservice;

import com.zgiot.app.server.module.sfmonitor.controller.SFSpeMonitorReq;
import com.zgiot.app.server.module.sfmonitor.controller.SystemMonitorDetailInfo;
import com.zgiot.app.server.module.sfmonitor.pojo.RelSfSpeMonThingMetric;
import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;

import java.util.List;
import java.util.Map;

public interface SFSpeMonitorService {

    /**
     * 获取专题监控首页
     */
    public List<ThingTag> getSpeMonitorIndex();

    /**
     * 获取专题监控二级页（有子系统的）
     */
    public SystemMonitorDetailInfo getSpecialMonitorDetailHaveSub(SFSpeMonitorReq sFSpeMonitorReq);

    /**
     * 获取专题监控二级页（通用）
     */
    public SystemMonitorDetailInfo getSpecialMonitorDetailCommon(SFSpeMonitorReq sFSpeMonitorReq);

    /**
     * 获取专题监控设备保护页
     */
    public SystemMonitorDetailInfo getSpecialMonitorDetailProtect(SFSpeMonitorReq sFSpeMonitorReq);

    /**
     * 获取专题监控三级目录
     */
    public List<ThingTag> getThingTagListThree(String thingTagId);

    /**
     * 获取专题监控页面查询的设备编号
     */
    List<String> getThingCodeList(SFSpeMonitorReq sFSpeMonitorReq);

    /**
     * 获取专题监控页面查询的信号
     */
    List<RelSfSpeMonThingMetric> getMetricList(SFSpeMonitorReq sFSpeMonitorReq);
}
