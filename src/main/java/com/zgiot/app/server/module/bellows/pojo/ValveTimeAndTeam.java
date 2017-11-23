package com.zgiot.app.server.module.bellows.pojo;


/**
 * @author wangwei
 */
public class ValveTimeAndTeam {

    private long nextBlowTime;

    private int lumpCount;

    private int slackCount;

    public ValveTimeAndTeam(Long nextBlowTime, int lumpCount, int slackCount) {
        this.nextBlowTime = nextBlowTime;
        this.lumpCount = lumpCount;
        this.slackCount = slackCount;
    }

    public Long getNextBlowTime() {
        return nextBlowTime;
    }

    public void setNextBlowTime(Long nextBlowTime) {
        this.nextBlowTime = nextBlowTime;
    }

    public int getLumpCount() {
        return lumpCount;
    }

    public void setLumpCount(int lumpCount) {
        this.lumpCount = lumpCount;
    }

    public int getSlackCount() {
        return slackCount;
    }

    public void setSlackCount(int slackCount) {
        this.slackCount = slackCount;
    }
}
