package com.zgiot.app.server.module.sfstop.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceArea;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceBag;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceRegion;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopLine;
import com.zgiot.app.server.module.sfstop.enums.ResultCode;
import com.zgiot.app.server.module.sfstop.mapper.StopDeviceAreaMapper;
import com.zgiot.app.server.module.sfstop.mapper.StopDeviceBagMapper;
import com.zgiot.app.server.module.sfstop.mapper.StopLineMapper;
import com.zgiot.app.server.module.sfstop.service.StopDeviceAreaService;
import com.zgiot.app.server.module.sfstop.util.PageListRsp;
import com.zgiot.common.pojo.CurrentUser;
import com.zgiot.common.pojo.SessionContext;
import com.zgiot.common.restcontroller.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.spi.ServiceRegistry;
import java.util.Date;
import java.util.List;


@Service
public class StopDeviceAreaServiceImpl implements StopDeviceAreaService {

    private Logger logger = LoggerFactory.getLogger(StopDeviceAreaServiceImpl.class);

    @Autowired
    private StopDeviceAreaMapper stopDeviceAreaMapper;

    @Autowired
    private StopDeviceBagMapper stopDeviceBagMapper;

    @Autowired
    private StopLineMapper stopLineMapper;



    @Override
    public String checkRegionHaveArea(StopDeviceRegion stopDeviceRegion) {
        List<StopDeviceArea> stopDeviceAreaList=stopDeviceAreaMapper.getAreaListByRegionId(stopDeviceRegion.getId());
        if(stopDeviceAreaList!=null && stopDeviceAreaList.size()>0){
            return ServerResponse.buildJson(ResultCode.REGION_HAVE_AREA.getInfo(),ResultCode.REGION_HAVE_AREA.getCode(),null);
        }
        return null;
    }

    @Override
    public List<StopDeviceArea> getStopDeviceArea(Long regionId) {
        List<StopDeviceArea> areaList = stopDeviceAreaMapper.getAreaListByRegionId(regionId);
        return areaList;
    }

    @Override
    public String validateStopDeviceAreaName(StopDeviceArea stopDeviceArea) {
        List<StopDeviceArea> stopDeviceAreaList = stopDeviceAreaMapper.getAreaListByNameAndRegionId(stopDeviceArea);
        if(stopDeviceAreaList!=null && stopDeviceAreaList.size()>0){
            return ServerResponse.buildJson(ResultCode.NAMEAGAIN.getInfo(),ResultCode.NAMEAGAIN.getCode(),null);
        }
        return null;
    }

    @Override
    public void addStopDeviceArea(StopDeviceArea stopDeviceArea) {
        CurrentUser currentUser = SessionContext.getCurrentUser();
        if(currentUser!=null){
            stopDeviceArea.setCreateUser(currentUser.getUserId());
            stopDeviceArea.setUpdateUser(currentUser.getUserId());
        }
        stopDeviceArea.setCreateTime(new Date());
        stopDeviceArea.setUpdateTime(new Date());
        if(stopDeviceArea.getLevel()==null){
            Integer level=stopDeviceAreaMapper.getCountByRegion(stopDeviceArea);
            stopDeviceArea.setLevel(++level);
        }else{
            addGreaterLevelArea(stopDeviceArea);
        }
        stopDeviceAreaMapper.addStopDeviceArea(stopDeviceArea);
        logger.info("新增区域数据："+stopDeviceArea.getId()+"-"+stopDeviceArea.getAreaName()+"-"+stopDeviceArea.getLevel());
    }

    /**
     * 将指定大区下大于指定区域的级别加1
     * @param stopDeviceArea
     */
    private void addGreaterLevelArea(StopDeviceArea stopDeviceArea) {
        List<StopDeviceArea> stopDeviceAreas = stopDeviceAreaMapper.getGreaterLevelAreaList(stopDeviceArea);
        for (StopDeviceArea stopDeviceAreaLevel:stopDeviceAreas) {
            Integer updateLevel = stopDeviceAreaLevel.getLevel();
            stopDeviceAreaLevel.setLevel(++updateLevel);
            stopDeviceAreaMapper.updateStopDeviceArea(stopDeviceAreaLevel);
        }
    }

    @Override
    public void updateStopDeviceArea(StopDeviceArea stopDeviceArea) {
        CurrentUser currentUser = SessionContext.getCurrentUser();
        if(currentUser!=null){
            stopDeviceArea.setUpdateUser(currentUser.getUserId());
        }
        stopDeviceArea.setUpdateTime(new Date());
        StopDeviceArea stopDeviceAreaLevel = stopDeviceAreaMapper.getStopDeviceAreaById(stopDeviceArea.getId());
        if(stopDeviceArea.getLevel()==null){
            Integer newLevel = stopDeviceAreaMapper.getCountByRegion(stopDeviceArea);
            updateStopDeviceAreaLevel(newLevel,stopDeviceAreaLevel.getLevel(),stopDeviceArea.getRegionId());
            stopDeviceArea.setLevel(newLevel);
        }else {
            updateStopDeviceAreaLevel(stopDeviceArea.getLevel(),stopDeviceAreaLevel.getLevel(),stopDeviceArea.getRegionId());
        }
        stopDeviceAreaMapper.updateStopDeviceArea(stopDeviceArea);
        logger.info("更新区域数据："+stopDeviceArea.getId()+"-"+stopDeviceArea.getAreaName()+"-"+stopDeviceArea.getLevel());
    }

    /**
     * 更改指定大区下的区域级别
     * @param newLevel
     * @param oldLevel
     * @param regionId
     */
    private void updateStopDeviceAreaLevel(Integer newLevel, Integer oldLevel, Long regionId) {
        List<StopDeviceArea> stopDeviceAreaList = stopDeviceAreaMapper.getAreaListByRegionId(regionId);
        for (StopDeviceArea stopDeviceArea:stopDeviceAreaList) {
            Integer updateLevel = stopDeviceArea.getLevel();
            if(updateLevel>=newLevel && updateLevel<=oldLevel){
                updateLevel++;
            }
            if(updateLevel<=newLevel && updateLevel>=oldLevel){
                updateLevel--;
            }
            stopDeviceArea.setLevel(updateLevel);
            stopDeviceAreaMapper.updateStopDeviceArea(stopDeviceArea);
        }
    }

    @Override
    public String checkAreaDel(StopDeviceArea stopDeviceArea) {
        List<StopDeviceBag> stopDeviceBags = stopDeviceBagMapper.getStopDeviceBagByAreaId(stopDeviceArea.getId());
        if(stopDeviceBags!=null && stopDeviceBags.size()>0){
            return ServerResponse.buildJson(ResultCode.LINE_HAVE_BAG.getInfo(),ResultCode.LINE_HAVE_BAG.getCode(),null);
        }
        List<StopLine> stopLines=stopLineMapper.getStopLineByAreaId(stopDeviceArea.getId());
        if(stopLines!=null && stopLines.size()>0){
            return ServerResponse.buildJson(ResultCode.AREA_HAVE_LINE.getInfo(),ResultCode.AREA_HAVE_LINE.getCode(),null);
        }
        return null;
    }

    @Override
    public void deleteStopDeviceArea(StopDeviceArea stopDeviceArea) {
        StopDeviceArea stopDeviceAreaLevel = stopDeviceAreaMapper.getStopDeviceAreaById(stopDeviceArea.getId());
        subGreaterLevelArea(stopDeviceAreaLevel);
        logger.info("物理删除区域数据："+stopDeviceAreaLevel.getId()+"-"+stopDeviceAreaLevel.getAreaName()+"-"+stopDeviceAreaLevel.getLevel());
        stopDeviceAreaMapper.deleteStopDeviceArea(stopDeviceArea.getId());
    }

    @Override
    public PageListRsp getStopDeviceAreaPage(Integer pageNum, Integer pageSize, Long regionId) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<StopDeviceArea> stopDeviceAreaList = stopDeviceAreaMapper.getAreaListByRegionId(regionId);
        PageListRsp pageListRsp=new PageListRsp<>();
        pageListRsp.setPageNum(page.getPageNum());
        pageListRsp.setPageSize(page.getPageSize());
        pageListRsp.setSize(page.getPages());
        pageListRsp.setTotal(page.getTotal());
        pageListRsp.setList(stopDeviceAreaList);
        return pageListRsp;
    }

    /**
     * 将指定大区下大于指定区域的级别减1
     * @param stopDeviceAreaLevel
     */
    private void subGreaterLevelArea(StopDeviceArea stopDeviceAreaLevel) {
        List<StopDeviceArea> greaterLevelAreaList = stopDeviceAreaMapper.getGreaterLevelAreaList(stopDeviceAreaLevel);
        for (StopDeviceArea stopDeviceArea : greaterLevelAreaList) {
            Integer updateLevel = stopDeviceArea.getLevel();
            stopDeviceArea.setLevel(--updateLevel);
            stopDeviceAreaMapper.updateStopDeviceArea(stopDeviceArea);
        }
    }

    @Override
    public List<StopDeviceArea> getStopDeviceArea(Long regionId, Integer areaSystem) {
        return stopDeviceAreaMapper.getStopDeviceArea(regionId, areaSystem);
    }
}
