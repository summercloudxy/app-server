package com.zgiot.app.server.module.reportforms.output.pojo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import java.util.Date;

@ApiModel("影响时间备注")
public class InfluenceTimeRemarks {

    @ApiModelProperty("备注主键")
    private Integer id;

    @ApiModelProperty(value = "当班开始时间",required = true)
    private Date dutyStartTime;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("厂值班领导")
    private String factoryDutyLeader;

    @ApiModelProperty("审核人员")
    private String checker;

    @ApiModelProperty("调度员")
    private String dispatcher;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFactoryDutyLeader() {
        return factoryDutyLeader;
    }

    public void setFactoryDutyLeader(String factoryDutyLeader) {
        this.factoryDutyLeader = factoryDutyLeader;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(String dispatcher) {
        this.dispatcher = dispatcher;
    }
}
