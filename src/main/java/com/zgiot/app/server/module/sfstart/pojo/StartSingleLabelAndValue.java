package com.zgiot.app.server.module.sfstart.pojo;


public class StartSingleLabelAndValue {

    // 标签内容
    private String dataLabel;

    // 发送值
    private Float value;

    public String getDataLabel() {
        return dataLabel;
    }

    public void setDataLabel(String dataLabel) {
        this.dataLabel = dataLabel;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
