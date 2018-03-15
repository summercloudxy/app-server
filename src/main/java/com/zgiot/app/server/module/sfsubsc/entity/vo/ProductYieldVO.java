package com.zgiot.app.server.module.sfsubsc.entity.vo;

import java.util.List;

/**
 * 产品产率
 *
 * @author jys
 */
public class ProductYieldVO {

    /**
     * 卡片标题
     */
    private String cardTitle;

    private List<MetricData> metricDatas;

    private String rawCoalCapValue;

    private String cleanCoalCapValue;

    private String mixedCoalCapValue;

    private String slurryCapValue;

    private String wasteRockCapValue;


    public class MetricData {

        private String thingName;

        private String thingCodeMetricPercent;


        public String getThingName() {
            return thingName;
        }

        public void setThingName(String thingName) {
            this.thingName = thingName;
        }

        public String getThingCodeMetricPercent() {
            return thingCodeMetricPercent;
        }

        public void setThingCodeMetricPercent(String thingCodeMetricPercent) {
            this.thingCodeMetricPercent = thingCodeMetricPercent;
        }
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public List<MetricData> getMetricDatas() {
        return metricDatas;
    }

    public void setMetricDatas(List<MetricData> metricDatas) {
        this.metricDatas = metricDatas;
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

    public String getMixedCoalCapValue() {
        return mixedCoalCapValue;
    }

    public void setMixedCoalCapValue(String mixedCoalCapValue) {
        this.mixedCoalCapValue = mixedCoalCapValue;
    }

    public String getSlurryCapValue() {
        return slurryCapValue;
    }

    public void setSlurryCapValue(String slurryCapValue) {
        this.slurryCapValue = slurryCapValue;
    }

    public String getWasteRockCapValue() {
        return wasteRockCapValue;
    }

    public void setWasteRockCapValue(String wasteRockCapValue) {
        this.wasteRockCapValue = wasteRockCapValue;
    }
}
