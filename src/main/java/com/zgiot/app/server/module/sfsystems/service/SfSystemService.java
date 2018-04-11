package com.zgiot.app.server.module.sfsystems.service;

import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;

public interface SfSystemService {

    public PageHelpInfo getDeviceInfoBySystemId(int systemId, int pageNum, int pageSize);

    public PageHelpInfo getFreeDeviceInfoByThingCode(String thingCode, int pageNum, int pageSize);

}
