package com.zgiot.app.server.module.sfsubsc.entity.vo;

import java.util.List;

public class HistoricalProductRateVO {

    /**
     * 卡片标题
     */
    private String cardTitle;
    /**
     * 卡片图例
     */
    private String[] legend;


    private List<MetricData> metricDatas;


    public class MetricData {

        private String metricIndex;
        /**
         * 原煤值
         */
        private String cleanCoalValue;
        /**
         * 原煤占比
         */
        private String cleanCoalPrecent;
        /**
         * 混煤值
         */
        private String mixedCoalValue;
        /**
         * 混煤占比
         */
        private String mixedCoalPrecent;
        /**
         * 矸石值
         */
        private String wasteRockValue;
        /**
         * 矸石占比
         */
        private String wasteRockPrecent;

        public String getMetricIndex() {
            return metricIndex;
        }

        public void setMetricIndex(String metricIndex) {
            this.metricIndex = metricIndex;
        }

        public String getCleanCoalValue() {
            return cleanCoalValue;
        }

        public void setCleanCoalValue(String cleanCoalValue) {
            this.cleanCoalValue = cleanCoalValue;
        }

        public String getCleanCoalPrecent() {
            return cleanCoalPrecent;
        }

        public void setCleanCoalPrecent(String cleanCoalPrecent) {
            this.cleanCoalPrecent = cleanCoalPrecent;
        }

        public String getMixedCoalValue() {
            return mixedCoalValue;
        }

        public void setMixedCoalValue(String mixedCoalValue) {
            this.mixedCoalValue = mixedCoalValue;
        }

        public String getMixedCoalPrecent() {
            return mixedCoalPrecent;
        }

        public void setMixedCoalPrecent(String mixedCoalPrecent) {
            this.mixedCoalPrecent = mixedCoalPrecent;
        }

        public String getWasteRockValue() {
            return wasteRockValue;
        }

        public void setWasteRockValue(String wasteRockValue) {
            this.wasteRockValue = wasteRockValue;
        }

        public String getWasteRockPrecent() {
            return wasteRockPrecent;
        }

        public void setWasteRockPrecent(String wasteRockPrecent) {
            this.wasteRockPrecent = wasteRockPrecent;
        }
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String[] getLegend() {
        return legend;
    }

    public void setLegend(String[] legend) {
        this.legend = legend;
    }

    public List<MetricData> getMetricDatas() {
        return metricDatas;
    }

    public void setMetricDatas(List<MetricData> metricDatas) {
        this.metricDatas = metricDatas;
    }
}
