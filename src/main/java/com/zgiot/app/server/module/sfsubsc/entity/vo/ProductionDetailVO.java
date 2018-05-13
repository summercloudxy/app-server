package com.zgiot.app.server.module.sfsubsc.entity.vo;

import java.util.List;

public class ProductionDetailVO extends DetailParent {

    /**
     * 生产量
     */
    private List<MetricData> productionQuantity;

    /**
     * 库存量
     */
    private List<MetricData> stockQuantity;

    /**
     * 事故池水位
     */
    private List<MetricData> waterLevel;

    /**
     * 外运计划
     */
    private List<MetricData> outboundPlan;

    /**
     * 实际外运
     */
    private List<MetricData> outboundActual;

    /**
     * 月累积量
     */
    private List<MetricData> outboundTotalMonth;

    public class MetricData {
        private String name;  // 名称, 类型, 一二期, 精混煤
        private String value1;// 产量, 储量, 格    , 装车
        private String value2;// 产率,     , 补水量, 待装
        private String value3;//                   , 未到

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue1() {
            return value1;
        }

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public String getValue2() {
            return value2;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public String getValue3() {
            return value3;
        }

        public void setValue3(String value3) {
            this.value3 = value3;
        }
    }

    public List<MetricData> getProductionQuantity() {
        return productionQuantity;
    }

    public void setProductionQuantity(List<MetricData> productionQuantity) {
        this.productionQuantity = productionQuantity;
    }

    public List<MetricData> getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(List<MetricData> stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<MetricData> getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(List<MetricData> waterLevel) {
        this.waterLevel = waterLevel;
    }

    public List<MetricData> getOutboundPlan() {
        return outboundPlan;
    }

    public void setOutboundPlan(List<MetricData> outboundPlan) {
        this.outboundPlan = outboundPlan;
    }

    public List<MetricData> getOutboundActual() {
        return outboundActual;
    }

    public void setOutboundActual(List<MetricData> outboundActual) {
        this.outboundActual = outboundActual;
    }

    public List<MetricData> getOutboundTotalMonth() {
        return outboundTotalMonth;
    }

    public void setOutboundTotalMonth(List<MetricData> outboundTotalMonth) {
        this.outboundTotalMonth = outboundTotalMonth;
    }

}
