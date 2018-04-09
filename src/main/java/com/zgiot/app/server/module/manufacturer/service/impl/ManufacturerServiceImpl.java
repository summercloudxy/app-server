package com.zgiot.app.server.module.manufacturer.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.manufacturer.mapper.ManufacturerMapper;
import com.zgiot.app.server.module.manufacturer.pojo.Manufacturer;
import com.zgiot.app.server.module.manufacturer.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Override
    public void addManufacturer(Manufacturer manufacturer) {
        manufacturerMapper.addManufacturer(manufacturer);
    }

    @Override
    public void editManufacturer(Manufacturer manufacturer) {
        manufacturerMapper.editManufacturer(manufacturer);
    }

    @Override
    public void deleteManufacturer(Long id) {
        manufacturerMapper.deleteManufacturer(id);
    }

    @Override
    public PageHelpInfo getManufacturerByType(String thingType1Code,int pageNum, int pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Manufacturer> mfList = manufacturerMapper.getManufacturerByType(thingType1Code);
        PageHelpInfo pageHelpInfo = new PageHelpInfo();
        pageHelpInfo.setList(mfList);
        pageHelpInfo.setSum(page.getTotal());
        return pageHelpInfo;
    }

    @Override
    public PageHelpInfo getManufacturerByCodeOrName(String thingType1Code,String codeOrName, int pageNum, int pageSize) {
        codeOrName = "%" + codeOrName + "%";
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Manufacturer> mfList = manufacturerMapper.getManufacturerByCodeOrName(thingType1Code,codeOrName,codeOrName);
        PageHelpInfo pageHelpInfo = new PageHelpInfo();
        pageHelpInfo.setList(mfList);
        pageHelpInfo.setSum(page.getTotal());
        return pageHelpInfo;
    }
}
