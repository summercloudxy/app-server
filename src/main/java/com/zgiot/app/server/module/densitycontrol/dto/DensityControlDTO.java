package com.zgiot.app.server.module.densitycontrol.dto;

import java.util.List;

public class DensityControlDTO {

    private Integer term;// 期
    private List<String> thingCodeList;// 关联设备
    private Double levelSetHigh;// 生产高液位
    private Double levelSetStop;// 停车安全液位
    private Double levelSetJJ;// 加介液位
    private Double levelSetLow;// 生产低液位
    private Double timeStop;// 停车预留时间

    // 正常液位逻辑参数
    private Double delayTest;// 检测延时
    private Double delayTestFL;// 分流阀开启检测延时
    private Double delayTestBS;// 补水阀开启检测延时
    private Double timeFL;// 分流阀开启时间
    private Double timeBS;// 补水阀开启时间
    private Double densityMax;// 密度最大范围
    private Double densityMin;// 密度最小范围

    // 高液位逻辑参数
    private Double timeFLHigh;// 分流阀开启时间
    private Double timeExecuteHigh;// 逻辑执行时间

    // 低液位逻辑参数
    private Double densityMinLow;// 密度最小范围
    private Double timeBSLow;// 补水阀开启时间
    private Double levelMaxLow;// 液位最大范围
    private Double levelMinLow;// 液位最小范围

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public List<String> getThingCodeList() {
        return thingCodeList;
    }

    public void setThingCodeList(List<String> thingCodeList) {
        this.thingCodeList = thingCodeList;
    }

    public Double getLevelSetHigh() {
        return levelSetHigh;
    }

    public void setLevelSetHigh(Double levelSetHigh) {
        this.levelSetHigh = levelSetHigh;
    }

    public Double getLevelSetStop() {
        return levelSetStop;
    }

    public void setLevelSetStop(Double levelSetStop) {
        this.levelSetStop = levelSetStop;
    }

    public Double getLevelSetJJ() {
        return levelSetJJ;
    }

    public void setLevelSetJJ(Double levelSetJJ) {
        this.levelSetJJ = levelSetJJ;
    }

    public Double getLevelSetLow() {
        return levelSetLow;
    }

    public void setLevelSetLow(Double levelSetLow) {
        this.levelSetLow = levelSetLow;
    }

    public Double getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(Double timeStop) {
        this.timeStop = timeStop;
    }

    public Double getDelayTest() {
        return delayTest;
    }

    public void setDelayTest(Double delayTest) {
        this.delayTest = delayTest;
    }

    public Double getDelayTestFL() {
        return delayTestFL;
    }

    public void setDelayTestFL(Double delayTestFL) {
        this.delayTestFL = delayTestFL;
    }

    public Double getDelayTestBS() {
        return delayTestBS;
    }

    public void setDelayTestBS(Double delayTestBS) {
        this.delayTestBS = delayTestBS;
    }

    public Double getTimeFL() {
        return timeFL;
    }

    public void setTimeFL(Double timeFL) {
        this.timeFL = timeFL;
    }

    public Double getTimeBS() {
        return timeBS;
    }

    public void setTimeBS(Double timeBS) {
        this.timeBS = timeBS;
    }

    public Double getDensityMax() {
        return densityMax;
    }

    public void setDensityMax(Double densityMax) {
        this.densityMax = densityMax;
    }

    public Double getDensityMin() {
        return densityMin;
    }

    public void setDensityMin(Double densityMin) {
        this.densityMin = densityMin;
    }

    public Double getTimeFLHigh() {
        return timeFLHigh;
    }

    public void setTimeFLHigh(Double timeFLHigh) {
        this.timeFLHigh = timeFLHigh;
    }

    public Double getTimeExecuteHigh() {
        return timeExecuteHigh;
    }

    public void setTimeExecuteHigh(Double timeExecuteHigh) {
        this.timeExecuteHigh = timeExecuteHigh;
    }

    public Double getDensityMinLow() {
        return densityMinLow;
    }

    public void setDensityMinLow(Double densityMinLow) {
        this.densityMinLow = densityMinLow;
    }

    public Double getTimeBSLow() {
        return timeBSLow;
    }

    public void setTimeBSLow(Double timeBSLow) {
        this.timeBSLow = timeBSLow;
    }

    public Double getLevelMaxLow() {
        return levelMaxLow;
    }

    public void setLevelMaxLow(Double levelMaxLow) {
        this.levelMaxLow = levelMaxLow;
    }

    public Double getLevelMinLow() {
        return levelMinLow;
    }

    public void setLevelMinLow(Double levelMinLow) {
        this.levelMinLow = levelMinLow;
    }
}
