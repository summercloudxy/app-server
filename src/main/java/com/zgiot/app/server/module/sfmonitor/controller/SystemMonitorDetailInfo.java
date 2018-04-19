package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.sfmonitor.pojo.SFSysMonitorThing;
import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;

import java.util.List;

public class SystemMonitorDetailInfo {
    private Short termCount;
    private List<ThingTag> thingTag;
    List<SFSysMonitorThing> sFSysMonitorThingList;

    public Short getTermCount() {
        return termCount;
    }

    public void setTermCount(Short termCount) {
        this.termCount = termCount;
    }

    public List<ThingTag> getThingTag() {
        return thingTag;
    }

    public void setThingTag(List<ThingTag> thingTag) {
        this.thingTag = thingTag;
    }

    public List<SFSysMonitorThing> getSFSysMonitorThingList() {
        return sFSysMonitorThingList;
    }

    public void setSfThingList(List<SFSysMonitorThing> SFSysMonitorThingList) {
        this.sFSysMonitorThingList = SFSysMonitorThingList;
    }
}
