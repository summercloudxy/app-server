package com.zgiot.app.server.module.sfmedium.entity.vo;

import java.util.List;

public class MediumDosingSystemVO {

    /**
     * 加介系统名称
     */
    private String mediumDosingSystemName;
    /**
     * 加介系统名称
     */
    private String mediumDosingSystemId;

    /**
     * 分选系统
     */
    private List<SeparatingSystem> separatingSystemList;

    /**
     * 补水阀
     */
    private WaterSupplementValve waterSupplementValve;
    /**
     * 鼓风阀
     */
    private BlowerValve blowerValve;

    /**
     * 加介池
     */
    private MediumPool mediumPool;
    /**
     * 加介泵
     */
    private MediumDosingPump mediumDosingPump;


    public class SeparatingSystem {


        private String thingName;

        private String thingCode;

        private String metricCode;

        private String metricValue;

        private String metricValueName;

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

        public String getMetricCode() {
            return metricCode;
        }

        public void setMetricCode(String metricCode) {
            this.metricCode = metricCode;
        }

        public String getMetricValue() {
            return metricValue;
        }

        public void setMetricValue(String metricValue) {
            this.metricValue = metricValue;
        }

        public String getMetricValueName() {
            return metricValueName;
        }

        public void setMetricValueName(String metricValueName) {
            this.metricValueName = metricValueName;
        }
    }

    public class MediumPool {

        private String thingName;

        private String thingCode;

        private String levelPercent;

        private String thingImageName;

        private List<MetricData> metricDataList;

        public String getLevelPercent() {
            return levelPercent;
        }

        public void setLevelPercent(String levelPercent) {
            this.levelPercent = levelPercent;
        }

        public String getThingImageName() {
            return thingImageName;
        }

        public void setThingImageName(String thingImageName) {
            this.thingImageName = thingImageName;
        }

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

        public List<MetricData> getMetricDataList() {
            return metricDataList;
        }

        public void setMetricDataList(List<MetricData> metricDataList) {
            this.metricDataList = metricDataList;
        }
    }


    public class MetricData {

        private String metricName;
        private String metricCode;

        private String metricValue;
        private String metricValueName;


        public String getMetricName() {
            return metricName;
        }

        public void setMetricName(String metricName) {
            this.metricName = metricName;
        }

        public String getMetricCode() {
            return metricCode;
        }

        public void setMetricCode(String metricCode) {
            this.metricCode = metricCode;
        }

        public String getMetricValue() {
            return metricValue;
        }

        public void setMetricValue(String metricValue) {
            this.metricValue = metricValue;
        }

        public String getMetricValueName() {
            return metricValueName;
        }

        public void setMetricValueName(String metricValueName) {
            this.metricValueName = metricValueName;
        }
    }

    public class WaterSupplementValve {

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

    public class BlowerValve {

        private String thingName;
        private String thingCode;

        private String tapOpenMetricCode;
        private String tapOpenMetricValue;
        private String tapOpenMetricName;

        private String tapCloseMetricCode;
        private String tapCloseMetricValue;
        private String tapCloseMetricName;

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

        public String getTapOpenMetricValue() {
            return tapOpenMetricValue;
        }

        public void setTapOpenMetricValue(String tapOpenMetricValue) {
            this.tapOpenMetricValue = tapOpenMetricValue;
        }

        public String getTapOpenMetricName() {
            return tapOpenMetricName;
        }

        public void setTapOpenMetricName(String tapOpenMetricName) {
            this.tapOpenMetricName = tapOpenMetricName;
        }

        public String getTapCloseMetricCode() {
            return tapCloseMetricCode;
        }

        public void setTapCloseMetricCode(String tapCloseMetricCode) {
            this.tapCloseMetricCode = tapCloseMetricCode;
        }

        public String getTapCloseMetricValue() {
            return tapCloseMetricValue;
        }

        public void setTapCloseMetricValue(String tapCloseMetricValue) {
            this.tapCloseMetricValue = tapCloseMetricValue;
        }

        public String getTapCloseMetricName() {
            return tapCloseMetricName;
        }

        public void setTapCloseMetricName(String tapCloseMetricName) {
            this.tapCloseMetricName = tapCloseMetricName;
        }
    }

    public class MediumDosingPump {

        private String thingName;
        private String thingCode;

        private String runState;
        private String alertLevel;

        private String thingImageName;

        private String currentMetricName;
        private String currentMetricCode;
        private String currentMetricValue;


        private String intelligentMetricName;
        private String intelligentMetricCode;
        private String intelligentMetricValue;


        private String localMetricName;
        private String localMetricCode;
        private String localMetricValue;

        public String getThingImageName() {
            return thingImageName;
        }

        public void setThingImageName(String thingImageName) {
            this.thingImageName = thingImageName;
        }

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

        public String getAlertLevel() {
            return alertLevel;
        }

        public void setAlertLevel(String alertLevel) {
            this.alertLevel = alertLevel;
        }

        public String getCurrentMetricName() {
            return currentMetricName;
        }

        public void setCurrentMetricName(String currentMetricName) {
            this.currentMetricName = currentMetricName;
        }

        public String getCurrentMetricCode() {
            return currentMetricCode;
        }

        public void setCurrentMetricCode(String currentMetricCode) {
            this.currentMetricCode = currentMetricCode;
        }

        public String getCurrentMetricValue() {
            return currentMetricValue;
        }

        public void setCurrentMetricValue(String currentMetricValue) {
            this.currentMetricValue = currentMetricValue;
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

    public String getMediumDosingSystemName() {
        return mediumDosingSystemName;
    }

    public void setMediumDosingSystemName(String mediumDosingSystemName) {
        this.mediumDosingSystemName = mediumDosingSystemName;
    }

    public List<SeparatingSystem> getSeparatingSystemList() {
        return separatingSystemList;
    }

    public void setSeparatingSystemList(List<SeparatingSystem> separatingSystemList) {
        this.separatingSystemList = separatingSystemList;
    }

    public WaterSupplementValve getWaterSupplementValve() {
        return waterSupplementValve;
    }

    public void setWaterSupplementValve(WaterSupplementValve waterSupplementValve) {
        this.waterSupplementValve = waterSupplementValve;
    }

    public BlowerValve getBlowerValve() {
        return blowerValve;
    }

    public void setBlowerValve(BlowerValve blowerValve) {
        this.blowerValve = blowerValve;
    }

    public MediumPool getMediumPool() {
        return mediumPool;
    }

    public void setMediumPool(MediumPool mediumPool) {
        this.mediumPool = mediumPool;
    }

    public MediumDosingPump getMediumDosingPump() {
        return mediumDosingPump;
    }

    public void setMediumDosingPump(MediumDosingPump mediumDosingPump) {
        this.mediumDosingPump = mediumDosingPump;
    }

    public String getMediumDosingSystemId() {
        return mediumDosingSystemId;
    }

    public void setMediumDosingSystemId(String mediumDosingSystemId) {
        this.mediumDosingSystemId = mediumDosingSystemId;
    }
}
