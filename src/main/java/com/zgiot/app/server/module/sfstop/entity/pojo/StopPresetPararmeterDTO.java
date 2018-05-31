package com.zgiot.app.server.module.sfstop.entity.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("停车请求参数")
public class StopPresetPararmeterDTO {

    @ApiModelProperty("设备编码")
    private String thingCode;

    @ApiModelProperty("停车类型")
    private Integer stopType;

    @ApiModelProperty("预设参数对象集合")
    private List<StopPresetPararmeter> stopPresetPararmeterList;

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public Integer getStopType() {
        return stopType;
    }

    public void setStopType(Integer stopType) {
        this.stopType = stopType;
    }

    public List<StopPresetPararmeter> getStopPresetPararmeterList() {
        return stopPresetPararmeterList;
    }

    public void setStopPresetPararmeterList(List<StopPresetPararmeter> stopPresetPararmeterList) {
        this.stopPresetPararmeterList = stopPresetPararmeterList;
    }
}
