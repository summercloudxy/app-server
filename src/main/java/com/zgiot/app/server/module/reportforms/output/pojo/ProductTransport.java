package com.zgiot.app.server.module.reportforms.output.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import java.util.Date;

@ApiModel("产品外运对象")
public class ProductTransport {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("煤种类 1洗混煤，2气精煤，3煤泥，4洗矸,11自用煤")
    private Integer coalType;

    @ApiModelProperty("运输类型 1表示铁路 2表示地销 3表示合计")
    private Integer transportType;

    @ApiModelProperty("当天吨数")
    private Double dayVolume;

    @ApiModelProperty("当天列数")
    private Integer dayColumnNumber;

    @ApiModelProperty("当天车数")
    private Integer dayCarNumber;

    @ApiModelProperty("当月吨数")
    private Double monthVolume;

    @ApiModelProperty("当月列数")
    private Integer monthColumnNumber;

    @ApiModelProperty("当年吨数")
    private Double yearVolume;

    @ApiModelProperty("当年列数")
    private Integer yearColumnNumber;

    @ApiModelProperty("产品开始时间")
    private Date productStartTime;

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

    public Integer getTransportType() {
        return transportType;
    }

    public void setTransportType(Integer transportType) {
        this.transportType = transportType;
    }

    public Double getDayVolume() {
        return dayVolume;
    }

    public void setDayVolume(Double dayVolume) {
        this.dayVolume = dayVolume;
    }

    public Integer getDayColumnNumber() {
        return dayColumnNumber;
    }

    public void setDayColumnNumber(Integer dayColumnNumber) {
        this.dayColumnNumber = dayColumnNumber;
    }

    public Integer getDayCarNumber() {
        return dayCarNumber;
    }

    public void setDayCarNumber(Integer dayCarNumber) {
        this.dayCarNumber = dayCarNumber;
    }

    public Double getMonthVolume() {
        return monthVolume;
    }

    public void setMonthVolume(Double monthVolume) {
        this.monthVolume = monthVolume;
    }

    public Integer getMonthColumnNumber() {
        return monthColumnNumber;
    }

    public void setMonthColumnNumber(Integer monthColumnNumber) {
        this.monthColumnNumber = monthColumnNumber;
    }

    public Double getYearVolume() {
        return yearVolume;
    }

    public void setYearVolume(Double yearVolume) {
        this.yearVolume = yearVolume;
    }

    public Integer getYearColumnNumber() {
        return yearColumnNumber;
    }

    public void setYearColumnNumber(Integer yearColumnNumber) {
        this.yearColumnNumber = yearColumnNumber;
    }

    public Date getProductStartTime() {
        return productStartTime;
    }

    public void setProductStartTime(Date productStartTime) {
        this.productStartTime = productStartTime;
    }
}
