package com.zgiot.app.server.module.sfstop.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceBag;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceRegion;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopInformation;
import com.zgiot.app.server.module.sfstop.enums.ResultCode;
import com.zgiot.app.server.module.sfstop.mapper.StopInformationMapper;
import com.zgiot.app.server.module.sfstop.service.StopInformationService;
import com.zgiot.app.server.module.sfstop.util.PageListRsp;
import com.zgiot.common.pojo.SessionContext;
import com.zgiot.common.restcontroller.ServerResponse;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopInformation;
import com.zgiot.app.server.module.sfstop.mapper.StopInformationMapper;
import com.zgiot.app.server.module.sfstop.service.StopInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StopInformationServiceImpl implements StopInformationService {

    @Autowired
    private StopInformationMapper stopInformationMapper;
@Override
    public String checkBagHaveInformation(StopDeviceBag stopDeviceBag) {
        List<StopInformation> stopInformationList = stopInformationMapper.getStopInformationByBagId(stopDeviceBag.getId());
        if (stopInformationList != null && stopInformationList.size() > 0) {
            return ServerResponse.buildJson(ResultCode.BAG_HAVE_DEVICE.getInfo(), ResultCode.BAG_HAVE_DEVICE.getCode(), null);
        }
        return null;
    }


    @Override
    public PageListRsp getStopInformationPageByNoBagId(Integer pageNum, Integer pageSize) {
        Page page=PageHelper.startPage(pageNum,pageSize);
        List<StopInformation> stopInformationList=stopInformationMapper.getStopInformationByNoBagId();
        PageListRsp pageListRsp=new PageListRsp();
        pageListRsp.setPageNum(page.getPageNum());
        pageListRsp.setPageSize(page.getPageSize());
        pageListRsp.setSize(page.getPages());
        pageListRsp.setTotal(page.getTotal());
        pageListRsp.setList(stopInformationList);
        return pageListRsp;
    }

    @Override
    public List<StopInformation> getStopInformationByNoBagId() {
        List<StopInformation> stopInformationList = stopInformationMapper.getStopInformationByNoBagId();
        return stopInformationList;
    }

    @Override
    public void updateStopInformation(StopInformation stopInformation) {
        if(SessionContext.getCurrentUser()!=null){
            stopInformation.setUpdateUser(SessionContext.getCurrentUser().getUserId());
        }
        stopInformation.setUpdateTime(new Date());
        stopInformationMapper.updateStopInformation(stopInformation);
    }

    @Override
    public StopInformation getStopInformationByTC(StopInformation stopInformation) {
        StopInformation stopInformationByTC=stopInformationMapper.getStopInformationByTC(stopInformation);
        return stopInformationByTC;
    }

    @Override
    public PageListRsp getStopInformationPage(Integer pageNum, Integer pageSize) {
        Page page=PageHelper.startPage(pageNum,pageSize);
        List<StopInformation> stopInformationList=stopInformationMapper.getStopInformationList();
        PageListRsp pageListRsp=new PageListRsp();
        pageListRsp.setPageNum(page.getPageNum());
        pageListRsp.setPageSize(page.getPageSize());
        pageListRsp.setSize(page.getPages());
        pageListRsp.setTotal(page.getTotal());
        pageListRsp.setList(stopInformationList);
        return pageListRsp;
    }

    @Override
    public List<StopInformation> getStopInformationAll() {
        List<StopInformation> stopInformationList=stopInformationMapper.getStopInformationList();
        return stopInformationList;
    }

    @Override
    public PageListRsp getStopInformationPageByBagId(Integer pageNum, Integer pageSize, Long bagId) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<StopInformation> stopInformationList=stopInformationMapper.getStopInformationByBagId(bagId);
        PageListRsp pageListRsp=new PageListRsp<>();
        pageListRsp.setPageNum(page.getPageNum());
        pageListRsp.setPageSize(page.getPageSize());
        pageListRsp.setSize(page.getPages());
        pageListRsp.setTotal(page.getTotal());
        pageListRsp.setList(stopInformationList);
        return pageListRsp;
    }

    @Override
    public void relieveStopInformationAndBag(String thingCode) {
        stopInformationMapper.relieveStopInformationAndBag(thingCode);
    }

    @Override
    public List<StopInformation> getInformationByTCAndNameNoBagId(String string) {
        List<StopInformation> stopInformationList=stopInformationMapper.getInformationByTCAndNameNoBagId(string);
        return stopInformationList;
    }

    @Override
    public List<StopInformation> getInformationByTCAndName(String string) {
        List<StopInformation> stopInformationList=stopInformationMapper.getInformationByTCAndName(string);
        return stopInformationList;
    }

    public List<StopInformation> getStopInformationByBagId(Long bagId) {
        return stopInformationMapper.getStopInformationByBagId(bagId);
    }
}
