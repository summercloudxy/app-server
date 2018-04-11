package com.zgiot.app.server.module.sfsystems.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.equipments.mapper.TBSystemMapper;
import com.zgiot.app.server.module.sfsystems.service.SfSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SfSystemServiceImpl implements SfSystemService {

    @Autowired
    TBSystemMapper tbSystemMapper;

    @Override
    public PageHelpInfo getDeviceInfoBySystemId(int systemId, int pageNum, int pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<DeviceInfo> deviceInfoList = tbSystemMapper.getDeviceInfoBySystemId(systemId);

        PageHelpInfo pageHelpInfo = new PageHelpInfo();
        pageHelpInfo.setList(deviceInfoList);
        pageHelpInfo.setSum(page.getTotal());
        return pageHelpInfo;
    }

    @Override
    public PageHelpInfo getFreeDeviceInfoByThingCode(String thingCode, int pageNum, int pageSize) {
        thingCode = "%" + thingCode + "%";
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<DeviceInfo> deviceInfoList = tbSystemMapper.getFreeDeviceInfoByThingCode(thingCode);

        PageHelpInfo pageHelpInfo = new PageHelpInfo();
        pageHelpInfo.setList(deviceInfoList);
        pageHelpInfo.setSum(page.getTotal());
        return pageHelpInfo;
    }

}
