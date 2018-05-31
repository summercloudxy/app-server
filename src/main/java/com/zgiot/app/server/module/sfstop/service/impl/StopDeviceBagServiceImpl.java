package com.zgiot.app.server.module.sfstop.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceArea;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceBag;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopInformation;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopTypeSetDelay;
import com.zgiot.app.server.module.sfstop.enums.ResultCode;
import com.zgiot.app.server.module.sfstop.mapper.StopDeviceBagMapper;
import com.zgiot.app.server.module.sfstop.mapper.StopInformationMapper;
import com.zgiot.app.server.module.sfstop.mapper.StopTypeSetDelayMapper;
import com.zgiot.app.server.module.sfstop.service.StopDeviceBagService;
import com.zgiot.app.server.module.sfstop.util.PageListRsp;
import com.zgiot.common.pojo.CurrentUser;
import com.zgiot.common.pojo.SessionContext;
import com.zgiot.common.restcontroller.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import com.zgiot.app.server.module.sfstop.mapper.StopDeviceBagMapper;
import com.zgiot.app.server.module.sfstop.service.StopDeviceBagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopDeviceBagServiceImpl implements StopDeviceBagService {

    private Logger logger = LoggerFactory.getLogger(StopDeviceBagServiceImpl.class);

    @Autowired
    private StopDeviceBagMapper stopDeviceBagMapper;

    @Autowired
    private StopInformationMapper stopInformationMapper;

    @Autowired
    private StopTypeSetDelayMapper stopTypeSetDelayMapper;

    @Override
    public String checkLineHaveBag(Long stopLineId) {
        List<StopDeviceBag> stopDeviceBags = stopDeviceBagMapper.getStopDeviceBagByLineId(stopLineId);
        if(stopDeviceBags!=null && stopDeviceBags.size()>0){
            return ServerResponse.buildJson(ResultCode.LINE_HAVE_BAG.getInfo(),ResultCode.LINE_HAVE_BAG.getCode(),null);
        }
        return null;
    }

    @Override
    public List<StopDeviceBag> getStopDeviceBagByArea(StopDeviceBag stopDeviceBag) {
        List<StopDeviceBag> stopDeviceBags=stopDeviceBagMapper.getStopDeviceBagByAreaId(stopDeviceBag.getAreaId());
        if(stopDeviceBags!=null){
            for (StopDeviceBag stopDeviceBagDutyTime:stopDeviceBags) {
                getDelayTime(stopDeviceBagDutyTime);
            }
        }
        return stopDeviceBags;
    }

    @Override
    public List<StopDeviceBag> getStopDeviceBagByLine(StopDeviceBag stopDeviceBag) {
        List<StopDeviceBag> stopDeviceBags = stopDeviceBagMapper.getStopDeviceBagByLineId(stopDeviceBag.getStopLineId());
        if(stopDeviceBags!=null){
            for (StopDeviceBag stopDeviceBagDutyTime:stopDeviceBags) {
                getDelayTime(stopDeviceBagDutyTime);
            }
        }
        return stopDeviceBags;
    }

    @Override
    public String checkAreaHaveBagName(StopDeviceBag stopDeviceBag) {
        List<StopDeviceBag> stopDeviceBags=stopDeviceBagMapper.checkAreaHaveBagName(stopDeviceBag);
        if(stopDeviceBags!=null && stopDeviceBags.size()>0){
            return ServerResponse.buildJson(ResultCode.NAMEAGAIN.getInfo(),ResultCode.NAMEAGAIN.getCode(),null);
        }
        return null;
    }

    @Override
    public void addStopDeviceBag(StopDeviceBag stopDeviceBag) {
        CurrentUser currentUser = SessionContext.getCurrentUser();
        if(currentUser!=null){
            stopDeviceBag.setCreateUser(currentUser.getUserId());
            stopDeviceBag.setUpdateUser(currentUser.getUserId());
        }
        stopDeviceBag.setCreateTime(new Date());
        stopDeviceBag.setUpdateTime(new Date());

        stopDeviceBagMapper.addStopDeviceBag(stopDeviceBag);
        logger.info("添加设备包数据："+stopDeviceBag.getId()+"-"+stopDeviceBag.getBagName());
    }

    @Override
    public void updateStopDeviceBag(StopDeviceBag stopDeviceBag) {
        CurrentUser currentUser = SessionContext.getCurrentUser();
        if(currentUser!=null){
            stopDeviceBag.setUpdateUser(currentUser.getUserId());
        }
        stopDeviceBag.setUpdateTime(new Date());
        stopDeviceBagMapper.updateStopDeviceBag(stopDeviceBag);
        logger.info("更新设备包数据："+stopDeviceBag.getId()+"-"+stopDeviceBag.getBagName());
    }

    @Override
    public void deleteStopDeviceBag(StopDeviceBag stopDeviceBag) {
        stopDeviceBagMapper.deleteStopDeviceBag(stopDeviceBag);
        logger.info("物理删除设备包数据：" + stopDeviceBag.getId());

    }

    @Override
    public List<StopDeviceBag> getStopDeviceBagByStartLineId(Long stopLineId) {
        return stopDeviceBagMapper.getStopDeviceBagByLineId(stopLineId);
    }

    @Override
    public PageListRsp getStopDeviceBagByAreaPage(Integer pageNum, Integer pageSize, StopDeviceBag stopDeviceBag) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<StopDeviceBag> stopDeviceAreaList = stopDeviceBagMapper.getStopDeviceBagByAreaId(stopDeviceBag.getAreaId());
        if(stopDeviceAreaList!=null){
            for (StopDeviceBag stopDeviceBagDutyTime:stopDeviceAreaList) {
                getDelayTime(stopDeviceBagDutyTime);
            }
        }
        PageListRsp pageListRsp=new PageListRsp<>();
        pageListRsp.setPageNum(page.getPageNum());
        pageListRsp.setPageSize(page.getPageSize());
        pageListRsp.setSize(page.getPages());
        pageListRsp.setTotal(page.getTotal());
        pageListRsp.setList(stopDeviceAreaList);
        return pageListRsp;
    }

    @Override
    public PageListRsp getStopDeviceBagByLinePage(Integer pageNum, Integer pageSize, StopDeviceBag stopDeviceBag) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<StopDeviceBag> stopDeviceAreaList = stopDeviceBagMapper.getStopDeviceBagByLineId(stopDeviceBag.getStopLineId());
        if(stopDeviceAreaList!=null){
            for (StopDeviceBag stopDeviceBagDutyTime:stopDeviceAreaList) {
                getDelayTime(stopDeviceBagDutyTime);
            }
        }
        PageListRsp pageListRsp=new PageListRsp<>();
        pageListRsp.setPageNum(page.getPageNum());
        pageListRsp.setPageSize(page.getPageSize());
        pageListRsp.setSize(page.getPages());
        pageListRsp.setTotal(page.getTotal());
        pageListRsp.setList(stopDeviceAreaList);
        return pageListRsp;
    }

    @Override
    public StopDeviceBag getInformationCountById(Long bagId) {
        StopDeviceBag stopDeviceBag=stopDeviceBagMapper.getStopDeviceBagById(bagId);
        getDelayTime(stopDeviceBag);
        return stopDeviceBag;
    }

    public StopDeviceBag getDelayTime(StopDeviceBag stopDeviceBag){
        List<StopInformation> stopInformationList= stopInformationMapper.getStopInformationByBagId(stopDeviceBag.getId());
        if(stopInformationList!=null && stopInformationList.size()>0){
            Long sum=0L;
            for (StopInformation stopInformation:stopInformationList) {
                List<StopTypeSetDelay> stopTypeSetDelays=stopTypeSetDelayMapper.getStopTypeSetDelayByThingCode(stopInformation.getThingCode());
                if(stopTypeSetDelays!=null && stopTypeSetDelays.size()>0){
                    for (StopTypeSetDelay stopTypeSetDelay:stopTypeSetDelays) {
                        sum+=stopTypeSetDelay.getDelayTime();
                    }
                }
            }
            stopDeviceBag.setDelayTime(sum);
            stopDeviceBag.setStopInformationCount(stopInformationList.size());
        }
        return stopDeviceBag;
    }

}
