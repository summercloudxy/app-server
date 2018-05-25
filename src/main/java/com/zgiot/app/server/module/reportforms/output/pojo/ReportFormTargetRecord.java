package com.zgiot.app.server.module.reportforms.output.pojo;


import io.swagger.annotations.ApiModel;

import java.util.Date;

@ApiModel("指标记录")
public class ReportFormTargetRecord {

    private Integer id;
    private Date dutyStartTime;
    private Integer targetType;
    private Double classValue = 0.0;
    private Double monthValue = 0.0;
    private Double yearValue = 0.0;
    private Integer term;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDutyStartTime() {
        return dutyStartTime;
    }

    public void setDutyStartTime(Date dutyStartTime) {
        this.dutyStartTime = dutyStartTime;
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public Double getClassValue() {
        return classValue;
    }

    public void setClassValue(Double classValue) {
        this.classValue = classValue;
    }

    public Double getMonthValue() {
        return monthValue;
    }

    public void setMonthValue(Double monthValue) {
        this.monthValue = monthValue;
    }

    public Double getYearValue() {
        return yearValue;
    }

    public void setYearValue(Double yearValue) {
        this.yearValue = yearValue;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }
}
