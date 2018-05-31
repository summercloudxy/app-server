package com.zgiot.app.server.module.sfstop.entity.pojo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 停车参数类型设置
 */

public class StopTypeSetPararmeter {

    @ApiModelProperty("停车参数类型主键")
    private Long id;

    @ApiModelProperty("设备编码")
    private String thingCode;

    @ApiModelProperty("指标信号编码")
    private String metricCode;

    @ApiModelProperty("是否删除")
    private Integer isDeleted;

    @ApiModelProperty("比较符1:> 2:= 3:<")
    private String comparisonOperator;

    @ApiModelProperty("值")
    private String comparisonValue;

    @ApiModelProperty("隶属于那个设备id")
    private String parentThingCode;

    @ApiModelProperty("父设备名称")
    private String parentThingName;

    @ApiModelProperty("创建用户")
    private String createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改用户")
    private String updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("是否检修")
    private Integer isChecked;

    @ApiModelProperty("停车类型")
    private Integer stopType;

    @ApiModelProperty("延迟时间")
    private Integer delayTime;

    @ApiModelProperty("指示信号名称")
    private String metricName;

    @ApiModelProperty("比较符名称")
    private String comparisonOperatorValue;

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getParentThingName() {
        return parentThingName;
    }

    public void setParentThingName(String parentThingName) {
        this.parentThingName = parentThingName;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getComparisonOperator() {
        return comparisonOperator;
    }

    public void setComparisonOperator(String comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    public String getComparisonValue() {
        return comparisonValue;
    }

    public void setComparisonValue(String comparisonValue) {
        this.comparisonValue = comparisonValue;
    }

    public String getParentThingCode() {
        return parentThingCode;
    }

    public void setParentThingCode(String parentThingCode) {
        this.parentThingCode = parentThingCode;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStopType() {
        return stopType;
    }

    public void setStopType(Integer stopType) {
        this.stopType = stopType;
    }

    public Integer getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getComparisonOperatorValue() {
        return comparisonOperatorValue;
    }

    public void setComparisonOperatorValue(String comparisonOperatorValue) {
        this.comparisonOperatorValue = comparisonOperatorValue;
    }
}
                                                  