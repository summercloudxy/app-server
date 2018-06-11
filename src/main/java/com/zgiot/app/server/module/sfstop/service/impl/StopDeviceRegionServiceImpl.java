package com.zgiot.app.server.module.sfstop.service.impl;

import org.springframework.stereotype.Service;

import java.util.logging.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceRegion;
import com.zgiot.app.server.module.sfstop.enums.ResultCode;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceRegion;
import com.zgiot.app.server.module.sfstop.mapper.StopDeviceRegionMapper;
import com.zgiot.app.server.module.sfstop.service.StopDeviceRegionService;
import com.zgiot.app.server.module.sfstop.util.PageListRsp;
import com.zgiot.common.pojo.CurrentUser;
import com.zgiot.common.pojo.SessionContext;
import com.zgiot.common.restcontroller.ServerResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StopDeviceRegionServiceImpl implements StopDeviceRegionService {


    private org.slf4j.Logger logger = LoggerFactory.getLogger(StopDeviceRegionServiceImpl.class);

    @Autowired
    private StopDeviceRegionMapper stopDeviceRegionMapper;


    @Override
    public PageListRsp getStopDeviceRegionPageTurn(Integer pageNum, Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        page.setOrderBy("level ASC");
        List<StopDeviceRegion> stopDeviceRegionList=stopDeviceRegionMapper.selectAllStopDeviceRegion();
        PageListRsp pageListRsp=new PageListRsp<>();
        pageListRsp.setPageNum(page.getPageNum());
        pageListRsp.setPageSize(page.getPageSize());
        pageListRsp.setSize(page.getPages());
        pageListRsp.setTotal(page.getTotal());
        pageListRsp.setList(stopDeviceRegionList);
        return pageListRsp;
    }

    @Override
    public List<StopDeviceRegion> getStopDeviceRegion() {
        List<StopDeviceRegion> stopDeviceRegionList=stopDeviceRegionMapper.selectAllStopDeviceRegionOrder();
        return stopDeviceRegionList;
    }

    @Override
    public String validateStopDeviceRegionName(StopDeviceRegion stopDeviceRegion) {
        List<StopDeviceRegion>  stopDeviceRegionList=stopDeviceRegionMapper.validateStopDeviceRegionName(stopDeviceRegion);
        if(stopDeviceRegionList!=null && stopDeviceRegionList.size()>0){
            return  ServerResponse.buildJson(ResultCode.NAMEAGAIN.getInfo(),ResultCode.NAMEAGAIN.getCode(),null);
        }
        return null;
    }

    @Override
    public void addStopDeviceRegion(StopDeviceRegion stopDeviceRegion) {
        CurrentUser currentUser = SessionContext.getCurrentUser();
        if(currentUser!=null){
            stopDeviceRegion.setCreateUser(currentUser.getUserId());
            stopDeviceRegion.setUpdateUser(currentUser.getUserId());
        }
        stopDeviceRegion.setCreateTime(new Date());
        stopDeviceRegion.setUpdateTime(new Date());
        if(stopDeviceRegion.getLevel()==null){
            Integer UpdateLevel=stopDeviceRegionMapper.getRegionCount();
            stopDeviceRegion.setLevel(++UpdateLevel);
        }else{
            updateMoreOrEqualLevelRegionAdd(stopDeviceRegion.getLevel());
        }
        stopDeviceRegionMapper.addStopDeviceRegion(stopDeviceRegion);
        logger.info("添加大区数据："+stopDeviceRegion.getId()+"-"+stopDeviceRegion.getLevel()+"-"+stopDeviceRegion.getRegionName());
    }

    /**
     * 变更所有大于改等级的设备等级(级别加)
     * @param level
     */
    private void updateMoreOrEqualLevelRegionAdd(Integer level) {
        List<StopDeviceRegion> stopDeviceRegionList = stopDeviceRegionMapper.selectAllRegionByLevel(level);
        for (StopDeviceRegion stopDeviceRegion:stopDeviceRegionList) {
            Integer updateLevel = stopDeviceRegion.getLevel();
            stopDeviceRegion.setLevel(++updateLevel);
            stopDeviceRegionMapper.updateStopDeviceRegion(stopDeviceRegion);
        }
    }

    @Override
    public void updateStopDeviceRegion(StopDeviceRegion stopDeviceRegion) {
        CurrentUser currentUser = SessionContext.getCurrentUser();
        if(currentUser!=null){
            stopDeviceRegion.setUpdateUser(currentUser.getUserId());
        }
        stopDeviceRegion.setUpdateTime(new Date());
        StopDeviceRegion stopDeviceRegionLevel=stopDeviceRegionMapper.selectStopDeviceRegionById(stopDeviceRegion.getId());
        if(stopDeviceRegion.getLevel()==null){
            Integer newLevel=stopDeviceRegionMapper.getRegionCount();
            updateStopDeviceRegionLevel(newLevel,stopDeviceRegionLevel.getLevel());
            stopDeviceRegion.setLevel(newLevel);
        }else{
            updateStopDeviceRegionLevel(stopDeviceRegion.getLevel(),stopDeviceRegionLevel.getLevel());
        }
        stopDeviceRegionMapper.updateStopDeviceRegion(stopDeviceRegion);
        logger.info("更新大区数据："+stopDeviceRegion.getId()+"-"+stopDeviceRegion.getLevel()+"-"+stopDeviceRegion.getRegionName());
    }

    /**
     * 更改设备大区级别
     * @param newLevel
     * @param oldLevel
     */
    private void updateStopDeviceRegionLevel(Integer newLevel, Integer oldLevel) {
        List<StopDeviceRegion> stopDeviceRegionList = stopDeviceRegionMapper.selectAllStopDeviceRegionOrder();
        for (StopDeviceRegion stopDeviceRegion:stopDeviceRegionList) {
            Integer updateLevel = stopDeviceRegion.getLevel();
            if(updateLevel>=newLevel && updateLevel<=oldLevel){
                updateLevel++;
            }
            if(updateLevel<=newLevel && updateLevel>=oldLevel){
                updateLevel--;
            }
            stopDeviceRegion.setLevel(updateLevel);
            stopDeviceRegionMapper.updateStopDeviceRegion(stopDeviceRegion);
        }
    }

    @Override
    public void deleteStopDeviceRegion(StopDeviceRegion stopDeviceRegion) {
        StopDeviceRegion stopDeviceRegionLevel= stopDeviceRegionMapper.selectStopDeviceRegionById(stopDeviceRegion.getId());
        updateMoreOrEqualLevelRegionSub(stopDeviceRegionLevel.getLevel());
        logger.info("物理删除大区数据："+stopDeviceRegionLevel.getId()+"-"+stopDeviceRegionLevel.getRegionName()+"-"+stopDeviceRegionLevel.getLevel());
        stopDeviceRegionMapper.deleteStopDeviceRegion(stopDeviceRegion.getId());
    }

    /**
     * 变更所有大于改等级的设备等级(级别减)
     * @param level
     */
    private void updateMoreOrEqualLevelRegionSub(Integer level) {
        List<StopDeviceRegion> stopDeviceRegionList = stopDeviceRegionMapper.selectAllRegionByLevel(level);
        for (StopDeviceRegion stopDeviceRegion : stopDeviceRegionList) {
            Integer updateLevel = stopDeviceRegion.getLevel();
            stopDeviceRegion.setLevel(--updateLevel);
            stopDeviceRegionMapper.updateStopDeviceRegion(stopDeviceRegion);
        }
    }

}
