package com.zgiot.app.server.module.reportforms.output.productionmonitor.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel("开车情况记录")
public class ReportFormSystemStartRecord {

  @ApiModelProperty("id")
  private long id;
  @ApiModelProperty("当班开始时间")
  private Date dutyStartTime;
  @ApiModelProperty("状态开始时间")
  private Date startTime;
  @ApiModelProperty("状态结束时间")
  private Date endTime;
  @ApiModelProperty("8#煤设备")
  private String coal8ThingCode;
  @ApiModelProperty("13#煤设备")
  private String coal13ThingCode;
  @ApiModelProperty("洗煤设备数量,用来计算配洗类型")
  private int washingCoalEquipmentCount;
  @ApiModelProperty("配洗类型")
  private String blendingWashingType;
  @ApiModelProperty("生产描述 1.生产、2.检修、3.欠煤、4.欠车、5.欠水")
  private Short productionDescription;
  @ApiModelProperty("时长，分钟为单位")
  private Long duration;
  @ApiModelProperty("重介洗煤系统 块A/B")
  private String heavyMediumLump;
  @ApiModelProperty("重介洗煤系统 末1/2/3")
  private String heavyMediumSlack;
  @ApiModelProperty("857运行情况：运行显示x857，不运行显示/")
  private String tcs857;
  @ApiModelProperty("858运行情况：运行显示x858，不运行显示/")
  private String tcs858;
  @ApiModelProperty("859运行情况：运行显示x859，不运行显示/")
  private String tcs859;
  @ApiModelProperty("原因")
  private String reason;
  @ApiModelProperty("系统期数")
  private Integer term;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getCoal8ThingCode() {
    return coal8ThingCode;
  }

  public void setCoal8ThingCode(String coal8ThingCode) {
    this.coal8ThingCode = coal8ThingCode;
  }

  public String getCoal13ThingCode() {
    return coal13ThingCode;
  }

  public void setCoal13ThingCode(String coal13ThingCode) {
    this.coal13ThingCode = coal13ThingCode;
  }

  public String getBlendingWashingType() {
    return blendingWashingType;
  }

  public void setBlendingWashingType(String blendingWashingType) {
    this.blendingWashingType = blendingWashingType;
  }

  public Short getProductionDescription() {
    return productionDescription;
  }

  public void setProductionDescription(Short productionDescription) {
    this.productionDescription = productionDescription;
  }

  public Long getDuration() {
    return duration;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }

  public String getHeavyMediumLump() {
    return heavyMediumLump;
  }

  public void setHeavyMediumLump(String heavyMediumLump) {
    this.heavyMediumLump = heavyMediumLump;
  }

  public String getHeavyMediumSlack() {
    return heavyMediumSlack;
  }

  public void setHeavyMediumSlack(String heavyMediumSlack) {
    this.heavyMediumSlack = heavyMediumSlack;
  }

  public String getTcs857() {
    return tcs857;
  }

  public void setTcs857(String tcs857) {
    this.tcs857 = tcs857;
  }

  public String getTcs858() {
    return tcs858;
  }

  public void setTcs858(String tcs858) {
    this.tcs858 = tcs858;
  }

  public String getTcs859() {
    return tcs859;
  }

  public void setTcs859(String tcs859) {
    this.tcs859 = tcs859;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public Integer getTerm() {
    return term;
  }

  public void setTerm(Integer term) {
    this.term = term;
  }

  public int getWashingCoalEquipmentCount() {
    return washingCoalEquipmentCount;
  }

  public void setWashingCoalEquipmentCount(int washingCoalEquipmentCount) {
    this.washingCoalEquipmentCount = washingCoalEquipmentCount;
  }

  public Date getDutyStartTime() {
    return dutyStartTime;
  }

  public void setDutyStartTime(Date dutyStartTime) {
    this.dutyStartTime = dutyStartTime;
  }
}
