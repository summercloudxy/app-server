package com.zgiot.app.server.module.reportforms.output.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel("运销表")
public class Transport {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("煤品种 1.洗混煤 2.气精煤 3.煤泥")
    private Integer coalType;

    @ApiModelProperty("开装时间")
    private Date transportStartTime;

    @ApiModelProperty("装完时间")
    private Date transportEndTime;

    @ApiModelProperty("到站")
    private String destination;

    @ApiModelProperty("批次")
    private Integer batch;

    @ApiModelProperty("车厢数")
    private Integer carriageNumber;

    @ApiModelProperty("运输量(吨)")
    private Double transportVolume;

    @ApiModelProperty("装车配比")
    private String ratio;

    @ApiModelProperty("装车站点")
    private String  station;

    @ApiModelProperty("装车用时 分钟数")
    private Integer duration;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("当班开始时间")
    private Date dutyStartTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCoalType() {
        return coalType;
    }

    public void setCoalType(Integer coalType) {
        this.coalType = coalType;
    }

    public Date getTransportStartTime() {
        return transportStartTime;
    }

    public void setTransportStartTime(Date transportStartTime) {
        this.transportStartTime = transportStartTime;
    }

    public Date getTransportEndTime() {
        return transportEndTime;
    }

    public void setTransportEndTime(Date transportEndTime) {
        this.transportEndTime = transportEndTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getBatch() {
        return batch;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }

    public Integer getCarriageNumber() {
        return carriageNumber;
    }

    public void setCarriageNumber(Integer carriageNumber) {
        this.carriageNumber = carriageNumber;
    }

    public Double getTransportVolume() {
        return transportVolume;
    }

    public void setTransportVolume(Double transportVolume) {
        this.transportVolume = transportVolume;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getDutyStartTime() {
        return dutyStartTime;
    }

    public void setDutyStartTime(Date dutyStartTime) {
        this.dutyStartTime = dutyStartTime;
    }

}
