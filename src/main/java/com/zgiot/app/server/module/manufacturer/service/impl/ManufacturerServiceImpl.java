package com.zgiot.app.server.module.manufacturer.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.manufacturer.mapper.ManufacturerMapper;
import com.zgiot.app.server.module.manufacturer.pojo.Manufacturer;
import com.zgiot.app.server.module.manufacturer.service.ManufacturerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Override
    public void addManufacturer(Manufacturer manufacturer) {
        String codePrefix = manufacturer.getTypeCode();
        manufacturer.setManufacturerCode(getManufacturerCodeByCode(codePrefix));
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
    public PageHelpInfo getManufacturerByType(String thingType1Code, int pageNum, int pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Manufacturer> mfList = manufacturerMapper.getManufacturerByType(thingType1Code);
        PageHelpInfo pageHelpInfo = new PageHelpInfo();
        pageHelpInfo.setList(mfList);
        pageHelpInfo.setSum(page.getTotal());
        return pageHelpInfo;
    }

    @Override
    public PageHelpInfo getManufacturerByCodeOrName(String thingType1Code, String codeOrName, int pageNum, int pageSize) {
        codeOrName = "%" + codeOrName + "%";
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Manufacturer> mfList = manufacturerMapper.getManufacturerByCodeOrName(thingType1Code, codeOrName, codeOrName);
        PageHelpInfo pageHelpInfo = new PageHelpInfo();
        pageHelpInfo.setList(mfList);
        pageHelpInfo.setSum(page.getTotal());
        return pageHelpInfo;
    }

    @Override
    public boolean isNameRepeat(String manufacturerName) {
        if (null != manufacturerMapper.getManufacturerByName(manufacturerName)) {
            return true;
        }
        return false;
    }

    @Override
    public String getManufacturerCodeByCode(String codePrefix) {
        String code = codePrefix + "%";
        String mCode = codePrefix + "001";
        List<String> codeList = manufacturerMapper.getManufacturerCodeByCode(code);
        if (null != codeList && codeList.size() > 0) {
            List<Integer> fff = new ArrayList<Integer>();
            for (String s : codeList) {
                fff.add(Integer.parseInt(StringUtils.substringAfter(s, codePrefix)));
            }
            int maxCode = Collections.max(fff);
            String codeNumStr = String.valueOf(maxCode + 1);
            int codeLength = codeNumStr.length();
            switch (codeLength) {
                case 1:
                    mCode = codePrefix + "00" + codeNumStr;
                    break;
                case 2:
                    mCode = codePrefix + "0" + codeNumStr;
                    break;
                case 3:
                    mCode = codePrefix + codeNumStr;
                    break;
            }
        }
        return mCode;
    }

}
