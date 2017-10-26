package com.zgiot.app.server.module.alert.pojo;

import com.zgiot.common.pojo.ThingModel;

import java.util.List;

public class AlertMaskRsp {
    private String thingCode;
    private ThingModel thingModel;
    private String systemId;
    private String alertInfo;
    private int alertType;
    private List<AlertMask> alertMasks;

}
