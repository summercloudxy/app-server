package com.zgiot.app.server.module.reportforms.output.pojo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel("影响时间")
public class InfluenceTime {

    @ApiModelProperty(value = "主键",required = true)
    private Integer id;

    @ApiModelProperty( value = "当班开始时间",required = true)
    private Date dutyStartTime;

    @ApiModelProperty(value = "影响时间类型",required = true)
    private Integer influenceType;

    @ApiModelProperty(value = "当班时长 分钟",required = true)
    private Long classDuration;

    @ApiModelProperty(value = "月累计时长 分钟",required = true)
    private Long monthDuration;

    @ApiModelProperty(value = "年累计时长",required = true)
    private Long yearDuration;

    @ApiModelProperty(value = "期数",required = true)
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

    public Integer getInfluenceType() {
        return influenceType;
    }

    public void setInfluenceType(Integer influenceType) {
        this.influenceType = influenceType;
    }

    public Long getClassDuration() {
        return classDuration;
    }

    public void setClassDuration(Long classDuration) {
        this.classDuration = classDuration;
    }

    public Long getMonthDuration() {
        return monthDuration;
    }

    public void setMonthDuration(Long monthDuration) {
        this.monthDuration = monthDuration;
    }

    public Long getYearDuration() {
        return yearDuration;
    }

    public void setYearDuration(Long yearDuration) {
        this.yearDuration = yearDuration;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

}
