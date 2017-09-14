package com.zgiot.app.server.module.filterpress;


public class FilterPress {
    private String code;

    /**
     * 入料开始时间，若不处于入料状态，值为空
     */
    private Long feedStartTime;
    /**
     * 入料结束时间
     */
    private Long feedOverTime;
    /**
     * 进料时长
     */
    private Long feedDuration;
    /**
     * 入料泵电流
     */
    private double feedPumpCurrent;

    public void onRun() {

    }

    public void onStop() {

    }

    public void onFault() {

    }

    public void onLoosen() {

    }

    public void onTaken() {

    }

    public void onPull() {

    }

    public void onPress() {

    }

    public void onFeed() {

    }

    public void onFeedOver() {

    }

    public void onBlow() {

    }

    public void onCycle() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setFeedOverTime(Long feedOverTime) {
        this.feedOverTime = feedOverTime;
    }

    public Long getFeedDuration() {
        return feedDuration;
    }

    public void setFeedDuration(Long feedDuration) {
        this.feedDuration = feedDuration;
    }

    public Long getFeedStartTime() {
        return feedStartTime;
    }

    public void setFeedStartTime(Long feedStartTime) {
        this.feedStartTime = feedStartTime;
    }

    public Long getFeedOverTime() {
        return feedOverTime;
    }

    public double getFeedPumpCurrent() {
        return feedPumpCurrent;
    }

    public void setFeedPumpCurrent(double feedPumpCurrent) {
        this.feedPumpCurrent = feedPumpCurrent;
    }
}
