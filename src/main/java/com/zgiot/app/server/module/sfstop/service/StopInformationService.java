package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceBag;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopInformation;
import com.zgiot.app.server.module.sfstop.util.PageListRsp;

import java.util.List;

public interface StopInformationService {


    /**
     * 查询停车包下的停车设备
     *
     * @param bagId
     * @return
     */
    List<StopInformation> getStopInformationByBagId(Long bagId);

    String checkBagHaveInformation(StopDeviceBag stopDeviceBag);

    PageListRsp getStopInformationPageByNoBagId(Integer pageNum, Integer pageSize);

    List<StopInformation> getStopInformationByNoBagId();

    void updateStopInformation(StopInformation stopInformation);

    StopInformation getStopInformationByTC(StopInformation stopInformation);

    PageListRsp getStopInformationPage(Integer pageNum, Integer pageSize);

    List<StopInformation> getStopInformationAll();

    PageListRsp getStopInformationPageByBagId(Integer pageNum, Integer pageSize, Long bagId);

    void relieveStopInformationAndBag(String thingCode);

    List<StopInformation> getInformationByTCAndNameNoBagId(String string);

    List<StopInformation> getInformationByTCAndName(String string);
}
                                                  