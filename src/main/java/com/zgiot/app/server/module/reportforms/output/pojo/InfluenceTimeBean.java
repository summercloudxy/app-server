package com.zgiot.app.server.module.reportforms.output.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel("影响时间Bean")
public class InfluenceTimeBean {
    @ApiModelProperty("影响时间集合,这里是将相同类型的1、2期数据放入到同一个对象中")
    private List<InfluenceTimeRsp> influenceTimeRsps=new ArrayList<>();

    @ApiModelProperty("备注")
    private InfluenceTimeRemarks influenceTimeRemarks;

    @ApiModelProperty("当班开始时间")
    private Date dutyStartTime;

    public List<InfluenceTimeRsp> getInfluenceTimeRsps() {
        return influenceTimeRsps;
    }

    public void setInfluenceTimeRsps(List<InfluenceTimeRsp> influenceTimeRsps) {
        this.influenceTimeRsps = influenceTimeRsps;
    }

    public InfluenceTimeRemarks getInfluenceTimeRemarks() {
        return influenceTimeRemarks;
    }

    public void setInfluenceTimeRemarks(InfluenceTimeRemarks influenceTimeRemarks) {
        this.influenceTimeRemarks = influenceTimeRemarks;
    }

    public Date getDutyStartTime() {
        return dutyStartTime;
    }

    public void setDutyStartTime(Date dutyStartTime) {
        this.dutyStartTime = dutyStartTime;
    }
}
                                                  