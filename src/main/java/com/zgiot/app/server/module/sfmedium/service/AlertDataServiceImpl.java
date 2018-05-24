package com.zgiot.app.server.module.sfmedium.service;

import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.sfmedium.mapper.AlertDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertDataServiceImpl implements AlertDataService {

    @Autowired
    private AlertDataMapper alertDataMapper;

    @Override
    public AlertData getMaxLevelAlertData(String thingCode) {
        return alertDataMapper.getMaxLevelAlertData(thingCode);
    }

}
