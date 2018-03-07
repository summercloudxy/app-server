package com.zgiot.app.server.module.sfsubsc.vo;

import java.util.List;

/**
 * @author jys
 * 历史入洗量
 */
public class HistoryWashingQuantity {
    /**
     * 卡片标题
     */
    private String cardTitle;
    /**
     * 卡片图例
     */
    private String[] legend;
    /**
     * 指标数据
     */

    private List<MetricData> metricDatas;


    public class MetricData {

        private String metricIndex;

        private String firstMetricValue;

        private String firstMetricPecent;

        private String secondMetricValue;

        private String secondMetricPecent;


        public String getMetricIndex() {
            return metricIndex;
        }

        public void setMetricIndex(String metricIndex) {
            this.metricIndex = metricIndex;
        }

        public String getFirstMetricValue() {
            return firstMetricValue;
        }

        public void setFirstMetricValue(String firstMetricValue) {
            this.firstMetricValue = firstMetricValue;
        }

        public String getFirstMetricPecent() {
            return firstMetricPecent;
        }

        public void setFirstMetricPecent(String firstMetricPecent) {
            this.firstMetricPecent = firstMetricPecent;
        }

        public String getSecondMetricValue() {
            return secondMetricValue;
        }

        public void setSecondMetricValue(String secondMetricValue) {
            this.secondMetricValue = secondMetricValue;
        }

        public String getSecondMetricPecent() {
            return secondMetricPecent;
        }

        public void setSecondMetricPecent(String secondMetricPecent) {
            this.secondMetricPecent = secondMetricPecent;
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
