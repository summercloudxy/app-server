package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.sfmonitor.pojo.RelSFSysMonitorTermThing;
import com.zgiot.app.server.module.sfmonitor.pojo.RelSfSpeMonThingMetric;
import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;

import java.util.ArrayList;
import java.util.List;

public class SystemMonitorDetailInfo {
    private Short termCount;
    private List<ThingTag> thingTagList;
    private List<RelSFSysMonitorTermThing> relSFSysMonitorTermThingList;
    private List<RelSfSpeMonThingMetric> speMonThingMetricList;//设备保护模块数据

    public List<RelSfSpeMonThingMetric> getSpeMonThingMetricList() {
        return speMonThingMetricList;
    }

    public void setSpeMonThingMetricList(List<RelSfSpeMonThingMetric> speMonThingMetricList) {
        this.speMonThingMetricList = speMonThingMetricList;
    }

    public List<RelSFSysMonitorTermThing> getRelSFSysMonitorTermThingList() {
        return relSFSysMonitorTermThingList;
    }

    public void setRelSFSysMonitorTermThingList(List<RelSFSysMonitorTermThing> relSFSysMonitorTermThingList) {
        this.relSFSysMonitorTermThingList = relSFSysMonitorTermThingList;
    }

    public List<ThingTag> getThingTagList() {
        return thingTagList;
    }

    public void setThingTagList(List<ThingTag> thingTagList) {
        this.thingTagList = thingTagList;
    }

    public Short getTermCount() {
        return termCount;
    }

    public void setTermCount(Short termCount) {
        this.termCount = termCount;
    }

}
