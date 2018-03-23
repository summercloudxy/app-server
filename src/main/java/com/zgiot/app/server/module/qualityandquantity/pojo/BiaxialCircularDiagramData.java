package com.zgiot.app.server.module.qualityandquantity.pojo;

public class BiaxialCircularDiagramData {
    private String title;
    private MetricDataValue valueOne;
    private MetricDataValue valueTwo;
    private MetricDataValue valueThree;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public MetricDataValue getValueOne() {
        return valueOne;
    }

    public void setValueOne(MetricDataValue valueOne) {
        this.valueOne = valueOne;
    }

    public MetricDataValue getValueTwo() {
        return valueTwo;
    }

    public void setValueTwo(MetricDataValue valueTwo) {
        this.valueTwo = valueTwo;
    }

    public MetricDataValue getValueThree() {
        return valueThree;
    }

    public void setValueThree(MetricDataValue valueThree) {
        this.valueThree = valueThree;
    }
}
