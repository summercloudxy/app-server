package com.zgiot.app.server.module.sfsubsc.entity.vo;

import java.util.List;

/**
 * 化验数据详情
 *
 * @author jys
 */
public class ChemicalTestsDataChartDetailVO {

    private String cleanCoalName;

    private String mixedCoadName;

    private String cleanCoalAadName;

    private String mixedCoadAadName;

    private String cleanCoalAadValue;

    private String mixedCoadAadValue;

    private String cleanCoalAadAvgValue;

    private String mixedCoadAadAvgValue;

    private String ashContentCleanCoalScope;
    private String ashContentMixedCoalScope;
    private String sulfurContentScope;
    private String moistureContentScope;

    private List<String> times;

    private List<String> cleanCoalAadValues;
    private List<String> cleanCoalStadValues;


    private List<String> mixedCoalAadValues;
    private List<String> mixedCoalMtValues;

    public String getCleanCoalName() {
        return cleanCoalName;
    }

    public void setCleanCoalName(String cleanCoalName) {
        this.cleanCoalName = cleanCoalName;
    }

    public String getMixedCoadName() {
        return mixedCoadName;
    }

    public void setMixedCoadName(String mixedCoadName) {
        this.mixedCoadName = mixedCoadName;
    }

    public String getCleanCoalAadName() {
        return cleanCoalAadName;
    }

    public void setCleanCoalAadName(String cleanCoalAadName) {
        this.cleanCoalAadName = cleanCoalAadName;
    }

    public String getMixedCoadAadName() {
        return mixedCoadAadName;
    }

    public void setMixedCoadAadName(String mixedCoadAadName) {
        this.mixedCoadAadName = mixedCoadAadName;
    }

    public String getCleanCoalAadValue() {
        return cleanCoalAadValue;
    }

    public void setCleanCoalAadValue(String cleanCoalAadValue) {
        this.cleanCoalAadValue = cleanCoalAadValue;
    }

    public String getMixedCoadAadValue() {
        return mixedCoadAadValue;
    }

    public void setMixedCoadAadValue(String mixedCoadAadValue) {
        this.mixedCoadAadValue = mixedCoadAadValue;
    }

    public String getCleanCoalAadAvgValue() {
        return cleanCoalAadAvgValue;
    }

    public void setCleanCoalAadAvgValue(String cleanCoalAadAvgValue) {
        this.cleanCoalAadAvgValue = cleanCoalAadAvgValue;
    }

    public String getMixedCoadAadAvgValue() {
        return mixedCoadAadAvgValue;
    }

    public void setMixedCoadAadAvgValue(String mixedCoadAadAvgValue) {
        this.mixedCoadAadAvgValue = mixedCoadAadAvgValue;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public List<String> getCleanCoalAadValues() {
        return cleanCoalAadValues;
    }

    public void setCleanCoalAadValues(List<String> cleanCoalAadValues) {
        this.cleanCoalAadValues = cleanCoalAadValues;
    }

    public List<String> getCleanCoalStadValues() {
        return cleanCoalStadValues;
    }

    public void setCleanCoalStadValues(List<String> cleanCoalStadValues) {
        this.cleanCoalStadValues = cleanCoalStadValues;
    }

    public List<String> getMixedCoalAadValues() {
        return mixedCoalAadValues;
    }

    public void setMixedCoalAadValues(List<String> mixedCoalAadValues) {
        this.mixedCoalAadValues = mixedCoalAadValues;
    }

    public List<String> getMixedCoalMtValues() {
        return mixedCoalMtValues;
    }

    public void setMixedCoalMtValues(List<String> mixedCoalMtValues) {
        this.mixedCoalMtValues = mixedCoalMtValues;
    }

    public String getAshContentCleanCoalScope() {
        return ashContentCleanCoalScope;
    }

    public void setAshContentCleanCoalScope(String ashContentCleanCoalScope) {
        this.ashContentCleanCoalScope = ashContentCleanCoalScope;
    }

    public String getAshContentMixedCoalScope() {
        return ashContentMixedCoalScope;
    }

    public void setAshContentMixedCoalScope(String ashContentMixedCoalScope) {
        this.ashContentMixedCoalScope = ashContentMixedCoalScope;
    }

    public String getSulfurContentScope() {
        return sulfurContentScope;
    }

    public void setSulfurContentScope(String sulfurContentScope) {
        this.sulfurContentScope = sulfurContentScope;
    }

    public String getMoistureContentScope() {
        return moistureContentScope;
    }

    public void setMoistureContentScope(String moistureContentScope) {
        this.moistureContentScope = moistureContentScope;
    }
}
