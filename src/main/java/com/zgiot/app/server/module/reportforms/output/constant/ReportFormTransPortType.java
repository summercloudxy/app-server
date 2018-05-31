package com.zgiot.app.server.module.reportforms.output.constant;

import io.swagger.annotations.ApiModelProperty;

public class ReportFormTransPortType {

    @ApiModelProperty("铁路")
    public static final int railway=1;

    @ApiModelProperty("地销")
    public static final int localSales=2;

    @ApiModelProperty("合计")
    public static final int total=3;
}
