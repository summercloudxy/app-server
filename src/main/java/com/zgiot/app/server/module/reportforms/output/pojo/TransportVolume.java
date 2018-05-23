package com.zgiot.app.server.module.reportforms.output.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("运销统计表")
public class TransportVolume {

    @ApiModelProperty("煤类型")
    private Integer coalType;

    @ApiModelProperty("煤列数或车数")
    private Integer trainNumber;

    @ApiModelProperty("吨数")
    private Double transportVolume;

    public Integer getCoalType() {
        return coalType;
    }

    public void setCoalType(Integer coalType) {
        this.coalType = coalType;
    }

    public Integer getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(Integer trainNumber) {
        this.trainNumber = trainNumber;
    }

    public Double getTransportVolume() {
        return transportVolume;
    }

    public void setTransportVolume(Double transportVolume) {
        this.transportVolume = transportVolume;
    }
}
