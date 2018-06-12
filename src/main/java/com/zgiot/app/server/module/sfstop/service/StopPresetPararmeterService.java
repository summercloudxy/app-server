package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopPresetPararmeter;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopPresetPararmeterDTO;

import java.util.List;

/**
 * 停车预设参数service
 */
public interface StopPresetPararmeterService {
    List<StopPresetPararmeter> getStopPresetPararmeterByTC(StopPresetPararmeter stopPresetPararmeter);

    void updateStopPresetPararmeterByTC(StopPresetPararmeterDTO stopPresetPararmeterDTO);
}
                                                  