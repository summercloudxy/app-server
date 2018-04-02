package com.zgiot.app.server.module.equipments.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.equipments.mapper.CoalStorageDepotMapper;
import com.zgiot.app.server.module.equipments.pojo.CoalStorageDepot;
import com.zgiot.app.server.module.equipments.service.CoalStorageDepotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoalStorageDepotServiceImpl implements CoalStorageDepotService {

    @Autowired
    private CoalStorageDepotMapper coalStorageDepotMapper;

    @Override
    public void addCoalStorageDepot(CoalStorageDepot coalStorageDepot) {
        coalStorageDepotMapper.addCoalStorageDepot(coalStorageDepot);
    }

    @Override
    public CoalStorageDepot getCoalStorageDepotById(Long id) {
        return coalStorageDepotMapper.getCoalStorageDepotById(id);
    }

    @Override
    public void editCoalStorageDepot(CoalStorageDepot coalStorageDepot) {
        coalStorageDepotMapper.editCoalStorageDepot(coalStorageDepot);
    }

    @Override
    public void deleteCoalStorageDepot(Long id) {
        coalStorageDepotMapper.deleteCoalStorageDepot(id);
    }

    @Override
    public PageHelpInfo getCoalStorageDepotAll(int pageNum, int pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<CoalStorageDepot> csdList = coalStorageDepotMapper.getCoalStorageDepotAll();
        PageHelpInfo pageHelpInfo = new PageHelpInfo();
        pageHelpInfo.setList(csdList);
        pageHelpInfo.setSum(page.getTotal());
        return pageHelpInfo;
    }
}
