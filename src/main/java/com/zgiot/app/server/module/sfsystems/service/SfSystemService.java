package com.zgiot.app.server.module.sfsystems.service;

import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;

public interface SfSystemService {
    public PageHelpInfo getDeviceInfoBySystemId(int systemId, int pageNum, int pageSize);
}
