package com.zgiot.app.server.module.filterpress.pojo;

import java.util.Map;

/**
 * Created by xiayun on 2017/9/15.
 */
public class FeedOverWholeParam {
    private Map<String, Integer> intelligentManuState;
    private Integer autoManuConfirmState;
    private Map<String, FilterPressElectricity> electricityMap;

    public Map<String, Integer> getIntelligentManuState() {
        return intelligentManuState;
    }

    public void setIntelligentManuState(Map<String, Integer> intelligentManuState) {
        this.intelligentManuState = intelligentManuState;
    }

    public Integer getAutoManuConfirmState() {
        return autoManuConfirmState;
    }

    public void setAutoManuConfirmState(Integer autoManuConfirmState) {
        this.autoManuConfirmState = autoManuConfirmState;
    }

    public Map<String, FilterPressElectricity> getElectricityMap() {
        return electricityMap;
    }

    public void setElectricityMap(Map<String, FilterPressElectricity> electricityMap) {
        this.electricityMap = electricityMap;
    }
}
