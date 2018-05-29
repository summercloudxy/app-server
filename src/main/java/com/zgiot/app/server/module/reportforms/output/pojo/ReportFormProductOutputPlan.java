package com.zgiot.app.server.module.reportforms.output.pojo;


import java.util.Date;

public class ReportFormProductOutputPlan {

  private Integer id;
  private Date productionStartTime;
  private Integer coalType;
  private Integer planDay;
  private Integer planMonth;
  private Integer planYear;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Date getProductionStartTime() {
    return productionStartTime;
  }

  public void setProductionStartTime(Date productionStartTime) {
    this.productionStartTime = productionStartTime;
  }

  public Integer getCoalType() {
    return coalType;
  }

  public void setCoalType(Integer coalType) {
    this.coalType = coalType;
  }

  public Integer getPlanDay() {
    return planDay;
  }

  public void setPlanDay(Integer planDay) {
    this.planDay = planDay;
  }

  public Integer getPlanMonth() {
    return planMonth;
  }

  public void setPlanMonth(Integer planMonth) {
    this.planMonth = planMonth;
  }

  public Integer getPlanYear() {
    return planYear;
  }

  public void setPlanYear(Integer planYear) {
    this.planYear = planYear;
  }
}
