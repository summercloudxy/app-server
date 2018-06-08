package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceBag;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopLine;
import com.zgiot.app.server.module.sfstop.util.PageListRsp;


import java.util.List;

/**
 * 停车包serveice
 */
public interface StopDeviceBagService {

    String checkLineHaveBag(Long id);

    List<StopDeviceBag> getStopDeviceBagByArea(StopDeviceBag stopDeviceBag);

    List<StopDeviceBag> getStopDeviceBagByLine(StopDeviceBag stopDeviceBag);

    String checkAreaHaveBagName(StopDeviceBag stopDeviceBag);

    void addStopDeviceBag(StopDeviceBag stopDeviceBag);

    void updateStopDeviceBag(StopDeviceBag stopDeviceBag);

    void deleteStopDeviceBag(StopDeviceBag stopDeviceBag);

    /**
     * 根据停车线查询停车包
     *
     * @param stopLineId
     * @return
     */
    List<StopDeviceBag> getStopDeviceBagByStartLineId(Long stopLineId);

    PageListRsp getStopDeviceBagByAreaPage(Integer pageNum, Integer pageSize, StopDeviceBag stopDeviceBag);

    PageListRsp getStopDeviceBagByLinePage(Integer pageNum, Integer pageSize, StopDeviceBag stopDeviceBag);

    StopDeviceBag getInformationCountById(Long bagId);

    List<StopDeviceBag> getStopDeviceBagByAreaIdAndNoLine(Long areaId);

    void relieveStopDeviceBagToLine(Long bagId);
}
                                                  