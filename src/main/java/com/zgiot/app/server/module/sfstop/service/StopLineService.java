package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopLine;

import java.util.List;

/**
 * 停车线service
 */
public interface StopLineService {

    List<StopLine> getStopLine(StopLine stopLine);

    String validateStopLineName(StopLine stopLine);

    void addStopLine(StopLine stopLine);

    void updateStopLine(StopLine stopLine);

    void deleteStopLine(StopLine stopLine);
    /**
     * 按照区域查询停车线
     *
     * @param areaId
     * @return
     */
    List<StopLine> getStopLineByAreaId(Long areaId);
}
                                                  