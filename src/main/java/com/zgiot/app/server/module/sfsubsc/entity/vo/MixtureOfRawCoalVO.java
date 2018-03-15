package com.zgiot.app.server.module.sfsubsc.entity.vo;

import java.util.List;

/**
 * 原煤配比
 *
 * @author jys
 */
public class MixtureOfRawCoalVO {

    /**
     * 卡片标题
     */
    private String cardTitle;
    /**
     * 系统名称
     */
    private String systemName;

    private String instantPercent;

    private List<MetricData> instantMetrics;

    private String teamPercent;

    private List<MetricData> teamMetrics;


    public class MetricData {


        private String thingCode;
        private String metricValue;

        public String getThingCode() {
            return thingCode;
        }

        public void setThingCode(String thingCode) {
            this.thingCode = thingCode;
        }

        public String getMetricValue() {
            return metricValue;
        }

        public void setMetricValue(String metricValue) {
            this.metricValue = metricValue;
        }
    }


    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getInstantPercent() {
        return instantPercent;
    }

    public void setInstantPercent(String instantPercent) {
        this.instantPercent = instantPercent;
    }

    public List<MetricData> getInstantMetrics() {
        return instantMetrics;
    }

    public void setInstantMetrics(List<MetricData> instantMetrics) {
        this.instantMetrics = instantMetrics;
    }

    public String getTeamPercent() {
        return teamPercent;
    }

    public void setTeamPercent(String teamPercent) {
        this.teamPercent = teamPercent;
    }

    public List<MetricData> getTeamMetrics() {
        return teamMetrics;
    }

    public void setTeamMetrics(List<MetricData> teamMetrics) {
        this.teamMetrics = teamMetrics;
    }
}
