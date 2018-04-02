package com.zgiot.app.server.module.sfsystems.service.impl;

import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.mapper.TBSystemMapper;
import com.zgiot.app.server.module.sfsystems.service.SfSystemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SfSystemServiceImpl implements SfSystemService {

    @Autowired
    TBSystemMapper tbSystemMapper;

    @Override
    public List<DeviceInfo> getDeviceInfoBySystemId(int systemId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return tbSystemMapper.getDeviceInfoBySystemId(systemId);
    }
}
