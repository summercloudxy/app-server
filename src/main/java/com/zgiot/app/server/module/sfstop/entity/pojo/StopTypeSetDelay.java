package com.zgiot.app.server.module.sfstop.entity.pojo;

import com.zgiot.app.server.module.util.validate.GetValidate;
import com.zgiot.app.server.module.util.validate.UpdateValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 停车延时配置
 */
@ApiModel("停车延时")
public class StopTypeSetDelay {

    @ApiModelProperty("停车延时主键")
    private Long id;

    @ApiModelProperty("设备编码")
    private String thingCode;

    @ApiModelProperty("设备延迟时间")
    private Long delayTime;

    @ApiModelProperty("是否删除")
    private Integer isDeleted;

    @ApiModelProperty("父设备编码")
    private String parentThingCode;

    @ApiModelProperty("父设备名称")
    private String parentThingName;

    @ApiModelProperty("创建用户")
    private String createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新用户")
    private String updateUser;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("是否是检修模式 空:表示不是 1:表示是检修模式")
    private Integer isChecked;

    @ApiModelProperty("停车类型 0:调节参数 1:停止设备")
    @NotNull(message = "停车类型{stopType}不能为空", groups = {GetValidate.class, UpdateValidate.class})
    private Integer stopType;

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

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getParentThingCode() {
        return parentThingCode;
    }

    public void setParentThingCode(String parentThingCode) {
        this.parentThingCode = parentThingCode;
    }

    public String getParentThingName() {
        return parentThingName;
    }

    public void setParentThingName(String parentThingName) {
        this.parentThingName = parentThingName;
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
}
