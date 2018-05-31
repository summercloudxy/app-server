package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetPararmeter;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetPararmeterDTO;

import java.util.List;

/**
 * 停车参数条件
 */
public interface StopTypeSetPararmeterService {

    List<StopTypeSetPararmeter> getPararmeterNoChecked(StopTypeSetPararmeter stopTypeSetPararmeter);

    List<StopTypeSetPararmeter> getPararmeterChecked(StopTypeSetPararmeter stopTypeSetPararmeter);

    void updatePararmeterNoChecked(StopTypeSetPararmeterDTO stopTypeSetPararmeterDTO);

    void updatePararmeterChecked(StopTypeSetPararmeterDTO stopTypeSetPararmeterDTO);
}
                                                  