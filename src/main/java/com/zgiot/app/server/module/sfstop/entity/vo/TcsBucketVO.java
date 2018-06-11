package com.zgiot.app.server.module.sfstop.entity.vo;

/**
 * TCS桶选择
 */
public class TcsBucketVO {

    private String thingCode;

    private String runState;

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getRunState() {
        return runState;
    }

    public void setRunState(String runState) {
        this.runState = runState;
    }
}
