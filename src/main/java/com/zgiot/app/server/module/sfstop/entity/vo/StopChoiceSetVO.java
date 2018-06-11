package com.zgiot.app.server.module.sfstop.entity.vo;

import java.util.List;

public class StopChoiceSetVO {

    private String rawStopSet;

    private String tcsStopSet;

    private String filterpressStopSet;

    private List<String> beltRouteSet;

    private List<String> lineIds;
    /**
     * 一期二期
     */
    private String system;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public List<String> getLineIds() {
        return lineIds;
    }

    public void setLineIds(List<String> lineIds) {
        this.lineIds = lineIds;
    }

    public String getRawStopSet() {
        return rawStopSet;
    }

    public void setRawStopSet(String rawStopSet) {
        this.rawStopSet = rawStopSet;
    }

    public String getTcsStopSet() {
        return tcsStopSet;
    }

    public void setTcsStopSet(String tcsStopSet) {
        this.tcsStopSet = tcsStopSet;
    }

    public String getFilterpressStopSet() {
        return filterpressStopSet;
    }

    public void setFilterpressStopSet(String filterpressStopSet) {
        this.filterpressStopSet = filterpressStopSet;
    }

    public List<String> getBeltRouteSet() {
        return beltRouteSet;
    }

    public void setBeltRouteSet(List<String> beltRouteSet) {
        this.beltRouteSet = beltRouteSet;
    }
}
