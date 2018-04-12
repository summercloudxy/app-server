package com.zgiot.app.server.module.sfsystems.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.equipments.mapper.TBSystemMapper;
import com.zgiot.app.server.module.equipments.service.ThingManagementService;
import com.zgiot.app.server.module.sfsystems.service.SfSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SfSystemServiceImpl implements SfSystemService {

    @Autowired
    TBSystemMapper tbSystemMapper;
    @Autowired
    ThingManagementService thingManagementService;

    @Override
    public PageHelpInfo getDeviceInfoBySystemId(int systemId, int pageNum, int pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<DeviceInfo> deviceInfoList = tbSystemMapper.getDeviceInfoBySystemId(systemId);
        List<String> thingCodeList = getThingCodeListByDeviceInfo(deviceInfoList);

        PageHelpInfo pageHelpInfo = thingManagementService.getDeviceInfoByThingCode(thingCodeList, pageNum, pageSize);
        return pageHelpInfo;
    }

    @Override
    public List<DeviceInfo> getDeviceInfoByThingCode(String thingCode) {
        List<String> thingCodeList = new ArrayList<String>();
        thingCodeList.add(thingCode);
        PageHelpInfo pageHelpInfo = thingManagementService.getDeviceInfoByThingCode(thingCodeList, 1, 10);
        return pageHelpInfo.getList();
    }

    @Override
    public List<DeviceInfo> getFreeDeviceInfo(String thingCode, Long areaId) {
        thingCode = "%" + thingCode + "%";
        return tbSystemMapper.getFreeDeviceInfo(thingCode, areaId);
    }

    /**
     * 获取thingCode集合
     * @param deviceInfoList
     * @return
     */
    private List<String> getThingCodeListByDeviceInfo(List<DeviceInfo> deviceInfoList) {
        List<String> thingCodeList = null;
        if (deviceInfoList != null && deviceInfoList.size() > 0) {
            thingCodeList = new ArrayList<>();
            for (int i = 0; i < deviceInfoList.size(); i++) {
                thingCodeList.add(deviceInfoList.get(i).getThingCode());
            }
        }
        return thingCodeList;
    }

}
