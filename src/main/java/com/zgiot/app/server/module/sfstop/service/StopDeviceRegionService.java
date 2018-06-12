package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceRegion;
import com.zgiot.app.server.module.sfstop.util.PageListRsp;

import java.util.List;

/**
 * 设备大区
 */
public interface StopDeviceRegionService {


    PageListRsp getStopDeviceRegionPageTurn(Integer pageNum, Integer pageSize);



    String validateStopDeviceRegionName(StopDeviceRegion stopDeviceRegion);

    void addStopDeviceRegion(StopDeviceRegion stopDeviceRegion);

    void updateStopDeviceRegion(StopDeviceRegion stopDeviceRegion);

    void deleteStopDeviceRegion(StopDeviceRegion stopDeviceRegion);
    /**
     * 查询停车设备大区
     *
     * @return
     */
    List<StopDeviceRegion> getStopDeviceRegion();
}
                                                  