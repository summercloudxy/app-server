package com.zgiot.app.server.module.densitycontrol.pojo;

public class MonitoringParam {

    private Boolean intelligentState = false;

    private Boolean preStopState = false;

    private String thingCode;

    private String currentStage;

    //比例阀开度
    private Double currentValveOpening;
    //设定高开度
    private Double settedHighValveOpening;
    //设定低开度
    private Double settedLowValveOpening;
    //停车前比例阀设定低开度
    private Double preStopSettedLowValveOpening;

    //当前密度
    private Double currentDensity;
    //设定密度
    private Double settedDensity;
    //密度波动值
    private Double fluctuantDensity;

    //当前液位
    private Double currentFuelLevel;

    //设定高液位
    private Double settedHighLevel;
    //设定低液位
    private Double settedLowLevel;
    //停车前合介桶设定高液位
    private Double preStopSettedHighLevel;
    //密度所属区间
    private int densityStage;
    //开度所属区间
    private int valveOpeningStage;
    //液位所属区间
    private int levelStage;

    public Double getCurrentValveOpening() {
        return currentValveOpening;
    }

    public void setCurrentValveOpening(Double currentValveOpening) {
        this.currentValveOpening = currentValveOpening;
    }

    public Double getSettedHighValveOpening() {
        return settedHighValveOpening;
    }

    public void setSettedHighValveOpening(Double settedHighValveOpening) {
        this.settedHighValveOpening = settedHighValveOpening;
    }

    public Double getSettedLowValveOpening() {
        return settedLowValveOpening;
    }

    public void setSettedLowValveOpening(Double settedLowValveOpening) {
        this.settedLowValveOpening = settedLowValveOpening;
    }

    public Double getPreStopSettedLowValveOpening() {
        return preStopSettedLowValveOpening;
    }

    public void setPreStopSettedLowValveOpening(Double preStopSettedLowValveOpening) {
        this.preStopSettedLowValveOpening = preStopSettedLowValveOpening;
    }

    public Double getCurrentDensity() {
        return currentDensity;
    }

    public void setCurrentDensity(Double currentDensity) {
        this.currentDensity = currentDensity;
    }

    public Double getSettedDensity() {
        return settedDensity;
    }

    public void setSettedDensity(Double settedDensity) {
        this.settedDensity = settedDensity;
    }

    public Double getFluctuantDensity() {
        return fluctuantDensity;
    }

    public void setFluctuantDensity(Double fluctuantDensity) {
        this.fluctuantDensity = fluctuantDensity;
    }

    public Double getCurrentFuelLevel() {
        return currentFuelLevel;
    }

    public void setCurrentFuelLevel(Double currentFuelLevel) {
        this.currentFuelLevel = currentFuelLevel;
    }

    public Double getSettedHighLevel() {
        return settedHighLevel;
    }

    public void setSettedHighLevel(Double settedHighLevel) {
        this.settedHighLevel = settedHighLevel;
    }

    public Double getSettedLowLevel() {
        return settedLowLevel;
    }

    public void setSettedLowLevel(Double settedLowLevel) {
        this.settedLowLevel = settedLowLevel;
    }

    public Double getPreStopSettedHighLevel() {
        return preStopSettedHighLevel;
    }

    public void setPreStopSettedHighLevel(Double preStopSettedHighLevel) {
        this.preStopSettedHighLevel = preStopSettedHighLevel;
    }

    public int getDensityStage() {
        return densityStage;
    }

    public void setDensityStage(int densityStage) {
        this.densityStage = densityStage;
    }

    public int getValveOpeningStage() {
        return valveOpeningStage;
    }

    public void setValveOpeningStage(int valveOpeningStage) {
        this.valveOpeningStage = valveOpeningStage;
    }

    public int getLevelStage() {
        return levelStage;
    }

    public void setLevelStage(int levelStage) {
        this.levelStage = levelStage;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public Boolean getIntelligentState() {
        return intelligentState;
    }

    public void setIntelligentState(Boolean intelligentState) {
        this.intelligentState = intelligentState;
    }

    public Boolean getPreStopState() {
        return preStopState;
    }

    public void setPreStopState(Boolean preStopState) {
        this.preStopState = preStopState;
    }

    public String getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }

    public MonitoringParam() {
    }

    public MonitoringParam(String thingCode) {
        this.thingCode = thingCode;
    }
}
