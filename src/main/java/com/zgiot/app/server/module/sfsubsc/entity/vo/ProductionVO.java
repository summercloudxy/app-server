package com.zgiot.app.server.module.sfsubsc.entity.vo;

public class ProductionVO {

    /**
     * 卡片标题
     */
    private String cardTitle;

    /**
     * 日期
     */
    private String date;

    /**
     * 班次
     */
    private String shift;

    /**
     * 班次开始时间
     */
    private String timeBegin;

    /**
     * 班次结束时间
     */
    private String timeEnd;

    /**
     * 入洗原煤
     */
    private String rawCoal;

    /**
     * 综合产率
     */
    private String totalYield;

    /**
     * 精煤产率
     */
    private String cleanCoalYield;

    /**
     * 混煤产率
     */
    private String mixedCoalYield;

    /**
     * 煤泥产率
     */
    private String coalMudYield;

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(String timeBegin) {
        this.timeBegin = timeBegin;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getRawCoal() {
        return rawCoal;
    }

    public void setRawCoal(String rawCoal) {
        this.rawCoal = rawCoal;
    }

    public String getTotalYield() {
        return totalYield;
    }

    public void setTotalYield(String totalYield) {
        this.totalYield = totalYield;
    }

    public String getCleanCoalYield() {
        return cleanCoalYield;
    }

    public void setCleanCoalYield(String cleanCoalYield) {
        this.cleanCoalYield = cleanCoalYield;
    }

    public String getMixedCoalYield() {
        return mixedCoalYield;
    }

    public void setMixedCoalYield(String mixedCoalYield) {
        this.mixedCoalYield = mixedCoalYield;
    }

    public String getCoalMudYield() {
        return coalMudYield;
    }

    public void setCoalMudYield(String coalMudYield) {
        this.coalMudYield = coalMudYield;
    }
}
