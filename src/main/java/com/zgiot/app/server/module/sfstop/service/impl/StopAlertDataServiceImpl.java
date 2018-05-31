package com.zgiot.app.server.module.sfstop.service.impl;

import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.sfstop.mapper.StopAlertDataMapper;
import com.zgiot.app.server.module.sfstop.service.StopAlertDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StopAlertDataServiceImpl implements StopAlertDataService {

    @Autowired
    private StopAlertDataMapper alertDataMapper;

    @Override
    public AlertData getMaxLevelAlertData(String thingCode) {
        return alertDataMapper.getMaxLevelAlertData(thingCode);
    }
}
