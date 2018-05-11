package com.zgiot.app.server.module.sfsubsc.entity.vo;

import java.util.List;

public class CoalQualityVO {

    /**
     * 卡片标题
     */
    private String cardTitle;

    /**
     * 日期
     */
    private String date;

    /**
     * 班次
     */
    private String shift;

    /**
     * 班次开始时间
     */
    private String timeBegin;

    /**
     * 班次结束时间
     */
    private String timeEnd;

    /**
     * 一期数据
     */
    private List<MetricData> termOne;

    /**
     * 二期数据
     */
    private List<MetricData> termTwo;

    public class MetricData {
        /**
         * 精煤灰分
         */
        private String cleanCoalAad;
        /**
         * 混煤发热量
         */
        private String mixedCoalQar;
        /**
         * 是否异常:0正常,1异常
         */
        private String unusual;

        public String getCleanCoalAad() {
            return cleanCoalAad;
        }

        public void setCleanCoalAad(String cleanCoalAad) {
            this.cleanCoalAad = cleanCoalAad;
        }

        public String getMixedCoalQar() {
            return mixedCoalQar;
        }

        public void setMixedCoalQar(String mixedCoalQar) {
            this.mixedCoalQar = mixedCoalQar;
        }

        public String getUnusual() {
            return unusual;
        }

        public void setUnusual(String unusual) {
            this.unusual = unusual;
        }
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(String timeBegin) {
        this.timeBegin = timeBegin;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public List<MetricData> getTermOne() {
        return termOne;
    }

    public void setTermOne(List<MetricData> termOne) {
        this.termOne = termOne;
    }

    public List<MetricData> getTermTwo() {
        return termTwo;
    }

    public void setTermTwo(List<MetricData> termTwo) {
        this.termTwo = termTwo;
    }
}
