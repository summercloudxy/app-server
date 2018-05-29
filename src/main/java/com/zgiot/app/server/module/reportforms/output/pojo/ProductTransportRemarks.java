package com.zgiot.app.server.module.reportforms.output.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel("产品外运信息对象")
public class ProductTransportRemarks {

    @ApiModelProperty("产品外运信息主键")
    private Integer id;

    @ApiModelProperty("日报开始时间")
    private Date productStartTime;

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

    public Date getProductStartTime() {
        return productStartTime;
    }

    public void setProductStartTime(Date productStartTime) {
        this.productStartTime = productStartTime;
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
