package com.zgiot.app.server.module.alert.pojo;

import java.util.Date;

public class AlertMask {
  private Integer id;
  private Integer alertId;
  private Integer maskId;
  private Date time;
  private String userId;
  private Double paramValue;
  private Double paramLower;
  private Double paramUpper;
  private String info;

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getAlertId() {
    return alertId;
  }

  public void setAlertId(Integer alertId) {
    this.alertId = alertId;
  }

  public Integer getMaskId() {
    return maskId;
  }

  public void setMaskId(Integer maskId) {
    this.maskId = maskId;
  }

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Double getParamValue() {
    return paramValue;
  }

  public void setParamValue(Double paramValue) {
    this.paramValue = paramValue;
  }

  public Double getParamLower() {
    return paramLower;
  }

  public void setParamLower(Double paramLower) {
    this.paramLower = paramLower;
  }

  public Double getParamUpper() {
    return paramUpper;
  }

  public void setParamUpper(Double paramUpper) {
    this.paramUpper = paramUpper;
  }
}
