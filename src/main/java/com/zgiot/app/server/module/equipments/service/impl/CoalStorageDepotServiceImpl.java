package com.zgiot.app.server.module.equipments.service.impl;

import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.equipments.mapper.CoalStorageDepotMapper;
import com.zgiot.app.server.module.equipments.pojo.CoalStorageDepot;
import com.zgiot.app.server.module.equipments.service.CoalStorageDepotService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    public List<CoalStorageDepot> getCoalStorageDepotAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CoalStorageDepot> csdList = coalStorageDepotMapper.getCoalStorageDepotAll();
        return csdList;
    }
}
