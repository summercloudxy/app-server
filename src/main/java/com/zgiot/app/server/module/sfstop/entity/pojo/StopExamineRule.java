package com.zgiot.app.server.module.sfstop.entity.pojo;

public class StopExamineRule {

    // 启车规则id
    private Integer ruleId;

    // 启车检查类型
    private Integer examineType;

    // 实际检查设备
    private String examineThingCode;

    // 检查信号点
    private String examineMetricCode;

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


    public Integer getExamineType() {
        return examineType;
    }

    public void setExamineType(Integer examineType) {
        this.examineType = examineType;
    }

    public String getExamineThingCode() {
        return examineThingCode;
    }

    public void setExamineThingCode(String examineThingCode) {
        this.examineThingCode = examineThingCode;
    }

    public String getExamineMetricCode() {
        return examineMetricCode;
    }

    public void setExamineMetricCode(String examineMetricCode) {
        this.examineMetricCode = examineMetricCode;
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
