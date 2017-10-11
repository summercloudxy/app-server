package com.zgiot.app.server.module.filterpress.pojo;

import java.util.Map;

/**
 * Created by xiayun on 2017/9/15.
 */
public class FilterPressParam {
    private Map<String, Boolean> intelligentManuState;
    private Boolean autoManuConfirmState;
    private Map<String, FilterPressElectricity> electricityMap;
    private Integer maxUnloadParallel;

    public Map<String, Boolean> getIntelligentManuState() {
        return intelligentManuState;
    }

    public void setIntelligentManuState(Map<String, Boolean> intelligentManuState) {
        this.intelligentManuState = intelligentManuState;
    }

    public Boolean getAutoManuConfirmState() {
        return autoManuConfirmState;
    }

    public void setAutoManuConfirmState(Boolean autoManuConfirmState) {
        this.autoManuConfirmState = autoManuConfirmState;
    }

    public Map<String, FilterPressElectricity> getElectricityMap() {
        return electricityMap;
    }

    public void setElectricityMap(Map<String, FilterPressElectricity> electricityMap) {
        this.electricityMap = electricityMap;
    }

    public Integer getMaxUnloadParallel() {
        return maxUnloadParallel;
    }

    public void setMaxUnloadParallel(Integer maxUnloadParallel) {
        this.maxUnloadParallel = maxUnloadParallel;
    }
}
