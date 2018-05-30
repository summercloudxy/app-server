package com.zgiot.app.server.module.sfmedium.entity.vo;

import java.util.List;

/**
 * 加介进程监控
 */
public class SeparatingSystemVO {

    private MediumPool mediumPool;

    private List<SeparatingSystem> separatingSystemList;


    public MediumPool getMediumPool() {
        return mediumPool;
    }

    public void setMediumPool(MediumPool mediumPool) {
        this.mediumPool = mediumPool;
    }

    public List<SeparatingSystem> getSeparatingSystemList() {
        return separatingSystemList;
    }

    public void setSeparatingSystemList(List<SeparatingSystem> separatingSystemList) {
        this.separatingSystemList = separatingSystemList;
    }

    public class MediumPool {

        private String thingCode;

        private String thingName;

        private String mediumCompoundingMetricValue;

        private String mediumCompoundingMetricValueShow;

        private String level;

        private WterSupplementValue waterSupplementValue;

        private BlowerValve blowerValve;


        private MediumDosingPump mediumDosingPump;

        private String mediumCompoundingState;

        public String getThingCode() {
            return thingCode;
        }

        public void setThingCode(String thingCode) {
            this.thingCode = thingCode;
        }

        public String getThingName() {
            return thingName;
        }

        public void setThingName(String thingName) {
            this.thingName = thingName;
        }

        public String getMediumCompoundingMetricValue() {
            return mediumCompoundingMetricValue;
        }

        public void setMediumCompoundingMetricValue(String mediumCompoundingMetricValue) {
            this.mediumCompoundingMetricValue = mediumCompoundingMetricValue;
        }

        public String getMediumCompoundingMetricValueShow() {
            return mediumCompoundingMetricValueShow;
        }

        public void setMediumCompoundingMetricValueShow(String mediumCompoundingMetricValueShow) {
            this.mediumCompoundingMetricValueShow = mediumCompoundingMetricValueShow;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public WterSupplementValue getWaterSupplementValue() {
            return waterSupplementValue;
        }

        public void setWaterSupplementValue(WterSupplementValue waterSupplementValue) {
            this.waterSupplementValue = waterSupplementValue;
        }

        public BlowerValve getBlowerValve() {
            return blowerValve;
        }

        public void setBlowerValve(BlowerValve blowerValve) {
            this.blowerValve = blowerValve;
        }

        public MediumDosingPump getMediumDosingPump() {
            return mediumDosingPump;
        }

        public void setMediumDosingPump(MediumDosingPump mediumDosingPump) {
            this.mediumDosingPump = mediumDosingPump;
        }

        public String getMediumCompoundingState() {
            return mediumCompoundingState;
        }

        public void setMediumCompoundingState(String mediumCompoundingState) {
            this.mediumCompoundingState = mediumCompoundingState;
        }
    }

    public class SeparatingSystem {

        private String systemName;

        private String combinedBucketLevel;

        private String combinedBucketDensity;

        private String elapsedTime;

        private String remainingTime;

        private String mediumDosingState;

        private String mediumDosingStateShow;

        public String getSystemName() {
            return systemName;
        }

        public void setSystemName(String systemName) {
            this.systemName = systemName;
        }

        public String getCombinedBucketLevel() {
            return combinedBucketLevel;
        }

        public void setCombinedBucketLevel(String combinedBucketLevel) {
            this.combinedBucketLevel = combinedBucketLevel;
        }

        public String getCombinedBucketDensity() {
            return combinedBucketDensity;
        }

        public void setCombinedBucketDensity(String combinedBucketDensity) {
            this.combinedBucketDensity = combinedBucketDensity;
        }

        public String getElapsedTime() {
            return elapsedTime;
        }

        public void setElapsedTime(String elapsedTime) {
            this.elapsedTime = elapsedTime;
        }

        public String getRemainingTime() {
            return remainingTime;
        }

        public void setRemainingTime(String remainingTime) {
            this.remainingTime = remainingTime;
        }

        public String getMediumDosingState() {
            return mediumDosingState;
        }

        public void setMediumDosingState(String mediumDosingState) {
            this.mediumDosingState = mediumDosingState;
        }

        public String getMediumDosingStateShow() {
            return mediumDosingStateShow;
        }

        public void setMediumDosingStateShow(String mediumDosingStateShow) {
            this.mediumDosingStateShow = mediumDosingStateShow;
        }
    }

    public class WterSupplementValue {

        private String thingName;

        private String thingCode;
        private String tapOpenMetricCode;
        private String tapOpenMetricName;
        private String tapOpenMetricValue;
        private String tapCloseMetricCode;
        private String tapCloseMetricName;
        private String tapCloseMeticValue;

        public String getThingName() {
            return thingName;
        }

        public void setThingName(String thingName) {
            this.thingName = thingName;
        }

        public String getThingCode() {
            return thingCode;
        }

        public void setThingCode(String thingCode) {
            this.thingCode = thingCode;
        }

        public String getTapOpenMetricCode() {
            return tapOpenMetricCode;
        }

        public void setTapOpenMetricCode(String tapOpenMetricCode) {
            this.tapOpenMetricCode = tapOpenMetricCode;
        }

        public String getTapOpenMetricName() {
            return tapOpenMetricName;
        }

        public void setTapOpenMetricName(String tapOpenMetricName) {
            this.tapOpenMetricName = tapOpenMetricName;
        }

        public String getTapOpenMetricValue() {
            return tapOpenMetricValue;
        }

        public void setTapOpenMetricValue(String tapOpenMetricValue) {
            this.tapOpenMetricValue = tapOpenMetricValue;
        }

        public String getTapCloseMetricCode() {
            return tapCloseMetricCode;
        }

        public void setTapCloseMetricCode(String tapCloseMetricCode) {
            this.tapCloseMetricCode = tapCloseMetricCode;
        }

        public String getTapCloseMetricName() {
            return tapCloseMetricName;
        }

        public void setTapCloseMetricName(String tapCloseMetricName) {
            this.tapCloseMetricName = tapCloseMetricName;
        }

        public String getTapCloseMeticValue() {
            return tapCloseMeticValue;
        }

        public void setTapCloseMeticValue(String tapCloseMeticValue) {
            this.tapCloseMeticValue = tapCloseMeticValue;
        }
    }

    public class BlowerValve {


        private String thingName;

        private String thingCode;

        private String tapOpenMetricCode;
        private String tapOpenMetricName;
        private String tapOpenMetricValue;
        private String tapCloseMetricCode;
        private String tapCloseMetricName;
        private String tapCloseMetricValue;


        public String getThingName() {
            return thingName;
        }

        public void setThingName(String thingName) {
            this.thingName = thingName;
        }

        public String getThingCode() {
            return thingCode;
        }

        public void setThingCode(String thingCode) {
            this.thingCode = thingCode;
        }

        public String getTapOpenMetricCode() {
            return tapOpenMetricCode;
        }

        public void setTapOpenMetricCode(String tapOpenMetricCode) {
            this.tapOpenMetricCode = tapOpenMetricCode;
        }

        public String getTapOpenMetricName() {
            return tapOpenMetricName;
        }

        public void setTapOpenMetricName(String tapOpenMetricName) {
            this.tapOpenMetricName = tapOpenMetricName;
        }

        public String getTapOpenMetricValue() {
            return tapOpenMetricValue;
        }

        public void setTapOpenMetricValue(String tapOpenMetricValue) {
            this.tapOpenMetricValue = tapOpenMetricValue;
        }

        public String getTapCloseMetricCode() {
            return tapCloseMetricCode;
        }

        public void setTapCloseMetricCode(String tapCloseMetricCode) {
            this.tapCloseMetricCode = tapCloseMetricCode;
        }

        public String getTapCloseMetricName() {
            return tapCloseMetricName;
        }

        public void setTapCloseMetricName(String tapCloseMetricName) {
            this.tapCloseMetricName = tapCloseMetricName;
        }


        public String getTapCloseMetricValue() {
            return tapCloseMetricValue;
        }

        public void setTapCloseMetricValue(String tapCloseMetricValue) {
            this.tapCloseMetricValue = tapCloseMetricValue;
        }
    }

    public class MediumDosingPump {

        private String thingName;
        private String thingCode;

        private String runState;

        private String intelligentMetricName;
        private String intelligentMetricCode;
        private String intelligentMetricValue;


        private String localMetricName;
        private String localMetricCode;
        private String localMetricValue;


        public String getThingName() {
            return thingName;
        }

        public void setThingName(String thingName) {
            this.thingName = thingName;
        }

        public String getThingCode() {
            return thingCode;
        }

        public void setThingCode(String thingCode) {
            this.thingCode = thingCode;
        }

        public String getRunState() {
            return runState;
        }

        public void setRunState(String runState) {
            this.runState = runState;
        }

        public String getIntelligentMetricName() {
            return intelligentMetricName;
        }

        public void setIntelligentMetricName(String intelligentMetricName) {
            this.intelligentMetricName = intelligentMetricName;
        }

        public String getIntelligentMetricCode() {
            return intelligentMetricCode;
        }

        public void setIntelligentMetricCode(String intelligentMetricCode) {
            this.intelligentMetricCode = intelligentMetricCode;
        }

        public String getIntelligentMetricValue() {
            return intelligentMetricValue;
        }

        public void setIntelligentMetricValue(String intelligentMetricValue) {
            this.intelligentMetricValue = intelligentMetricValue;
        }

        public String getLocalMetricName() {
            return localMetricName;
        }

        public void setLocalMetricName(String localMetricName) {
            this.localMetricName = localMetricName;
        }

        public String getLocalMetricCode() {
            return localMetricCode;
        }

        public void setLocalMetricCode(String localMetricCode) {
            this.localMetricCode = localMetricCode;
        }

        public String getLocalMetricValue() {
            return localMetricValue;
        }

        public void setLocalMetricValue(String localMetricValue) {
            this.localMetricValue = localMetricValue;
        }
    }
}
