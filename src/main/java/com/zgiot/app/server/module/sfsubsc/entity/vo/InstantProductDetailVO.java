package com.zgiot.app.server.module.sfsubsc.entity.vo;


import java.util.List;

/**
 * 瞬时产品量详情
 *
 * @author jys
 */
public class InstantProductDetailVO {


    private String rawCoalName;

    private String cleanCoalName;

    private String mixedCoadName;

    private String wasteRockName;

    private String rawCoalCapValue;

    private String cleanCoalCapValue;

    private String mixedCoadCapValue;

    private String wasteRockCapValue;

    private String rawCoalAvgValue;

    private String cleanCoalAvgValue;

    private String mixedCoadAvgValue;

    private String wasteRockAvgValue;


    private List<String> times;

    private List<String> rawCoalCapLists;

    private List<String> cleanCoalCapLists;

    private List<String> mixedCoadCapists;

    private List<String> wasteRockCapLists;

    public String getRawCoalName() {
        return rawCoalName;
    }

    public void setRawCoalName(String rawCoalName) {
        this.rawCoalName = rawCoalName;
    }

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

    public String getWasteRockName() {
        return wasteRockName;
    }

    public void setWasteRockName(String wasteRockName) {
        this.wasteRockName = wasteRockName;
    }

    public String getRawCoalCapValue() {
        return rawCoalCapValue;
    }

    public void setRawCoalCapValue(String rawCoalCapValue) {
        this.rawCoalCapValue = rawCoalCapValue;
    }

    public String getCleanCoalCapValue() {
        return cleanCoalCapValue;
    }

    public void setCleanCoalCapValue(String cleanCoalCapValue) {
        this.cleanCoalCapValue = cleanCoalCapValue;
    }

    public String getMixedCoadCapValue() {
        return mixedCoadCapValue;
    }

    public void setMixedCoadCapValue(String mixedCoadCapValue) {
        this.mixedCoadCapValue = mixedCoadCapValue;
    }

    public String getWasteRockCapValue() {
        return wasteRockCapValue;
    }

    public void setWasteRockCapValue(String wasteRockCapValue) {
        this.wasteRockCapValue = wasteRockCapValue;
    }

    public String getRawCoalAvgValue() {
        return rawCoalAvgValue;
    }

    public void setRawCoalAvgValue(String rawCoalAvgValue) {
        this.rawCoalAvgValue = rawCoalAvgValue;
    }

    public String getCleanCoalAvgValue() {
        return cleanCoalAvgValue;
    }

    public void setCleanCoalAvgValue(String cleanCoalAvgValue) {
        this.cleanCoalAvgValue = cleanCoalAvgValue;
    }

    public String getMixedCoadAvgValue() {
        return mixedCoadAvgValue;
    }

    public void setMixedCoadAvgValue(String mixedCoadAvgValue) {
        this.mixedCoadAvgValue = mixedCoadAvgValue;
    }

    public String getWasteRockAvgValue() {
        return wasteRockAvgValue;
    }

    public void setWasteRockAvgValue(String wasteRockAvgValue) {
        this.wasteRockAvgValue = wasteRockAvgValue;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public List<String> getRawCoalCapLists() {
        return rawCoalCapLists;
    }

    public void setRawCoalCapLists(List<String> rawCoalCapLists) {
        this.rawCoalCapLists = rawCoalCapLists;
    }

    public List<String> getCleanCoalCapLists() {
        return cleanCoalCapLists;
    }

    public void setCleanCoalCapLists(List<String> cleanCoalCapLists) {
        this.cleanCoalCapLists = cleanCoalCapLists;
    }


    public List<String> getWasteRockCapLists() {
        return wasteRockCapLists;
    }

    public void setWasteRockCapLists(List<String> wasteRockCapLists) {
        this.wasteRockCapLists = wasteRockCapLists;
    }

    public List<String> getMixedCoadCapists() {
        return mixedCoadCapists;
    }

    public void setMixedCoadCapists(List<String> mixedCoadCapists) {
        this.mixedCoadCapists = mixedCoadCapists;
    }
}
