package com.zgiot.app.server.module.reportforms.output.constant;

import io.swagger.annotations.ApiModelProperty;

public class ReportFormCoalTypeConstant {
    @ApiModelProperty(value = "气精煤")
    public static final int clenedCoal =2;
    @ApiModelProperty(value = "洗混煤")
    public static final int washedCoal =1;
    @ApiModelProperty(value = "落地洗混煤")
    public static final int localWashedCoal = 7;
    @ApiModelProperty(value = "煤泥")
    public static final int slime =3;
    @ApiModelProperty(value = "洗矸")
    public static final int washeryRejects = 4;
    @ApiModelProperty(value = "入洗原煤")
    public static final int rawCoal = 5;

    @ApiModelProperty(value = "介质")
    public static final int medium = 6;

}
