package com.zgiot.app.server.module.sfmedium.service;

import com.zgiot.app.server.module.alert.pojo.AlertData;

public interface AlertDataService {


    /**
     * 查询某个设备级别最高的告警信息
     *
     * @param thingCode
     * @return
     */
    AlertData getMaxLevelAlertData(String thingCode);

}
