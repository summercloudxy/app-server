package com.zgiot.app.server.module.qualityandquantity.pojo;

import com.zgiot.common.pojo.ProductionInspectRecord;

import java.util.Date;

public class ProductionInspectData {
    private String thingCode;
    private String thingName;
    private MetricDataValue positive1Point45;
    private MetricDataValue negative1Point45;
    private MetricDataValue positive1Point8;
    private MetricDataValue negative1Point8;
    private MetricDataValue onePoint45To1Point8;
    private MetricDataValue positive50mm;
    private MetricDataValue negative50mm;
    private Date time;
    private String target;
    private MetricDataValue avgDensity;
    private Integer system;
    private String type;
    /**
     * 0位：密度  1位：1.45  2位：1.8  3位：50 4位：1.45-1.8
     */
    private Integer showBit;

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public MetricDataValue getPositive1Point45() {
        return positive1Point45;
    }

    public void setPositive1Point45(MetricDataValue positive1Point45) {
        this.positive1Point45 = positive1Point45;
    }

    public MetricDataValue getNegative1Point45() {
        return negative1Point45;
    }

    public void setNegative1Point45(MetricDataValue negative1Point45) {
        this.negative1Point45 = negative1Point45;
    }

    public MetricDataValue getPositive1Point8() {
        return positive1Point8;
    }

    public void setPositive1Point8(MetricDataValue positive1Point8) {
        this.positive1Point8 = positive1Point8;
    }

    public MetricDataValue getNegative1Point8() {
        return negative1Point8;
    }

    public void setNegative1Point8(MetricDataValue negative1Point8) {
        this.negative1Point8 = negative1Point8;
    }

    public MetricDataValue getOnePoint45To1Point8() {
        return onePoint45To1Point8;
    }

    public void setOnePoint45To1Point8(MetricDataValue onePoint45To1Point8) {
        this.onePoint45To1Point8 = onePoint45To1Point8;
    }

    public MetricDataValue getPositive50mm() {
        return positive50mm;
    }

    public void setPositive50mm(MetricDataValue positive50mm) {
        this.positive50mm = positive50mm;
    }

    public MetricDataValue getNegative50mm() {
        return negative50mm;
    }

    public void setNegative50mm(MetricDataValue negative50mm) {
        this.negative50mm = negative50mm;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public MetricDataValue getAvgDensity() {
        return avgDensity;
    }

    public void setAvgDensity(MetricDataValue avgDensity) {
        this.avgDensity = avgDensity;
    }

    public Integer getSystem() {
        return system;
    }

    public void setSystem(Integer system) {
        this.system = system;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getShowBit() {
        return showBit;
    }

    public void setShowBit(Integer showBit) {
        this.showBit = showBit;
    }

    public ProductionInspectData() {
    }

    public ProductionInspectData(ProductionInspectRecord record) {
        this.thingCode = record.getSample();
        this.target = record.getTarget();
        this.system = record.getSystem();
        this.time = record.getTime();
    }
}
