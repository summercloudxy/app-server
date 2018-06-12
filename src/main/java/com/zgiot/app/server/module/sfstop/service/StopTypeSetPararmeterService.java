package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetPararmeter;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetPararmeterDTO;
import com.zgiot.app.server.module.sfstop.entity.vo.StopPararmeterVO;

import java.util.List;

/**
 * 停车参数条件
 */
public interface StopTypeSetPararmeterService {

    List<StopTypeSetPararmeter> getPararmeterNoChecked(StopTypeSetPararmeter stopTypeSetPararmeter);

    List<StopTypeSetPararmeter> getPararmeterChecked(StopTypeSetPararmeter stopTypeSetPararmeter);

    void updatePararmeterNoChecked(StopTypeSetPararmeterDTO stopTypeSetPararmeterDTO);

    void updatePararmeterChecked(StopTypeSetPararmeterDTO stopTypeSetPararmeterDTO);

    /**
     * 查询停车设备配置的参数条件
     *
     * @param thingCode
     * @return
     */
    List<StopPararmeterVO> getParentStopTypeSetPararmeter(String thingCode);
}
                                                  