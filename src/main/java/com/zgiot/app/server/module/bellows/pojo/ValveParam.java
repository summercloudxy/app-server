package com.zgiot.app.server.module.bellows.pojo;

/**
 * @author wangwei
 */
public class ValveParam {

    /**
     * 每组最大个数
     */
    private Integer maxCount;

    /**
     * 每组运行时间
     */
    private Integer runTime;

    /**
     * 鼓风前等待时间
     */
    private Integer waitTime;


    public Integer getMaxCount() {
        return maxCount;
    }

    public ValveParam(Integer maxCount, Integer runTime, Integer waitTime) {
        this.maxCount = maxCount;
        this.runTime = runTime;
        this.waitTime = waitTime;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }
}
