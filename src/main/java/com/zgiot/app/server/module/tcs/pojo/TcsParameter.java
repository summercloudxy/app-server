package com.zgiot.app.server.module.tcs.pojo;

/**
 * Created by xiayun on 2017/6/2.
 */
public class TcsParameter {
    /**
     * 瞬时带煤量
     */
    private Double currentCoal;
    /**
     * 本班带煤量
     */
    private Double classCoal;
    /**
     * 本月带煤量
     */
    private Double monthCoal;
    /**
     * 在线灰分
     */
    private Double currentAsh;
    /**
     * TCS入料量
     */
    private Double tcsFeed;
    /**
     * 单桶入料量
     */
    private Double tcsSingleFeed;
    private String currentCoalStr;
    private String classCoalStr;
    private String monthCoalStr;
    private String currentAshStr;
    private String tcsFeedStr;
    private String tcsSingleFeedStr;

    public String getCurrentCoalStr() {
        return currentCoalStr;
    }

    public void setCurrentCoalStr(String currentCoalStr) {
        this.currentCoalStr = currentCoalStr;
    }

    public String getClassCoalStr() {
        return classCoalStr;
    }

    public void setClassCoalStr(String classCoalStr) {
        this.classCoalStr = classCoalStr;
    }

    public String getMonthCoalStr() {
        return monthCoalStr;
    }

    public void setMonthCoalStr(String monthCoalStr) {
        this.monthCoalStr = monthCoalStr;
    }

    public String getCurrentAshStr() {
        return currentAshStr;
    }

    public void setCurrentAshStr(String currentAshStr) {
        this.currentAshStr = currentAshStr;
    }

    public String getTcsFeedStr() {
        return tcsFeedStr;
    }

    public void setTcsFeedStr(String tcsFeedStr) {
        this.tcsFeedStr = tcsFeedStr;
    }

    public String getTcsSingleFeedStr() {
        return tcsSingleFeedStr;
    }

    public void setTcsSingleFeedStr(String tcsSingleFeedStr) {
        this.tcsSingleFeedStr = tcsSingleFeedStr;
    }

    public Double getCurrentCoal() {
        return currentCoal;
    }

    public void setCurrentCoal(Double currentCoal) {
        this.currentCoal = currentCoal;
    }

    public Double getClassCoal() {
        return classCoal;
    }

    public void setClassCoal(Double classCoal) {
        this.classCoal = classCoal;
    }

    public Double getMonthCoal() {
        return monthCoal;
    }

    public void setMonthCoal(Double monthCoal) {
        this.monthCoal = monthCoal;
    }

    public Double getCurrentAsh() {
        return currentAsh;
    }

    public void setCurrentAsh(Double currentAsh) {
        this.currentAsh = currentAsh;
    }

    public Double getTcsFeed() {
        return tcsFeed;
    }

    public void setTcsFeed(Double tcsFeed) {
        this.tcsFeed = tcsFeed;
    }

    public Double getTcsSingleFeed() {
        return tcsSingleFeed;
    }

    public void setTcsSingleFeed(Double tcsSingleFeed) {
        this.tcsSingleFeed = tcsSingleFeed;
    }

    public TcsParameter() {
    }

    public TcsParameter(Double currentCoal, Double classCoal, Double monthCoal, Double currentAsh, Double tcsFeed, Double tcsSingleFeed) {
        this.currentCoal = currentCoal;
        this.classCoal = classCoal;
        this.monthCoal = monthCoal;
        this.currentAsh = currentAsh;
        this.tcsFeed = tcsFeed;
        this.tcsSingleFeed = tcsSingleFeed;
    }

    public TcsParameter(String currentCoalStr, String classCoalStr, String monthCoalStr, String currentAshStr, String tcsFeedStr, String tcsSingleFeedStr) {
        this.currentCoalStr = currentCoalStr;
        this.classCoalStr = classCoalStr;
        this.monthCoalStr = monthCoalStr;
        this.currentAshStr = currentAshStr;
        this.tcsFeedStr = tcsFeedStr;
        this.tcsSingleFeedStr = tcsSingleFeedStr;
    }
}
