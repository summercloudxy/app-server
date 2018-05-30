package com.zgiot.app.server.module.reportforms.output.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel("运销统计表")
public class TransportSaleStatistics {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("当班开始时间")
    private Date dutyStartTime;

    @ApiModelProperty("煤的种类 1.混洗煤 2.气精煤 3.煤泥")
    private Integer coalType;

    @ApiModelProperty("车数")
    private Integer trainNumber;

    @ApiModelProperty("吨数")
    private Double coalVolunm;

    @ApiModelProperty("月累计列数/车数")
    private Integer monthTrainNumber;

    @ApiModelProperty("月累计吨数")
    private Double monthCoalVolunm;

    @ApiModelProperty("年累计列数/车数")
    private Integer yearTrainNumber;

    @ApiModelProperty("年累计吨数")
    private Double yearCoalVolunm;

    @ApiModelProperty("统计类型 1.外运 2.地销")
    private Integer stastisticsType;

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

    public Integer getCoalType() {
        return coalType;
    }

    public void setCoalType(Integer coalType) {
        this.coalType = coalType;
    }

    public Integer getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(Integer trainNumber) {
        this.trainNumber = trainNumber;
    }

    public Double getCoalVolunm() {
        return coalVolunm;
    }

    public void setCoalVolunm(Double coalVolunm) {
        this.coalVolunm = coalVolunm;
    }

    public Integer getMonthTrainNumber() {
        return monthTrainNumber;
    }

    public void setMonthTrainNumber(Integer monthTrainNumber) {
        this.monthTrainNumber = monthTrainNumber;
    }

    public Double getMonthCoalVolunm() {
        return monthCoalVolunm;
    }

    public void setMonthCoalVolunm(Double monthCoalVolunm) {
        this.monthCoalVolunm = monthCoalVolunm;
    }

    public Integer getYearTrainNumber() {
        return yearTrainNumber;
    }

    public void setYearTrainNumber(Integer yearTrainNumber) {
        this.yearTrainNumber = yearTrainNumber;
    }

    public Double getYearCoalVolunm() {
        return yearCoalVolunm;
    }

    public void setYearCoalVolunm(Double yearCoalVolunm) {
        this.yearCoalVolunm = yearCoalVolunm;
    }

    public Integer getStastisticsType() {
        return stastisticsType;
    }

    public void setStastisticsType(Integer stastisticsType) {
        this.stastisticsType = stastisticsType;
    }

}
