package com.zgiot.app.server.module.filterpress.pojo;

/**
 * Created by xiayun on 2017/9/13.
 */
public class FeedOverParam {
    private String thingCode;
    private Integer autoManuConfirmState;
    private Integer intelligentManuState;

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public Integer getAutoManuConfirmState() {
        return autoManuConfirmState;
    }

    public void setAutoManuConfirmState(Integer autoManuConfirmState) {
        this.autoManuConfirmState = autoManuConfirmState;
    }

    public Integer getIntelligentManuState() {
        return intelligentManuState;
    }

    public void setIntelligentManuState(Integer intelligentManuState) {
        this.intelligentManuState = intelligentManuState;
    }
}
