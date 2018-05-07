package com.zgiot.app.server.module.sfmedium.entity.vo;

import java.util.List;

/**
 * 加介控制
 */
public class MediumDosingControlVO {

    private MediumDosingPump mediumDosingPump;

    private List<ValueControl> valueControls;


    public MediumDosingPump getMediumDosingPump() {
        return mediumDosingPump;
    }

    public void setMediumDosingPump(MediumDosingPump mediumDosingPump) {
        this.mediumDosingPump = mediumDosingPump;
    }

    public List<ValueControl> getValueControls() {
        return valueControls;
    }

    public void setValueControls(List<ValueControl> valueControls) {
        this.valueControls = valueControls;
    }

    public class ValueControl {

        private String title1;

        private ChangeValue changeValue;

        private String title2;

        private List<MediumDosingValue> mediumDosingValues;

        private List<CombinedBucket> combinedBuckets;

        public ChangeValue getChangeValue() {
            return changeValue;
        }

        public void setChangeValue(ChangeValue changeValue) {
            this.changeValue = changeValue;
        }

        public List<MediumDosingValue> getMediumDosingValues() {
            return mediumDosingValues;
        }

        public void setMediumDosingValues(List<MediumDosingValue> mediumDosingValues) {
            this.mediumDosingValues = mediumDosingValues;
        }

        public List<CombinedBucket> getCombinedBuckets() {
            return combinedBuckets;
        }

        public void setCombinedBuckets(List<CombinedBucket> combinedBuckets) {
            this.combinedBuckets = combinedBuckets;
        }

        public String getTitle1() {
            return title1;
        }

        public void setTitle1(String title1) {
            this.title1 = title1;
        }

        public String getTitle2() {
            return title2;
        }

        public void setTitle2(String title2) {
            this.title2 = title2;
        }
    }

    public class ChangeValue {

        private String thingCode;
        private String thingName;
        private String thingImageName;

        private String startMetricCode;
        private String stopMetricCode;
        private String tapOpenMetricCode;
        private String tapOpenMetricValue;
        private String tapOpenMetricName;

        private String tapCloseMetricCode;
        private String tapCloseMetricValue;
        private String tapCloseMetricName;

        public String getStartMetricCode() {
            return startMetricCode;
        }

        public void setStartMetricCode(String startMetricCode) {
            this.startMetricCode = startMetricCode;
        }

        public String getStopMetricCode() {
            return stopMetricCode;
        }

        public void setStopMetricCode(String stopMetricCode) {
            this.stopMetricCode = stopMetricCode;
        }

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

        public String getThingImageName() {
            return thingImageName;
        }

        public void setThingImageName(String thingImageName) {
            this.thingImageName = thingImageName;
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

    public class MediumDosingValue {
        private String thingCode;
        private String thingName;
        private String thingImageName;

        private String startMetricCode;
        private String stopMetricCode;

        private String tapOpenMetricCode;
        private String tapOpenMetricValue;
        private String tapOpenMetricName;

        private String tapCloseMetricCode;
        private String tapCloseMetricValue;
        private String tapCloseMetricName;

        public String getStartMetricCode() {
            return startMetricCode;
        }

        public void setStartMetricCode(String startMetricCode) {
            this.startMetricCode = startMetricCode;
        }

        public String getStopMetricCode() {
            return stopMetricCode;
        }

        public void setStopMetricCode(String stopMetricCode) {
            this.stopMetricCode = stopMetricCode;
        }

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

        public String getThingImageName() {
            return thingImageName;
        }

        public void setThingImageName(String thingImageName) {
            this.thingImageName = thingImageName;
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

        private String thingCode;
        private String thingName;

        private String alertInfo;

        private String runState;

        private String startCmdMetricName;
        private String startCmdMetricCode;

        private String stopCmdMetricName;
        private String stopCmdMetricCode;

        private String intelligentMetricName;
        private String intelligentMetricCode;
        private String intelligentMetricValue;


        private String localMetricName;
        private String localMetricCode;
        private String localMetricValue;

        public String getRunState() {
            return runState;
        }

        public void setRunState(String runState) {
            this.runState = runState;
        }

        public String getStartCmdMetricName() {
            return startCmdMetricName;
        }

        public void setStartCmdMetricName(String startCmdMetricName) {
            this.startCmdMetricName = startCmdMetricName;
        }

        public String getStartCmdMetricCode() {
            return startCmdMetricCode;
        }

        public void setStartCmdMetricCode(String startCmdMetricCode) {
            this.startCmdMetricCode = startCmdMetricCode;
        }


        public String getStopCmdMetricName() {
            return stopCmdMetricName;
        }

        public void setStopCmdMetricName(String stopCmdMetricName) {
            this.stopCmdMetricName = stopCmdMetricName;
        }

        public String getStopCmdMetricCode() {
            return stopCmdMetricCode;
        }

        public void setStopCmdMetricCode(String stopCmdMetricCode) {
            this.stopCmdMetricCode = stopCmdMetricCode;
        }


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

        public String getAlertInfo() {
            return alertInfo;
        }

        public void setAlertInfo(String alertInfo) {
            this.alertInfo = alertInfo;
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

    public class CombinedBucket {
        private String thingCode;
        private String thingName;

        private String levelMetricCode;
        private String levelMetricName;
        private String levelMetricValue;

        private String densityMetricCode;
        private String densityMetricName;
        private String densityMetricValue;

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

        public String getLevelMetricCode() {
            return levelMetricCode;
        }

        public void setLevelMetricCode(String levelMetricCode) {
            this.levelMetricCode = levelMetricCode;
        }

        public String getLevelMetricName() {
            return levelMetricName;
        }

        public void setLevelMetricName(String levelMetricName) {
            this.levelMetricName = levelMetricName;
        }

        public String getLevelMetricValue() {
            return levelMetricValue;
        }

        public void setLevelMetricValue(String levelMetricValue) {
            this.levelMetricValue = levelMetricValue;
        }

        public String getDensityMetricCode() {
            return densityMetricCode;
        }

        public void setDensityMetricCode(String densityMetricCode) {
            this.densityMetricCode = densityMetricCode;
        }

        public String getDensityMetricName() {
            return densityMetricName;
        }

        public void setDensityMetricName(String densityMetricName) {
            this.densityMetricName = densityMetricName;
        }

        public String getDensityMetricValue() {
            return densityMetricValue;
        }

        public void setDensityMetricValue(String densityMetricValue) {
            this.densityMetricValue = densityMetricValue;
        }
    }
}
                                                  