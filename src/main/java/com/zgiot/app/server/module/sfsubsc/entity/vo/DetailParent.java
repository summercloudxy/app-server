package com.zgiot.app.server.module.sfsubsc.entity.vo;

import java.util.Date;

public class DetailParent {

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
     * 班次有效开始时间
     */
    Date shiftTimeBegin;

    /**
     * 班次有效结束时间
     */
    Date shiftTimeEnd;

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

    public Date getShiftTimeBegin() {
        return shiftTimeBegin;
    }

    public void setShiftTimeBegin(Date shiftTimeBegin) {
        this.shiftTimeBegin = shiftTimeBegin;
    }

    public Date getShiftTimeEnd() {
        return shiftTimeEnd;
    }

    public void setShiftTimeEnd(Date shiftTimeEnd) {
        this.shiftTimeEnd = shiftTimeEnd;
    }

}
