package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceArea;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceRegion;
import com.zgiot.app.server.module.sfstop.util.PageListRsp;

import java.util.List;

/**
 * 设备区域
 */
public interface StopDeviceAreaService {

    /**
     * 查询所有的停车设备区域
     *
     * @return
     */
    List<StopDeviceArea> getStopDeviceArea(Long regionId, Integer areaSystem);

    String checkRegionHaveArea(StopDeviceRegion stopDeviceRegion);

    List<StopDeviceArea> getStopDeviceArea(Long regionId);

    String validateStopDeviceAreaName(StopDeviceArea stopDeviceArea);

    void addStopDeviceArea(StopDeviceArea stopDeviceArea);

    void updateStopDeviceArea(StopDeviceArea stopDeviceArea);

    String checkAreaDel(StopDeviceArea stopDeviceArea);

    void deleteStopDeviceArea(StopDeviceArea stopDeviceArea);

    PageListRsp getStopDeviceAreaPage(Integer pageNum, Integer pageSize, Long regionId);
}
                                                  