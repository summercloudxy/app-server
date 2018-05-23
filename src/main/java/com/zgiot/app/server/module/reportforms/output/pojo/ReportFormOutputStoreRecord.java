package com.zgiot.app.server.module.reportforms.output.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel("生产库存记录")
public class ReportFormOutputStoreRecord {

    private Integer id;
    private Date dutyStartTime;
    @ApiModelProperty(value = "气精煤")
    private Double clenedCoal;
    @ApiModelProperty(value = "洗混煤")
    private Double washedCoal;
    @ApiModelProperty(value = "落地洗混煤")
    private Double localWashedCoal;
    @ApiModelProperty(value = "煤泥")
    private Double slime;
    @ApiModelProperty(value = "洗矸")
    private Double washeryRejects;
    @ApiModelProperty(value = "入洗原煤")
    private Double rawCoal;
    @ApiModelProperty(value = "8#原煤")
    private Double rawCoal8;
    @ApiModelProperty(value = "13#原煤")
    private Double rawCoal13;
    @ApiModelProperty(value = "类型：1.生产  2.库存")
    private Integer type;
    @ApiModelProperty(value = "期数")
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

    public Double getClenedCoal() {
        return clenedCoal;
    }

    public void setClenedCoal(Double clenedCoal) {
        this.clenedCoal = clenedCoal;
    }

    public Double getWashedCoal() {
        return washedCoal;
    }

    public void setWashedCoal(Double washedCoal) {
        this.washedCoal = washedCoal;
    }

    public Double getSlime() {
        return slime;
    }

    public void setSlime(Double slime) {
        this.slime = slime;
    }

    public Double getWasheryRejects() {
        return washeryRejects;
    }

    public void setWasheryRejects(Double washeryRejects) {
        this.washeryRejects = washeryRejects;
    }

    public Double getLocalWashedCoal() {
        return localWashedCoal;
    }

    public void setLocalWashedCoal(Double localWashedCoal) {
        this.localWashedCoal = localWashedCoal;
    }

    public Double getRawCoal() {
        return rawCoal;
    }

    public void setRawCoal(Double rawCoal) {
        this.rawCoal = rawCoal;
    }

    public Double getRawCoal8() {
        return rawCoal8;
    }

    public void setRawCoal8(Double rawCoal8) {
        this.rawCoal8 = rawCoal8;
    }

    public Double getRawCoal13() {
        return rawCoal13;
    }

    public void setRawCoal13(Double rawCoal13) {
        this.rawCoal13 = rawCoal13;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public ReportFormOutputStoreRecord() {
    }


    public ReportFormOutputStoreRecord(ReportFormOutputStoreRecord record) {
        this.id = record.getId();
        this.dutyStartTime = record.getDutyStartTime();
        this.clenedCoal = record.getClenedCoal();
        this.washedCoal = record.getWashedCoal();
        this.slime = record.getSlime();
        this.washeryRejects = record.getWasheryRejects();
        this.rawCoal = record.getRawCoal();
        this.rawCoal8 = record.getRawCoal8();
        this.rawCoal13 = record.getRawCoal13();
        this.type = record.getType();
        this.term = record.getTerm();
    }
}
