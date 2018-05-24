package com.zgiot.app.server.module.reportforms.output.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.Map;

@ApiModel("影响时间响应对象")
public class InfluenceTimeRsp {

    @ApiModelProperty("当班开始时间")
    private Date dutyStratTime;

    @ApiModelProperty("影响时间类型")
    private Integer influenceType;

    @ApiModelProperty("一期数据id")
    private Integer oneTermId;

    @ApiModelProperty("一期当班时长")
    private Long oneTermDutyTimeLong;

    @ApiModelProperty("一期月累计时长")
    private Long oneTermMonthTimeLong;

    @ApiModelProperty("一期年累计时长")
    private Long oneTermYearTimeLong;

    @ApiModelProperty("二期数据id")
    private Integer twoTermId;

    @ApiModelProperty("二期当班时长")
    private Long twoTermDutyTimeLong;

    @ApiModelProperty("二期月累计时长")
    private Long twoTermMonthTimeLong;

    @ApiModelProperty("二期年累计时长")
    private Long twoTermYearTimeLong;

    @ApiModelProperty("当班合计")
    private Double dutyVolunm;

    @ApiModelProperty("月累计合计")
    private Double monthVolunm;

    @ApiModelProperty("年累计合计")
    private Double yearVolunm;

    public Date getDutyStratTime() {
        return dutyStratTime;
    }

    public void setDutyStratTime(Date dutyStratTime) {
        this.dutyStratTime = dutyStratTime;
    }

    public Integer getInfluenceType() {
        return influenceType;
    }

    public void setInfluenceType(Integer influenceType) {
        this.influenceType = influenceType;
    }

    public Integer getOneTermId() {
        return oneTermId;
    }

    public void setOneTermId(Integer oneTermId) {
        this.oneTermId = oneTermId;
    }

    public Long getOneTermDutyTimeLong() {
        return oneTermDutyTimeLong;
    }

    public void setOneTermDutyTimeLong(Long oneTermDutyTimeLong) {
        this.oneTermDutyTimeLong = oneTermDutyTimeLong;
    }

    public Long getOneTermMonthTimeLong() {
        return oneTermMonthTimeLong;
    }

    public void setOneTermMonthTimeLong(Long oneTermMonthTimeLong) {
        this.oneTermMonthTimeLong = oneTermMonthTimeLong;
    }

    public Long getOneTermYearTimeLong() {
        return oneTermYearTimeLong;
    }

    public void setOneTermYearTimeLong(Long oneTermYearTimeLong) {
        this.oneTermYearTimeLong = oneTermYearTimeLong;
    }

    public Integer getTwoTermId() {
        return twoTermId;
    }

    public void setTwoTermId(Integer twoTermId) {
        this.twoTermId = twoTermId;
    }

    public Long getTwoTermDutyTimeLong() {
        return twoTermDutyTimeLong;
    }

    public void setTwoTermDutyTimeLong(Long twoTermDutyTimeLong) {
        this.twoTermDutyTimeLong = twoTermDutyTimeLong;
    }

    public Long getTwoTermMonthTimeLong() {
        return twoTermMonthTimeLong;
    }

    public void setTwoTermMonthTimeLong(Long twoTermMonthTimeLong) {
        this.twoTermMonthTimeLong = twoTermMonthTimeLong;
    }

    public Long getTwoTermYearTimeLong() {
        return twoTermYearTimeLong;
    }

    public void setTwoTermYearTimeLong(Long twoTermYearTimeLong) {
        this.twoTermYearTimeLong = twoTermYearTimeLong;
    }

    public Double getDutyVolunm() {
        return dutyVolunm;
    }

    public void setDutyVolunm(Double dutyVolunm) {
        this.dutyVolunm = dutyVolunm;
    }

    public Double getMonthVolunm() {
        return monthVolunm;
    }

    public void setMonthVolunm(Double monthVolunm) {
        this.monthVolunm = monthVolunm;
    }

    public Double getYearVolunm() {
        return yearVolunm;
    }

    public void setYearVolunm(Double yearVolunm) {
        this.yearVolunm = yearVolunm;
    }
}
