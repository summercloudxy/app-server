package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetDelay;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetDelayDTO;

import java.util.List;

/**
 * 停车设备延时
 */
public interface StopTypeSetDelayService {

    List<StopTypeSetDelay> getStopTypeSetDelayByTCNoChecked(StopTypeSetDelay stopTypeSetDelay);

    List<StopTypeSetDelay> getStopTypeSetDelayByTCNIsChecked(StopTypeSetDelay stopTypeSetDelay);

    void updateDelayByCodeNoChecked(StopTypeSetDelayDTO stopTypeSetDelayDTO);

    void updateDelayByCodeChecked(StopTypeSetDelayDTO stopTypeSetDelayDTO);
}
                                                  