package com.zgiot.app.server.module.sfmedium.entity.vo;

/**
 * 配介显示
 */
public class MediumCompoundingVO {

    private String mediumPoolThingCode;


    private String mediumCompoundingLevel;

    private String lowLevel;

    private String mediumCompoundingState;

    /**
     * 补水阀
     */
    private WaterSupplementValve waterSupplementValve;
    /**
     * 鼓风阀
     */
    private BlowerValve blowerValve;

    private String mediumCompoundingMetricCode;


    public class WaterSupplementValve {

        private String thingName;

        private String thingCode;

        private String thingImageName;

        private String startMetricCode;
        private String stopMetricCode;

        private String tapOpenMetricCode;
        private String tapOpenMetricName;
        private String tapOpenMetricValue;
        private String tapCloseMetricCode;
        private String tapCloseMetricName;
        private String tapCloseMeticValue;

        private String levelLockMetricCode;
        private String levelLockMetricName;
        private String levelLockMetricValue;

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

        public String getLevelLockMetricCode() {
            return levelLockMetricCode;
        }

        public void setLevelLockMetricCode(String levelLockMetricCode) {
            this.levelLockMetricCode = levelLockMetricCode;
        }

        public String getLevelLockMetricName() {
            return levelLockMetricName;
        }

        public void setLevelLockMetricName(String levelLockMetricName) {
            this.levelLockMetricName = levelLockMetricName;
        }

        public String getLevelLockMetricValue() {
            return levelLockMetricValue;
        }

        public void setLevelLockMetricValue(String levelLockMetricValue) {
            this.levelLockMetricValue = levelLockMetricValue;
        }
    }

    public class BlowerValve {

        private String thingName;

        private String thingCode;
        private String thingImageName;

        private String tapOpenMetricCode;
        private String tapOpenMetricName;
        private String tapOpenMetricValue;
        private String tapCloseMetricCode;
        private String tapCloseMetricName;
        private String tapCloseMeticValue;


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


    public String getMediumCompoundingLevel() {
        return mediumCompoundingLevel;
    }

    public void setMediumCompoundingLevel(String mediumCompoundingLevel) {
        this.mediumCompoundingLevel = mediumCompoundingLevel;
    }

    public String getLowLevel() {
        return lowLevel;
    }

    public void setLowLevel(String lowLevel) {
        this.lowLevel = lowLevel;
    }

    public String getMediumCompoundingState() {
        return mediumCompoundingState;
    }

    public void setMediumCompoundingState(String mediumCompoundingState) {
        this.mediumCompoundingState = mediumCompoundingState;
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


    public String getMediumCompoundingMetricCode() {
        return mediumCompoundingMetricCode;
    }

    public void setMediumCompoundingMetricCode(String mediumCompoundingMetricCode) {
        this.mediumCompoundingMetricCode = mediumCompoundingMetricCode;
    }

    public String getMediumPoolThingCode() {
        return mediumPoolThingCode;
    }

    public void setMediumPoolThingCode(String mediumPoolThingCode) {
        this.mediumPoolThingCode = mediumPoolThingCode;
    }
}
