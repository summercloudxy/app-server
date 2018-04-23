package com.zgiot.app.server.module.sfmonitor.monitorservice;

import com.zgiot.app.server.module.sfmonitor.controller.SystemMonitorDetailInfo;
import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;

import java.util.List;

public interface SFSysMonitorService {

    /**
     * 获取系统监控首页
     */
    public List<ThingTag> getSysMonitorIndex();

    /**
     * 获取系统监控二级页面
     */
    public SystemMonitorDetailInfo getSystemMonitorDetail(String thingTagId1,String thingTagId2,String term);
}
