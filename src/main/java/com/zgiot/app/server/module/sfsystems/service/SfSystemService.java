package com.zgiot.app.server.module.sfsystems.service;

import com.zgiot.app.server.module.equipments.controller.DeviceInfo;

import java.util.List;

public interface SfSystemService {
    public List<DeviceInfo> getDeviceInfoBySystemId(int systemId, int pageNum, int pageSize);
}
