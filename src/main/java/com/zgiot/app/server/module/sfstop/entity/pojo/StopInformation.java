package com.zgiot.app.server.module.sfstop.entity.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 停车设备
 */
@ApiModel("停车设备")
public class StopInformation {

    @ApiModelProperty("设备主键")
    private Long id;

    @ApiModelProperty("期数")
    private Integer system;

    @ApiModelProperty("设备id")
    private String thingCode;

    @ApiModelProperty("停车规则号")
    private Integer stopRuleId;

    @ApiModelProperty("停车顺序")
    private Integer stopSequence;

    @ApiModelProperty("停车延时")
    private Integer stopWaitTime;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("设备层级")
    private String stopHierarchy;

    @ApiModelProperty("是否人工干预")
    private Integer isIntervention;

    @ApiModelProperty("所属包")
    private Long bagId;

    @ApiModelProperty("创建用户")
    private String createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改用户")
    private String updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("设备名称")
    private String thingName;

    @ApiModelProperty("设备类型 1普通设备 2TCS设备")
    private Integer thingType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSystem() {
        return system;
    }

    public void setSystem(Integer system) {
        this.system = system;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public Integer getStopRuleId() {
        return stopRuleId;
    }

    public void setStopRuleId(Integer stopRuleId) {
        this.stopRuleId = stopRuleId;
    }

    public Integer getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(Integer stopSequence) {
        this.stopSequence = stopSequence;
    }

    public Integer getStopWaitTime() {
        return stopWaitTime;
    }

    public void setStopWaitTime(Integer stopWaitTime) {
        this.stopWaitTime = stopWaitTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getStopHierarchy() {
        return stopHierarchy;
    }

    public void setStopHierarchy(String stopHierarchy) {
        this.stopHierarchy = stopHierarchy;
    }

    public Integer getIsIntervention() {
        return isIntervention;
    }

    public void setIsIntervention(Integer isIntervention) {
        this.isIntervention = isIntervention;
    }

    public Long getBagId() {
        return bagId;
    }

    public void setBagId(Long bagId) {
        this.bagId = bagId;
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

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public Integer getThingType() {
        return thingType;
    }

    public void setThingType(Integer thingType) {
        this.thingType = thingType;
    }
}
