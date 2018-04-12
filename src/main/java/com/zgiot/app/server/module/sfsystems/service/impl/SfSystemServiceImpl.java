package com.zgiot.app.server.module.sfsystems.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.equipments.controller.DeviceInfo;
import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.equipments.mapper.TBSystemMapper;
import com.zgiot.app.server.module.equipments.pojo.TBSystem;
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
    public PageHelpInfo getDeviceInfoBySystemId(Long systemId, int pageNum, int pageSize) {
        List<Long> systemIdList = new ArrayList<>();
        systemIdList.add(systemId);
        List<String> thingCodeList = tbSystemMapper.getThingCodeBySystemId(systemIdList);

        PageHelpInfo pageHelpInfo = thingManagementService.getDeviceInfoByThingCode(thingCodeList, pageNum, pageSize);
        return pageHelpInfo;
    }

    @Override
    public PageHelpInfo getDeviceInfoByAreaId(Long areaId, int pageNum, int pageSize) {
        List<TBSystem> systemList = tbSystemMapper.getSystemByParentId(areaId);

        List<Long> systemIdList = new ArrayList<>();
        if (systemList != null && systemList.size() > 0) {
            for (int i=0;i<systemList.size();i++ ){
                systemIdList.add(systemList.get(i).getId());
            }
        }
        List<String> thingCodeList = tbSystemMapper.getThingCodeBySystemId(systemIdList);

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

    @Override
    public TBSystem getMenu(Long id, int level) {
        level--;
        // 当前节点
        TBSystem tbSystem = tbSystemMapper.getSystemById(id);
        if(level <= 0){
            return tbSystem;
        }
        // 子节点
        List<TBSystem> tbSystemList = tbSystemMapper.getSystemByParentId(id);
        if (tbSystemList != null && tbSystemList.size() > 0) {
            for (int i = 0; i < tbSystemList.size(); i++) {
                TBSystem system = getMenu(tbSystemList.get(i).getId(), level);// 递归
                // 将子节点添加至父节点list中
                tbSystem.getNodeList().add(system);
            }
        }
        return tbSystem;
    }

}
