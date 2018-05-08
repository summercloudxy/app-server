package com.zgiot.app.server.module.sfstart.pojo;


public class StartExamineRule {

    // 启车规则id
    private Integer ruleId;

    // 启车自检规则涉及设备
    private String startDeviceId;

    // 启车检查类型
    private Integer examineType;

    // 实际检查设备
    private String examineDeviceId;

    // 检查信号点
    private String examineName;

    // 比较符(greater：大于,less:小于,equal:等于,greaterAndEqual:大于等于,lessAndEqual:小于等于,notEqual:不等于)
    private String operator;

    // 比较值
    private double compareValue;

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getStartDeviceId() {
        return startDeviceId;
    }

    public void setStartDeviceId(String startDeviceId) {
        this.startDeviceId = startDeviceId;
    }

    public Integer getExamineType() {
        return examineType;
    }

    public void setExamineType(Integer examineType) {
        this.examineType = examineType;
    }

    public String getExamineDeviceId() {
        return examineDeviceId;
    }

    public void setExamineDeviceId(String examineDeviceId) {
        this.examineDeviceId = examineDeviceId;
    }

    public String getExamineName() {
        return examineName;
    }

    public void setExamineName(String examineName) {
        this.examineName = examineName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public double getCompareValue() {
        return compareValue;
    }

    public void setCompareValue(double compareValue) {
        this.compareValue = compareValue;
    }
}
