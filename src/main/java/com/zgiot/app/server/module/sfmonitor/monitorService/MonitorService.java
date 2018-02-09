package com.zgiot.app.server.module.sfmonitor.monitorService;

import com.zgiot.app.server.module.sfmonitor.controller.AddOrEditMonitorResponse;
import com.zgiot.app.server.module.sfmonitor.controller.MonitorInfo;
import org.springframework.stereotype.Service;

@Service
public interface MonitorService {

    public AddOrEditMonitorResponse addOrEditMonitorInfo(MonitorInfo monitorInfo);

}
