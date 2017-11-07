package com.zgiot.app.server.module.bellows.compressor;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author wangwei
 */
public class Compressor {

    public static final String TYPE_LOW = "low";
    public static final String TYPE_HIGH = "high";

    public static final int YES = 1;
    public static final int NO = 0;

    /**
     * 名称
     */
    private final String name;

    /**
     * 设备号
     */
    private final String thingCode;

    /**
     * 类型
     */
    private final String type;

    /**
     * 排序
     */
    private final int sort;


    @JSONField(serialize = false)
    private final CompressorManager manager;

    /**
     * 是否就地，1集控，0就地
     */
    private volatile int remote;

    /**
     * 加载状态
     */
    private volatile int loadState;

    /**
     * 运行状态
     */
    private volatile int runState;

    /**
     * 排气压力
     */
    private volatile double pressure;

    /**
     * 排气温度
     */
    private volatile double temperature;

    /**
     * 当前电流
     */
    private volatile double current;

    /**
     * 额定电流
     */
    private volatile double ratedCurrent;

    /**
     * 运行时间
     */
    private volatile int runTime;

    /**
     * 加载时间
     */
    private volatile int loadTime;

    public Compressor(String name, String thingCode, String type, int sort, CompressorManager manager) {
        this.name = name;
        this.thingCode = thingCode;
        this.type = type;
        this.sort = sort;
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public String getThingCode() {
        return thingCode;
    }

    public String getType() {
        return type;
    }

    public int getSort() {
        return sort;
    }

    public CompressorManager getManager() {
        return manager;
    }

    public int getRemote() {
        return remote;
    }

    public void setRemote(int remote) {
        this.remote = remote;
    }

    public int getLoadState() {
        return loadState;
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
    }

    public int getRunState() {
        return runState;
    }

    public void setRunState(int runState) {
        this.runState = runState;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getRatedCurrent() {
        return ratedCurrent;
    }

    public void setRatedCurrent(double ratedCurrent) {
        this.ratedCurrent = ratedCurrent;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public int getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(int loadTime) {
        this.loadTime = loadTime;
    }
}
