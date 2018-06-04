package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetDelay;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetDelayDTO;
import com.zgiot.app.server.module.sfstop.entity.vo.StopThing;

import java.util.List;

/**
 * 停车设备延时
 */
public interface StopTypeSetDelayService {

    List<StopTypeSetDelay> getStopTypeSetDelayByTCNoChecked(StopTypeSetDelay stopTypeSetDelay);

    List<StopTypeSetDelay> getStopTypeSetDelayByTCNIsChecked(StopTypeSetDelay stopTypeSetDelay);

    void updateDelayByCodeNoChecked(StopTypeSetDelayDTO stopTypeSetDelayDTO);

    void updateDelayByCodeChecked(StopTypeSetDelayDTO stopTypeSetDelayDTO);


    /**
     * 查询停车设备配置的设备条件
     *
     * @param thingCode
     * @return
     */
    List<StopThing> getParentStopTypeSetDelay(String thingCode);
}
                                                  