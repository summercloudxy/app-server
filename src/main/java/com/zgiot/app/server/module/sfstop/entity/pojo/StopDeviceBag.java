package com.zgiot.app.server.module.sfstop.entity.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 设备包
 */
@ApiModel("设备包")
public class StopDeviceBag {

    @ApiModelProperty("设备包id")
    private Long id;

    @ApiModelProperty("设备包名称{bagName}")
    private String bagName;

    @ApiModelProperty("区域主键")
    private Long areaId;

    @ApiModelProperty("是否删除")
    private Integer isDeleted;

    @ApiModelProperty("创建用户")
    private String createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改用户")
    private String updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("包编号")
    private Integer number;

    @ApiModelProperty("停车线主键")
    private Long stopLineId;

    @ApiModelProperty("延迟时间")
    private Long delayTime;

    @ApiModelProperty("设备数量")
    private Integer stopInformationCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBagName() {
        return bagName;
    }

    public void setBagName(String bagName) {
        this.bagName = bagName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }


    public Long getStopLineId() {
        return stopLineId;
    }

    public void setStopLineId(Long stopLineId) {
        this.stopLineId = stopLineId;
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }

    public Integer getStopInformationCount() {
        return stopInformationCount;
    }

    public void setStopInformationCount(Integer stopInformationCount) {
        this.stopInformationCount = stopInformationCount;
    }
}
