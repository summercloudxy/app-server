package com.zgiot.app.server.module.sfstop.entity.pojo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("更改延迟时间请求对象")
public class StopTypeSetDelayDTO {

    @ApiModelProperty("设备编码")
    private String thingCode;

    @ApiModelProperty("停车类型")
    private Integer stopType;

    @ApiModelProperty("延迟时间对象集合")
    private List<StopTypeSetDelay> stopTypeSetDelayList;

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public List<StopTypeSetDelay> getStopTypeSetDelayList() {
        return stopTypeSetDelayList;
    }

    public void setStopTypeSetDelayList(List<StopTypeSetDelay> stopTypeSetDelayList) {
        this.stopTypeSetDelayList = stopTypeSetDelayList;
    }

    public Integer getStopType() {
        return stopType;
    }

    public void setStopType(Integer stopType) {
        this.stopType = stopType;
    }
}
