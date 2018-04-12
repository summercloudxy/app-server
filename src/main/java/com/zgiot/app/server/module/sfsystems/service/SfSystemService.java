package com.zgiot.app.server.module.sfsystems.service;

import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.equipments.pojo.Thing;

import java.util.List;

public interface SfSystemService {

    public PageHelpInfo getDeviceInfoBySystemId(int systemId, int pageNum, int pageSize);

    public List<DeviceInfo> getDeviceInfoByThingCode(String thingCode);

    public List<DeviceInfo> getFreeDeviceInfo(String thingCode, Long areaId);
}
