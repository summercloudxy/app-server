package com.zgiot.app.server.module.sfstop.entity.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 停车系统参数
 */
@ApiModel("停车系统参数")
public class StopSysParameter {

    @ApiModelProperty("停车系统参数主键")
    private Long id;

    @ApiModelProperty("字典属性")
    private String parameterClass;

    @ApiModelProperty("字典key值")
    private String parameterKey;

    @ApiModelProperty("字典key对应值")
    private String parameterValue;

    @ApiModelProperty("字段说明")
    private String parameterMark;

    @ApiModelProperty("预留字段")
    private String note;

    @ApiModelProperty("是否删除")
    private Integer isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParameterClass() {
        return parameterClass;
    }

    public void setParameterClass(String parameterClass) {
        this.parameterClass = parameterClass;
    }

    public String getParameterKey() {
        return parameterKey;
    }

    public void setParameterKey(String parameterKey) {
        this.parameterKey = parameterKey;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getParameterMark() {
        return parameterMark;
    }

    public void setParameterMark(String parameterMark) {
        this.parameterMark = parameterMark;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
