package com.zgiot.app.server.module.sfstop.entity.pojo;

public class StopExamineRecord {

    // 检查id
    private Integer examineId;

    // 检查对应启车操作记录
    private Integer operateId;

    // 启车设备id
    private String examineThingCode;

    // 检查规则id
    private Integer ruleId;

    // 启车检查类型(1:自检)
    private Integer examineType;

    // 异常详情
    private String examineInformation;

    // 是否故障(0:未检测完毕，1:正常，2:故障)
    private Integer examineResult;


    public Integer getExamineId() {
        return examineId;
    }

    public void setExamineId(Integer examineId) {
        this.examineId = examineId;
    }

    public Integer getOperateId() {
        return operateId;
    }

    public void setOperateId(Integer operateId) {
        this.operateId = operateId;
    }


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

    public String getExamineInformation() {
        return examineInformation;
    }

    public void setExamineInformation(String examineInformation) {
        this.examineInformation = examineInformation;
    }

    public Integer getExamineResult() {
        return examineResult;
    }

    public void setExamineResult(Integer examineResult) {
        this.examineResult = examineResult;
    }


    public String getExamineThingCode() {
        return examineThingCode;
    }

    public void setExamineThingCode(String examineThingCode) {
        this.examineThingCode = examineThingCode;
    }

    
}
