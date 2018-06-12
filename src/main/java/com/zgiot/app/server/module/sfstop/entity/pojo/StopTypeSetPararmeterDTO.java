package com.zgiot.app.server.module.sfstop.entity.pojo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 停车参数类型设置
 */

public class StopTypeSetPararmeterDTO {

    @ApiModelProperty("设备编码")
    private String thingCode;

    @ApiModelProperty("停车类型")
    private Integer stopType;

    @ApiModelProperty("停车参数集合")
    private List<StopTypeSetPararmeter> stopTypeSetPararmeterList;

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

    public List<StopTypeSetPararmeter> getStopTypeSetPararmeterList() {
        return stopTypeSetPararmeterList;
    }

    public void setStopTypeSetPararmeterList(List<StopTypeSetPararmeter> stopTypeSetPararmeterList) {
        this.stopTypeSetPararmeterList = stopTypeSetPararmeterList;
    }
}
                                                  