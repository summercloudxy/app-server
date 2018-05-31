package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.alert.pojo.AlertData;

public interface StopAlertDataService {

    /**
     * 查询某个设备级别最高的告警信息
     *
     * @param thingCode
     * @return
     */
    AlertData getMaxLevelAlertData(String thingCode);


}
