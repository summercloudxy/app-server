package com.zgiot.app.server.module.sfmonitor.monitorService;

import com.zgiot.app.server.module.sfmonitor.controller.AddOrEditMonitorResponse;
import com.zgiot.app.server.module.sfmonitor.controller.EquipmentRelateToSignalWrapperReq;
import com.zgiot.app.server.module.sfmonitor.controller.MonitorInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface MonitorService {

    public AddOrEditMonitorResponse addOrEditMonitorInfo(MonitorInfo monitorInfo);

    public void editEquipmentMonitorInfo(EquipmentRelateToSignalWrapperReq equipmentRelateToSignalWrapperReq);

    public void deleteEquipmentConfig(int id);

    public Map<String, Boolean> getWrapperMatchRule(String zoneCode);

}
