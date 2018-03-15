package com.zgiot.app.server.module.sfsubsc.entity.vo;

import java.util.List;

/**
 * 智能压滤
 *
 * @author jys
 */
public class IntelligentFilterVO {

    /**
     * 卡片标题
     */
    private String cardTitle;
    /**
     * 浓缩机
     */
    private List<ThickenerMetric> thickenerMetrics;
    /**
     * 压板名称
     */
    private List<String> plateName;
    /**
     * 压板数量
     */
    private List<String> plateCnt;


    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public List<ThickenerMetric> getThickenerMetrics() {
        return thickenerMetrics;
    }

    public void setThickenerMetrics(List<ThickenerMetric> thickenerMetrics) {
        this.thickenerMetrics = thickenerMetrics;
    }

    public List<String> getPlateName() {
        return plateName;
    }

    public void setPlateName(List<String> plateName) {
        this.plateName = plateName;
    }

    public List<String> getPlateCnt() {
        return plateCnt;
    }

    public void setPlateCnt(List<String> plateCnt) {
        this.plateCnt = plateCnt;
    }

    public class ThickenerMetric {
        private String thingCode;

        private String currentValue;

        public String getThingCode() {
            return thingCode;
        }

        public void setThingCode(String thingCode) {
            this.thingCode = thingCode;
        }

        public String getCurrentValue() {
            return currentValue;
        }

        public void setCurrentValue(String currentValue) {
            this.currentValue = currentValue;
        }
    }
}
