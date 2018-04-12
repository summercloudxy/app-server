package com.zgiot.app.server.module.sfsystems.service;

import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.equipments.pojo.TBSystem;
import com.zgiot.app.server.module.equipments.pojo.Thing;

import java.util.List;

public interface SfSystemService {

    public PageHelpInfo getDeviceInfoBySystemId(Long systemId, int pageNum, int pageSize);

    public PageHelpInfo getDeviceInfoByAreaId(Long areaId, int pageNum, int pageSize);

    public List<DeviceInfo> getDeviceInfoByThingCode(String thingCode);

    public List<DeviceInfo> getFreeDeviceInfo(String thingCode, Long areaId);

    public TBSystem getMenu(Long id, int level);
}
