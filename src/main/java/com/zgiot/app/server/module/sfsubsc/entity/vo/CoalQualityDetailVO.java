package com.zgiot.app.server.module.sfsubsc.entity.vo;

import java.util.Date;
import java.util.List;

public class CoalQualityDetailVO {

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
     * 班次有效开始时间
     */
    Date shiftTimeBegin;

    /**
     * 班次有效结束时间
     */
    Date shiftTimeEnd;

    /**
     * 一期生产指标
     */
    private List<MetricData> production1;

    /**
     * 二期生产指标
     */
    private List<MetricData> production2;

    /**
     * 一期矸石带煤
     */
    private RockData rock1;

    /**
     * 二期矸石带煤
     */
    private RockData rock2;

    /**
     * 一期TCS
     */
    private List<MetricData> tcs1;

    /**
     * 二期TCS
     */
    private List<MetricData> tcs2;

    public class MetricData {
        /**
         * 产品名
         */
        private String productionName;
        /**
         * 灰分
         */
        private String aad;
        /**
         * 水分
         */
        private String mt;
        /**
         * 硫分
         */
        private String stad;
        /**
         * 发热量
         */
        private String qar;

        public String getProductionName() {
            return productionName;
        }

        public void setProductionName(String productionName) {
            this.productionName = productionName;
        }

        public String getAad() {
            return aad;
        }

        public void setAad(String aad) {
            this.aad = aad;
        }

        public String getMt() {
            return mt;
        }

        public void setMt(String mt) {
            this.mt = mt;
        }

        public String getStad() {
            return stad;
        }

        public void setStad(String stad) {
            this.stad = stad;
        }

        public String getQar() {
            return qar;
        }

        public void setQar(String qar) {
            this.qar = qar;
        }
    }

    public class RockData {
        /**
         * 末矸
         */
        private String powderRock;
        /**
         * 块矸
         */
        private String blockRock;

        public String getPowderRock() {
            return powderRock;
        }

        public void setPowderRock(String powderRock) {
            this.powderRock = powderRock;
        }

        public String getBlockRock() {
            return blockRock;
        }

        public void setBlockRock(String blockRock) {
            this.blockRock = blockRock;
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

    public Date getShiftTimeBegin() {
        return shiftTimeBegin;
    }

    public void setShiftTimeBegin(Date shiftTimeBegin) {
        this.shiftTimeBegin = shiftTimeBegin;
    }

    public Date getShiftTimeEnd() {
        return shiftTimeEnd;
    }

    public void setShiftTimeEnd(Date shiftTimeEnd) {
        this.shiftTimeEnd = shiftTimeEnd;
    }

    public List<MetricData> getProduction1() {
        return production1;
    }

    public void setProduction1(List<MetricData> production1) {
        this.production1 = production1;
    }

    public List<MetricData> getProduction2() {
        return production2;
    }

    public void setProduction2(List<MetricData> production2) {
        this.production2 = production2;
    }

    public RockData getRock1() {
        return rock1;
    }

    public void setRock1(RockData rock1) {
        this.rock1 = rock1;
    }

    public RockData getRock2() {
        return rock2;
    }

    public void setRock2(RockData rock2) {
        this.rock2 = rock2;
    }

    public List<MetricData> getTcs1() {
        return tcs1;
    }

    public void setTcs1(List<MetricData> tcs1) {
        this.tcs1 = tcs1;
    }

    public List<MetricData> getTcs2() {
        return tcs2;
    }

    public void setTcs2(List<MetricData> tcs2) {
        this.tcs2 = tcs2;
    }
}
