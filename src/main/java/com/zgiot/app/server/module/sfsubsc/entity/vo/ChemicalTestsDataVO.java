package com.zgiot.app.server.module.sfsubsc.entity.vo;

import java.util.List;

/**
 * 化验数据
 *
 * @author jys
 */
public class ChemicalTestsDataVO {


    /**
     * 卡片标题
     */
    private String cardTitle;
    /**
     * 精煤采样时间
     */
    private String cleanCoalSamplingTime;
    /**
     * 混煤采样时间
     */
    private String mixedCoalSamplingTime;
    /**
     * 精煤指标
     */
    private List<MetricData> cleanCoalMetric;
    /**
     * 混煤指标
     */
    private List<MetricData> mixedCoalMetric;


    public class MetricData {

        private String metricName;

        private String metricValue;

        private String startScope;

        private String endScope;

        private String flag;

        private String percent;

        public String getMetricName() {
            return metricName;
        }

        public void setMetricName(String metricName) {
            this.metricName = metricName;
        }

        public String getMetricValue() {
            return metricValue;
        }

        public void setMetricValue(String metricValue) {
            this.metricValue = metricValue;
        }

        public String getStartScope() {
            return startScope;
        }

        public void setStartScope(String startScope) {
            this.startScope = startScope;
        }

        public String getEndScope() {
            return endScope;
        }

        public void setEndScope(String endScope) {
            this.endScope = endScope;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getCleanCoalSamplingTime() {
        return cleanCoalSamplingTime;
    }

    public void setCleanCoalSamplingTime(String cleanCoalSamplingTime) {
        this.cleanCoalSamplingTime = cleanCoalSamplingTime;
    }

    public String getMixedCoalSamplingTime() {
        return mixedCoalSamplingTime;
    }

    public void setMixedCoalSamplingTime(String mixedCoalSamplingTime) {
        this.mixedCoalSamplingTime = mixedCoalSamplingTime;
    }

    public List<MetricData> getCleanCoalMetric() {
        return cleanCoalMetric;
    }

    public void setCleanCoalMetric(List<MetricData> cleanCoalMetric) {
        this.cleanCoalMetric = cleanCoalMetric;
    }

    public List<MetricData> getMixedCoalMetric() {
        return mixedCoalMetric;
    }

    public void setMixedCoalMetric(List<MetricData> mixedCoalMetric) {
        this.mixedCoalMetric = mixedCoalMetric;
    }
}
